package com.eva.image2textreader.data.ml

import com.eva.image2textreader.domain.ml.ImageToTextRecognizer
import com.eva.image2textreader.domain.models.RecognizedTextModel
import com.eva.image2textreader.util.Resource
import com.google.mlkit.common.MlKitException
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text.TextBlock
import com.google.mlkit.vision.text.TextRecognizer
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class ImageToTextRecognizerImpl(
	private val textRecognizer: TextRecognizer
) : ImageToTextRecognizer {

	override suspend fun recognizeImage(image: InputImage): Resource<RecognizedTextModel> {
		return suspendCancellableCoroutine { cont ->
			textRecognizer.process(image).apply {
				addOnCompleteListener {

					addOnSuccessListener { text ->
						val recognizedText = RecognizedTextModel(
							wholeText = text.text,
							lines = text.textBlocks.map(TextBlock::getText),
							languageCode = text.textBlocks.firstOrNull()?.recognizedLanguage
						)
						val success = Resource.Success(data = recognizedText)
						cont.resume(success)
					}

					addOnFailureListener { exception ->
						val error: Resource<RecognizedTextModel> = Resource.Error(
							message = exception.message ?: ""
						)
						if (exception is MlKitException) cont.cancel(exception)
						else cont.resume(error)
					}

				}

				addOnCanceledListener(cont::cancel)
			}

		}
	}

}