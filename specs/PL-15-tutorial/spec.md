# PL-15 — Tutorial de primer uso

Fecha: 2026-11-26. Estado: **PEDIDA POR EL PO E IMPLEMENTADA en `feat/ml-ia-en-app`; pendiente de compilar en CI y revisar en teléfono.**

## Alcance
Recorrido de 10 pasos a pantalla completa que explica para qué sirve cada botón principal: grupos, crear reto (rival o todo el grupo, recompensa, fianza), por voz, aceptar y subir evidencia, jurado (mínimo 3 personas), Explorar e Ideas con IA, torneos (lectura del marcador), avisos y ranking, y perfil.

## Requisitos
- RF1: Se abre solo la primera vez que alguien entra en este teléfono (bandera `tutorial_v1_seen` en DataStore, no sensible).
- RF2: "Saltar tutorial" o "¡Listo, a jugar!" lo cierran y no vuelve a abrirse solo.
- RF3: Perfil · "Cómo usar PactoGG" lo abre de nuevo desde el primer paso.
- RF4: Cada paso dice dónde está el botón ("Barra de abajo · Crear", "Inicio · Jurado"…) y su explicación cabe en 240 caracteres.
- RF5: Atrás del sistema regresa un paso; en el primero cierra el tutorial.

## Criterios
| ID | Resultado | Prueba |
|---|---|---|
| PL-15-AC1 | Pasos completos y breves | `TutorialTest.PL_15_stepsExplainEveryMainButtonBriefly` |
| PL-15-AC2 | La navegación no se sale de los pasos | `TutorialTest.PL_15_pagerStaysInsideTheSteps` |
| PL-15-AC3 | Se abre una vez y luego solo desde Perfil | `TutorialTest.PL_15_opensOnceOnFirstUseAndAgainOnlyWhenAsked` |
