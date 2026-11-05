> Actualización posterior: el usuario autorizó implementar estas once specs. Sus recorridos positivos ya se verificaron en teléfono; [candidato y límites](integracion-social-2026-11-04.md). La sección inferior conserva el alcance documental de recepción, no el estado técnico vigente.

# Recepción y correspondencia de specs — Sistema.docx

Fecha: 2026-11-04. Solicitud del usuario: equivalentes móviles de las specs backend solamente cuando estén en el documento enviado. **Confirmación = presencia expresa en Sistema.docx**, corroborada con Git; no aceptación Android ni certificación del despliegue. No se implementan funciones por esta preparación.

Fuente recibida: Sistema.docx en Downloads. SHA-256 público del documento: `3111d065cc43a55892faf4d0db330cadfa840294f483218659bbd7a8f148a146`. Backend fijado al commit `8baa9e6d04038c1d6cf9588c5526ad63b94c3f87`; [índice backend](https://github.com/equipopatogg/backend/blob/8baa9e6d04038c1d6cf9588c5526ad63b94c3f87/specs/README.md). La guía lo llama v0.5.0, mientras info.version del OpenAPI sigue 1.0.0; se fija commit para evitar ambigüedad.

## Once adiciones expresas y su equivalente móvil

| ID en documento | Operación | Spec Android | Spec backend |
|---|---|---|---|
| USR-02 | `GET /api/v1/users/me` | [MO-USR-02](../../specs/MO-USR-02-consultar-perfil/spec.md) | [Fuente](https://github.com/equipopatogg/backend/blob/8baa9e6d04038c1d6cf9588c5526ad63b94c3f87/specs/USR-02-get-profile/spec.md) |
| USR-03 | `PATCH /api/v1/users/me` | [MO-USR-03](../../specs/MO-USR-03-editar-perfil/spec.md) | [Fuente](https://github.com/equipopatogg/backend/blob/8baa9e6d04038c1d6cf9588c5526ad63b94c3f87/specs/USR-03-update-profile/spec.md) |
| USR-06 | `GET /api/v1/users/check-nickname?nickname=` | [MO-USR-06](../../specs/MO-USR-06-disponibilidad-apodo/spec.md) | [Fuente](https://github.com/equipopatogg/backend/blob/8baa9e6d04038c1d6cf9588c5526ad63b94c3f87/specs/USR-06-check-nickname/spec.md) |
| GRP-02 | `GET /api/v1/groups` | [MO-GRP-02](../../specs/MO-GRP-02-listar-grupos/spec.md) | [Fuente](https://github.com/equipopatogg/backend/blob/8baa9e6d04038c1d6cf9588c5526ad63b94c3f87/specs/GRP-02-list-groups/spec.md) |
| GRP-03 | `GET /api/v1/groups/{id}` | [MO-GRP-03](../../specs/MO-GRP-03-detalle-grupo/spec.md) | [Fuente](https://github.com/equipopatogg/backend/blob/8baa9e6d04038c1d6cf9588c5526ad63b94c3f87/specs/GRP-03-get-group/spec.md) |
| MEM-01 | `POST /api/v1/groups/join` | [MO-MEM-01](../../specs/MO-MEM-01-unirse-grupo/spec.md) | [Fuente](https://github.com/equipopatogg/backend/blob/8baa9e6d04038c1d6cf9588c5526ad63b94c3f87/specs/MEM-01-join-group/spec.md) |
| MEM-02 | `GET /api/v1/groups/{id}/members` | [MO-MEM-02](../../specs/MO-MEM-02-listar-miembros/spec.md) | [Fuente](https://github.com/equipopatogg/backend/blob/8baa9e6d04038c1d6cf9588c5526ad63b94c3f87/specs/MEM-02-list-members/spec.md) |
| CON-02 | `GET /api/v1/groups/{gid}/contracts` | [MO-CON-02](../../specs/MO-CON-02-listar-retos/spec.md) | [Fuente](https://github.com/equipopatogg/backend/blob/8baa9e6d04038c1d6cf9588c5526ad63b94c3f87/specs/CON-02-list-contracts/spec.md) |
| CON-03 | `GET /api/v1/contracts/{id}` | [MO-CON-03](../../specs/MO-CON-03-detalle-reto/spec.md) | [Fuente](https://github.com/equipopatogg/backend/blob/8baa9e6d04038c1d6cf9588c5526ad63b94c3f87/specs/CON-03-get-contract/spec.md) |
| CON-06 | `POST /api/v1/contracts/{id}/accept` | [MO-CON-06](../../specs/MO-CON-06-aceptar-reto/spec.md) | [Fuente](https://github.com/equipopatogg/backend/blob/8baa9e6d04038c1d6cf9588c5526ad63b94c3f87/specs/CON-06-accept-contract/spec.md) |
| CON-07 | `POST /api/v1/contracts/{id}/reject` | [MO-CON-07](../../specs/MO-CON-07-rechazar-reto/spec.md) | [Fuente](https://github.com/equipopatogg/backend/blob/8baa9e6d04038c1d6cf9588c5526ad63b94c3f87/specs/CON-07-reject-contract/spec.md) |

## Criterio de adaptación

Una operación por expediente, mismo ID base, prefijo MO para distinguir consumidor Android. Encabezados como backend: objetivo, requisitos, errores, implementación y criterios de aceptación. Android conserva también pruebas/límites y control de aprobación, además de plan.md, tasks.md y checklists/checklist.md según su plantilla propia. Los once nuevos en el árbol backend revisado solo incluyen spec.md aunque su README afirme expediente completo; no se atribuye existencia de sus otros archivos ni se copia esa ausencia al móvil.

Las ocho operaciones ya integradas se conservan con sus IDs y evidencia. AUTH-03 figura en el documento y API aunque backend no tenga expediente propio; MO-AUTH-03 sigue válido. Total móvil: **19 specs consumidor + 4 de base/coordinación = 23 expedientes**. GET /health y GET / son auxiliares técnicos, no nuevas pantallas/specs funcionales; los dos alias no generan duplicados.

La lectura auxiliar de detalle que ya existe bajo MO-EVI-02 se reconoce como avance parcial de MO-CON-03. No se declara terminado su modelo/recorrido general ni se falsea la autoría anterior. Las reglas de evidencia/aceptación discrepantes, catálogo de avatares y otros pendientes siguen en la revisión de contratos.

El documento sugiere simular operaciones que aún no existen; es información externa, no autorización. Se conserva acuerdo del usuario: mantenerlas ocultas/desactivadas en recorrido conectado. Sin specs de votos/jurado, editar/cancelar retos, historial de puntos, compromisos, crear torneos/partidas, notificaciones, voz, reportes o eliminar cuenta. No se renumera ni elimina documentación anterior.

Solo documentación. Sin cambios nuevos Kotlin/Gradle, dependencias, build/tests, cuentas, AWS, commits o push en este encargo. Los cambios técnicos anteriores continúan locales y conservan su evidencia independiente.
