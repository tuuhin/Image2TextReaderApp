package com.eva.image2textreader.data.repository

import android.content.Context
import androidx.core.net.toUri
import com.eva.image2textreader.domain.ml.ImageToTextRecognizer
import com.eva.image2textreader.domain.models.RecognizedTextModel
import com.eva.image2textreader.domain.repository.RecognizerRepo
import com.eva.image2textreader.util.Resource
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RecognizerRepoImpl(
	private val context: Context,
	private val imageToTextRecognizer: ImageToTextRecognizer
) : RecognizerRepo {
	override fun recognizeTextFromImageUri(uri: String): Flow<Resource<RecognizedTextModel>> {

		val contentUri = uri.toUri()
		val inputImage = InputImage.fromFilePath(context, contentUri)

		return flow {
			emit(Resource.Loading)
			val results = imageToTextRecognizer.recognizeImage(inputImage)
			emit(results)
		}
	}
}