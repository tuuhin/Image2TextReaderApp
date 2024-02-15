package com.eva.image2textreader.domain.fs


interface ExternalFileSaver {

	suspend fun saveFile(fileName: String, content: String): String?
}