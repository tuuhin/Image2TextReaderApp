package com.eva.image2textreader.domain.models

import java.time.LocalDateTime

data class ResultsModel(
	val id: Int,
	val text: String,
	val lastUpdated: LocalDateTime = LocalDateTime.now(),
	val imageUri: String? = null,
	val languageCode: String? = null
)