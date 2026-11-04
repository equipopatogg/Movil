# Diseño UI — acceso y primer ingreso

Fecha: 2026-11-03. Alcance confirmado por el usuario: **solo specs y diseño**, sin implementar Compose, red, dependencias o publicación. Prepara dos etapas reales: UI/navegación y posterior integración. La fecha de preparación no constituye aceptación ni implementación.

## Fuentes y decisiones aplicables

[DESIGN](../../DESIGN.md), [primer flujo](primer-flujo-movil.md), historias originales allí trazadas, [PG-03](../../specs/PG-03-acceso-cuenta/spec.md), PG-04 (antecedente retirado del expediente activo), PDF v3 y boceto Figma Make recibido. Las notas privadas ignoradas son ayuda, no dependencia de esta documentación pública. Figma aporta composición, jerarquía, tarjetas y recorridos; no impone clip/video, cifras, políticas de sesión o condiciones de jurado.

Paleta actualizada por petición expresa posterior del usuario: fondo #0B1020, superficie #121A2D, campos #0C1425, naranja #FF7A1A, texto #E2EBFA y secundario #B4C3DC; títulos lavanda #B89AF9 y enlaces/foco cian #22D3EE, recuperados del archivo Make. Sin brillo/degradados decorativos. Componentes Material 3, recursos de texto, targets mínimos 48 dp, contenido desplazable y teclado accesible. Los textos siguientes son propuestas de UI, no reglas del servidor.

## Inventario del primer alcance visual

| ID de diseño | Pantalla | Contenido / componentes | Acciones y salida |
|---|---|---|---|
| UI-A01 | Acceso | Marca/eslogan, título Iniciar sesión, correo, contraseña oculta y mostrar, botón principal, acceso a registro | Editar, alternar visibilidad, solicitar ingreso; carga/error por estado de UI. Sin sesión ficticia. Recuperación solo si se incluye y define ese recorrido. |
| UI-A02 | Registro | Título Crear cuenta; apodo, correo, contraseña; ayuda y espacio para consentimiento conforme a texto aprobado | Editar, solicitar registro, volver a acceso. Apodo obligatorio por decisión de producto; disponibilidad/longitudes y consentimiento quedan sujetos a contrato. |
| UI-A03 | Resolución de acceso | Estado de carga/validación y mensaje seguro ante error; sin contenido protegido debajo | El coordinador futuro recibe resultado real. La variante de muestra pertenece solo a catálogo visual aislado. |
| UI-P01 | Tablero inicial propuesto | Encabezado con marca/perfil; grupo activo si existe; vacío de actividad; accesos del alcance aprobado | Navegar a perfil/grupos. No mostrar saldos, tareas, retos ni miembros inventados. |
| UI-P02 | Perfil/apodo propuesto | Identidad o vacío coherente; apodo, acciones pertinentes, enlaces privacidad y cierre | Editar si la operación entra al corte; salida con confirmar/cancelar según política futura. No marcar cambio guardado o logout remoto por un toque visual. |
| UI-P03 | Grupos propuesto | Título, listado/vacío, carga/error; nombres de ejemplo solo en catálogo | Seleccionar grupo existente; crear/unirse son variantes propuestas, sin alta simulada. Si entran, definir contrato y spec del cambio aplicable antes de conectarlas. |

UI-A01/A02 corresponden a registro/login del corte. UI-P01/P02/P03 son una propuesta concreta para revisar el destino posterior; el PDF no nombra esas tres pantallas. Esta preparación no ratifica por sí sola su contenido funcional.

## Bocetos textuales de distribución

Acceso: marca → título/explicación → correo → contraseña + mostrar → espacio de error → botón principal → enlace crear cuenta. Campo enfocado queda visible con teclado; todo puede desplazarse. No se ofrece «mantener sesión» sin definir su efecto respecto a la continuidad de sesión exigida.

Registro: volver/marca → título → apodo → correo → contraseña/mostrar → ayuda y consentimiento ratificado cuando exista → error → crear cuenta → volver a login. No copiar mínimo de ocho caracteres o checkbox de puntos como política legal solo porque Figma los muestra.

Tablero: encabezado → contexto del grupo → título breve → actividad real o vacío → accesos útiles. Perfil y grupos reutilizan barra superior y componentes de estado. El acceso inicial no muestra barra inferior de la zona autenticada. Para zona interna se conserva la navegación de DESIGN hasta aprobar explícitamente otra; no cambiarla por las cinco pestañas del boceto.

## Estados y eventos de presentación propuestos

| Familia | Estados que deben poder diseñarse | Eventos sin efecto de red en esta fase |
|---|---|---|
| Formulario acceso/registro | Inicial, edición, contraseña visible/oculta, validación requerida, envío, rechazo y fallo recuperable | Cambiar campo, alternar visibilidad, enviar intención, volver |
| Tablero/perfil/grupos | Carga, vacío, contenido autorizado, error, permiso denegado, sesión no disponible | Solicitar recarga, abrir destino, volver, solicitar salida |
| Diálogo salida | Cerrado, abierto, confirmación pendiente | Abrir, cancelar, confirmar intención |

Los errores junto al campo y mensajes generales no deben revelar existencia de cuentas ni detalles internos; redacción final se concilia con contrato. Reglas de formato, disponibilidad y contraseña no se inventan. Las intenciones no garantizan respuesta ni éxito.

## Frontera UI / integración posterior

Se diseñan Screen, estado de presentación y callbacks; ViewModel/repositorio real se conectarán después. No escribir ahora DTO HTTP, rutas, persistencia, Keystore, auth flags o cliente fake dentro de producción. No guardar contraseña en SavedStateHandle, Bundle, navegación, preferencias, logs o fixtures permanentes.

Cuando se autorice implementar solo la UI, usar Previews o catálogo aislado de debug para variantes con datos sintéticos; no configurar release para entrar como autenticado ni simular registro exitoso. El diseño deberá permitir inyectar estado real sin rehacer pantallas. Este catálogo es una propuesta futura, no archivos ya creados.

## Accesibilidad y adaptación Android

Orden de foco lógico, labels persistentes, estado de error anunciado, botón mostrar contraseña con descripción y estado comprensible, contraste a verificar, controles de 48 dp, fuente ampliada y scroll. Considerar barras/insets/IME, atrás del sistema y botón volver coherentes. No importar marco iPhone, barra de Figma ni textos de demo al producto.

## Criterios de cierre de diseño

- Pantallas, estados y eventos trazados a PG03-UI1–UI4 y PG04-UI1–UI3.
- Componentes y estilos coherentes con DESIGN; diferencias con Figma explícitas.
- Acceso real y variantes visuales separados; sin datos/operaciones de negocio ficticios.
- Pendientes de contrato y selección de pantallas internas visibles.
- Sin código creado, pruebas ejecutadas, aceptación humana atribuida ni integración declarada.

Al integrar se revisan las specs con la nueva versión contractual y se registra el cambio; no se retrofechan documentos ni se transforman pruebas visuales en evidencia de backend.
