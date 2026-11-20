#!/usr/bin/env python3
"""Sube el AAB firmado a Google Play (pista interna por defecto) con la API Android Publisher v3.

Variables: PLAY_SERVICE_ACCOUNT_JSON (contenido JSON de la cuenta de servicio, secreto de GitHub),
PLAY_TRACK (internal | alpha | beta | production; por defecto internal).
Uso: python3 scripts/play-upload.py app/build/outputs/bundle/release/app-release.aab
Requiere: pip install google-auth google-api-python-client
"""
import json
import os
import sys

from google.oauth2 import service_account
from googleapiclient.discovery import build
from googleapiclient.http import MediaFileUpload

PACKAGE = "com.pactogg.app"


def main(bundle_path: str) -> None:
    info = json.loads(os.environ["PLAY_SERVICE_ACCOUNT_JSON"])
    track = os.environ.get("PLAY_TRACK", "internal")
    if track not in {"internal", "alpha", "beta", "production"}:
        raise SystemExit("PLAY_TRACK inválido")
    credentials = service_account.Credentials.from_service_account_info(
        info, scopes=["https://www.googleapis.com/auth/androidpublisher"])
    edits = build("androidpublisher", "v3", credentials=credentials, cache_discovery=False).edits()
    edit = edits.insert(packageName=PACKAGE, body={}).execute()
    media = MediaFileUpload(bundle_path, mimetype="application/octet-stream", resumable=True)
    bundle = edits.bundles().upload(packageName=PACKAGE, editId=edit["id"], media_body=media).execute()
    edits.tracks().update(packageName=PACKAGE, editId=edit["id"], track=track, body={
        "track": track,
        "releases": [{"versionCodes": [str(bundle["versionCode"])], "status": "completed"}],
    }).execute()
    edits.commit(packageName=PACKAGE, editId=edit["id"]).execute()
    print(f"Publicado versionCode {bundle['versionCode']} en la pista {track}")


if __name__ == "__main__":
    if len(sys.argv) != 2 or not os.path.isfile(sys.argv[1]):
        raise SystemExit("Uso: play-upload.py <ruta del .aab>")
    main(sys.argv[1])
