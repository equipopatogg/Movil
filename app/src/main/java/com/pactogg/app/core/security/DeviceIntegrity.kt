package com.pactogg.app.core.security

import android.os.Build
import java.io.File

/**
 * MASVS-RESILIENCE (detección básica): señales comunes de un teléfono con root.
 * Solo avisa a la persona en "Privacidad y seguridad"; no bloquea la app porque PactoGG no mueve dinero y las
 * reglas (puntos, votos, permisos) se validan en el servidor. Se puede saltar en un equipo modificado: es un aviso,
 * no un control de confianza.
 */
object DeviceIntegrity {
    val SuPaths = listOf(
        "/system/bin/su", "/system/xbin/su", "/sbin/su", "/su/bin/su", "/data/local/su", "/data/local/bin/su",
        "/data/local/xbin/su", "/system/sd/xbin/su", "/system/app/Superuser.apk", "/data/adb/magisk",
    )

    fun looksRooted(buildTags: String?, exists: (String) -> Boolean): Boolean =
        buildTags.orEmpty().contains("test-keys") || SuPaths.any { runCatching { exists(it) }.getOrDefault(false) }

    fun current(): Boolean = looksRooted(Build.TAGS) { File(it).exists() }
}
