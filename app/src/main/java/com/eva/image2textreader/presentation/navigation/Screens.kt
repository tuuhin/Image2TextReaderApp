package com.eva.image2textreader.presentation.navigation

import android.net.Uri
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import java.net.URLEncoder

sealed class Screens(val route: String) {
	data object ResultsScreen : Screens("/results") {

		fun navigateToRecognizeScreen(navController: NavController, uri: Uri) {
			val encodedUri = URLEncoder.encode("$uri", Charsets.UTF_8.name())
			navController.navigate("/recognize/$encodedUri")
		}
	}

	data object RecognizeScreen : Screens("/recognize/{${NavParams.URI_PATH_PARAM}}") {

		val navArgs = listOf(
			navArgument(name = NavParams.URI_PATH_PARAM) {
				type = NavType.StringType
				nullable = false
			},
		)

	}
}
