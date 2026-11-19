package com.pactogg.app.feature.connected.presentation

import com.pactogg.app.core.network.ApiFailure
import java.io.IOException

fun Exception.userMessage(): String = when (this) {
    is ApiFailure -> displayMessage ?: ApiMessages[code] ?: statusMessage(status)
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
    "INSUFFICIENT_POINTS" to "No hay puntos suficientes para la fianza o la inscripción.",
    "CANNOT_ACCEPT_OWN_CONTRACT" to "No puedes aceptar tu propio reto.",
    "NOT_CONTRACT_TARGET" to "Esta acción corresponde al usuario retado.",
    "NOT_GROUP_MEMBER" to "No perteneces a este grupo.",
    "ALREADY_MEMBER" to "Ya perteneces a este grupo. Puedes abrirlo desde Mis grupos.",
    "GROUP_FULL" to "El grupo alcanzó su límite de miembros.",
    "IMAGE_TOO_LARGE" to "La imagen es muy pesada o demasiado grande; elige otra o recórtala.",
    "UNSUPPORTED_MEDIA_TYPE" to "Formato no válido. Elige una imagen JPEG, PNG o WebP.",
    "IMAGE_UNAVAILABLE" to "No pudimos abrir esa foto. Vuelve a elegirla desde la galería.",
    "INVALID_OR_EXPIRED_TOKEN" to "El enlace no es válido o ya caducó. Pide uno nuevo.",
    "INVALID_GOOGLE_TOKEN" to "Google no confirmó tu cuenta. Intenta de nuevo o usa otra cuenta.",
    "GOOGLE_AUTH_UNAVAILABLE" to "El acceso con Google no está disponible por ahora. Usa tu correo y contraseña.",
    "NICKNAME_REQUIRED" to "Es tu primera vez: elige un apodo para terminar tu cuenta.",
    "EMAIL_NOT_VERIFIED" to "Verifica tu correo para crear o unirte a un grupo. Abre el enlace que te mandamos " +
        "o toca \"Reenviar correo\".",
    "EMAIL_ALREADY_VERIFIED" to "Tu correo ya está verificado.",
    "INVALID_OR_EXPIRED_TOKEN" to "El código no es válido o ya caducó. Pide uno nuevo.",
    "ALREADY_REPORTED" to "Ya habías reportado esto. El administrador del grupo lo revisará.",
    "BLOCKED_BY_USER" to "No puedes hacer esto con esa persona.",
    "NOT_JUROR" to "No formas parte del jurado de este reto.",
    "ALREADY_VOTED" to "Ya votaste en este reto.",
    "VOTING_CLOSED" to "La votación ya cerró.",
    "VOTING_NOT_FINISHED" to "La votación sigue abierta; aún no hay resultado.",
    "ADMIN_IS_PARTY" to "Como participas en el reto, no puedes resolver la votación.",
    "NOT_COMMITMENT_PARTY" to "Este compromiso no es tuyo.",
    "INVALID_COMMITMENT_STATE" to "El compromiso ya cambió. Actualiza la lista.",
    "NOT_GROUP_ADMIN" to "Solo el administrador del grupo puede hacer esto.",
    "TOURNAMENT_NOT_OPEN" to "El torneo ya no acepta inscripciones.",
    "TOURNAMENT_FULL" to "El torneo ya está lleno.",
    "ALREADY_IN_TOURNAMENT" to "Tu grupo ya está inscrito en este torneo.",
    "NOT_IN_TOURNAMENT" to "Tu grupo no está inscrito en este torneo.",
    "NOT_TOURNAMENT_ORGANIZER" to "Solo el organizador puede hacer esto.",
    "NOT_TOURNAMENT_MEMBER" to "No perteneces a ningún grupo de este torneo.",
    "ORGANIZER_GROUP_CANNOT_LEAVE" to "El grupo organizador no puede salir del torneo.",
    "NOT_ENOUGH_GROUPS" to "Faltan grupos: se necesitan al menos 2 además del organizador.",
    "TOURNAMENT_ALREADY_ACTIVE" to "Tu grupo ya tiene un torneo activo.",
    "TOURNAMENT_NOT_IN_PROGRESS" to "El torneo no está en curso.",
    "INVALID_MATCH_STATE" to "La partida ya cambió. Actualízala antes de continuar.",
    "NOT_MATCH_PLAYER" to "Solo los grupos de esta partida pueden reportar o confirmar.",
    "CANNOT_CONFIRM_OWN_REPORT" to "Tu grupo reportó el resultado: lo confirma o disputa el otro grupo.",
    "ORGANIZER_PLAYS_MATCH" to "El organizador no puede jugar ni reportar partidas.",
    "TIE_NOT_ALLOWED" to "No hay empates: jueguen el desempate y reporten al ganador.",
    "REPORT_TOO_RECENT" to "El rival todavía tiene 24 h para confirmar o disputar.",
    "AI_UNAVAILABLE" to "El dictado inteligente no está disponible. Crea el reto a mano.",
    "AI_PARSE_FAILED" to "No entendimos la frase. Intenta decirla de otra forma o créalo a mano.",
    "RATE_LIMITED" to "Espera un momento antes de volver a intentarlo.",
)
