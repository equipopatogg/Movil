# Checklist de evaluación móvil — corte y entrega general

Preparación solicitada el 2026-11-03. Fuente: PDF Móvil v3, página 4 para corte 3–4 noviembre; evaluación general 2 diciembre. Esta lista coordina entregables y evidencia, no sustituye specs de código ni autoriza cuentas/pagos/firma/publicación.

## Corte del 3–4 de noviembre

| Entregable del profesor | Expediente | Estado real | Evidencia que falta |
|---|---|---|---|
| Registro funcional | PG-03 | Pendiente | Cuenta de prueba creada vía API y recorrido en Android |
| Login funcional | PG-03 | Pendiente | Acceso/rechazo/red y sesión real |
| Pantallas después del primer ingreso | PG-04, alcance propuesto | Pendiente | Recorrido delimitado y ejecutado; no placeholders |
| Registro/configuración Play iniciado | Checklist de esta página | Sin comprobar | Responsable y estado de consola, sin exponer información privada |

El backend del corte debe estar en nube con URL y registro/login/logout más cuatro endpoints; esto condiciona la demostración integrada móvil. Health/probes no los sustituyen. No se fija una fecha nueva ni se acredita cumplimiento por disponer de CI.

## Antes del primer desarrollo funcional

Seguir la [secuencia contrastada con el profesor](correspondencia-specs-profesor.md): base de compilación/CI, núcleo, frontera de almacenamiento y compuertas antes de ampliar funciones. Revisar lo existente, sin recrearlo ni darlo por terminado por tener carpetas.

- [ ] Contrastar DI, arranque/navegación, errores y logging de la base con sus pruebas y amenazas.
- [ ] Confirmar frontera de persistencia: Room/DataStore no son una custodia de tokens; diseñar y probar el mecanismo sensible con PG-03.
- [ ] Delimitar cobertura/dispositivo/release pendientes en PG-01/02 y criterios aún abiertos de PG-00.

- [ ] Cerrar contrato versionado de registro/login/logout: campos, apodo, consentimiento, errores, sesión, refresh/revocación, ejemplos sintéticos y HTTPS de prueba.
- [ ] Resolver si verificación de correo/perfil condicionan el acceso; no heredar políticas del ejemplo o del boceto.
- [ ] Cerrar recorrido del primer ingreso y pantallas del corte; propuesta PG-04: tablero/perfil/grupos.
- [ ] Conciliar tema/navegación de DESIGN con el boceto sin importar sus reglas o cifras.
- [ ] Conservar matriz de versiones y preparar datos/cuentas de prueba autorizados, sin secretos en documentos.

## Google Play — preparación administrativa y de entrega

- [ ] Identificar titular de la cuenta PactoGG, acceso disponible y responsable. No registrar contraseñas, códigos, documentos de identidad o pagos aquí.
- [ ] Confirmar estado del registro/verificación de cuenta y creación de la aplicación; capturas con datos privados redactados si se usan como evidencia.
- [ ] Confirmar applicationId definitivo antes de publicar; el actual com.pactogg.app sigue provisional. Sin cambiarlo por esta lista.
- [ ] Definir responsables de clave de carga/Play App Signing y custodia; nunca subir keystore, claves o credenciales.
- [ ] Preparar ficha, iconos/capturas, privacidad/datos y canal/participantes de prueba según condiciones vigentes de la cuenta; verificar reglas oficiales al ejecutar esa etapa.
- [ ] Separar inicio de configuración exigido para el corte de publicación final/descarga desde tienda pedida en la evaluación general.
- [ ] Verificar release/16 KB, permisos, declaración de datos y cuenta eliminable antes de distribución. No marcar Play listo por compilar debug.

## Evidencia técnica disponible

[CI remoto](ci-remoto-android.md): commit 2a9fb5e; calidad y seguridad PASS, 5 JVM, Lint/Detekt, compilación debug/androidTest y SBOM/escáneres. Pruebas instrumentadas ejecutadas, cobertura, aceptación completa de PG-00 y release/16 KB siguen pendientes. No es evidencia de registro/login.

## Requisitos generales conservados para el 2 de diciembre

- [ ] SDD, desarrollo con agentes y DevSecOps trazados a cambios/evidencia.
- [ ] Unitarias, seguridad e integración móviles/API; CI y proceso de entrega verificables.
- [ ] Logout y continuidad de sesión acordados con backend; caché móvil justificada.
- [ ] Moderación antes de almacenamiento de contenidos aplicables, con backend; fotos/enlaces sin cámara/video.
- [ ] Push para procesos principales: resolver alcance/proveedor, FCM sigue aplazado y no se implementa por esta checklist.
- [ ] Aviso de privacidad/datos en app y landing, eliminar cuenta y permisos mínimos pertinentes.
- [ ] Función ML e IA generativa delimitadas y verificadas; una demo de voz no acredita IA conectada.
- [ ] Google Play, landing/analítica, versionamiento y entrega acordada.
- [ ] Nube/NoSQL/réplicas/Redis/observabilidad/CDN/rendimiento coordinados con backend; no alojar esos servicios dentro de Android.

SQLite/sincronización es condicional; no abre WorkManager/Offline-First. GitHub→Play es sugerencia, MCP opcional, Terraform recomendado. Conservar el conjunto de requisitos en [matriz completa](evaluacion-unidad-2.md).

## Presentación al profesor

APK/candidato y commit identificados, recorrido real de cuenta nueva y recurrente, errores observables sin datos privados, URL backend autorizada, enlaces a specs/planes/tareas/checklists y resultados del mismo candidato. Informar pendientes/fallos sin simular aprobación humana.


## Alineación recibida — 2026-11-04

[Contrato y matriz del corte](../integracion/alineacion-backend-2026-11-04.md). PDF página 4 pide registro/login funcionales, pantallas posteriores y avance Play en móvil; cuatro endpoints extra son requisito backend. El usuario además solicita correspondencia Android con operaciones disponibles. Specs redactadas no satisfacen funcionalidad ni Play; conservar casillas pendientes hasta evidencia. El alcance general de diciembre no queda cerrado.
