package com.pactogg.app.feature.play

import com.pactogg.app.feature.play.domain.PlayRepository
import java.lang.reflect.Proxy

/** Repositorio de jurado/avisos/torneos para pruebas que no lo usan: cualquier llamada falla. */
val UnusedPlayRepository: PlayRepository = Proxy.newProxyInstance(
    PlayRepository::class.java.classLoader, arrayOf(PlayRepository::class.java),
) { _, method, _ -> throw IllegalStateException("No se esperaba ${method.name} en esta prueba") } as PlayRepository
