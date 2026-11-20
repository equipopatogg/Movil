# Matriz de pruebas por endpoint (app)

Generada con `python3 scripts/matriz-endpoints.py` a partir de las anotaciones de Retrofit y de `app/src/test`.
La categoría sale de `@Category` (UnitChecks, IntegrationChecks, SecurityChecks).

Endpoints consumidos: **62** · sin prueba de integración: **0**

| Método | Ruta | Unitarias | Integración | Seguridad |
|---|---|---|---|---|
| POST | `/api/v1/auth/forgot-password` | AccountRulesTest | PasswordRecoveryApiTest | AccountSecurityTest |
| POST | `/api/v1/auth/google` | AccountRulesTest | GoogleAccessApiTest | AccountSecurityTest |
| POST | `/api/v1/auth/login` | ConnectedFlowTest, LoginValidationTest | ApiIntegrationTest, LoginApiTest, SocialApiIntegrationTest | LoginSecurityTest |
| POST | `/api/v1/auth/logout` | LogoutValidationTest | ApiIntegrationTest, DeviceApiTest, LogoutApiTest, RefreshSessionApiTest | LogoutSecurityTest |
| POST | `/api/v1/auth/refresh` | RefreshSessionValidationTest | ApiIntegrationTest, RefreshSessionApiTest | PlaySecurityTest, RefreshSessionSecurityTest |
| POST | `/api/v1/auth/register` | RegisterValidationTest | ApiIntegrationTest, RegisterApiTest | RegisterSecurityTest |
| POST | `/api/v1/auth/reset-password` | AccountRulesTest | PasswordRecoveryApiTest | AccountSecurityTest |
| POST | `/api/v1/auth/verify-email` | AccountRulesTest | PlayRoutesApiTest | AccountSecurityTest |
| POST | `/api/v1/auth/verify-email/resend` | AccountRulesTest | PlayRoutesApiTest | AccountSecurityTest |
| POST | `/api/v1/commitments/{id}/complete` | PlayRulesTest | PlayRoutesApiTest | PlaySecurityTest |
| POST | `/api/v1/commitments/{id}/request-complete` | PlayRulesTest | PlayRoutesApiTest | PlaySecurityTest |
| POST | `/api/v1/contracts` | CreateChallengeValidationTest | ApiIntegrationTest, CreateChallengeApiTest, OfflineCacheTest | CreateChallengeSecurityTest |
| GET | `/api/v1/contracts/{id}` | ChallengeDetailsValidationTest, CreateChallengeValidationTest, EvidenceConfirmationTest, InputRulesTest | ApiIntegrationTest, ChallengeDetailsApiTest, RejectChallengeApiTest, SocialApiIntegrationTest | ChallengeDetailsSecurityTest |
| POST | `/api/v1/contracts/{id}/accept` | AcceptChallengeValidationTest, NicknameQueriesTest | AcceptChallengeApiTest, RejectChallengeApiTest, SocialApiIntegrationTest | AcceptChallengeSecurityTest |
| GET | `/api/v1/contracts/{id}/evidence` | EvidenceConfirmationTest, SendEvidenceValidationTest | ApiIntegrationTest, SendEvidenceApiTest | SendEvidenceSecurityTest |
| POST | `/api/v1/contracts/{id}/evidence` | SendEvidenceValidationTest | ApiIntegrationTest, SendEvidenceApiTest | SendEvidenceSecurityTest |
| POST | `/api/v1/contracts/{id}/evidence` | SendEvidenceValidationTest | ApiIntegrationTest, SendEvidenceApiTest | SendEvidenceSecurityTest |
| POST | `/api/v1/contracts/{id}/reject` | NicknameQueriesTest, RejectChallengeValidationTest | RejectChallengeApiTest, SocialApiIntegrationTest | RejectChallengeSecurityTest |
| GET | `/api/v1/contracts/{id}/result` | PlayRulesTest | PlayRoutesApiTest | PlaySecurityTest |
| PUT | `/api/v1/contracts/{id}/votes` | PlayRulesTest, PlayValidationTest | PlayApiTest | PlaySecurityTest |
| POST | `/api/v1/contracts/{id}/votes/resolve` | PlayRulesTest, PlayValidationTest | PlayRoutesApiTest | PlaySecurityTest |
| GET | `/api/v1/contracts/{id}/votes/summary` | PlayRulesTest | PlayApiTest | PlaySecurityTest |
| DELETE | `/api/v1/devices/me` | PushRegistrationTest | DeviceApiTest | AccountSecurityTest |
| PUT | `/api/v1/devices/me` | PushRegistrationTest | DeviceApiTest | AccountSecurityTest |
| GET | `/api/v1/groups` | CreateGroupValidationTest, ListGroupsValidationTest, NicknameQueriesTest | CreateGroupApiTest, ListGroupsApiTest, OfflineCacheTest, SocialApiIntegrationTest | CreateGroupSecurityTest, ListGroupsSecurityTest |
| POST | `/api/v1/groups` | CreateGroupValidationTest, GroupDetailsValidationTest, ListGroupsValidationTest, NicknameQueriesTest | CreateGroupApiTest, GroupDetailsApiTest, ListGroupsApiTest, OfflineCacheTest, SocialApiIntegrationTest | CreateGroupSecurityTest, GroupDetailsSecurityTest, ListGroupsSecurityTest |
| POST | `/api/v1/groups/join` | JoinGroupValidationTest, NicknameQueriesTest | JoinGroupApiTest, SocialApiIntegrationTest | JoinGroupSecurityTest |
| GET | `/api/v1/groups/{gid}/commitments` | PlayRulesTest | PlayApiTest | PlaySecurityTest |
| GET | `/api/v1/groups/{gid}/commitments/summary` | PlayRulesTest | PlayRoutesApiTest | PlaySecurityTest |
| GET | `/api/v1/groups/{gid}/contracts` | ListChallengesValidationTest, NicknameQueriesTest | ListChallengesApiTest, SocialApiIntegrationTest | ListChallengesSecurityTest |
| POST | `/api/v1/groups/{gid}/contracts/parse` | PlayRulesTest, PlayValidationTest | PlayApiTest | PlaySecurityTest |
| GET | `/api/v1/groups/{gid}/fame` | PlayRulesTest | PlayRoutesApiTest | PlaySecurityTest |
| GET | `/api/v1/groups/{gid}/ranking` | PlayRulesTest | PlayRoutesApiTest | PlaySecurityTest |
| GET | `/api/v1/groups/{gid}/shame` | PlayRulesTest | PlayRoutesApiTest | PlaySecurityTest |
| POST | `/api/v1/groups/{gid}/tournaments` | PlayRulesTest, PlayValidationTest | PlayApiTest | PlaySecurityTest |
| GET | `/api/v1/groups/{id}` | GroupDetailsValidationTest | GroupDetailsApiTest, JoinGroupApiTest, SocialApiIntegrationTest | GroupDetailsSecurityTest, JoinGroupSecurityTest |
| GET | `/api/v1/groups/{id}/members` | ListMembersValidationTest, NicknameQueriesTest | ListMembersApiTest, SocialApiIntegrationTest | ListMembersSecurityTest |
| GET | `/api/v1/matches/{id}` | PlayRulesTest | PlayRoutesApiTest | PlaySecurityTest |
| POST | `/api/v1/matches/{id}/confirm` | PlayRulesTest | PlayApiTest, PlayRoutesApiTest | PlaySecurityTest |
| POST | `/api/v1/matches/{id}/dispute` | PlayRulesTest, PlayValidationTest | PlayRoutesApiTest | PlaySecurityTest |
| POST | `/api/v1/matches/{id}/report` | PlayRulesTest, PlayValidationTest | PlayApiTest | PlaySecurityTest |
| POST | `/api/v1/matches/{id}/resolve` | PlayRulesTest | PlayRoutesApiTest | PlaySecurityTest |
| GET | `/api/v1/me/matches/next` | PlayRulesTest | PlayApiTest | PlaySecurityTest |
| GET | `/api/v1/me/votes/pending` | PlayRulesTest | PlayApiTest | PlaySecurityTest |
| GET | `/api/v1/notifications` | PlayRulesTest | PlayApiTest | PlaySecurityTest |
| POST | `/api/v1/notifications/read-all` | PlayRulesTest | PlayRoutesApiTest | PlaySecurityTest |
| DELETE | `/api/v1/notifications/{id}` | PlayRulesTest | PlayRoutesApiTest | PlaySecurityTest |
| PATCH | `/api/v1/notifications/{id}/read` | PlayRulesTest | PlayRoutesApiTest | PlaySecurityTest |
| POST | `/api/v1/reports` | PlayRulesTest, PlayValidationTest | PlayApiTest | PlaySecurityTest |
| GET | `/api/v1/tournaments` | EvidenceConfirmationTest, ListTournamentsValidationTest | ApiIntegrationTest, ListTournamentsApiTest | ListTournamentsSecurityTest |
| GET | `/api/v1/tournaments/{id}` | ConnectedFlowTest, PlayRulesTest | PlayRoutesApiTest | PlaySecurityTest |
| DELETE | `/api/v1/tournaments/{id}/join` | PlayRulesTest | PlayApiTest | PlaySecurityTest |
| POST | `/api/v1/tournaments/{id}/join` | PlayRulesTest, PlayValidationTest | PlayApiTest | PlaySecurityTest |
| POST | `/api/v1/tournaments/{id}/start` | PlayRulesTest | PlayRoutesApiTest | PlaySecurityTest |
| GET | `/api/v1/users/check-nickname` | CheckNicknameValidationTest, NicknameQueriesTest | CheckNicknameApiTest, SocialApiIntegrationTest | CheckNicknameSecurityTest |
| DELETE | `/api/v1/users/me` | EditProfileValidationTest, PlayValidationTest, ProfileDetailsValidationTest | EditProfileApiTest, PlayApiTest, ProfileDetailsApiTest, SocialApiIntegrationTest | EditProfileSecurityTest, PlaySecurityTest, ProfileDetailsSecurityTest |
| GET | `/api/v1/users/me` | EditProfileValidationTest, NicknameQueriesTest, ProfileDetailsValidationTest | EditProfileApiTest, PlayApiTest, ProfileDetailsApiTest, SocialApiIntegrationTest | EditProfileSecurityTest, ProfileDetailsSecurityTest |
| PATCH | `/api/v1/users/me` | EditProfileValidationTest, NicknameQueriesTest, ProfileDetailsValidationTest | EditProfileApiTest, PlayApiTest, ProfileDetailsApiTest, SocialApiIntegrationTest | EditProfileSecurityTest, ProfileDetailsSecurityTest |
| PATCH | `/api/v1/users/me` | EditProfileValidationTest, PlayValidationTest, ProfileDetailsValidationTest | EditProfileApiTest, PlayApiTest, ProfileDetailsApiTest, SocialApiIntegrationTest | EditProfileSecurityTest, PlaySecurityTest, ProfileDetailsSecurityTest |
| POST | `/api/v1/users/me/avatar` | PlayValidationTest | PlayApiTest | PlaySecurityTest |
| DELETE | `/api/v1/users/{id}/block` | PlayRulesTest, PlayValidationTest | PlayApiTest | PlaySecurityTest |
| POST | `/api/v1/users/{id}/block` | AccountRulesTest, PlayRulesTest, PlayValidationTest | OfflineCacheTest, PlayApiTest | PlaySecurityTest |
