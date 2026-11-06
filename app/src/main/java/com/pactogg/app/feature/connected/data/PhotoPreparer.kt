package com.pactogg.app.feature.connected.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import androidx.exifinterface.media.ExifInterface
import com.pactogg.app.core.network.ApiFailure
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/** Ephemeral JPEG; never saved to disk, instance state, logs or the repository. */
class PreparedPhoto(val bytes: ByteArray, val width: Int, val height: Int) {
    val mimeType: String = "image/jpeg"
    fun clear() = bytes.fill(0)
}

class PhotoPreparer(private val context: Context) {
    suspend fun prepare(uri: Uri): PreparedPhoto = withContext(Dispatchers.IO) {
        try {
            val mime = context.contentResolver.getType(uri)
            if (mime !in ApiProductRepository.AllowedPhotoTypes) throw ApiFailure(422, "UNSUPPORTED_MEDIA_TYPE")
            val source = context.contentResolver.openInputStream(uri)?.use { input ->
                val out = ByteArrayOutputStream()
                val buffer = ByteArray(8192)
                var count = input.read(buffer)
                while (count != -1) {
                    if (out.size() + count > MaxSourceBytes) throw ApiFailure(422, "IMAGE_TOO_LARGE")
                    out.write(buffer, 0, count)
                    count = input.read(buffer)
                }
                out.toByteArray()
            } ?: throw ApiFailure(422, "IMAGE_UNAVAILABLE")
            try { normalize(source) } finally { source.fill(0) }
        } catch (_: IOException) {
            throw ApiFailure(422, "IMAGE_UNAVAILABLE")
        } catch (_: SecurityException) {
            throw ApiFailure(422, "IMAGE_UNAVAILABLE")
        }
    }

    companion object {
        private const val MaxSourceBytes = 32 * 1024 * 1024
        private const val MaxDimension = 8000
        private const val UploadDimension = 2048

        internal fun normalize(source: ByteArray): PreparedPhoto {
            val bounds = bounds(source)
            var sample = 1
            while (maxOf(bounds.outWidth, bounds.outHeight) / sample > UploadDimension) sample *= 2
            val bitmap = BitmapFactory.decodeByteArray(source, 0, source.size,
                BitmapFactory.Options().apply { inSampleSize = sample })
                ?: throw ApiFailure(422, "UNSUPPORTED_MEDIA_TYPE")
            val oriented = orient(bitmap, source)
            try { return encode(oriented) } finally {
                if (oriented !== bitmap) oriented.recycle()
                bitmap.recycle()
            }
        }

        private fun bounds(source: ByteArray): BitmapFactory.Options {
            val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
            BitmapFactory.decodeByteArray(source, 0, source.size, bounds)
            if (bounds.outWidth <= 0 || bounds.outHeight <= 0 ||
                bounds.outMimeType !in ApiProductRepository.AllowedPhotoTypes) {
                throw ApiFailure(422, "UNSUPPORTED_MEDIA_TYPE")
            }
            if (bounds.outWidth > MaxDimension || bounds.outHeight > MaxDimension) {
                throw ApiFailure(422, "IMAGE_TOO_LARGE")
            }
            return bounds
        }

        private fun encode(bitmap: Bitmap): PreparedPhoto {
            val output = ByteArrayOutputStream()
            if (!bitmap.compress(Bitmap.CompressFormat.JPEG, 90, output)) {
                throw ApiFailure(422, "UNSUPPORTED_MEDIA_TYPE")
            }
            val bytes = output.toByteArray()
            if (bytes.size !in 1..ApiProductRepository.MaxPhotoBytes) throw ApiFailure(422, "IMAGE_TOO_LARGE")
            return PreparedPhoto(bytes, bitmap.width, bitmap.height)
        }

        private fun orient(bitmap: Bitmap, source: ByteArray): Bitmap {
            val orientation = runCatching {
                ByteArrayInputStream(source).use { ExifInterface(it)
                    .getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL) }
            }.getOrDefault(ExifInterface.ORIENTATION_NORMAL)
            val matrix = Matrix().apply {
                when (orientation) {
                    2 -> setScale(-1f, 1f)
                    3 -> setRotate(180f)
                    4 -> setScale(1f, -1f)
                    5 -> { setRotate(90f); postScale(-1f, 1f) }
                    6 -> setRotate(90f)
                    7 -> { setRotate(-90f); postScale(-1f, 1f) }
                    8 -> setRotate(-90f)
                }
            }
            return if (matrix.isIdentity) bitmap
            else Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        }
    }
}
