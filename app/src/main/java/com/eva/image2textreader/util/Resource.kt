package com.eva.image2textreader.util

sealed interface Resource<out T> {

	data class Success<T>(val data: T, val message: String? = null) : Resource<T>

	data object Loading : Resource<Nothing>

	data class Error<T>(val data: T? = null, val message: String = "") : Resource<T>
}
