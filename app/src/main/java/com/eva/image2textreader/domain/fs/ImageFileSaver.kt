package com.eva.image2textreader.domain.fs

interface ImageFileSaver {

	suspend fun saveFileFromContentUri(uriString: String, quality: Int = 80): String?

	suspend fun deleteFile(uriString: String): Boolean

	suspend fun deleteFiles(uriStrings: Collection<String>): Boolean
}