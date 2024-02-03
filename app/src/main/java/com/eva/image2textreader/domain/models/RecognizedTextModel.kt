package com.eva.image2textreader.domain.models

data class RecognizedTextModel(
	val wholeText: String,
	val languageCode: String? = null,
	val lines: List<String>
)
