"""Offline fixtures for CI evidence policy; no Docker, Gradle or credentials."""
import importlib.util
import json
import os
from pathlib import Path
import tempfile
import unittest
from unittest.mock import patch

spec = importlib.util.spec_from_file_location("evidence", Path(__file__).with_name("android-evidence.py"))
evidence = importlib.util.module_from_spec(spec)
spec.loader.exec_module(evidence)


class EvidenceTests(unittest.TestCase):
    def setUp(self):
        self.temp = tempfile.TemporaryDirectory(prefix="pactogg-ci-fixture-")
        self.root = Path(self.temp.name)
        self.bom = self.root / "app/build/reports/cyclonedx-direct/bom.json"
        self.bom.parent.mkdir(parents=True)
        self.bom.write_text(json.dumps({"bomFormat": "CycloneDX", "components": [{"name": "Synthetic"}]}))
        for filename in (".gitleaks.toml", ".semgrep.yml"):
            (self.root / filename).write_text("fixture")
        self.patches = [patch.object(evidence, "ROOT", self.root), patch.object(evidence, "BOM", self.bom),
                        patch.object(evidence, "OUT", self.root / "artifacts/android-security")]
        for item in self.patches:
            item.start()

    def tearDown(self):
        for item in reversed(self.patches):
            item.stop()
        self.temp.cleanup()

    def test_findings_errors_and_redaction(self):
        samples = {
            "gitleaks": ([{"RuleID": "synthetic", "File": "sample.kt", "StartLine": 1,
                           "Secret": "DO_NOT_RETAIN", "Match": "DO_NOT_RETAIN"}], [], 10),
            "semgrep": ({"results": [{"check_id": "synthetic", "path": "sample.kt", "start": {"line": 1},
                                      "extra": {"lines": "DO_NOT_RETAIN"}}], "errors": []}, {"results": [], "errors": []}, 1),
            "trivy": ({"SchemaVersion": 2, "Results": [{"Vulnerabilities": [{"VulnerabilityID": "Synthetic",
                       "PkgName": "Example", "Severity": "HIGH", "Description": "DO_NOT_RETAIN"}]}]}, {"SchemaVersion": 2, "Results": []}, 10),
        }
        for name, (positive, negative, code) in samples.items():
            with self.subTest(scanner=name):
                state, findings = evidence.sanitized(name, positive, code)
                self.assertEqual("FAIL", state)
                self.assertNotIn("DO_NOT_RETAIN", json.dumps(findings))
                self.assertEqual("PASS", evidence.sanitized(name, negative, 0)[0])
                self.assertEqual("ERROR", evidence.sanitized(name, negative, 2)[0])
                self.assertEqual("ERROR", evidence.sanitized(name, positive, 0)[0])
                self.assertEqual("ERROR", evidence.sanitized(name, negative, code)[0])

    def test_semgrep_errors_do_not_become_clean_scan(self):
        self.assertEqual("ERROR", evidence.sanitized("semgrep", {"results": [], "errors": [{"message": "synthetic"}]}, 0)[0])

    def test_bad_schema_is_rejected(self):
        for name in evidence.IMAGES:
            with self.subTest(scanner=name), self.assertRaises((ValueError, KeyError, TypeError)):
                evidence.sanitized(name, {}, 0)

    def test_missing_docker_preserves_error_report(self):
        with patch.object(evidence.subprocess, "run", side_effect=FileNotFoundError):
            self.assertEqual(1, evidence.scan("gitleaks"))
        report = json.loads((evidence.OUT / "gitleaks.json").read_text())
        self.assertEqual("ERROR", report["state"])
        self.assertIsNone(report["exitCode"])

    def test_summary_rejects_missing_stale_and_changed_input(self):
        steps = {"gitleaks": {"outcome": "success"}}
        with patch.dict(os.environ, {"STEPS_JSON": json.dumps(steps)}):
            self.assertEqual(1, evidence.summary(["gitleaks"]))
            evidence.save(evidence.OUT / "gitleaks.json", None)
            self.assertEqual(1, evidence.summary(["gitleaks"]))
            report = {"execution": evidence.execution(), "state": "PASS", "inputSha256": evidence.digest(self.root / ".gitleaks.toml")}
            evidence.save(evidence.OUT / "gitleaks.json", report)
            self.assertEqual(0, evidence.summary(["gitleaks"]))
            report["execution"]["run"] = "old"
            evidence.save(evidence.OUT / "gitleaks.json", report)
            self.assertEqual(1, evidence.summary(["gitleaks"]))
            report["execution"] = evidence.execution()
            evidence.save(evidence.OUT / "gitleaks.json", report)
            (self.root / ".gitleaks.toml").write_text("changed")
            self.assertEqual(1, evidence.summary(["gitleaks"]))

    def test_skipped_prerequisite_and_finding_have_distinct_states(self):
        for outcome, expected in (("skipped", "NO_EJECUTADO"), ("failure", "FAIL")):
            with self.subTest(outcome=outcome), patch.dict(os.environ, {"STEPS_JSON": json.dumps({"gitleaks": {"outcome": outcome}})}):
                if outcome == "failure":
                    evidence.save(evidence.OUT / "gitleaks.json", {"execution": evidence.execution(), "state": "FAIL",
                                  "inputSha256": evidence.digest(self.root / ".gitleaks.toml")})
                self.assertEqual(1, evidence.summary(["gitleaks"]))
                self.assertEqual(expected, json.loads((evidence.OUT / "summary.json").read_text())["controls"]["gitleaks"]["state"])

    def test_quality_requires_tests_sbom_and_successful_prerequisites(self):
        steps = {key: {"outcome": "success"} for key in ("checkout", "java", "gradle", "sdk", "build", "sbom")}
        output = self.root / "app/build/test-results/testDebugUnitTest"
        output.mkdir(parents=True)
        with patch.dict(os.environ, {"STEPS_JSON": json.dumps(steps)}):
            self.assertEqual(1, evidence.quality())
            (output / "TEST-Synthetic.xml").write_text('<testsuite tests="1" failures="0" skipped="0"><testcase name="Synthetic"/></testsuite>')
            self.assertEqual(0, evidence.quality())
            self.bom.write_text("null")
            self.assertEqual(1, evidence.quality())
            self.bom.unlink()
            self.assertEqual(1, evidence.quality())

    def test_quality_does_not_hide_build_failure(self):
        steps = {key: {"outcome": "success"} for key in ("checkout", "java", "gradle", "sdk", "build", "sbom")}
        steps["build"]["outcome"] = "failure"
        with patch.dict(os.environ, {"STEPS_JSON": json.dumps(steps)}):
            self.assertEqual(1, evidence.quality())
            self.assertEqual("failure", json.loads((self.root / "artifacts/android-quality/summary.json").read_text())["steps"]["build"])


if __name__ == "__main__":
    unittest.main()
