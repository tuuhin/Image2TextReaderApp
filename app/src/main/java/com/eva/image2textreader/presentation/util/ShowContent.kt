package com.eva.image2textreader.presentation.util


data class ShowContent<T>(
	val isLoading: Boolean = true,
	val content: T
)
