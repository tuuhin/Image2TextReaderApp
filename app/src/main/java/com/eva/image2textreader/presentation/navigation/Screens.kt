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

		fun navigateToEditScreen(navController: NavController, resultId: Long) {
			navController.navigate("/edit/$resultId")
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

	data object EditScreen : Screens("/edit/{${NavParams.RESULT_ID}}") {

		val navArgs = listOf(
			navArgument(name = NavParams.RESULT_ID) {
				type = NavType.LongType
				nullable = false
			}
		)
	}
}
