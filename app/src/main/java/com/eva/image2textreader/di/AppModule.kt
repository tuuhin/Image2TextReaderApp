package com.eva.image2textreader.di


import com.eva.image2textreader.data.image.ImageFileSaver
import com.eva.image2textreader.data.image.InternalImageSaverImpl
import com.eva.image2textreader.data.mapper.RecognizerResultMapper
import com.eva.image2textreader.data.mapper.ResultsModelEntityMapper
import com.eva.image2textreader.data.repository.RecognizerRepoImpl
import com.eva.image2textreader.data.repository.ResultsHistoryRepoImpl
import com.eva.image2textreader.domain.repository.RecognizerRepo
import com.eva.image2textreader.domain.repository.ResultHistoryRepo
import com.eva.image2textreader.presentation.feature_recognizer.RecognizerViewModel
import com.eva.image2textreader.presentation.feature_results.ResultsViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {

	//imageSaver
	singleOf(::InternalImageSaverImpl) bind ImageFileSaver::class

	//mappers
	factoryOf(::ResultsModelEntityMapper)
	factoryOf(::RecognizerResultMapper)

	// results-history factory
	factoryOf(::ResultsHistoryRepoImpl) bind ResultHistoryRepo::class
	//recognizer factory
	factoryOf(::RecognizerRepoImpl) bind RecognizerRepo::class

	// Results ViewModel
	viewModelOf(::ResultsViewModel)
	// Recognizer ViewModel
	viewModelOf(::RecognizerViewModel)

}