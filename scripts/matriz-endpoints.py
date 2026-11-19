#!/usr/bin/env python3
"""Genera docs/pruebas/matriz-endpoints.md: cada endpoint que consume la app (anotaciones de Retrofit)
con las clases de prueba unitaria, de integración y de seguridad que lo ejercitan.
Uso: python3 scripts/matriz-endpoints.py"""
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
MAIN = ROOT / "app/src/main/java"
TEST = ROOT / "app/src/test/java"
ROUTE = re.compile(r'@(GET|POST|PUT|PATCH|DELETE)\("([^"]+)"\)|@HTTP\(method = "(\w+)", path = "([^"]+)"')
CATEGORY = re.compile(r"@Category\((\w+)Checks::class\)")

endpoints = []
for file in sorted(MAIN.rglob("*.kt")):
    text = file.read_text(encoding="utf-8")
    for match in ROUTE.finditer(text):
        method = match.group(1) or match.group(3)
        path = "/" + (match.group(2) or match.group(4)).lstrip("/")
        call = re.search(r"fun (\w+)\(", text[match.end():])
        endpoints.append((method, path, call.group(1) if call else "", file.name))
endpoints = sorted(set(endpoints), key=lambda e: (e[1], e[0]))

tests = []
for file in sorted(TEST.rglob("*.kt")):
    text = file.read_text(encoding="utf-8")
    kind = CATEGORY.search(text)
    tests.append((file.stem, kind.group(1).lower() if kind else "unit", text))

# Nombre del método del repositorio cuando difiere del de la interfaz de Retrofit.
ALIASES = {"uploadAvatar": ["avatarPhoto"], "report": ["reportUser", "reportEvidence"], "block": ["blockUser"],
           "unblock": ["unblockUser"], "registerDevice": ["sync"], "removeDevice": ["detach"]}
MATRIX = __import__("json").loads((ROOT / "docs/pruebas/operaciones.json").read_text(encoding="utf-8"))


def users(path, function):
    route = re.escape(path)
    route = re.sub(r"\\\{[^}]+\\\}", r"[^\"/?]+", route)
    by_route = re.compile(route + r'(["?]|$)', re.M)
    names = [function] + ALIASES.get(function, []) if function else []
    by_call = re.compile(r"(\.|(?<![\w.]))(" + "|".join(map(re.escape, names)) + r")\(") if names else None
    found = {"unit": set(), "integration": set(), "security": set()}
    for row in MATRIX["endpoints"]:
        if row["path"] == path:
            for kind in found:
                found[kind].add(row["tests"][kind]["class"].rsplit(".", 1)[1])
    for name, kind, text in tests:
        if by_route.search(text) or (by_call and by_call.search(text)):
            found[kind].add(name)
    return found

# PlaySecurityTest recorre todas las operaciones de PlayApi (sin sesión, sesión revocada, ids manipulados).
rows, gaps = [], 0
for method, path, function, source in endpoints:
    found = users(path, function)
    if source == "PlayApi.kt":
        found["security"].add("PlaySecurityTest")
    if not found["integration"]:
        gaps += 1
    rows.append((method, path, *(", ".join(sorted(found[k])) or "—" for k in ("unit", "integration", "security"))))

lines = ["# Matriz de pruebas por endpoint (app)", "",
         "Generada con `python3 scripts/matriz-endpoints.py` a partir de las anotaciones de Retrofit y de `app/src/test`.",
         "La categoría sale de `@Category` (UnitChecks, IntegrationChecks, SecurityChecks).", "",
         f"Endpoints consumidos: **{len(rows)}** · sin prueba de integración: **{gaps}**", "",
         "| Método | Ruta | Unitarias | Integración | Seguridad |", "|---|---|---|---|---|"]
lines += [f"| {m} | `{p}` | {u} | {i} | {s} |" for m, p, u, i, s in rows]
(ROOT / "docs/pruebas/matriz-endpoints.md").write_text("\n".join(lines) + "\n", encoding="utf-8")
print(len(rows), "endpoints;", gaps, "sin integración")
for m, p, u, i, s in rows:
    if i == "—" or s == "—":
        print(f"  {m} {p} | int={i[:40]} | sec={s[:40]}")
