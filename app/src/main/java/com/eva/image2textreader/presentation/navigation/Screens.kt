package com.eva.image2textreader.presentation.navigation

sealed class Screens(val route: String) {
	data object Resents : Screens("/resents")
	data object Recognize : Screens("/recognize")
}
