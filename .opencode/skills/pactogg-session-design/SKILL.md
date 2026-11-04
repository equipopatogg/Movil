---
name: pactogg-session-design
description: Preparar o revisar sesión, refresh, logout y almacenamiento de tokens
  de PactoGG. Usar para especificaciones de autenticación y privacidad local.
---

# pactogg-session-design

Raíz del proyecto: tres niveles arriba de esta carpeta. Aplicar la fase vigente de AGENTS.md y la constitución; esta skill no amplía la autorización del usuario.

El móvil consume sesión; la API valida y emite tokens. Expiración/renovación provienen del contrato y decisiones del equipo; no copiar el MVP sin refresh del profesor ni asignar TTLs propios.
Diseñar almacenamiento cifrado respaldado por Keystore, revocación, limpieza local, cierre de sesión y efectos en datos de caché. Room y DataStore no cifran tokens por sí mismos. Verificar vigencia y compatibilidad de la API de almacenamiento antes de elegir una biblioteca.
Distinguir fallo de red, sesión inválida y 403 por permisos. No usar finishAffinity como requisito universal de logout. Salida: ciclo de sesión, fallos, datos a limpiar y pruebas pendientes; sin código de autenticación en preparación.
