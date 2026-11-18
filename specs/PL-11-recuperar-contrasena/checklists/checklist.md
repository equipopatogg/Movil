# Checklist — PL-11

## Estado al 2026-11-16

Código y pruebas escritos; **sin compilar ni ejecutar todavía**.

| Criterio | Prueba | Estado |
|---|---|---|
| PL-11-AC1 | `PasswordRecoveryApiTest.PL_11_forgotPasswordSendsOnlyTheEmailWithoutSession` | Escrita, sin ejecutar |
| PL-11-AC2 | `PasswordRecoveryApiTest.PL_11_resetPasswordSendsTokenAndNewPassword` | Escrita, sin ejecutar |
| PL-11-AC3 | `PasswordRecoveryApiTest.PL_11_expiredLinkReportsItsCode` | Escrita, sin ejecutar |
| PL-11-AC4 | `PasswordRecoveryApiTest.PL_11_badEmailTokenOrShortPasswordNeverReachTheNetwork` | Escrita, sin ejecutar |

- [ ] Correo recibido y enlace abierto en el teléfono
