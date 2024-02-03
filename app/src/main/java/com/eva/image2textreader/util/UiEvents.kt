package com.eva.image2textreader.util

sealed interface UiEvents {
	data class ShowSnackBar(val text: String, val action: (() -> Unit)? = null) : UiEvents

	data class ShowToast(val message: String) : UiEvents
}