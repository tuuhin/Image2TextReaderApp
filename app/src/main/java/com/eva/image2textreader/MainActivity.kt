package com.eva.image2textreader

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.eva.image2textreader.ui.theme.Image2TextReaderTheme

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			Image2TextReaderTheme {

			}
		}
	}
}

