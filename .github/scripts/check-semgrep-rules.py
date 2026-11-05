"""Exercise unchanged production rules/paths with positive, negative and out-of-scope fixtures."""
import hashlib
import importlib.util
import json
import os
from pathlib import Path
import shutil
import subprocess
import tempfile

ROOT = Path(__file__).resolve().parents[2]
spec = importlib.util.spec_from_file_location("scanner", Path(__file__).with_name("android-evidence.py"))
scanner = importlib.util.module_from_spec(spec)
spec.loader.exec_module(scanner)


def expected_cases(source):
    expected = set()
    negatives = set()
    for number, line in enumerate(source.splitlines(), 1):
        if "// ruleid: " in line:
            expected.add((line.split("// ruleid: ")[1].strip(), number + 1))
        if "// ok: " in line:
            negatives.add((line.split("// ok: ")[1].strip(), number + 1))
    if len(expected) != 3 or len(negatives) != 3 or {x[0] for x in expected} != {x[0] for x in negatives}:
        raise ValueError("Three positive/negative rule pairs required")
    return expected


def verify(data, code, expected):
    if code != 1 or data.get("errors") or not data.get("results"):
        raise ValueError("Engine, missing findings or invalid response")
    rows = data["results"]
    found = {(r["check_id"], r["start"]["line"]) for r in rows
             if r["path"].endswith("app/src/main/security/RuleCases.kt")}
    if found != expected or len(rows) != len(expected):
        raise ValueError("Missed positive, unexpected negative or path scope regression")


def main():
    config = ROOT / ".semgrep.yml"
    fixture = ROOT / ".semgrep-tests/cases.kt"
    report = dict(state="ERROR", execution=scanner.execution(), tool=scanner.IMAGES["semgrep"],
                  configSha256=scanner.digest(config), fixtureSha256=scanner.digest(fixture))
    try:
        expected = expected_cases(fixture.read_text(encoding="utf-8"))
        with tempfile.TemporaryDirectory(prefix="pactogg-rule-check-", dir=os.environ.get("RUNNER_TEMP")) as tmp:
            base = Path(tmp)
            for scope in ("main/security", "test/security"):
                target = base / "app/src" / scope / "RuleCases.kt"
                target.parent.mkdir(parents=True)
                shutil.copy2(fixture, target)
            shutil.copy2(config, base / ".semgrep.yml")
            command = ["docker", "run", "--rm", "-v", f"{base}:/src", "-w", "/src",
                       scanner.IMAGES["semgrep"], "semgrep", "scan", "--config", "/src/.semgrep.yml",
                       "--error", "--metrics=off", "--disable-version-check", "--json", "--output", "/src/result.json", "/src/app/src"]
            result = subprocess.run(command, capture_output=True, timeout=600, check=False)
            data = json.loads((base / "result.json").read_text(encoding="utf-8"))
            verify(data, result.returncode, expected)
            report.update(state="PASS", positive=3, negative=3, outsideProductionPath=3)
    except (OSError, ValueError, KeyError, TypeError, subprocess.SubprocessError):
        report["reason"] = "Rules/fixtures, engine, expected detections or scope verification failed"
    scanner.save(scanner.OUT / "semgrep-rules.json", report)
    print(json.dumps({k: v for k, v in report.items() if k in ("state", "positive", "negative", "outsideProductionPath")}))
    return 0 if report["state"] == "PASS" else 1


if __name__ == "__main__":
    raise SystemExit(main())
