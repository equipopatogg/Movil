# MO-NOT — Avisos dentro de la app

Fecha: 2026-11-14. Estado: **APROBADA POR EL PO E IMPLEMENTADA EN LOCAL**. Backend: NOT-01..04. Maqueta: campana del encabezado y pantalla `notificaciones`. El push nativo (DEV-01..03, FCM) queda fuera de esta spec hasta tener `google-services.json` en la app.

## Requisitos
- RF1: `GET /api/v1/notifications` (paginado, `unread=true` opcional) al abrir la pantalla; la campana muestra el número de no leídos (`GET ?unread=true&limit=50`) al entrar al inicio.
- RF2: Tocar un aviso lo marca leído (`PATCH /notifications/{id}/read`) y, si trae `contract_id`, abre ese reto.
- RF3: "Marcar todo como leído" (`POST /notifications/read-all`) y borrar un aviso (`DELETE /notifications/{id}`).
- RF4: Los textos se muestran tal como los manda el servidor (`title`, `body`), sin interpretar HTML.

## Criterios
| ID | Resultado |
|---|---|
| MO-NOT-AC1 | La campana muestra el contador real de no leídos. |
| MO-NOT-AC2 | Abrir un aviso de reto lleva al reto y lo deja leído. |
| MO-NOT-AC3 | "Marcar todo" deja el contador en 0. |
