package com.eva.image2textreader.domain.ml

import com.eva.image2textreader.domain.models.RecognizedTextModel
import com.eva.image2textreader.util.Resource
import com.google.mlkit.vision.common.InputImage

interface ImageToTextRecognizer {

	suspend fun recognizeImage(image: InputImage): Resource<RecognizedTextModel>
}