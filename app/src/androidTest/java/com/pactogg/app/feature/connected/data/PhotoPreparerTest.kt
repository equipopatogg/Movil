package com.pactogg.app.feature.connected.data

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.exifinterface.media.ExifInterface
import androidx.activity.result.contract.ActivityResultContracts
import androidx.test.platform.app.InstrumentationRegistry
import com.pactogg.app.core.network.ApiFailure
import java.io.ByteArrayOutputStream
import java.io.File
import org.junit.Assert.*
import org.junit.Test

class PhotoPreparerTest {
    @Test fun jpegPngAndWebpBecomeBoundedJpegWithOriginalDimensions() {
        for (format in listOf(Bitmap.CompressFormat.JPEG, Bitmap.CompressFormat.PNG, Bitmap.CompressFormat.WEBP)) {
            val bitmap = Bitmap.createBitmap(120, 80, Bitmap.Config.ARGB_8888)
            val source = ByteArrayOutputStream().apply { bitmap.compress(format, 90, this) }.toByteArray()
            bitmap.recycle()
            val result = PhotoPreparer.normalize(source)
            assertEquals("image/jpeg", result.mimeType)
            assertEquals(120, result.width); assertEquals(80, result.height)
            assertTrue(result.bytes.size in 1..ApiProductRepository.MaxPhotoBytes)
            val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
            BitmapFactory.decodeByteArray(result.bytes, 0, result.bytes.size, bounds)
            assertEquals("image/jpeg", bounds.outMimeType)
            result.clear(); assertTrue(result.bytes.all { it == 0.toByte() })
        }
    }

    @Test fun largeImageIsSampledAndInvalidOrOverdimensionedImagesAreRejected() {
        fun image(width: Int, height: Int): ByteArray {
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            return try { ByteArrayOutputStream().apply {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, this)
            }.toByteArray() } finally { bitmap.recycle() }
        }
        val result = PhotoPreparer.normalize(image(4000, 2400))
        assertTrue(result.width <= 2048 && result.height <= 2048)
        assertEquals(5f / 3f, result.width.toFloat() / result.height, 0.01f)
        result.clear()
        assertEquals("IMAGE_TOO_LARGE", (runCatching { PhotoPreparer.normalize(image(8001, 1)) }
            .exceptionOrNull() as ApiFailure).code)
        for (bytes in listOf(byteArrayOf(), byteArrayOf(1, 2, 3))) {
            assertEquals("UNSUPPORTED_MEDIA_TYPE", (runCatching { PhotoPreparer.normalize(bytes) }
                .exceptionOrNull() as ApiFailure).code)
        }
    }

    @Test fun exifRotationIsAppliedAndRemovedFromUpload() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val file = File.createTempFile("orientation-", ".jpg", context.cacheDir)
        try {
            val bitmap = Bitmap.createBitmap(120, 80, Bitmap.Config.RGB_565)
            file.outputStream().use { bitmap.compress(Bitmap.CompressFormat.JPEG, 90, it) }; bitmap.recycle()
            ExifInterface(file.path).apply {
                setAttribute(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_ROTATE_90.toString())
                setAttribute(ExifInterface.TAG_MAKE, "private-synthetic-device")
                saveAttributes()
            }
            val result = PhotoPreparer.normalize(file.readBytes())
            assertEquals(80, result.width); assertEquals(120, result.height)
            result.bytes.inputStream().use {
                assertNull(ExifInterface(it).getAttribute(ExifInterface.TAG_MAKE))
            }
            result.clear()
        } finally { file.delete() }
    }

    @Test fun cancellingNativePickerProducesNoSelection() {
        assertNull(ActivityResultContracts.PickVisualMedia().parseResult(Activity.RESULT_CANCELED, null))
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val permissions = context.packageManager.getPackageInfo(context.packageName,
            android.content.pm.PackageManager.GET_PERMISSIONS).requestedPermissions.orEmpty().toSet()
        for (forbidden in listOf("android.permission.CAMERA", "android.permission.READ_MEDIA_VIDEO",
            "android.permission.READ_MEDIA_IMAGES", "android.permission.READ_EXTERNAL_STORAGE")) {
            assertFalse(permissions.contains(forbidden))
        }
    }
}
