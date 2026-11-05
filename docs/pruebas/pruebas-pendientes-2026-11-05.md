# Pruebas pendientes — ejecución local 2026-11-05

Se completaron los cinco casos sin prueba específica y se ampliaron los errores/estados de las operaciones existentes. Se conserva el trabajo previo, sin cambios visuales, commits, push ni llamadas de escritura al proveedor real.

## Resultados

- JVM: **237 correctas, cero fallidas y cero omitidas** (41 nuevas).
- Android: **nueve correctas**, incluido bracket no vacío en emulador aislado API 37 (x86_64).
- Validadores de evidencia: **27 correctos**.
- Build, Lint y Detekt: **correctos**; Lint informa cero errores, cero advertencias y dos sugerencias. No se atribuye CI remoto a esta fase.

## Cinco pendientes principales

| Criterio | Comprobación local |
|---|---|
| MO-TOR-02-AC3 | Fixture no vacía mapea ronda, participantes y puntuación; componente Compose muestra ronda/partido/marcadores y ausencia de acciones de escritura. No acredita datos del despliegue real. |
| MO-USR-03-AC2 | PATCH 409 conserva identidad/tokens; formulario mantiene apodo editable y permite corregir; PATCH confirmado y GET posterior reflejan identidad nueva. |
| MO-MEM-01-AC2 | ALREADY_MEMBER 409 no repite POST ni sustituye sesión. Estado conserva grupo/saldo y permite corregir invitación sin duplicación local. |
| MO-MEM-02-AC3 | Repositorio sigue cursor; frontera HTTP envía limit=1 en dos páginas. Estado agrega sin duplicados y deja de consultar al final; la pantalla normal conserva limit=20. |
| MO-CON-07-AC3 | Secuencia HTTPS reject→GET→accept termina en 409, sin éxito ni cambio de identidad; estado cancelado bloquea aceptar y conserva saldo. |

## Casos añadidos

| Nivel | Clase y método exactos |
|---|---|
| integration | `com.pactogg.app.integration.ApiIntegrationTest#rotatingTokenPersistenceFailureClearsOldAndFreshIdentityAtomically` |
| integration | `com.pactogg.app.feature.auth.login.LoginApiTest#AUTH_01_specificProviderErrorsNeverSucceedRetryOrReplaceIdentity` |
| integration | `com.pactogg.app.feature.auth.login.LoginApiTest#AUTH_01_lostResponseIsNotReplayedOrReportedAsSuccess` |
| integration | `com.pactogg.app.feature.auth.logout.LogoutApiTest#AUTH_04_transportDisconnectStillErasesLocalTokensWithoutRetry` |
| integration | `com.pactogg.app.feature.auth.logout.LogoutApiTest#AUTH_04_specificProviderErrorsNeverSucceedRetryOrReplaceIdentity` |
| integration | `com.pactogg.app.feature.auth.register.RegisterApiTest#USR_01_specificProviderErrorsNeverSucceedRetryOrReplaceIdentity` |
| integration | `com.pactogg.app.feature.auth.register.RegisterApiTest#USR_01_lostResponseIsNotReplayedOrReportedAsSuccess` |
| integration | `com.pactogg.app.feature.auth.register.RegisterApiTest#USR_01_failedCustodyCannotPublishRegisteredIdentity` |
| integration | `com.pactogg.app.feature.auth.session.RefreshSessionApiTest#AUTH_03_twoConcurrent401ResponsesShareOneRotationAndRetryReadsOnce` |
| integration | `com.pactogg.app.feature.auth.session.RefreshSessionApiTest#AUTH_03_logoutDuringPendingRefreshEndsWithNoRestoredSession` |
| integration | `com.pactogg.app.feature.auth.session.RefreshSessionApiTest#AUTH_03_lostResponseIsNotReplayedOrReportedAsSuccess` |
| unit | `com.pactogg.app.feature.connected.presentation.ConnectedFlowTest#existingMembershipConflictKeepsGroupBalanceAndAllowsCorrection` |
| unit | `com.pactogg.app.feature.connected.presentation.ConnectedFlowTest#occupiedNicknameKeepsFormAndIdentityUntilCorrectedSuccessfully` |
| unit | `com.pactogg.app.feature.connected.presentation.ConnectedFlowTest#rejectedChallengeCannotBeConfirmedAsAcceptedAndKeepsBalance` |
| unit | `com.pactogg.app.feature.connected.presentation.ConnectedFlowTest#memberPagesAppendDeduplicateAndStopAfterLastPage` |
| unit | `com.pactogg.app.feature.connected.presentation.ConnectedFlowTest#nonemptyBracketSelectionIsReadOnlyAndCanReturnToList` |
| unit | `com.pactogg.app.feature.connected.presentation.ConnectedFlowTest#validAccessDoubleSubmitWaitsForCustodyAndNavigatesOnlyAfterSuccess` |
| unit | `com.pactogg.app.feature.connected.presentation.ConnectedFlowTest#validationAndModerationErrorsKeepDraftsEditableWithoutSuccessOrBalanceChanges` |
| unit | `com.pactogg.app.feature.connected.presentation.ConnectedFlowTest#insufficientPointsOrForbiddenActionNeverDeductsBalanceOrConfirmsSuccess` |
| unit | `com.pactogg.app.feature.connected.presentation.ConnectedFlowTest#evidenceModerationAndLimitErrorsKeepLinkWithoutInventingApprovedEvidence` |
| unit | `com.pactogg.app.feature.connected.presentation.NicknameQueriesTest#elapsedCooldownAllowsNewQueryAndEditingWithoutAutomaticPolling` |
| integration | `com.pactogg.app.feature.contracts.accept.AcceptChallengeApiTest#CON_06_specificProviderErrorsNeverSucceedRetryOrReplaceIdentity` |
| integration | `com.pactogg.app.feature.contracts.create.CreateChallengeApiTest#CON_01_specificProviderErrorsNeverSucceedRetryOrReplaceIdentity` |
| integration | `com.pactogg.app.feature.contracts.create.CreateChallengeApiTest#CON_01_lostResponseIsNotReplayedOrReportedAsSuccess` |
| integration | `com.pactogg.app.feature.contracts.detail.ChallengeDetailsApiTest#CON_03_specificProviderErrorsNeverSucceedRetryOrReplaceIdentity` |
| integration | `com.pactogg.app.feature.contracts.list.ListChallengesApiTest#CON_02_specificProviderErrorsNeverSucceedRetryOrReplaceIdentity` |
| integration | `com.pactogg.app.feature.contracts.reject.RejectChallengeApiTest#CON_07_rejectReadThenAcceptConflictNeverProducesSuccess` |
| integration | `com.pactogg.app.feature.contracts.reject.RejectChallengeApiTest#CON_07_specificProviderErrorsNeverSucceedRetryOrReplaceIdentity` |
| integration | `com.pactogg.app.feature.evidence.send.SendEvidenceApiTest#EVI_02_specificProviderErrorsNeverSucceedRetryOrReplaceIdentity` |
| integration | `com.pactogg.app.feature.evidence.send.SendEvidenceApiTest#EVI_02_lostResponseIsNotReplayedOrReportedAsSuccess` |
| integration | `com.pactogg.app.feature.groups.create.CreateGroupApiTest#GRP_01_specificProviderErrorsNeverSucceedRetryOrReplaceIdentity` |
| integration | `com.pactogg.app.feature.groups.detail.GroupDetailsApiTest#GRP_03_specificProviderErrorsNeverSucceedRetryOrReplaceIdentity` |
| integration | `com.pactogg.app.feature.groups.join.JoinGroupApiTest#MEM_01_existingMembershipConflictDoesNotRetryOrChangeSession` |
| integration | `com.pactogg.app.feature.groups.join.JoinGroupApiTest#MEM_01_specificProviderErrorsNeverSucceedRetryOrReplaceIdentity` |
| integration | `com.pactogg.app.feature.groups.members.ListMembersApiTest#MEM_02_followsNextCursorAndSupportsLimitOneAtWireBoundary` |
| integration | `com.pactogg.app.feature.profile.edit.EditProfileApiTest#USR_03_occupiedNicknamePreservesIdentityAndCanBeCorrected` |
| integration | `com.pactogg.app.feature.profile.edit.EditProfileApiTest#USR_03_specificProviderErrorsNeverSucceedRetryOrReplaceIdentity` |
| integration | `com.pactogg.app.feature.profile.nickname.CheckNicknameApiTest#USR_06_existingNameWithDifferentCaseIsShownOccupiedWithoutAuthentication` |
| integration | `com.pactogg.app.feature.tournaments.list.ListTournamentsApiTest#TOR_02_nonemptyBracketPreservesRoundsParticipantsAndScoresWithoutWrites` |
| integration | `com.pactogg.app.feature.tournaments.list.ListTournamentsApiTest#TOR_02_specificProviderErrorsNeverSucceedRetryOrReplaceIdentity` |
| integration | `com.pactogg.app.feature.tournaments.list.ListTournamentsApiTest#TOR_02_lostResponseIsNotReplayedOrReportedAsSuccess` |

Además: prueba Compose `TournamentBracketTest.nonemptyBracketShowsRoundParticipantsScoresAndNoWriteActions`.

## Límites que se conservan

Los 84 criterios originales siguen intactos. Cinco quedan comprobados en consumidor local; los criterios compuestos continúan parciales cuando falta interacción Compose, autorización del despliegue, accesibilidad o aceptación humana. No se equipara desconexión a timeout ni recreación de Activity a muerte de proceso. El cooldown se ensaya con deadline futuro/pasado sin esperar 60 segundos reales.

Las barreras de sesión ejercitan dos respuestas 401 simultáneas, logout solicitado mientras refresh sigue pendiente y fallo de persistencia de tokens rotados. No se afirma una prueba completa de refresh/logout/login B en Android.

Los errores HTTP se simulan con los códigos documentados en las specs; no demuestran que el backend real produzca cada error ni que su validación de campos sea correcta. El servidor de pruebas usa HTTPS y certificado de pruebas confiado explícitamente, sin bypass TLS.

Privacidad definitiva y catálogo de avatares esperan al backend. Play y entrega remota permanecen diferidos.

## Evidencia

JUnit local: `app/build/test-results/testDebugUnitTest/`; Android: `app/build/outputs/instrumented-offline/`. Identidades, fuente/línea, hashes y pendientes en [trazabilidad](trazabilidad-criterios.json); selección obligatoria de casos en [matriz](operaciones.json). Artefactos privados ignorados en `artifacts/pending-tests/`. Aceptación humana pendiente.
