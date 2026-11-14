# Inventario: maqueta Figma Make vs. app Android vs. backend 0.12 — 2026-11-14

**Fuente de la maqueta:** export `App PactoGG de Retos Gamer.make` (commit `02db210`, "Add clear group rankings fame and integrity hub"), extraído en `G:\PactoGGMovil\MaquetaFigmaMake\` (`src/App.tsx` + CSS). La maqueta es React con datos de ejemplo; aquí es la referencia visual y de flujos, no de reglas: las reglas salen de las specs del backend.
**App:** `main` en `90ada9c` (PR #11, 2026-11-05): 19 operaciones conectadas (specs MO-*).
**Backend:** API 0.12.x, 88 endpoints del CRUD (`docs/API-movil.md` del backend).

Estado: **Conectada** = la pantalla usa la API real · **Maqueta local** = existe en `prototype/` con datos de ejemplo · **Falta** = no existe.

| # | Pantalla de la maqueta (ruta) | Qué muestra | Endpoints del backend | Estado en la app | Spec móvil |
|---|---|---|---|---|---|
| 1 | Acceso: login (`login`) | Correo, contraseña, mantener sesión, recuperar acceso, entrar a la demo | AUTH-01, AUTH-03, AUTH-06/07 | Conectada (sin recuperar acceso) | MO-AUTH-01/03 ✔; falta MO-AUTH-06/07 |
| 2 | Registro (`registro`) | Alta con apodo y aviso de privacidad | USR-01, USR-06, USR-07 | Conectada | MO-USR-01/06 ✔ |
| 3 | Demo previa al login (`demo`) | Recorrido de 3–4 pasos con un reto de ejemplo, modo foto/enlace | ninguno (local) | Maqueta local | falta spec MO-DEMO |
| 4 | Inicio / tablero (`inicio`) | Selector de squad, accesos (voz, squad, ranking), "Lo que sigue" con filtros Todos/Tu turno/Jurado, enlace a torneos, guía "¿Cómo funciona un pacto?" | CON-02, VOT-01, MAT-07, MEM-06 | Parcial: tablero conectado, sin filtros Tu turno/Jurado ni siguiente partida | ampliar MO-CON-02; nueva MO-HOME |
| 5 | Explorar (`explorar`) | Ideas de reto por categoría/juego, detalle de condiciones, "Usar esta idea" | ninguno (catálogo local) | Maqueta local | nueva MO-EXP (catálogo local) |
| 6 | Grupos (`grupos`) | Mis grupos / Unirme / Crear grupo | GRP-01/02/03, MEM-01/02 | Conectada | MO-GRP-*, MO-MEM-* ✔ |
| 7 | Crear reto manual (`crear`, 3 pasos) | Grupo, juego, condición, fecha; rival del grupo, acuerdo, fianza; resumen | CON-01 | Conectada | MO-CON-01 ✔ |
| 8 | Crear por voz (`voz`) | Dictar, revisar los 4 campos, pasar al manual | CON-08 + CON-01 `source=voice` | Falta | nueva MO-CON-08 |
| 9 | Contrato y evidencia (`contrato`, `evidencia`) | Estado del contrato, subir foto o enlace, evidencia recibida | CON-03/06/07, EVI-02/03/04/05 | Conectada (sin reemplazar/borrar evidencia) | MO-CON-03/06/07, MO-EVI-02 ✔; falta MO-EVI-04/05 |
| 10 | Jurado (`jurado`) | Revisar evidencia de otros, votar Legítimo/Trampa, conteo | VOT-01..05 | Falta | nuevas MO-VOT-01..05 |
| 11 | Torneos (`torneos`) | Pestañas, torneo destacado, otros torneos | TOR-02 (listar), TOR-03 | Parcial (solo consulta) | MO-TOR-02 ✔; ampliar |
| 12 | Nuevo torneo (`torneo/nuevo`) | Configurar cuadro amistoso | TOR-01, TOR-06, TOR-08, TOR-09 | Falta | nuevas MO-TOR-01/06/08/09 |
| 13 | Bracket (`torneo/bracket`) | Cuadro por rondas, reportes de partidas | MAT-01, MAT-02 | Maqueta local | nueva MO-MAT-01/02 |
| 14 | Reportar partida (`torneo/reportar`) | Marcador + foto | MAT-03 | Falta | nueva MO-MAT-03 |
| 15 | Revisar resultado (`torneo/confirmar`) | Confirmar o disputar | MAT-04, MAT-05 (MAT-06 organizador) | Falta | nuevas MO-MAT-04/05/06 |
| 16 | Palabra y ranking (`compromisos`) | Pestañas Ranking / Libro / Fama / Integridad, "¿Cómo se calcula?" | ACH-03, COM-01..05, ACH-01, ACH-02, PTS-01/02 | Falta | nuevas MO-ACH-01/02/03, MO-COM-01..05 |
| 17 | Perfil (`perfil`) | Identidad (apodo, avatar), "Dentro de PactoGG", privacidad, cerrar sesión | USR-02/03, AUTH-04, ACH-04 | Conectada (sin logros) | MO-USR-02/03, MO-AUTH-04 ✔ |
| 18 | Notificaciones (`notificaciones`) | Lista de avisos | NOT-01..04, DEV-01..03 (push) | Falta | nuevas MO-NOT-01..04, MO-DEV-01..03 |

## Funciones del backend que la maqueta no dibuja

Administración de grupo (GRP-04..07, MEM-03..06), reportes y bloqueos (REP-01..06), auditoría (AUD-01), cancelación por admin (CON-09), editar/cancelar reto propio (CON-04/05), borrar cuenta (USR-04), cambio de contraseña, verificación de correo (AUTH-05) y Google (AUTH-02). Se dejan fuera del primer alcance salvo que el PO las pida; la regla del docente es no quitar funciones de la maqueta, no agregar todas las del backend.

## Bloques propuestos (orden por valor para la evaluación del 2-dic)

1. **Jurado** (#10) — sin votación el ciclo del reto no cierra.
2. **Inicio completo** (#4) — filtros Tu turno/Jurado y siguiente partida.
3. **Torneos** (#11–#15).
4. **Palabra y ranking** (#16).
5. **Notificaciones y push** (#18).
6. **Voz** (#8) — funciona en cuanto el backend tenga configurado el modelo; mientras, la app ofrece escribir a mano ante `503 AI_UNAVAILABLE`.
7. **Demo y Explorar** (#3, #5), recuperar acceso, evidencias editables.

Cada bloque sigue el método del repo: spec → plan → tasks → implementación con pruebas → evidencia → rama y PR.
