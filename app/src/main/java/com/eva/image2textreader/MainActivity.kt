package com.eva.image2textreader

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.eva.image2textreader.presentation.navigation.NavigationGraph
import com.eva.image2textreader.presentation.util.compLocal.LocalSnackBarProvider
import com.eva.image2textreader.ui.theme.Image2TextReaderTheme

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		// splash screen
		installSplashScreen()
		// fill whole screen
		WindowCompat.setDecorFitsSystemWindows(window, false)

		setContent {

			val snackBarHost = remember { SnackbarHostState() }

			Image2TextReaderTheme {
				CompositionLocalProvider(
					LocalSnackBarProvider provides snackBarHost
				) {
					NavigationGraph()
				}
			}
		}
	}
}

