"""Runs only isolated, offline Android tests; never supplies live account flags or credentials."""
import argparse
import hashlib
import importlib.util
import json
import os
import re
from pathlib import Path
import subprocess
import sys
import xml.etree.ElementTree as ET

ROOT = Path(__file__).resolve().parents[1]
OUT = ROOT / "app/build/outputs/instrumented-offline"
module = importlib.util.spec_from_file_location("ci_checks", ROOT / ".github/scripts/android-ci-checks.py")
checks = importlib.util.module_from_spec(module)
module.loader.exec_module(checks)
EXPECTED = checks.EXPECTED_DEVICE


def parse_status(output):
    cases, fields = {}, {}
    for line in output.splitlines():
        if line.startswith("INSTRUMENTATION_STATUS: "):
            key, separator, value = line[len("INSTRUMENTATION_STATUS: "):].partition("=")
            if separator and key in ("class", "test"):
                fields[key] = value.strip()
        if line.startswith("INSTRUMENTATION_STATUS_CODE:"):
            code = int(line.partition(":")[2].strip())
            key = (fields.get("class"), fields.get("test"))
            if all(key) and code != 1:
                if key in cases:
                    raise ValueError("Duplicate Android result")
                cases[key] = "PASS" if code == 0 else "SKIPPED" if code in (-3, -4) else "FAIL"
            fields = {}
    return cases


def failure_diagnostics(output):
    """Retain exception types and code locations, never assertion/provider messages."""
    diagnostics, fields, stack = {}, {}, []
    collecting = False
    for line in output.splitlines():
        if line.startswith("INSTRUMENTATION_STATUS: "):
            key, _, value = line[len("INSTRUMENTATION_STATUS: "):].partition("=")
            collecting = key == "stack"
            if key in ("class", "test"):
                fields[key] = value.strip()
            if collecting:
                stack = [value]
        elif line.startswith("INSTRUMENTATION_STATUS_CODE:"):
            key = (fields.get("class"), fields.get("test"))
            if all(key) and int(line.partition(":")[2].strip()) in (-1, -2):
                safe = []
                for entry in stack:
                    exception = re.match(r"^(?:Caused by: )?((?:[A-Za-z_$][\w$]*\.)*[A-Za-z_$][\w$]*(?:Exception|Error))(?=:|$)", entry.strip())
                    frame = re.fullmatch(r"\s*at ((?:com\.pactogg\.app|androidx|android|java|kotlin|kotlinx)\.[\w.$]+\([\w.$]+\.(?:kt|java):\d+\))\s*", entry)
                    if exception:
                        safe.append(exception.group(1))
                    elif frame:
                        safe.append("at " + frame.group(1))
                diagnostics[key] = "\n".join(safe[:40]) or "No safe exception type or code location supplied by runner"
            fields, stack, collecting = {}, [], False
        elif collecting:
            stack.append(line)
    return diagnostics


def write_junit(cases, diagnostics=None):
    root = ET.Element("testsuite", name="AndroidOffline", tests=str(len(cases)),
                      failures=str(sum(v == "FAIL" for v in cases.values())),
                      skipped=str(sum(v == "SKIPPED" for v in cases.values())))
    for (klass, name), result in sorted(cases.items()):
        case = ET.SubElement(root, "testcase", classname=klass, name=name)
        if result == "FAIL":
            failure = ET.SubElement(case, "failure", message="Android failure; messages and UI/token data redacted")
            failure.text = (diagnostics or {}).get((klass, name), "No safe diagnostic available")
        elif result == "SKIPPED":
            ET.SubElement(case, "skipped")
    OUT.mkdir(parents=True, exist_ok=True)
    ET.ElementTree(root).write(OUT / "TEST-AndroidOffline.xml", encoding="utf-8", xml_declaration=True)


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--device", required=True, help="Explicit serial; use a fresh isolated emulator")
    args = parser.parse_args()
    sdk = os.environ.get("ANDROID_HOME") or os.environ.get("ANDROID_SDK_ROOT")
    adb = str(Path(sdk) / "platform-tools" / ("adb.exe" if os.name == "nt" else "adb")) if sdk else "adb"
    cases = {}
    diagnostics = {}
    environment = {"state": "ERROR", "expectedTests": len(EXPECTED),
                   "deviceHash": hashlib.sha256(args.device.encode()).hexdigest()}
    def call(*command, timeout=60):
        result = subprocess.run([adb, "-s", args.device, *command], capture_output=True, text=True,
                                encoding="utf-8", errors="replace", timeout=timeout, check=True)
        return result.stdout
    try:
        if call("get-state").strip() != "device":
            raise ValueError("Selected device is not online")
        environment["api"] = call("shell", "getprop", "ro.build.version.sdk").strip()
        environment["abi"] = call("shell", "getprop", "ro.product.cpu.abi").strip()
        for relative in ("app/build/outputs/apk/debug/app-debug.apk",
                         "app/build/outputs/apk/androidTest/debug/app-debug-androidTest.apk"):
            apk = ROOT / relative
            environment[apk.name + "Sha256"] = checks.sha(apk)
            call("install", "-r", str(apk), timeout=120)
        classes = ",".join(sorted({klass for klass, _ in EXPECTED}))
        output = call("shell", "am", "instrument", "-w", "-r", "-e", "class", classes,
                      "com.pactogg.app.test/androidx.test.runner.AndroidJUnitRunner", timeout=300)
        cases = parse_status(output)
        diagnostics = failure_diagnostics(output)
        environment["missing"] = ["::".join(key) for key in sorted(EXPECTED - set(cases))]
        failed = any(v != "PASS" for v in cases.values()) or set(cases) != EXPECTED
        final_codes = [line.partition(":")[2].strip() for line in output.splitlines()
                       if line.startswith("INSTRUMENTATION_CODE:")]
        crashed = "INSTRUMENTATION_FAILED" in output or "Process crashed" in output
        completed = final_codes == ["-1"]
        environment["state"] = "PASS" if not failed and not crashed and completed else "FAIL"
    except (OSError, ValueError, subprocess.SubprocessError):
        environment["reason"] = "Device, install or instrumentation environment failed; no fabricated successful cases"
    write_junit(cases, diagnostics)
    (OUT / "run.json").write_text(json.dumps(environment, indent=2) + "\n", encoding="utf-8")
    print(json.dumps({"state": environment["state"], "tests": len(cases),
                      "failures": sum(v == "FAIL" for v in cases.values()),
                      "skipped": sum(v == "SKIPPED" for v in cases.values())}))
    return 0 if environment["state"] == "PASS" else 1


if __name__ == "__main__":
    sys.exit(main())
