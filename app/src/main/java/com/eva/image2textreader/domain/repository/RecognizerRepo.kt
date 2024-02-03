package com.eva.image2textreader.domain.repository

import com.eva.image2textreader.domain.models.RecognizedTextModel
import com.eva.image2textreader.util.Resource
import kotlinx.coroutines.flow.Flow

interface RecognizerRepo {

	fun recognizeTextFromImageUri(uri: String): Flow<Resource<RecognizedTextModel>>
}