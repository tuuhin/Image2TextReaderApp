package com.eva.image2textreader.presentation.feature_recognizer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.eva.image2textreader.R
import com.eva.image2textreader.domain.models.RecognizedTextModel
import com.eva.image2textreader.presentation.feature_recognizer.composables.ContentWithImagePreview
import com.eva.image2textreader.presentation.feature_recognizer.composables.RecognizedTextMetaData
import com.eva.image2textreader.ui.theme.Image2TextReaderTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecognizerScreen(
	recognizedText: RecognizedTextModel?,
	modifier: Modifier = Modifier,
	navigation: @Composable () -> Unit = {},
	selectedImageUri: String? = null,
) {
	val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

	val scrollState = rememberScrollState()

	Scaffold(
		topBar = {
			TopAppBar(
				title = { Text(text = stringResource(id = R.string.recognize_screen_title)) },
				navigationIcon = navigation,
				scrollBehavior = scrollBehavior,
			)
		}, bottomBar = {
			BottomAppBar(
				actions = {},
				floatingActionButton = {
					FloatingActionButton(
						onClick = {},
						elevation = FloatingActionButtonDefaults
							.bottomAppBarFabElevation(2.dp)
					) {
						Icon(
							painter = painterResource(id = R.drawable.ic_edit_pen),
							contentDescription = null,
						)
					}
				},
			)
		}
	) { scPadding ->
		ContentWithImagePreview(
			imageUri = selectedImageUri,
			modifier = modifier
				.padding(scPadding)
				.fillMaxSize()
		) {
			recognizedText?.let { model ->
				LazyColumn(
					contentPadding = PaddingValues(horizontal = dimensionResource(id = R.dimen.scaffold_padding)),
					verticalArrangement = Arrangement.spacedBy(16.dp),
					modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
				) {
					item {
						RecognizedTextMetaData(
							model = model,
							modifier = Modifier.fillMaxWidth()
						)
					}
					item {
						Text(text = model.wholeText, style = MaterialTheme.typography.bodyMedium)
					}
				}
			}
		}
	}
}

@PreviewLightDark
@Composable
fun RecognizerScreenPreview() = Image2TextReaderTheme {
	RecognizerScreen(
		navigation = {
			Icon(
				imageVector = Icons.AutoMirrored.Filled.ArrowBack,
				contentDescription = null
			)
		},
		recognizedText = null,
	)
}