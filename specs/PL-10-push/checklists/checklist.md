# Checklist — PL-10

## Estado al 2026-11-16

Código y pruebas escritos; **sin compilar ni ejecutar todavía**.

| Criterio | Prueba | Estado |
|---|---|---|
| PL-10-AC1 | `DeviceApiTest.PL_10_registersTokenWithPutAndBearer` | Escrita, sin ejecutar |
| PL-10-AC2 | Prueba en teléfono | Pendiente |
| PL-10-AC3 | `DeviceApiTest.PL_10_removesTokenWithDelete` y `PushRegistrationTest.PL_10_detachRemovesRegisteredToken` | Escrita, sin ejecutar |
| PL-10-AC4 | `PushRegistrationTest.PL_10_noPermissionInvalidTokenOrChangedAccountRegistersNothing` | Escrita, sin ejecutar |

- [ ] `google-services.json` en `app/` (no se sube a Git)
- [ ] Firebase configurado en el servidor
- [ ] Aviso recibido en el teléfono con la app abierta y cerrada
