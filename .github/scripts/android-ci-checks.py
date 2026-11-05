"""Local and CI gates: exact JUnit cases, nonempty coverage and current-run artifacts."""
import argparse
import hashlib
import importlib.util
import json
import os
from pathlib import Path
import sys
import xml.etree.ElementTree as ET

ROOT = Path(__file__).resolve().parents[2]
MATRIX = ROOT / "docs/pruebas/operaciones.json"
OUT = ROOT / "artifacts/android-ci"
KINDS = ("unit", "integration", "security")
EXPECTED_DEVICE = {
    ("com.pactogg.app.core.database.CacheMetadataDaoTest", "upsertReplacesTimestampAndClearRemovesData"),
    ("com.pactogg.app.security.EncryptedSessionStoreTest", "roundTripIsEncryptedAndClearRemovesTheFile"),
    ("com.pactogg.app.security.EncryptedSessionStoreTest", "corruptedOrMissingKeyDoesNotRestoreAnAccount"),
    ("com.pactogg.app.navigation.AccessNavigationTest", "emptyLoginCannotBypassAuthentication"),
    ("com.pactogg.app.navigation.AccessNavigationTest", "registerDisplaysProvisionalPrivacyBeforeAnySubmission"),
}
REQUIRED_JOBS = ("integrity", "static-analysis", "tests", "coverage", "build", "instrumented",
                 "secrets", "sast", "dependencies")


def sha(path):
    return hashlib.sha256(path.read_bytes()).hexdigest()


def execution():
    return {"run": os.environ.get("GITHUB_RUN_ID", "local"),
            "attempt": os.environ.get("GITHUB_RUN_ATTEMPT", "1"),
            "source": os.environ.get("GITHUB_SHA", "LOCAL_WORKTREE"),
            "helperSha256": sha(Path(__file__)), "matrixSha256": sha(MATRIX)}


def save(stage, data):
    OUT.mkdir(parents=True, exist_ok=True)
    (OUT / (stage + ".json")).write_text(json.dumps(data, indent=2) + "\n", encoding="utf-8")


def matrix():
    document = json.loads(MATRIX.read_text(encoding="utf-8"))
    rows = document["endpoints"]
    classes = document["category_classes"]
    flat = [name for kind in KINDS for name in classes[kind]]
    if len(flat) != len(set(flat)) or sum(bool(row["protected"]) for row in rows) != 15:
        raise ValueError("Overlapping categories or changed protection scope")
    ids = [r["id"] for r in rows]
    if len(ids) != 19 or len(set(ids)) != 19:
        raise ValueError("Expected 19 unique confirmed operations")
    spec_ids = {p.name.split("-", 3)[1] + "-" + p.name.split("-", 3)[2]
                for p in (ROOT / "specs").glob("MO-*") if p.is_dir()}
    if set(ids) != spec_ids:
        raise ValueError("Operation/spec mismatch")
    used = set()
    for row in rows:
        if not (ROOT / row["spec"]).resolve().is_relative_to(ROOT) or not (ROOT / row["spec"]).is_file() or not row["path"].startswith("/api/v1/"):
            raise ValueError("Invalid spec or route")
        for kind in KINDS:
            case = row["tests"][kind]
            key = (kind, case["class"], case["name"])
            if key in used or case["class"] not in classes[kind] or not case["name"].endswith("[" + row["id"] + "]"):
                raise ValueError("Duplicate or non-exact test mapping")
            used.add(key)
    return rows


def expected(kind):
    if kind == "instrumented":
        return EXPECTED_DEVICE
    result = set()
    for current in KINDS if kind == "all" else (kind,):
        for case in json.loads(MATRIX.read_text(encoding="utf-8")).get("required_regressions", {}).get(current, []):
            result.add((case["class"], case["name"]))
    for row in matrix():
        for current in KINDS if kind == "all" else (kind,):
            case = row["tests"][current]
            result.add((case["class"], case["name"]))
        if row["protected"] and kind in ("all", "security"):
            result.add(("com.pactogg.app.testing.ProtectedEndpointSessionTest",
                        "noProtectedRequestLeavesDeviceWithoutSession[" + row["id"] + "]"))
    return result


def wrong_category(kind, cases):
    if kind == "instrumented":
        return set(cases) - EXPECTED_DEVICE
    classes = json.loads(MATRIX.read_text(encoding="utf-8"))["category_classes"]
    allowed = set(name for current in (KINDS if kind == "all" else (kind,)) for name in classes[current])
    return {key for key in cases if key[0] not in allowed}


def junit(directory):
    cases = {}
    reports = sorted(directory.rglob("TEST*.xml"))
    if not reports:
        raise ValueError("JUnit report absent")
    for file in reports:
        root = ET.parse(file).getroot()
        for case in root.iter("testcase"):
            key = (case.get("classname"), case.get("name"))
            if not all(key) or key in cases:
                raise ValueError("Missing identity or duplicate/stale JUnit case")
            cases[key] = "FAIL" if case.find("failure") is not None or case.find("error") is not None else (
                "SKIPPED" if case.find("skipped") is not None else "PASS")
    if not cases:
        raise ValueError("Zero executed cases")
    return cases, reports


def tests(kind, directory):
    report = {"execution": execution(), "stage": kind, "state": "ERROR"}
    try:
        cases, files = junit(directory)
        missing = expected(kind) - set(cases)
        misplaced = wrong_category(kind, cases)
        if kind == "instrumented":
            environment_path = directory / "run.json"
            environment = json.loads(environment_path.read_text(encoding="utf-8"))
            if environment["state"] != "PASS" or not environment.get("api") or not environment.get("abi"):
                raise ValueError("Instrumented environment failed")
            files.append(environment_path)
        report.update(tests=len(cases), failures=sum(v == "FAIL" for v in cases.values()),
                      skipped=sum(v == "SKIPPED" for v in cases.values()),
                      missing=["::".join(v) for v in sorted(missing)],
                      wrongCategory=["::".join(v) for v in sorted(misplaced)],
                      reports={str(p.relative_to(ROOT)).replace("\\", "/"): sha(p) for p in files})
        report["state"] = "PASS" if not missing and not misplaced and all(v == "PASS" for v in cases.values()) else "FAIL"
    except (OSError, ValueError, KeyError, TypeError, ET.ParseError):
        report["reason"] = "Missing/invalid JUnit or operation mapping"
    save(kind, report)
    return report


def coverage(directory):
    report = {"execution": execution(), "stage": "coverage", "state": "ERROR", "engine": "JaCoCo 0.8.14"}
    try:
        files = [p for p in directory.rglob("*.xml") if ET.parse(p).getroot().tag == "report"]
        if len(files) != 1:
            raise ValueError("Expected one JaCoCo XML")
        root = ET.parse(files[0]).getroot()
        line = next(c for c in root.findall("counter") if c.get("type") == "LINE")
        covered, missed = int(line.get("covered")), int(line.get("missed"))
        if covered + missed <= 0:
            raise ValueError("Coverage inventory empty")
        report.update(state="PASS", covered=covered, missed=missed,
                      linePercent=100 * covered / (covered + missed), threshold=None,
                      reports={str(files[0].relative_to(ROOT)).replace("\\", "/"): sha(files[0])})
    except (OSError, ValueError, KeyError, TypeError, ET.ParseError, StopIteration):
        report["reason"] = "Coverage XML absent, ambiguous or empty"
    save("coverage", report)
    return report


def build():
    report = {"execution": execution(), "stage": "build", "state": "ERROR"}
    files = [ROOT / "app/build/outputs/apk/debug/app-debug.apk",
             ROOT / "app/build/outputs/apk/androidTest/debug/app-debug-androidTest.apk"]
    if all(p.is_file() and p.stat().st_size > 0 for p in files):
        report.update(state="PASS", reports={str(p.relative_to(ROOT)).replace("\\", "/"): sha(p) for p in files})
    save("build", report)
    return report


def validate_artifact(path, stage):
    report_path = path / "artifacts/android-ci" / (stage + ".json")
    report = json.loads(report_path.read_text(encoding="utf-8"))
    if report["state"] != "PASS" or report["stage"] != stage or report["execution"] != execution():
        raise ValueError("Failed, foreign or stale artifact")
    files = report["reports"]
    if not files:
        raise ValueError("Missing inventory")
    for name, digest in files.items():
        file = (path / name).resolve()
        if not file.is_relative_to(path.resolve()) or sha(file) != digest:
            raise ValueError("Changed or escaping report")
    if stage in (*KINDS, "all", "instrumented"):
        cases, _ = junit(path / ("app/build/outputs/instrumented-offline" if stage == "instrumented"
                               else "app/build/test-results/testDebugUnitTest" if stage == "all"
                               else "app/build/test-results/testDebugUnitTest"))
        if expected(stage) - set(cases) or wrong_category(stage, cases) or any(v != "PASS" for v in cases.values()):
            raise ValueError("Executed cases missing or unsuccessful")
        if len(cases) != report["tests"] or report["failures"] or report["skipped"]:
            raise ValueError("Contradictory counts")
    if stage == "coverage":
        xml = next(path / name for name in files if name.endswith(".xml"))
        counters = ET.parse(xml).getroot().findall("counter")
        line = next(c for c in counters if c.get("type") == "LINE")
        if (int(line.get("covered")), int(line.get("missed"))) != (report["covered"], report["missed"]):
            raise ValueError("Contradictory coverage")
        if report["covered"] + report["missed"] <= 0:
            raise ValueError("Empty coverage")
    return report


def gate(inputs, needs):
    controls = {name: needs.get(name, {}).get("result", "missing") for name in REQUIRED_JOBS}
    report = {"execution": execution(), "stage": "gate", "jobs": controls, "state": "ERROR", "controls": {}}
    try:
        if any(state != "success" for state in controls.values()):
            raise ValueError("Required job failed, skipped, cancelled or missing")
        validate_artifact(inputs / "android-coverage", "all")
        for kind in (*KINDS, "coverage", "build", "instrumented"):
            report["controls"][kind] = validate_artifact(inputs / ("android-" + kind), kind)["state"]
        candidate = validate_artifact(inputs / "android-build", "build")
        device = json.loads((inputs / "android-instrumented/app/build/outputs/instrumented-offline/run.json")
                            .read_text(encoding="utf-8"))
        for relative, digest in candidate["reports"].items():
            if device.get(Path(relative).name + "Sha256") != digest:
                raise ValueError("Device did not execute the current candidate")
        # Reuse scanner sanitization and execution/config checks. Never accept only a green upload step.
        spec = importlib.util.spec_from_file_location("scanner", Path(__file__).with_name("android-evidence.py"))
        scanner = importlib.util.module_from_spec(spec)
        spec.loader.exec_module(scanner)
        for name, artifact in (("gitleaks", "secrets"), ("semgrep", "sast"), ("trivy", "dependencies")):
            base = inputs / ("android-" + artifact)
            files = list(base.rglob(name + ".json"))
            if len(files) != 1:
                raise ValueError("Scanner artifact absent or ambiguous")
            data = json.loads(files[0].read_text(encoding="utf-8"))
            wanted = scanner.execution()
            if data["state"] != "PASS" or data.get("tool") != scanner.IMAGES[name] or data.get("exitCode") != 0 or data.get("findings") != []:
                raise ValueError("Scanner not clean")
            if any(data["execution"][key] != wanted[key] for key in ("run", "attempt", "source", "helperSha256")):
                raise ValueError("Scanner from another execution")
            source = ROOT / (".gitleaks.toml" if name == "gitleaks" else ".semgrep.yml")
            if name == "trivy":
                boms = list(base.rglob("bom.json"))
                if len(boms) != 1:
                    raise ValueError("SBOM inventory absent or ambiguous")
                source = boms[0]
                bom = json.loads(source.read_text(encoding="utf-8"))
                if bom.get("bomFormat") != "CycloneDX" or not bom.get("components"):
                    raise ValueError("Empty SBOM")
            if data["inputSha256"] != sha(source):
                raise ValueError("Changed scanner input")
            report["controls"][name] = "PASS"
        report["state"] = "PASS"
    except (OSError, ValueError, KeyError, TypeError, ET.ParseError) as error:
        report["reason"] = str(error) if isinstance(error, ValueError) else "Missing or invalid evidence"
    save("gate", report)
    summary = os.environ.get("GITHUB_STEP_SUMMARY")
    if summary:
        with open(summary, "a", encoding="utf-8") as stream:
            stream.write("## Android PR quality gate\n\n| Control | Resultado |\n|---|---|\n")
            for key, value in {**controls, **report["controls"]}.items():
                stream.write(f"| {key} | {value} |\n")
            stream.write("\nResultado: **" + report["state"] + "**. Omisión no significa PASS.\n")
    return report


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("stage", choices=(*KINDS, "all", "instrumented", "coverage", "matrix", "build", "gate", "candidate"))
    parser.add_argument("--directory", type=Path)
    args = parser.parse_args()
    if args.stage == "matrix":
        matrix(); print("19 operations, three test categories, exact identities: OK"); return 0
    if args.stage in (*KINDS, "all", "instrumented"):
        directory = args.directory or ROOT / ("app/build/outputs/instrumented-offline" if args.stage == "instrumented"
            else "app/build/test-results/testDebugUnitTest" if args.stage == "all"
            else "app/build/test-results/testDebugUnitTest")
        report = tests(args.stage, directory)
    elif args.stage == "coverage":
        report = coverage(args.directory or ROOT / "app/build/reports/coverage/test/debug")
    elif args.stage == "build": report = build()
    elif args.stage == "candidate":
        report = validate_artifact(args.directory or ROOT, "build")
    else: report = gate(args.directory or ROOT / "artifacts/ci-input", json.loads(os.environ.get("NEEDS_JSON", "{}")))
    print(json.dumps({k: v for k, v in report.items() if k in ("stage", "state", "tests", "failures", "skipped", "missing")}))
    return 0 if report["state"] == "PASS" else 1


if __name__ == "__main__":
    sys.exit(main())
