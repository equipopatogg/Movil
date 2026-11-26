package com.pactogg.app.feature.assist.data

import android.graphics.BitmapFactory
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.pactogg.app.feature.assist.domain.ScoreParser
import kotlin.coroutines.resume
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext

/**
 * PL-14 (ML): reconocimiento de texto de ML Kit con el modelo incluido en la app.
 * Corre en el teléfono: la foto no sale del dispositivo para leer el marcador.
 */
class MlKitScoreReader {
    suspend fun read(jpeg: ByteArray): Pair<Int, Int>? = withContext(Dispatchers.Default) {
        val copy = jpeg.copyOf()
        val bitmap = BitmapFactory.decodeByteArray(copy, 0, copy.size)
        copy.fill(0)
        if (bitmap == null) return@withContext null
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        try {
            val text = suspendCancellableCoroutine { continuation ->
                recognizer.process(InputImage.fromBitmap(bitmap, 0))
                    .addOnSuccessListener { continuation.resume(it.text) }
                    .addOnFailureListener { continuation.resume("") }
            }
            ScoreParser.parse(text)
        } finally {
            recognizer.close()
            bitmap.recycle()
        }
    }
}
