package com.pactogg.app.feature.connected.presentation

import com.pactogg.app.core.network.ApiFailure
import java.io.IOException

fun Exception.userMessage(): String = when (this) {
    is ApiFailure -> ApiMessages[code] ?: statusMessage(status)
    is IOException -> "Se perdió la conexión. No sabemos si la acción se completó. " +
        "Revisa su estado antes de enviarla otra vez."
    is IllegalArgumentException -> "Revisa los datos: están incompletos o no cumplen el formato permitido."
    else -> "No pudimos completar la acción. Revisa si se guardó antes de volver a intentarlo."
}

private fun statusMessage(status: Int): String = when (status) {
    401 -> "La sesión no es válida. Inicia sesión de nuevo."
    403 -> "No tienes permiso para esta operación."
    404 -> "No encontramos lo que buscas. Puede que ya no esté disponible."
    409 -> "La información cambió. Actualiza la pantalla antes de continuar."
    400, 422 -> "Hay datos que debemos corregir. Revisa lo que escribiste."
    429 -> "Espera un momento antes de volver a intentarlo."
    in 500..599 -> "El servicio no está disponible por ahora."
    else -> "No pudimos comprobar el resultado. Actualiza la pantalla para revisar si se guardó."
}

private val ApiMessages = mapOf(
    "MODERATION_UNAVAILABLE" to "No pudimos revisar el contenido en este momento. El envío no se confirmó.",
    "STORAGE_UNAVAILABLE" to "No pudimos guardar el archivo en este momento. El envío no se confirmó.",
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
    "INVALID_CONTRACT_STATE" to "El reto cambió, venció o no admite esta acción. Actualízalo antes de decidir.",
    "INSUFFICIENT_POINTS" to "No tienes puntos suficientes para la fianza.",
    "CANNOT_ACCEPT_OWN_CONTRACT" to "No puedes aceptar tu propio reto.",
    "NOT_CONTRACT_TARGET" to "Esta acción corresponde al usuario retado.",
    "NOT_GROUP_MEMBER" to "No perteneces a este grupo.",
    "ALREADY_MEMBER" to "Ya perteneces a este grupo. Puedes abrirlo desde Mis grupos.",
    "GROUP_FULL" to "El grupo alcanzó su límite de miembros.",
    "IMAGE_TOO_LARGE" to "La imagen supera el tamaño permitido.",
    "UNSUPPORTED_MEDIA_TYPE" to "Selecciona una imagen JPEG, PNG o WebP.",
)
