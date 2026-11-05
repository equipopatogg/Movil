"""Explicit manual QA membership probe; no registration, groups, contracts or point writes."""
import argparse
import hashlib
import json
from pathlib import Path
import re
import ssl
import urllib.error
import urllib.parse
import urllib.request

BASE = "https://52-3-200-211.sslip.io"


class NoRedirect(urllib.request.HTTPRedirectHandler):
    def redirect_request(self, request, file, code, message, headers, new_url):
        return None


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--accounts-dir", type=Path, required=True)
    parser.add_argument("--output", type=Path, default=Path("artifacts/live-access/permissions.json"))
    args = parser.parse_args()
    opener = urllib.request.build_opener(urllib.request.HTTPSHandler(context=ssl.create_default_context()), NoRedirect())
    report = {"state": "ERROR", "scope": "Two existing QA accounts; read-only membership checks and own probe login/logout",
              "checks": [], "cleanup": []}
    sessions = []

    def request(method, path, token=None, body=None):
        headers = {"Accept": "application/json", "Content-Type": "application/json"}
        if token:
            headers["Authorization"] = "Bearer " + token
        req = urllib.request.Request(BASE + path, data=json.dumps(body).encode() if body is not None else None,
                                     method=method, headers=headers)
        try:
            with opener.open(req, timeout=15) as response:
                status, data = response.status, response.read(1024 * 1024)
        except urllib.error.HTTPError as error:
            status, data = error.code, error.read(1024 * 1024)
            error.close()
        return status, json.loads(data) if data else None

    def check(name, status, expected):
        report["checks"].append({"case": name, "status": status, "expected": expected,
                                 "state": "PASS" if status == expected else "FAIL"})
        if status != expected:
            raise ValueError("Unexpected status; response body intentionally not retained")

    def groups(session):
        result, cursor = set(), None
        for _ in range(5):
            query = "?cursor=" + urllib.parse.quote(cursor, safe="") if cursor else ""
            status, data = request("GET", "/api/v1/groups" + query, session["access_token"])
            check("QA groups page", status, 200)
            result.update(group["id"] for group in data["data"])
            meta = data["meta"]
            if not meta["has_more"]:
                return result
            following = meta["next_cursor"]
            if not following or following == cursor:
                raise ValueError("Invalid pagination")
            cursor = following
        raise ValueError("Dataset exceeds bounded probe")

    try:
        for name in ("cuenta-test.json", "cuenta-social-test.json"):
            credentials = json.loads((args.accounts_dir / name).read_text(encoding="utf-8-sig"))
            status, data = request("POST", "/api/v1/auth/login", body={"email": credentials["email"],
                "password": credentials["password"], "device_id": "pactogg-membership-probe"})
            check("QA probe login", status, 200)
            session = data["data"]
            sessions.append(session)
            status, profile = request("GET", "/api/v1/users/me", session["access_token"])
            check("Token resolves its own profile", status, 200)
            if profile["data"]["id"] != session["user"]["id"]:
                raise ValueError("Profile identity mismatch")
        if sessions[0]["user"]["id"] == sessions[1]["user"]["id"]:
            raise ValueError("Distinct QA accounts required")
        first, second = groups(sessions[0]), groups(sessions[1])
        if first - second:
            owner, outsider, group_id = sessions[0], sessions[1], sorted(first - second)[0]
        elif second - first:
            owner, outsider, group_id = sessions[1], sessions[0], sorted(second - first)[0]
        else:
            raise ValueError("No existing private group suitable for membership probe")
        if not re.fullmatch(r"[a-fA-F0-9]{24}", group_id):
            raise ValueError("Invalid group identity")
        report["groupSha256"] = hashlib.sha256(group_id.encode()).hexdigest()
        for suffix, name in (("", "group detail"), ("/members", "members"), ("/contracts", "contracts board")):
            path = "/api/v1/groups/" + group_id + suffix
            status, _ = request("GET", path, owner["access_token"])
            check("Member can read " + name, status, 200)
            status, _ = request("GET", path, outsider["access_token"])
            check("Nonmember cannot read " + name, status, 403)
        report["state"] = "PASS"
    except (OSError, ValueError, KeyError, TypeError, urllib.error.URLError):
        report["reason"] = "Transport, response, dataset or permission check failed; no credentials or private bodies retained"
    finally:
        for session in sessions:
            try:
                status, _ = request("POST", "/api/v1/auth/logout", session["access_token"],
                                    {"refresh_token": session["refresh_token"], "all": False})
                report["cleanup"].append({"ownProbeSession": "CLOSED" if status == 204 else "UNCONFIRMED", "status": status})
            except (OSError, ValueError, urllib.error.URLError):
                report["cleanup"].append({"ownProbeSession": "UNCONFIRMED"})
        if any(row["ownProbeSession"] != "CLOSED" for row in report["cleanup"]):
            report["state"] = "ERROR"
        args.output.parent.mkdir(parents=True, exist_ok=True)
        args.output.write_text(json.dumps(report, indent=2) + "\n", encoding="utf-8")
        print(json.dumps({"state": report["state"], "checks": len(report["checks"]), "cleanup": report["cleanup"]}))
    return 0 if report["state"] == "PASS" else 1


if __name__ == "__main__":
    raise SystemExit(main())
