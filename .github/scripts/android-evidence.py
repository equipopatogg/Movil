"""Bounded Android CI evidence; raw scanner output never reaches logs/artifacts."""
import hashlib
import json
import os
from pathlib import Path
import subprocess
import sys
import tempfile
import xml.etree.ElementTree as ET

ROOT = Path(__file__).resolve().parents[2]
OUT = ROOT / "artifacts/android-security"
BOM = ROOT / "app/build/reports/cyclonedx-direct/bom.json"
IMAGES = {
    "gitleaks": "ghcr.io/gitleaks/gitleaks:v8.30.1@sha256:c00b6bd0aeb3071cbcb79009cb16a60dd9e0a7c60e2be9ab65d25e6bc8abbb7f",
    "semgrep": "semgrep/semgrep:1.182.0@sha256:4a34048c04fef049f71665047f074cffb00cc3d89482f965a5cd151d589237af",
    "trivy": "aquasec/trivy:0.76.1@sha256:790fd66d1697395d8693d1acaa1ea702406a20f109b6fc7bac8db623eca6c06c",
}


def digest(path):
    return hashlib.sha256(path.read_bytes()).hexdigest()


def execution():
    return {"run": os.environ.get("GITHUB_RUN_ID", "local-no-ci"),
            "attempt": os.environ.get("GITHUB_RUN_ATTEMPT", "1"),
            "job": os.environ.get("GITHUB_JOB", "local"),
            "source": os.environ.get("GITHUB_SHA", "NO_GIT"),
            "helperSha256": digest(Path(__file__))}


def save(path, data):
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(json.dumps(data, indent=2) + "\n", encoding="utf-8")


def sanitized(name, data, code):
    """Whitelist fields only: never copy Secret/Match/Lines/messages/snippets."""
    if name == "gitleaks":
        if not isinstance(data, list):
            raise ValueError("Gitleaks report must be an array")
        findings = [{"rule": f["RuleID"], "file": f["File"], "line": f["StartLine"]} for f in data]
        findings_code = 10
    elif name == "semgrep":
        findings = [{"rule": f["check_id"], "file": f["path"], "line": f["start"]["line"]} for f in data["results"]]
        if data.get("errors"):
            return "ERROR", findings
        findings_code = 1
    else:
        if data.get("SchemaVersion") != 2 or not isinstance(data.get("Results"), list):
            raise ValueError("Trivy schema/inventory missing")
        findings = [{"id": f["VulnerabilityID"], "package": f["PkgName"], "severity": f["Severity"]}
                    for target in data["Results"] for f in target.get("Vulnerabilities", [])]
        findings_code = 10
    if code == findings_code and findings:
        return "FAIL", findings
    return ("PASS" if code == 0 and not findings else "ERROR"), findings


def scan(name):
    report = {"execution": execution(), "tool": IMAGES[name], "state": "ERROR", "exitCode": None,
              "scope": "Android current files" if name != "trivy" else "releaseRuntimeClasspath SBOM"}
    try:
        with tempfile.TemporaryDirectory(prefix="pactogg-android-scan-", dir=os.environ.get("RUNNER_TEMP")) as temporary:
            base = ["docker", "run", "--rm", "-v", f"{ROOT}:/src:ro", "-v", f"{temporary}:/reports", "-w", "/src", IMAGES[name]]
            if name == "gitleaks":
                config = ROOT / ".gitleaks.toml"
                command = ["dir", "/src/Movil", "--config", "/src/.gitleaks.toml", "--redact=100", "--no-banner",
                           "--log-level", "error", "--report-format", "json", "--report-path", "/reports/raw.json", "--exit-code", "10"]
            elif name == "semgrep":
                config = ROOT / ".semgrep.yml"
                command = ["semgrep", "scan", "--config", "/src/.semgrep.yml", "--error", "--metrics=off",
                           "--disable-version-check", "--json", "--output", "/reports/raw.json", "/src/app/src/main"]
            else:
                config = BOM
                bom = json.loads(BOM.read_text(encoding="utf-8"))
                if bom["bomFormat"] != "CycloneDX" or not bom["components"]:
                    raise ValueError("Missing SBOM inventory")
                command = ["sbom", "--severity", "HIGH,CRITICAL", "--exit-code", "10", "--format", "json", "--output",
                           "/reports/raw.json", "/src/app/build/reports/cyclonedx-direct/bom.json"]
            report["inputSha256"] = digest(config)
            process = subprocess.run(base + command, stdout=subprocess.PIPE, stderr=subprocess.PIPE, timeout=600, check=False)
            report["exitCode"] = process.returncode
            data = json.loads((Path(temporary) / "raw.json").read_text(encoding="utf-8"))
            report["state"], report["findings"] = sanitized(name, data, process.returncode)
    except (OSError, ValueError, KeyError, TypeError, subprocess.SubprocessError):
        report["state"] = "ERROR"
        report["reason"] = "Tool unavailable, invalid/missing report or prerequisite; raw output not retained."
    save(OUT / f"{name}.json", report)
    return 0 if report["state"] == "PASS" else 1


def summary(names):
    steps = json.loads(os.environ["STEPS_JSON"])
    controls = {}
    for name in names:
        path = OUT / f"{name}.json"
        outcome = steps.get(name, {}).get("outcome", "skipped")
        entry = {"state": "NO_EJECUTADO" if outcome in ("skipped", "cancelled") else "ERROR"}
        try:
            data = json.loads(path.read_text(encoding="utf-8"))
            expected = "success" if data["state"] == "PASS" else "failure"
            config = ROOT / (".gitleaks.toml" if name == "gitleaks" else ".semgrep.yml") if name != "trivy" else BOM
            if data["execution"] == execution() and outcome == expected and data["inputSha256"] == digest(config):
                entry = {"state": data["state"], "reportSha256": digest(path)}
        except (OSError, ValueError, KeyError, TypeError):
            pass
        controls[name] = entry
    passed = all(c["state"] == "PASS" for c in controls.values())
    state = "PASS" if passed else "FAIL" if any(c["state"] == "FAIL" for c in controls.values()) else "ERROR"
    save(OUT / "summary.json", {"execution": execution(), "controls": controls, "state": state})
    return 0 if passed else 1


def quality():
    steps = json.loads(os.environ["STEPS_JSON"])
    report = {"execution": execution(), "steps": {key: value["outcome"] for key, value in steps.items()}, "state": "ERROR"}
    try:
        rows = [ET.parse(p).getroot() for p in (ROOT / "app/build/test-results/testDebugUnitTest").glob("TEST-*.xml")]
        tests = sum(int(r.attrib["tests"]) for r in rows)
        failures = sum(int(r.attrib.get("failures", 0)) + int(r.attrib.get("errors", 0)) for r in rows)
        skipped = sum(int(r.attrib.get("skipped", 0)) for r in rows)
        bom = json.loads(BOM.read_text(encoding="utf-8"))
        report.update(tests=tests, failures=failures, skipped=skipped, sbomSha256=digest(BOM))
        if tests - skipped > 0 and failures == 0 and bom["bomFormat"] == "CycloneDX" and bom["components"] and all(steps.get(k, {}).get("outcome") == "success" for k in ("checkout", "java", "gradle", "sdk", "build", "sbom")):
            report["state"] = "PASS"
    except (OSError, ValueError, KeyError, TypeError, ET.ParseError):
        pass
    save(ROOT / "artifacts/android-quality/summary.json", report)
    return 0 if report["state"] == "PASS" else 1


if __name__ == "__main__":
    if sys.argv[1] == "scan" and sys.argv[2] in IMAGES:
        sys.exit(scan(sys.argv[2]))
    if sys.argv[1] == "summary" and sys.argv[2:] and all(n in IMAGES for n in sys.argv[2:]):
        sys.exit(summary(sys.argv[2:]))
    if sys.argv[1] == "quality":
        sys.exit(quality())
    sys.exit(2)
