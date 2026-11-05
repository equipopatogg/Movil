> Actualización posterior: el usuario solicitó preparar las once specs confirmadas por Sistema.docx. Ya están creadas con plan/tareas/checklist; [matriz e IDs](specs-confirmadas-sistema-2026-11-04.md). Las frases inferiores de «propuestos/no creados» conservan el estado inicial de esta revisión. Implementación nueva aún pendiente.

# Revisión de contratos nuevos — 2026-11-04

Estado: evaluación documental; sin implementar funciones ni acreditar despliegue de las operaciones nuevas. Fuente fijada: backend commit `8baa9e6d04038c1d6cf9588c5526ad63b94c3f87`. Se contrastaron Sistema.docx, OpenAPI, guía, endpoints, DTO, servicios, repositorio Mongo de contratos y specs nuevas con PactoApi Android. Los archivos openapi.yaml/API-movil.md recibidos previamente en Downloads son anteriores; no usarlos como contrato nuevo.

[OpenAPI fijado](https://github.com/equipopatogg/backend/blob/8baa9e6d04038c1d6cf9588c5526ad63b94c3f87/api/openapi.yaml) · [Guía fijada](https://github.com/equipopatogg/backend/blob/8baa9e6d04038c1d6cf9588c5526ad63b94c3f87/docs/API-movil.md)

## Diferencia comprobada

Backend publica en fuentes 19 operaciones y 21 combinaciones método/ruta contando dos alias. Android consume las ocho operaciones anteriores. Las once adiciones de Sistema.docx sí aparecen en OpenAPI y código; no son solamente ideas. Presencia en Git no acredita que todas estén desplegadas ni probadas desde Android.

| ID pendiente Android | Ruta | Trabajo móvil |
|---|---|---|
| MO-USR-02 | GET /api/v1/users/me | Perfil real, estadísticas y cantidad de grupos; dejar de depender solo de Session.user |
| MO-USR-03 | PATCH /api/v1/users/me | Editar apodo/avatar predefinido; sincronizar identidad visible y custodia local sin sustituir tokens; errores 409/422/503 |
| MO-USR-06 | GET /api/v1/users/check-nickname | Disponibilidad pública; espera breve al escribir, cancelar consultas anteriores, evitar respuestas viejas y respetar 429; disponibilidad no garantiza registro/PATCH posterior |
| MO-GRP-02 | GET /api/v1/groups | Mis grupos con búsqueda/paginación; recuperar contexto después de reiniciar |
| MO-GRP-03 | GET /api/v1/groups/{id} | Detalle real, configuración, rol y saldo; elegir grupo activo |
| MO-MEM-01 | POST /api/v1/groups/join | Unirse con código alfanumérico de seis caracteres; errores de código, pertenencia, cupo y límite de intentos |
| MO-MEM-02 | GET /api/v1/groups/{id}/members | Miembros con búsqueda por prefijo, rol, saldo y paginación; selector de rival real |
| MO-CON-02 | GET /api/v1/groups/{gid}/contracts | Tablero paginado y filtros status/mine/game/q |
| MO-CON-03 | GET /api/v1/contracts/{id} | Detalle actualizado, accepted_by, evidence_count, version, fechas y motivo de cierre |
| MO-CON-06 | POST /api/v1/contracts/{id}/accept | Confirmar fianza virtual, enviar versión vista, actualizar estado/saldo; tratar 402/403/409 sin repetir automáticamente |
| MO-CON-07 | POST /api/v1/contracts/{id}/reject | Solo reto dirigido al usuario y open; confirmación, versión y cancelled/closed_reason=rejected; sin mover puntos |

Estos son expedientes propuestos para la siguiente implementación; esta revisión no los crea ni marca listos. Cada uno debe conservar spec/plan/tasks/checklist. Backend tiene spec.md para los once nuevos, pero en el árbol revisado no tienen los otros tres archivos: no copiar esa ausencia ni asumir aceptación completa.

## Ajustes a operaciones existentes

- MO-CON-01: permitir elegir rival real usando MEM-02, excluir usuario propio y conservar opción de grupo. Nunca inventar IDs.
- MO-EVI-02: abrir evidencia desde un reto recuperado por GET y refrescar detalle tras enviar; el estado local actual conserva una instantánea anterior a la evidencia. No mostrar open como estado actualizado después de pasar a voting. Mostrar límite según respuesta y tratar conflicto real.
- MO-GRP-01 y MO-TOR-02: usar grupo seleccionado real; después de crear/unirse invalidar/recargar listas. Conservar consulta global de torneos si corresponde al contrato.
- Sesión: perfil actualizado no equivale a rotación de credenciales. Evitar que una respuesta tardía de otra sesión repueble perfil/listas después de logout. Mantener separación por usuario y limpiar contexto.
- Listas: reiniciar cursor al cambiar filtros/grupo, evitar duplicados/respuestas tardías, distinguir vacío/error/cargando y refrescar después de acciones. La guía avisa caché 30–60 segundos para cambios ajenos.

## Aclaraciones para backend antes de cerrar criterios

1. **Evidencia y aceptación:** guía describe open → in_progress al aceptar y luego voting; EvidenceService y MongoContractRepository admiten evidencia en open, accepted, in_progress, evidence_pending y voting. Para retos de grupo basta pertenecer, sin exigir accepted_by; incluso el retador puede enviarla. Nuestra reprueba positiva creó y envió evidencia sin aceptación. Confirmar regla deseada, participantes autorizados y vencimiento; no presentar esa prueba como aceptación/fianza validada. Estados accepted/evidence_pending tampoco aparecen en la lista actual de estados de la guía.
2. **Avatares:** se recibe un identificador con patrón, sin catálogo de IDs/recursos acordado. Definir correspondencia entre plataformas y fallback; este endpoint no permite subir una foto de perfil.
3. **Versión del contrato documental:** guía/specs dicen v0.5.0 pero info.version de OpenAPI sigue 1.0.0. Fijar commit mientras concilian versión y publicar archivo vigente para el equipo.
4. **Settings:** bond_options en OpenAPI permite enteros >=0; servicio exige de uno a cinco valores únicos entre 10 y 200. Mantener settings editables fuera hasta conciliar, sin convertir esas cifras en nuevas reglas de producto ratificadas.
5. **Schemas:** UpdateProfileRequest no expresa en schema la exigencia de al menos un campo; el servicio sí rechaza ambos ausentes. Varias respuestas carecen de required explícitos. Definir campos mínimos/nulos; pruebas de parseo y respuestas inválidas.
6. **Fotos/historial:** no hay GET de listado/detalle de evidencias entre las 19 operaciones. GET contrato trae contador, no galería. Foto devuelve object_key y URL nullable; confirmar URL autorizada/CDN y acceso antes de ofrecer visualización histórica. Subida correcta no prueba recuperación de la imagen.
7. **Rechazo:** spec CON-07 menciona rejected_by, pero ContractResponse/OpenAPI no lo exponen. No mostrar quién rechazó sin dato acordado.
8. **Aceptación real:** servicio/repo usan transacción Mongo para retener puntos. Sus specs citan fakes locales; probar con dos cuentas QA en el despliegue, saldo antes/después, versión vieja/doble acción y autorización. No se ejecutó esa transacción en esta revisión ni en la reprueba anterior.
9. **Recuperación tras escritura incierta:** no se añadió idempotencia. Los GET ayudan a reconciliar, pero no garantizan por sí solos identificar un POST duplicado. Mantener escrituras sin reintento automático y mensajes de resultado incierto.

## Orden propuesto

1. Specs/contratos de lecturas: perfil, lista/detalle de grupos y miembros.
2. Unirse al grupo y editar perfil/consulta de apodo; respetar catálogo pendiente de avatar.
3. Tablero/detalle de retos y selector de rival dentro de crear reto.
4. Aceptar/rechazar con versión y saldo, después evidencia sobre detalle actualizado y reglas conciliadas.
5. Pruebas HTTP y de interfaz por operación, dos cuentas autorizadas, recuperación al reiniciar y errores; aceptación por criterios, no por cantidad de pantallas.

No hay endpoints nuevos de votos/jurado, historial de puntos, compromisos, crear torneos/partidas, notificaciones, voz, reportes, eliminar cuenta, Google login ni verificación de correo. Mantener funciones sin backend fuera del recorrido conectado. La sugerencia externa de simularlas no reemplaza el acuerdo del usuario de ocultarlas/desactivarlas.

Sin cambios Kotlin, dependencias, AWS, cuentas, build/tests, commit o push por esta revisión. Los resultados positivos anteriores de ocho operaciones se conservan; no acreditan estas once.
