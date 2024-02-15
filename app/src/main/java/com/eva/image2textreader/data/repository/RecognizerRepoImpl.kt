package com.eva.image2textreader.data.repository

import android.content.Context
import androidx.core.net.toUri
import com.eva.image2textreader.domain.ml.ImageToTextRecognizer
import com.eva.image2textreader.domain.models.RecognizedTextModel
import com.eva.image2textreader.domain.repository.RecognizerRepo
import com.eva.image2textreader.util.Resource
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RecognizerRepoImpl(
	private val context: Context,
	private val textRecognizer: ImageToTextRecognizer
) : RecognizerRepo {

	override fun recognizeTextFromImageUri(uri: String): Flow<Resource<RecognizedTextModel>> {

		val contentUri = uri.toUri()
		val inputImage = InputImage.fromFilePath(context, contentUri)

		return flow {
			//emit a loading state
			emit(Resource.Loading)
			// recognizer takes certain time and resources to recognize the underlying text
			val results = textRecognizer.recognizeImage(inputImage)
			//emit the results on completion
			emit(results)
		}.flowOn(Dispatchers.Default)
	}
}