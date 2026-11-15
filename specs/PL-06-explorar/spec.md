# PL-06 — Explorar: ideas del día

Fecha: 2026-11-14. Estado: **APROBADA POR EL PO; PANTALLA ESCRITA, FALTA COMPILAR Y PROBAR EN EL CELULAR**. Sin backend: catálogo local. Maqueta: `explorar`. Sustituye el aviso "Esta sección estará disponible próximamente" de la navegación.

## Decisión del PO (2026-11-14)
Explorar deja de estar bloqueado. No muestra datos de otras personas ni "datos de ejemplo": muestra ideas de retos de un catálogo incluido en la app (`app/src/main/assets/explorar/retos.json`, 500 ideas de 200 juegos). La lista de juegos es una selección editorial, no un ranking oficial, y los retos propios de cada juego deben revisarse por alguien que los juegue.

## Requisitos
- RF1: La entrada "Explorar" de la barra inferior abre la pantalla (ruta `EXPLORE`); ya no abre un diálogo.
- RF2: Se muestran 4 ideas por día. El mismo día salen las mismas 4; al cambiar el día del teléfono cambian.
- RF3: "Descartar" quita esa idea y entra otra en su lugar. Lo descartado se olvida al salir de la app.
- RF4: Buscador por juego o texto y categorías Todos / Habilidad / Cooperativo / Constancia, como en la maqueta; se aplican antes de elegir las 4.
- RF5: "Usar esta idea" guarda juego y condición en el borrador y abre la lista de miembros del grupo para elegir rival (ajuste tras la prueba en teléfono del 2026-11-14). Al tocar "Retar a …" se abre Crear (MO-CON-01) con juego, reto y rival puestos; fecha y fianza las pone la persona. Sin grupo seleccionado se manda a Grupos con un aviso. No se crea nada sin pasar por la revisión de Crear.
- RF6: El catálogo se lee una vez, fuera del hilo principal. Filas inválidas (sin juego, reto de más de 280, categoría desconocida) se omiten.
- RF7: Sin coincidencias: "Ese pacto aún está por inventar." y botón para ver todas las ideas.

## Criterios
| ID | Resultado |
|---|---|
| PL-06-AC1 | Explorar abre la pantalla y muestra 4 ideas del catálogo. |
| PL-06-AC2 | El mismo día se ven las mismas 4; otro día cambian. |
| PL-06-AC3 | Descartar reemplaza solo esa idea. |
| PL-06-AC4 | Usar una idea lleva a elegir rival y luego a Crear con juego, condición y rival llenos, sin lanzar el reto. |
| PL-06-AC5 | El catálogo incluido tiene 500 ideas válidas de 200 juegos y caben en el formulario. |
