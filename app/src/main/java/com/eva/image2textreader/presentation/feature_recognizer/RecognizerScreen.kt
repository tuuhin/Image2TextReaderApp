package com.eva.image2textreader.presentation.feature_recognizer

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.eva.image2textreader.R
import com.eva.image2textreader.domain.models.RecognizedTextModel
import com.eva.image2textreader.presentation.feature_recognizer.composables.ContentWithImagePreview
import com.eva.image2textreader.presentation.feature_recognizer.composables.LoadingContentDialog
import com.eva.image2textreader.presentation.feature_recognizer.composables.RecognizedTextMetaData
import com.eva.image2textreader.presentation.util.compLocal.LocalSnackBarProvider
import com.eva.image2textreader.presentation.util.preview.BooleanPreviewParams
import com.eva.image2textreader.ui.theme.Image2TextReaderTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecognizerScreen(
	imageUri: String?,
	model: RecognizedTextModel?,
	onBack: () -> Unit,
	modifier: Modifier = Modifier,
	isContentReady: Boolean = true,
	navigation: @Composable () -> Unit = {},
	snackBarHostState: SnackbarHostState = LocalSnackBarProvider.current
) {

	val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

	BackHandler(
		enabled = !isContentReady,
		onBack = onBack,
	)

	LoadingContentDialog(isVisible = !isContentReady)

	Scaffold(
		topBar = {
			TopAppBar(
				title = { Text(text = stringResource(id = R.string.recognize_screen_title)) },
				navigationIcon = navigation,
				scrollBehavior = scrollBehavior,
			)
		},
		snackbarHost = {
			SnackbarHost(hostState = snackBarHostState) { snackBarData ->
				Snackbar(
					snackbarData = snackBarData,
					shape = MaterialTheme.shapes.small,
					contentColor = MaterialTheme.colorScheme.inverseOnSurface,
					containerColor = MaterialTheme.colorScheme.inverseSurface,
				)
			}
		},
		modifier = modifier,
		contentWindowInsets = WindowInsets.systemBars
	) { scPadding ->

		ContentWithImagePreview(
			imageUri = imageUri,
			modifier = modifier
				.padding(scPadding)
				.fillMaxSize(),
		) {
			model?.let { model ->
				LazyColumn(
					contentPadding = PaddingValues(dimensionResource(id = R.dimen.scaffold_padding)),
					verticalArrangement = Arrangement.spacedBy(16.dp),
					modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
				) {
					item {
						RecognizedTextMetaData(
							model = model,
							modifier = Modifier.fillMaxWidth()
						)
					}
					itemsIndexed(items = model.textBlocksText) { _, line ->
						Text(
							text = line,
							style = MaterialTheme.typography.bodyMedium
						)
					}
				}
			}
		}
	}
}

@Preview(
	apiLevel = 33,
	uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Preview(
	apiLevel = 33,
	uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun RecognizerScreenPreview(
	@PreviewParameter(BooleanPreviewParams::class)
	isContentReady: Boolean
) = Image2TextReaderTheme {
	RecognizerScreen(
		imageUri = null,
		model = null,
		isContentReady = isContentReady,
		navigation = {
			Icon(
				imageVector = Icons.AutoMirrored.Filled.ArrowBack,
				contentDescription = null
			)
		},
		onBack = {},
		snackBarHostState = SnackbarHostState()
	)
}