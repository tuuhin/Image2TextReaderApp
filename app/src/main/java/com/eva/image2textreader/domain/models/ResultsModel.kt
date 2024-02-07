package com.eva.image2textreader.domain.models

import com.eva.image2textreader.util.now
import kotlinx.datetime.LocalDateTime

data class ResultsModel(
	val id: Long? = null,
	val text: String,
	val createdAt: LocalDateTime = LocalDateTime.now(),
	val lastUpdated: LocalDateTime?,
	val imageUri: String? = null,
	val languageCode: String? = null
)