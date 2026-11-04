package com.pactogg.app.feature.connected.presentation

import com.pactogg.app.core.network.ApiFailure
import java.io.IOException

fun Exception.userMessage(): String = when (this) {
    is ApiFailure -> ApiMessages[code] ?: statusMessage(status)
    is IOException -> "No se pudo confirmar la operación. Revisa tu conexión; no la repetimos automáticamente."
    is IllegalArgumentException -> "Revisa los datos: están incompletos o no cumplen el formato permitido."
    else -> "No se pudo completar la operación de forma segura. Vuelve a intentarlo cuando hayas revisado el estado."
}

private fun statusMessage(status: Int): String = when (status) {
    401 -> "La sesión no es válida. Inicia sesión de nuevo."
    403 -> "No tienes permiso para esta operación."
    404 -> "El recurso no está disponible."
    409 -> "La operación entra en conflicto con el estado actual."
    400, 422 -> "El servidor rechazó los datos. Revisa los campos."
    429 -> "Hay demasiadas solicitudes. Espera antes de volver a intentarlo."
    in 500..599 -> "El servicio no está disponible por ahora."
    else -> "La respuesta no pudo validarse. No confirmamos la operación."
}

private val ApiMessages = mapOf(
    "MODERATION_UNAVAILABLE" to "La moderación del servidor no está disponible. No se confirmó el envío.",
    "STORAGE_UNAVAILABLE" to "El almacenamiento del servidor no está disponible. No se confirmó el envío.",
    "INVALID_CREDENTIALS" to "Correo o contraseña incorrectos.",
    "EMAIL_ALREADY_EXISTS" to "El correo ya está registrado.",
    "NICKNAME_ALREADY_EXISTS" to "El apodo ya está en uso.",
    "SESSION_RENEWED" to "Se renovó tu sesión. Revisa los datos y vuelve a enviar.",
    "SESSION_CHANGED" to "La sesión cambió. Vuelve a iniciar sesión.",
    "GROUP_LIMIT_REACHED" to "Alcanzaste el límite de grupos permitido.",
    "CONTENT_REJECTED" to "El contenido fue rechazado. Elige otra imagen o revisa el texto o enlace.",
    "INVALID_BOND" to "La fianza no está permitida por el grupo.",
    "INVALID_TARGET" to "El destinatario del reto no es válido.",
    "EVIDENCE_LIMIT_REACHED" to "El reto alcanzó su límite de evidencias.",
    "INVALID_CONTRACT_STATE" to "El estado del reto no admite evidencias.",
    "IMAGE_TOO_LARGE" to "La imagen supera el tamaño permitido.",
    "UNSUPPORTED_MEDIA_TYPE" to "Selecciona una imagen JPEG, PNG o WebP.",
)
