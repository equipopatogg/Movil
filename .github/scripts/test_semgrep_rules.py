import importlib.util
from pathlib import Path
import unittest

spec = importlib.util.spec_from_file_location("rules", Path(__file__).with_name("check-semgrep-rules.py"))
rules = importlib.util.module_from_spec(spec)
spec.loader.exec_module(rules)


class SemgrepRuleChecksTests(unittest.TestCase):
    def test_expected_positive_negative_and_scope_are_exact(self):
        expected = rules.expected_cases((rules.ROOT / ".semgrep-tests/cases.kt").read_text())
        rows = [{"check_id": rule, "start": {"line": line}, "path": "/src/app/src/main/security/RuleCases.kt"}
                for rule, line in expected]
        rules.verify({"results": rows, "errors": []}, 1, expected)
        for data, code in (({"results": rows}, 0), ({"results": rows[:-1]}, 1),
                           ({"results": rows, "errors": ["engine failure"]}, 1),
                           ({"results": rows + [dict(rows[0], path="/src/app/src/test/security/RuleCases.kt")]}, 1),
                           ({"results": rows + [dict(rows[0], start={"line": 6})]}, 1)):
            with self.subTest(code=code, size=len(data["results"])), self.assertRaises(ValueError):
                rules.verify(data, code, expected)

    def test_missing_annotation_pairs_fail(self):
        with self.assertRaises(ValueError):
            rules.expected_cases("// ruleid: something\nunsafe()")
