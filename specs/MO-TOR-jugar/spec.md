# MO-TOR-JUGAR — Crear, inscribirse y jugar torneos

Fecha: 2026-11-14. Estado: **APROBADA POR EL PO E IMPLEMENTADA EN LOCAL**. Amplía MO-TOR-02 (consulta). Backend: TOR-01, TOR-03, TOR-06, TOR-07, TOR-08, MAT-01..07 (API 0.12.x). Maqueta: `torneos`, `torneo/nuevo`, `torneo/bracket`, `torneo/reportar`, `torneo/confirmar`.

## Requisitos
- RF1: **Nuevo torneo** (admin del grupo seleccionado): juego, formato (por defecto eliminación directa), máx. grupos (2–16), inscripción en puntos y fecha → `POST /groups/{gid}/tournaments`. Muestra el código para compartir.
- RF2: **Inscribirse con código**: `POST /tournaments/{code}/join` con `group_id` del grupo seleccionado e `Idempotency-Key`. Salir: `DELETE /tournaments/{id}/join`.
- RF3: **Detalle** (`GET /tournaments/{id}`): grupos con su nombre, bolsa, estado y bracket por rondas con nombres de grupo. El organizador ve "Iniciar torneo" (`POST /tournaments/{id}/start`) cuando hay 2 o más grupos.
- RF4: **Partida** (`GET /matches/{id}`): foto del resultado (`screenshot_url`), marcador y estado.
  - Reportar (miembro de uno de los dos grupos): marcador sin empate + foto de galería (JPEG/PNG/WebP, ≤5 MB, misma preparación que evidencias) → `POST /matches/{id}/report` multipart.
  - Grupo rival: "Confirmar" (`/confirm`) o "Disputar" con motivo 5–140 (`/dispute`).
  - Organizador: "Resolver" eligiendo ganador (`/resolve`) cuando la partida está en disputa, lleva 24 h reportada o es abandono.
- RF5: Inicio muestra "Tu siguiente partida" con `GET /me/matches/next` (204 = no hay).
- RF6: Errores por código: `TIE_NOT_ALLOWED`, `NOT_MATCH_PLAYER`, `CANNOT_CONFIRM_OWN_REPORT`, `ORGANIZER_PLAYS_MATCH`, `REPORT_TOO_RECENT`, `INVALID_MATCH_STATE`, `TOURNAMENT_NOT_IN_PROGRESS`, `INSUFFICIENT_POINTS`.

## Criterios
| ID | Resultado |
|---|---|
| MO-TORJ-AC1 | Crear torneo muestra su código y aparece en la lista. |
| MO-TORJ-AC2 | Otro grupo se inscribe con el código y el organizador lo inicia; aparece el bracket. |
| MO-TORJ-AC3 | Reportar con foto, confirmar por el rival y ver al ganador avanzar. |
| MO-TORJ-AC4 | Disputar y que el organizador resuelva; al terminar la final el torneo queda `finished`. |
