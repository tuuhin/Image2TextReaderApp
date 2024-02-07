package com.eva.image2textreader.data.image

interface ImageFileSaver {

	suspend fun saveFileFromContentUri(uriString: String, quality: Int = 80): String?

	suspend fun deleteFile(uriString: String): Boolean

	suspend fun deleteFiles(uriStrings: List<String>): Boolean
}