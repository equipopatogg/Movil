# PL-07 — Demo guiada antes de iniciar sesión

Fecha: 2026-11-14. Estado: **APROBADA POR EL PO; PANTALLA ESCRITA, FALTA COMPILAR Y PROBAR EN EL CELULAR**. Sin backend. Maqueta: `demo` ("Probar la demo interactiva" en el acceso).

## Decisión del PO (2026-11-14)
La demo es obligatoria. Es un recorrido local, sin cuenta, que explica el producto. Todo lo que muestra está marcado como simulación o ejemplo. El paso de "reto por voz" **no pide hablar**: es una animación en la que la frase y los campos se escriben solos.

## Requisitos
- RF1: En la pantalla de inicio de sesión (no en registro) aparece "Probar la demo interactiva". El acceso existente no cambia.
- RF2: Cinco pantallas como la maqueta: bienvenida, ficha del acuerdo, arma tu reto, comprueba y revisión de evidencia. Se puede avanzar, retroceder (también con el botón Atrás del teléfono) y salir en cualquier momento.
- RF3: "Arma tu reto" muestra un micrófono que pulsa, la frase escribiéndose letra por letra y luego los campos Videojuego, Reto, Rival y Recompensa llenándose en orden. No usa micrófono, permisos ni red. Hay "Saltar animación" y "Repetir animación".
- RF4: "Revisión" simula el envío de evidencia con un interruptor; no sube archivos ni decide ganador.
- RF5: El último paso ofrece "Crear mi cuenta", que cierra la demo y abre el registro.
- RF6: La demo no guarda nada, no crea sesión y no llama al servidor.

## Criterios
| ID | Resultado |
|---|---|
| PL-07-AC1 | Desde el acceso se abre la demo y se recorre de principio a fin sin cuenta. |
| PL-07-AC2 | La animación de voz escribe primero la frase y luego los campos en orden, sin micrófono. |
| PL-07-AC3 | Salir o Atrás en el primer paso regresa al acceso; "Crear mi cuenta" abre el registro. |
| PL-07-AC4 | La demo no hace ninguna llamada de red. |
