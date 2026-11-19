# Checklist — PL-08

## Estado al 2026-11-14

Código y pruebas escritos; **sin compilar ni ejecutar todavía**. No probado con una cuenta real de Google.

| Criterio | Prueba | Estado |
|---|---|---|
| PL-08-AC1 | `GoogleAccessApiTest.PL_08_existingAccountSignsInWithTokenOnly` | Escrita, sin ejecutar |
| PL-08-AC2 | `GoogleAccessApiTest.PL_08_newAccountSendsNicknameAndConsentAndStoresSession` | Escrita, sin ejecutar |
| PL-08-AC3 | `GoogleAccessApiTest.PL_08_nicknameRequiredCreatesNoSessionAndIsNotRetried` | Escrita, sin ejecutar |
| PL-08-AC4 | `GoogleAccessApiTest.PL_08_malformedTokenOrNicknameWithoutConsentNeverReachesTheNetwork` | Escrita, sin ejecutar |

- [ ] Huella SHA-1 registrada en Google Cloud
- [ ] Comprobado en teléfono con cuenta existente
- [ ] Comprobado en teléfono con cuenta nueva
