# MO-RANK — Palabra y ranking

Fecha: 2026-11-14. Estado: **APROBADA POR EL PO E IMPLEMENTADA EN LOCAL**. Backend: ACH-03 (ranking y Rey del Grupo), COM-01/03/04/05 (libro de compromisos), ACH-01 (Muro de la Fama), ACH-02 (Muro de la Vergüenza). Maqueta: pantalla `compromisos` con pestañas Ranking / Libro / Fama / Integridad.

## Requisitos
- RF1: Se trabaja sobre el grupo seleccionado; sin grupo, se pide elegir uno en Grupos.
- RF2: **Ranking**: `GET /groups/{gid}/ranking` → podio de 3 y lista, con el Rey del Grupo (`king`).
- RF3: **Libro**: `GET /groups/{gid}/commitments?role=debtor|creditor` y contador `.../summary`. El acreedor marca "Cumplido" (`POST /commitments/{id}/complete`); el deudor "Ya cumplí" (`POST /commitments/{id}/request-complete`).
- RF4: **Fama**: `GET /groups/{gid}/fame` con juego, condición, recompensa y fotos/enlaces.
- RF5: **Integridad**: `GET /groups/{gid}/shame`. Sin casos se muestra el texto de la maqueta ("No señalamos a nadie sin consenso").
- RF6: Explicación "¿Cómo se calcula?" con el texto de la maqueta.

## Criterios
| ID | Resultado |
|---|---|
| MO-RANK-AC1 | Cada pestaña muestra los datos del servidor o su estado vacío, nunca datos de ejemplo. |
| MO-RANK-AC2 | Marcar un compromiso cumplido lo saca de pendientes al recargar. |
