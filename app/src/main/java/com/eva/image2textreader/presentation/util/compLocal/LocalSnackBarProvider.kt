package com.eva.image2textreader.presentation.util.compLocal

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf


class CompositionLocalNotFoundException : Exception("Composition Local Not Found")

val LocalSnackBarProvider: ProvidableCompositionLocal<SnackbarHostState> = compositionLocalOf {
	throw CompositionLocalNotFoundException()
}