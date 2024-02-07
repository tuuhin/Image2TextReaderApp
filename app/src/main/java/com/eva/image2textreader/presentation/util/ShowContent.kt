package com.eva.image2textreader.presentation.util


data class ShowContent<T>(
	val isLoading: Boolean = false,
	val content: T
)
