package com.eva.image2textreader.di

import com.eva.image2textreader.data.ml.ImageToTextRecognizerImpl
import com.eva.image2textreader.domain.ml.ImageToTextRecognizer
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val mlModule = module {

	single<TextRecognizer> {
		TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
	}

	singleOf(::ImageToTextRecognizerImpl) bind ImageToTextRecognizer::class
}