"""Inspect APK ELF LOAD/RELRO and ZIP alignment; never edits third-party binaries."""
import argparse
import hashlib
import json
from pathlib import Path
import struct
import zipfile


def inspect_elf(data):
    if data[:4] != b"\x7fELF" or data[5] != 1 or data[4] not in (1, 2):
        raise ValueError("Unsupported ELF")
    wide = data[4] == 2
    offset = struct.unpack_from("<Q" if wide else "<I", data, 32 if wide else 28)[0]
    size, count = struct.unpack_from("<HH", data, 54 if wide else 42)
    segments = []
    for i in range(count):
        row = struct.unpack_from("<IIQQQQQQ" if wide else "<IIIIIIII", data, offset + size * i)
        if wide:
            kind, flags, file_offset, address, _, file_size, memory_size, alignment = row
        else:
            kind, file_offset, address, _, file_size, memory_size, flags, alignment = row
        segments.append(dict(type=kind, flags=flags, offset=file_offset, address=address,
                             fileSize=file_size, memorySize=memory_size, alignment=alignment))
    loads = [s for s in segments if s["type"] == 1]
    if not loads:
        raise ValueError("ELF without LOAD")
    minimum = 16384 if wide else 4096
    load_ok = all(s["alignment"] >= minimum and
                  (s["address"] - s["offset"]) % minimum == 0 for s in loads)
    relro = []
    for s in segments:
        if s["type"] != 0x6474E552:
            continue
        end = s["address"] + s["memorySize"]
        containing = [load for load in loads if load["flags"] & 2 and
                      load["address"] <= s["address"] < load["address"] + load["memorySize"]]
        safe = end % minimum == 0 or (bool(containing) and all(
            load["address"] + load["memorySize"] == end for load in containing))
        relro.append(dict(end=end, boundary=minimum, safe=safe))
    return dict(bits=64 if wide else 32, loadPass=load_ok, relro=relro,
                state="PASS" if load_ok and all(s["safe"] for s in relro) else "FAIL",
                segments=segments)


def inspect_apk(path):
    rows = []
    with zipfile.ZipFile(path) as archive, path.open("rb") as stream:
        for info in archive.infolist():
            if not info.filename.startswith("lib/") or not info.filename.endswith(".so"):
                continue
            data = archive.read(info)
            row = inspect_elf(data)
            stream.seek(info.header_offset)
            header = stream.read(30)
            name_size, extra_size = struct.unpack_from("<HH", header, 26)
            position = info.header_offset + 30 + name_size + extra_size
            row.update(file=info.filename, sha256=hashlib.sha256(data).hexdigest(),
                       compressed=info.compress_type != zipfile.ZIP_STORED, zipOffset=position)
            row["zipPass"] = row["compressed"] or position % (16384 if row["bits"] == 64 else 4096) == 0
            if not row["zipPass"]:
                row["state"] = "FAIL"
            rows.append(row)
    return dict(artifact=path.name, sha256=hashlib.sha256(path.read_bytes()).hexdigest(),
                state="PASS" if rows and all(row["state"] == "PASS" for row in rows) else "FAIL",
                libraries=rows, runtime="NOT_EXECUTED", productionSignature="NOT_CONFIGURED")


if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument("apk", type=Path)
    parser.add_argument("--output", type=Path, required=True)
    args = parser.parse_args()
    try:
        result = inspect_apk(args.apk)
    except (OSError, ValueError, struct.error, zipfile.BadZipFile):
        result = dict(state="ERROR", artifact=args.apk.name, libraries=[],
                      reason="Missing APK, invalid ZIP or unsupported/truncated ELF")
    args.output.parent.mkdir(parents=True, exist_ok=True)
    args.output.write_text(json.dumps(result, indent=2) + "\n", encoding="utf-8")
    print(json.dumps({"state": result["state"], "libraries": len(result["libraries"]),
                      "failed": [row["file"] for row in result["libraries"] if row["state"] != "PASS"]}))
    raise SystemExit(0 if result["state"] == "PASS" else 1)
