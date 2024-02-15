package com.eva.image2textreader.data.files

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import com.eva.image2textreader.R
import com.eva.image2textreader.domain.fs.ExternalFileSaver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.nio.charset.Charset

class SharedFileSaver(
	private val context: Context
) : ExternalFileSaver {
	override suspend fun saveFile(fileName: String, content: String): String {
		val bytesToWrite = content.toByteArray(Charset.defaultCharset())
		val result = withContext(Dispatchers.IO) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
				saveFileOnSharedStorage(name = fileName, contentBytes = bytesToWrite)
			} else {
				saveFileToPublicStorage(name = fileName, contentBytes = bytesToWrite)
			}
		}
		return result.toString()
	}

	private fun saveFileToPublicStorage(name: String, contentBytes: ByteArray): Uri {
		val documentsPublicDir =
			Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)

		val outFile = File(documentsPublicDir, name)
		FileOutputStream(outFile).use { stream ->
			stream.write(contentBytes)
		}
		return outFile.toUri()
	}

	@RequiresApi(Build.VERSION_CODES.Q)
	private fun saveFileOnSharedStorage(name: String, contentBytes: ByteArray): Uri? {
		val appName = context.getString(R.string.app_name)
		val relativePath = Environment.DIRECTORY_DOCUMENTS + File.separator + appName

		val contentValues = ContentValues().apply {
			put(MediaStore.Files.FileColumns.RELATIVE_PATH, relativePath)
			put(MediaStore.Files.FileColumns.TITLE, name)
			put(MediaStore.Files.FileColumns.DISPLAY_NAME, name)
			put(MediaStore.Files.FileColumns.MIME_TYPE, "text/plain")
			put(MediaStore.Files.FileColumns.DATE_ADDED, System.currentTimeMillis())
			put(MediaStore.Files.FileColumns.DATE_TAKEN, System.currentTimeMillis())
			put(MediaStore.Files.FileColumns.IS_PENDING, 1)
		}

		val collection = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL)

		val contentResolver = context.contentResolver

		val newFileUri = contentResolver.insert(collection, contentValues) ?: return null

		//After insert create a file uri
		return try {
			contentResolver.openOutputStream(newFileUri, "w")
				?.use { stream ->
					// write the content to the file
					stream.write(contentBytes)

					val updatedValues = ContentValues().apply {
						put(MediaStore.Files.FileColumns.IS_PENDING, 0)
						put(MediaStore.Files.FileColumns.DATE_MODIFIED, System.currentTimeMillis())
					}
					// update the file content
					contentResolver.update(newFileUri, updatedValues, null, null)
				}
			newFileUri
		} catch (e: Exception) {
			e.printStackTrace()
			//delete the uri if any problem occurs
			contentResolver.delete(newFileUri, null, null)
			null
		}
	}
}