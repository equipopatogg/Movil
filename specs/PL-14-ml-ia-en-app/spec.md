# PL-14 — Machine Learning e IA generativa dentro de la app

Fecha: 2026-11-26. Estado: **PROPUESTA, IMPLEMENTADA EN `feat/ml-ia-en-app`; pendiente de compilar en CI y probar en teléfono.** Origen: auditoría OpenCode del 2026-11-20 ("ML e IA generativa no están como capacidades del cliente móvil") y requisito del docente "al menos una función de Machine Learning y una de IA generativa dentro de la app".

## Alcance
- **ML en el teléfono (ML Kit, reconocimiento de texto con el modelo incluido):** al reportar el resultado de una partida de torneo con la captura, la app lee el marcador de la foto y lo propone en los campos si están vacíos. La foto no sale del teléfono para esta lectura.
- **IA generativa (Gemini con Firebase AI Logic, Gemini Developer API sin facturación):** en Explorar, "Ideas con IA" genera 3 retos para el juego que escribe la persona. Cada idea se usa con el mismo flujo de "Usar esta idea" (borrador → elegir rival → revisar). El backend sigue moderando el texto al crear el reto.
- El reto por voz (PL-05) sigue en el backend; esta spec no lo cambia.

## Requisitos
- RF1: El marcador se acepta solo si el texto reconocido trae **un único** par de números 0–999 separados por `-`, `–`, `:`, `x` o `a`; horas (`18:30`), versiones (`1.2`) y varios marcadores distintos no se proponen.
- RF2: Lo leído solo rellena campos vacíos y se muestra "Marcador leído de la foto con ML Kit: A – B. Revísalo antes de enviar." La persona siempre confirma.
- RF3: Al modelo de IA solo se envía el nombre del juego (1–40 caracteres; letras, números, espacio y `:'&.+-`). Sin apodos, correos, grupo ni ubicación. Comillas o saltos de línea se rechazan antes de la red.
- RF4: La respuesta se lee como JSON; se descartan ideas vacías, repetidas, de más de 60/280 caracteres o que mencionen dinero o apuestas; máximo 3.
- RF5: Máximo 5 consultas por sesión de la pantalla, para cuidar la cuota gratuita.
- RF6: Sin `google-services.json`, sin Firebase AI Logic activado, sin red o con error del modelo, se muestra "La IA no está disponible ahora. Usa las ideas de abajo." y Explorar sigue funcionando con el catálogo local.
- RF7: No hay llaves en el código. El modelo se configura con `pactogg.geminiModel` (por defecto `gemini-3.8-flash`, sin facturación).

## Criterios
| ID | Resultado | Prueba |
|---|---|---|
| PL-14-AC1 | Formatos de marcador comunes se leen; ambiguos no | `ScoreParserTest` |
| PL-14-AC2 | Solo un nombre de juego limpio llega al modelo | `IdeaPromptTest.PL_14_onlyAPlainGameNameReachesTheModel` (seguridad) |
| PL-14-AC3 | Salida filtrada (dinero, duplicados, tamaño, máximo 3) | `IdeaPromptTest.PL_14_dropsMoneyEmptyDuplicatedOversizedAndExtraIdeas` (seguridad) |
| PL-14-AC4 | Servicio caído muestra aviso y no rompe Explorar; límite por sesión | `AiIdeasViewModelTest` |
| PL-14-AC5 | En el teléfono: foto con "3 - 1" rellena el marcador; "Ideas con IA" para "Mario Kart" da 3 ideas | Prueba manual (pendiente) |

## Configuración necesaria (una vez, gratis)
1. Firebase console del proyecto de PactoGG → **AI Logic** → *Get started* → **Gemini Developer API** (plan Spark, sin tarjeta).
2. Verificar que la llave de API de Android de Firebase no tenga restricciones que bloqueen "Firebase AI Logic API".
3. Compilar con el `google-services.json` del proyecto (el mismo del push).

## Privacidad
El aviso en la app (`privacy.xml`) y la landing mencionan Gemini (solo nombre del juego) y ML Kit (en el teléfono).
