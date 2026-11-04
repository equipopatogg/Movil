# Recursos visuales del acceso

Fecha: 2026-11-03. El usuario pide incorporar los primeros iconos del inicio de sesión tras extraerlos de su archivo Make. Fuente: componente Icon en src/App.tsx del commit interno 02db2100b75b1c0a49d722ae06a0c236766d58fd. No se atribuye una licencia de librería externa por inferencia.

| Recurso Android | Origen | Uso |
|---|---|---|
| ic_pacto_bolt.xml | paths.bolt | Ilustración del acceso |
| ic_pacto_check.xml | paths.check | Confirmación decorativa |
| ic_pacto_shield.xml | paths.shield | Cabecera del formulario |
| ic_pacto_users.xml | paths.users | Variante de registro |
| ic_pacto_chevron.xml | paths.chevron | Botón principal |

SVG adaptados a VectorDrawable 24×24 con trazo 2.2, extremos/uniones redondeados y tinte aplicado por Material. Circle de users convertido a arcos equivalentes. Sin paquete adicional o renderizador SVG.

AccessArtwork recrea con Canvas/Surface la tarjeta inclinada, órbitas y marca del CSS de acceso; no se presenta como imagen original extraída. La ilustración es decorativa y no añade foco/lectura duplicados; los iconos de cabecera/botón acompañan texto visible. No se incorporan evidencia demo, fotos adjuntas o marco iPhone.

No cambia cuenta, sesión, backend, permisos, dependencias ni navegación. Verificación técnica registrada en verificacion-ui-local.md; revisión visual en dispositivo pendiente. Sin commit/push por este cambio.
