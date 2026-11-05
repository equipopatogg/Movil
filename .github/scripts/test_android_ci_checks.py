"""Negative controls for exact traceability, stale evidence, omissions and final CI gate."""
import copy
import hashlib
import importlib.util
import json
import os
from pathlib import Path
import shutil
import tempfile
import unittest
from unittest.mock import patch
import xml.etree.ElementTree as ET

spec = importlib.util.spec_from_file_location("checks", Path(__file__).with_name("android-ci-checks.py"))
checks = importlib.util.module_from_spec(spec)
spec.loader.exec_module(checks)
runner_spec = importlib.util.spec_from_file_location("runner", checks.ROOT / "scripts/run-instrumented.py")
runner = importlib.util.module_from_spec(runner_spec)
runner_spec.loader.exec_module(runner)


class CiChecksTests(unittest.TestCase):
    def setUp(self):
        self.temp = tempfile.TemporaryDirectory(prefix="pactogg-ci-policy-")
        self.root = Path(self.temp.name).resolve()
        self.matrix = self.root / "docs/pruebas/operaciones.json"
        self.matrix.parent.mkdir(parents=True)
        self.matrix.write_bytes(checks.MATRIX.read_bytes())
        for row in json.loads(self.matrix.read_text())["endpoints"]:
            p = self.root / row["spec"]
            p.parent.mkdir(parents=True, exist_ok=True); p.write_text("# Synthetic spec")
        self.patches = [patch.object(checks, "ROOT", self.root), patch.object(checks, "MATRIX", self.matrix),
                        patch.object(checks, "OUT", self.root / "artifacts/android-ci")]
        for item in self.patches: item.start()
    def tearDown(self):
        for item in reversed(self.patches): item.stop()
        self.temp.cleanup()
    def xml(self, kind, mutation=None):
        relative = "app/build/outputs/instrumented-offline" if kind == "instrumented" else (
            "app/build/test-results/testDebugUnitTest" if kind == "all" else "app/build/test-results/testDebugUnitTest")
        directory = self.root / relative; directory.mkdir(parents=True, exist_ok=True)
        root = ET.Element("testsuite")
        for klass, name in sorted(checks.expected(kind)):
            ET.SubElement(root, "testcase", classname=klass, name=name)
        if mutation: mutation(root)
        ET.ElementTree(root).write(directory / "TEST-Fixture.xml")
        if kind == "instrumented":
            (directory / "run.json").write_text(json.dumps({"state":"PASS","api":"35","abi":"x86_64",
                "app-debug.apkSha256":hashlib.sha256(b"synthetic-apk").hexdigest(),
                "app-debug-androidTest.apkSha256":hashlib.sha256(b"synthetic-apk").hexdigest()}))
        return directory
    def test_exact_matrix_and_no_duplicate_or_escaping_spec(self):
        self.assertEqual(19, len(checks.matrix()))
        data = json.loads(self.matrix.read_text()); original = copy.deepcopy(data)
        data["endpoints"][1]["id"] = data["endpoints"][0]["id"]
        self.matrix.write_text(json.dumps(data))
        with self.assertRaises(ValueError): checks.matrix()
        original["endpoints"][0]["spec"] = "../outside.md"
        self.matrix.write_text(json.dumps(original))
        with self.assertRaises(ValueError): checks.matrix()
    def test_all_three_kinds_have_real_exact_results(self):
        for kind in checks.KINDS:
            result = checks.tests(kind, self.xml(kind))
            self.assertEqual("PASS", result["state"])
            self.assertGreaterEqual(result["tests"], 19)
    def test_zero_missing_failure_skip_and_prefix_are_not_pass(self):
        mutations = [lambda root: root.clear(), lambda root: root.remove(list(root)[0]),
                     lambda root: ET.SubElement(list(root)[0], "failure"),
                     lambda root: ET.SubElement(list(root)[0], "skipped"),
                     lambda root: list(root)[0].set("name", list(root)[0].get("name") + "-partial")]
        for mutation in mutations:
            self.assertNotEqual("PASS", checks.tests("unit", self.xml("unit", mutation))["state"])
    def test_wrong_category_is_not_counted_as_unit_success(self):
        directory = self.xml("unit", lambda root: ET.SubElement(root, "testcase",
            classname="com.pactogg.app.testing.EndpointContractTest",
            name="consumesConfirmedWireContract[USR-01]"))
        self.assertEqual("FAIL", checks.tests("unit", directory)["state"])

    def test_duplicate_stale_changed_and_escaping_artifacts_fail(self):
        directory = self.xml("unit")
        checks.tests("unit", directory)
        checks.validate_artifact(self.root, "unit")
        file = directory / "TEST-Fixture.xml"
        file.write_text(file.read_text() + " ")
        with self.assertRaises(ValueError): checks.validate_artifact(self.root, "unit")
        checks.tests("unit", directory)
        summary = checks.OUT / "unit.json"; data = json.loads(summary.read_text())
        data["execution"]["run"] = "stale"; summary.write_text(json.dumps(data))
        with self.assertRaises(ValueError): checks.validate_artifact(self.root, "unit")
        checks.tests("unit", directory)
        data = json.loads(summary.read_text()); data["reports"] = {"../outside.xml":"bad"}
        summary.write_text(json.dumps(data))
        with self.assertRaises(ValueError): checks.validate_artifact(self.root, "unit")
    def test_coverage_requires_inventory_and_never_invents_threshold(self):
        directory = self.root / "app/build/reports/coverage/test/debug"; directory.mkdir(parents=True)
        self.assertEqual("ERROR", checks.coverage(directory)["state"])
        (directory / "report.xml").write_text('<report><counter type="LINE" covered="1" missed="3"/></report>')
        report = checks.coverage(directory)
        self.assertEqual("PASS", report["state"]); self.assertEqual(25, report["linePercent"])
        self.assertIsNone(report["threshold"])
        (directory / "report.xml").write_text('<report><counter type="LINE" covered="0" missed="0"/></report>')
        self.assertEqual("ERROR", checks.coverage(directory)["state"])
    def test_instrumented_environment_is_required_even_when_cases_pass(self):
        directory = self.xml("instrumented")
        self.assertEqual("PASS", checks.tests("instrumented", directory)["state"])
        (directory / "run.json").write_text('{"state":"ERROR"}')
        self.assertEqual("ERROR", checks.tests("instrumented", directory)["state"])
    def test_required_failed_skipped_cancelled_and_missing_jobs_are_not_pass(self):
        good = {name:{"result":"success"} for name in checks.REQUIRED_JOBS}
        for status in ("failure", "skipped", "cancelled", "missing"):
            jobs = copy.deepcopy(good); jobs["tests"]["result"] = status
            self.assertNotEqual("PASS", checks.gate(self.root / "absent", jobs)["state"])
    def test_complete_gate_and_failure_of_one_current_artifact(self):
        inputs = self.root / "inputs"
        for kind in (*checks.KINDS, "all", "instrumented"):
            checks.tests(kind, self.xml(kind))
            target = inputs / ("android-coverage" if kind == "all" else "android-" + kind)
            report = json.loads((checks.OUT / (kind + ".json")).read_text())
            for relative in ["artifacts/android-ci/" + kind + ".json", *report["reports"]]:
                out = target / relative; out.parent.mkdir(parents=True, exist_ok=True)
                shutil.copy2(self.root / relative, out)
        cov = self.root / "app/build/reports/coverage/test/debug"; cov.mkdir(parents=True)
        (cov / "report.xml").write_text('<report><counter type="LINE" covered="1" missed="1"/></report>')
        checks.coverage(cov)
        for name in ("app/build/outputs/apk/debug/app-debug.apk", "app/build/outputs/apk/androidTest/debug/app-debug-androidTest.apk"):
            file = self.root / name; file.parent.mkdir(parents=True, exist_ok=True); file.write_bytes(b"synthetic-apk")
        checks.build()
        for kind in ("coverage", "build"):
            target = inputs / ("android-coverage" if kind == "all" else "android-" + kind)
            report = json.loads((checks.OUT / (kind + ".json")).read_text())
            for relative in ["artifacts/android-ci/" + kind + ".json", *report["reports"]]:
                out = target / relative; out.parent.mkdir(parents=True, exist_ok=True)
                shutil.copy2(self.root / relative, out)
        scanner_spec = importlib.util.spec_from_file_location("fixture_scanner", Path(__file__).with_name("android-evidence.py"))
        scanner = importlib.util.module_from_spec(scanner_spec); scanner_spec.loader.exec_module(scanner)
        for tool, artifact, config in (("gitleaks","secrets",".gitleaks.toml"),("semgrep","sast",".semgrep.yml"),
                                       ("trivy","dependencies","bom.json")):
            target = inputs / ("android-" + artifact); target.mkdir(parents=True,exist_ok=True)
            source = target / config if tool == "trivy" else self.root / config
            source.write_text('{"bomFormat":"CycloneDX","components":[{"name":"fixture"}]}' if tool == "trivy" else "synthetic-config")
            report = {"state":"PASS","execution":scanner.execution(),"tool":scanner.IMAGES[tool],
                      "exitCode":0,"findings":[],"inputSha256":checks.sha(source)}
            (target / (tool + ".json")).write_text(json.dumps(report))
        jobs = {name:{"result":"success"} for name in checks.REQUIRED_JOBS}
        self.assertEqual("PASS", checks.gate(inputs, jobs)["state"])
        device_file = inputs / "android-instrumented/app/build/outputs/instrumented-offline/run.json"
        device_original = device_file.read_bytes()
        device = json.loads(device_original); device["app-debug.apkSha256"] = "other-candidate"
        device_file.write_text(json.dumps(device))
        instrumented = inputs / "android-instrumented/artifacts/android-ci/instrumented.json"
        original_report = instrumented.read_bytes(); data = json.loads(original_report)
        data["reports"]["app/build/outputs/instrumented-offline/run.json"] = checks.sha(device_file)
        instrumented.write_text(json.dumps(data))
        self.assertNotEqual("PASS", checks.gate(inputs, jobs)["state"])
        device_file.write_bytes(device_original); instrumented.write_bytes(original_report)
        file = inputs / "android-unit/artifacts/android-ci/unit.json"
        data = json.loads(file.read_text()); data["state"] = "FAIL"; file.write_text(json.dumps(data))
        self.assertNotEqual("PASS", checks.gate(inputs, jobs)["state"])
    def test_runner_does_not_count_started_tests_as_success(self):
        started = 'INSTRUMENTATION_STATUS: class=Example\nINSTRUMENTATION_STATUS: test=one\nINSTRUMENTATION_STATUS_CODE: 1\n'
        self.assertEqual({}, runner.parse_status(started))
        done = started + 'INSTRUMENTATION_STATUS: class=Example\nINSTRUMENTATION_STATUS: test=one\nINSTRUMENTATION_STATUS_CODE: 0\n'
        self.assertEqual({("Example","one"):"PASS"}, runner.parse_status(done))
        self.assertEqual({("Example","one"):"FAIL"}, runner.parse_status(done.replace('CODE: 0','CODE: -2')))
        self.assertEqual({("Example","one"):"SKIPPED"}, runner.parse_status(done.replace('CODE: 0','CODE: -3')))


if __name__ == "__main__": unittest.main()
