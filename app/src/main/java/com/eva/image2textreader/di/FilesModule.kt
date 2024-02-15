package com.eva.image2textreader.di

import com.eva.image2textreader.data.files.InternalImageSaverImpl
import com.eva.image2textreader.data.files.SharedFileSaver
import com.eva.image2textreader.domain.fs.ExternalFileSaver
import com.eva.image2textreader.domain.fs.ImageFileSaver
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val filesModule = module {
	//imageSaver
	singleOf(::InternalImageSaverImpl) bind ImageFileSaver::class
	//fileSaver
	factoryOf(::SharedFileSaver) bind ExternalFileSaver::class
}