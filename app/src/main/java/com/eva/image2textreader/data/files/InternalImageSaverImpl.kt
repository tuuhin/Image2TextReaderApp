package com.eva.image2textreader.data.files

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.eva.image2textreader.domain.fs.ImageFileSaver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileFilter
import java.io.FileOutputStream
import kotlin.random.Random

private const val FILE_CACHE_LOGGER = "FILE_LOGGER"

class InternalImageSaverImpl(
	private val context: Context
) : ImageFileSaver {

	private fun File.toContentUri(): Uri = FileProvider
		.getUriForFile(context, "${context.packageName}.provider", this)

	private val directory: File
		get() = File(context.filesDir, "local_images").apply {
			if (!exists()) mkdirs()
		}

	private val randomId: Int
		get() = Random(100).nextInt()

	override suspend fun saveFileFromContentUri(uriString: String, quality: Int): String? {

		val uri = uriString.toUri()
		// Takes the last segment mostly ends with digits convert it to int otherwise a randomId
		val uriId = uri.lastPathSegment?.takeLastWhile(Char::isDigit)
			?.toIntOrNull() ?: randomId

		val fileName = "file-$uriId.png"

		val fileFilter = FileFilter { file ->
			file.exists() && file.canRead() && file.path.endsWith("$uriId.png")
		}

		return withContext(Dispatchers.IO) {

			directory.listFiles(fileFilter)
				?.firstOrNull()
				?.let { file ->
					if (!file.exists()) return@let
					Log.d(FILE_CACHE_LOGGER, "FILE_EXITS NAME ${file.name}")
					return@withContext file.toContentUri().toString()
				}

			val file = File(directory, fileName)

			try {
				context.contentResolver.openInputStream(uri)
					?.use { stream ->
						val bitmap = BitmapFactory.decodeStream(stream)
						FileOutputStream(file).use { outputStream ->
							Log.d(FILE_CACHE_LOGGER, "FILE_WRITTEN")
							bitmap.compress(Bitmap.CompressFormat.PNG, quality, outputStream)
						}
					}
				file.toContentUri().toString()
			} catch (e: Exception) {
				e.printStackTrace()
				null
			}
		}
	}

	override suspend fun deleteFile(uriString: String): Boolean {
		return withContext(Dispatchers.IO) {

			try {
				val contentUri = uriString.toUri()

				val fileName = contentUri.lastPathSegment
				val filter = FileFilter { file -> file.name == fileName }

				directory.listFiles(filter)
					?.firstOrNull()
					?.let { file ->
						if (!file.exists()) return@let
						val operation = file.delete()
						Log.d(FILE_CACHE_LOGGER, "DELETE_FILE ${file.name} RESULT : $operation")
						return@withContext operation
					}
				false
			} catch (e: Exception) {
				e.printStackTrace()
				return@withContext false
			}
		}
	}

	override suspend fun deleteFiles(uriStrings: Collection<String>): Boolean {
		return withContext(Dispatchers.IO) {
			try {
				val lastSegments = uriStrings.map(String::toUri)
					.mapNotNull(Uri::getLastPathSegment)

				val filter = FileFilter { file -> file.name in lastSegments }

				val deferredOps = directory.listFiles(filter)
					?.mapNotNull { file ->
						if (!file.exists()) return@mapNotNull null
						return@mapNotNull async {
							val res = file.delete()
							Log.d(FILE_CACHE_LOGGER, "DELETED FILE ${file.name} RESULT: $res")
							res
						}
					} ?: return@withContext false

				return@withContext deferredOps.awaitAll().all { it }
			} catch (e: Exception) {
				e.printStackTrace()
				return@withContext false
			}
		}
	}
}