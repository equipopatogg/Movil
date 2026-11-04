# Diseño Android

Referencia documental: Descripción del Móvil, sección 6. La maqueta aprobada debe cerrar cada flujo funcional.

- Navegación inferior: Retos, Torneos, Cuentas, Perfil. Grupos accesible desde Perfil.
- Tema oscuro fijo: fondo #0B1020, superficie #121A2D, campos #0C1425, naranja #FF7A1A, texto #E2EBFA. Acentos cian #22D3EE y lavanda #B89AF9.
- Bordes visibles, esquinas discretas, sin sombras decorativas, brillos ni degradados.
- Un botón principal naranja por pantalla, textos breves, controles de al menos 48 dp mediante Material.
- Contenido desplazable para pantallas pequeñas y escalado de fuente; recursos de texto en strings.xml.
- Estados iniciales honestos: no hay listas inventadas, saldo simulado ni sesión falsa.
- Las letras de la barra son marcadores provisionales de iconografía; sustituir por los recursos aprobados de la maqueta.
- Falta validar visualmente todas las vistas en dispositivo y contra la maqueta. El cascarón no reproduce aún sus flujos completos.

## Paleta corregida con el archivo Make — 2026-11-03

Fuente: src/index.css y src/welcome.css del archivo local facilitado. A petición del usuario se sustituye la paleta gris anterior por los colores del boceto. Fondo azul-negro, tarjeta azul oscuro, borde #344461, secundario #B4C3DC, títulos con segundo tramo lavanda y enlaces/foco cian. Naranja reservado a acciones principales; morado #8B5CF6 para acentos. No cambia navegación, reglas de producto ni backend.
