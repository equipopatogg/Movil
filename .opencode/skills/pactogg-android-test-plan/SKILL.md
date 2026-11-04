---
name: pactogg-android-test-plan
description: Preparar pruebas Android significativas desde criterios de PactoGG. Usar
  en planes JVM, MockWebServer, Room, Compose y revisión de evidencia.
---

# pactogg-android-test-plan

Raíz del proyecto: tres niveles arriba de esta carpeta. Aplicar la fase vigente de AGENTS.md y la constitución; esta skill no amplía la autorización del usuario.

Elegir nivel de prueba por riesgo: lógica JVM, transporte MockWebServer, persistencia Room real/migraciones y navegación/accesibilidad en dispositivo. Distinguir prueba escrita, compilada, ejecutada y aprobada.
No imponer un test por clase ni pruebas ficticias para subir cobertura. Medir antes de proponer porcentajes; no importar 45/80 del profesor ni cambiar de JUnit/MockK/Turbine sin justificación.
Diseñar datos sintéticos y aislamiento, fallos de proveedor y casos de límite del criterio aprobado. Considerar memoria/dispositivo/ABI y distinguir fallo del entorno de fallo del producto. Entregar criterio→caso→nivel→evidencia esperada y controles no ejecutados.

Usar el [plan de pruebas](../../../docs/preparacion/plan-de-pruebas.md) y los [casos del primer flujo](../../../docs/preparacion/primer-flujo-movil.md). Un fake de almacén no verifica cifrado/Keystore; inspeccionar nombres o constructores no verifica custodia de datos; una fixture local no acredita al proveedor. Pedir la evidencia adecuada al criterio, sin impedir dobles de prueba aislados.
