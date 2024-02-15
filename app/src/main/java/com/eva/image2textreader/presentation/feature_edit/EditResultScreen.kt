package com.eva.image2textreader.presentation.feature_edit

import android.content.Context
import android.content.res.Configuration
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import coil.imageLoader
import com.eva.image2textreader.R
import com.eva.image2textreader.domain.models.ResultsModel
import com.eva.image2textreader.presentation.feature_edit.composables.EditConfirmDialog
import com.eva.image2textreader.presentation.feature_edit.composables.EditScreenTopBar
import com.eva.image2textreader.presentation.feature_edit.composables.ExportFileDialog
import com.eva.image2textreader.presentation.feature_edit.composables.ImagePreviewContainer
import com.eva.image2textreader.presentation.feature_edit.utils.FileSaverState
import com.eva.image2textreader.presentation.feature_edit.utils.SavedFilePreviewContracts
import com.eva.image2textreader.presentation.util.ShowContent
import com.eva.image2textreader.presentation.util.compLocal.LocalSnackBarProvider
import com.eva.image2textreader.presentation.util.contracts.ImagePreviewFromUriContracts
import com.eva.image2textreader.presentation.util.contracts.ShareResultsActivityContracts
import com.eva.image2textreader.presentation.util.preview.ResultsShowContentSinglePreviewParams
import com.eva.image2textreader.ui.theme.Image2TextReaderTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditResultScreen(
	results: ShowContent<ResultsModel?>,
	onTextChange: (String) -> Unit,
	fileSaverState: FileSaverState,
	onEditComplete: () -> Unit,
	onExport: () -> Unit,
	modifier: Modifier = Modifier,
	navigation: @Composable () -> Unit = {},
	context: Context = LocalContext.current,
	snackBarHostState: SnackbarHostState = LocalSnackBarProvider.current,
) {

	var isEditDialogVisible by remember { mutableStateOf(false) }
	var isExportDialogVisible by remember { mutableStateOf(false) }

	val isFilePrepared by remember(fileSaverState.isPreparing) {
		derivedStateOf {
			!fileSaverState.isPreparing
		}
	}

	EditConfirmDialog(
		isVisible = isEditDialogVisible,
		onConfirm = {
			onEditComplete()
			isEditDialogVisible = false
		},
		onDismiss = { isEditDialogVisible = false },
	)

	ExportFileDialog(
		isVisible = isExportDialogVisible,
		onDismiss = {
			isExportDialogVisible = false
		},
		onConfirm = {
			onExport()
			isExportDialogVisible = false
		},
	)

	val shareResultsLauncher = rememberLauncherForActivityResult(
		contract = ShareResultsActivityContracts(),
		onResult = {},
	)

	val imagePreviewLauncher = rememberLauncherForActivityResult(
		contract = ImagePreviewFromUriContracts(),
		onResult = {},
	)

	val exportedFilePreviewLauncher = rememberLauncherForActivityResult(
		contract = SavedFilePreviewContracts(),
		onResult = {}
	)

	LaunchedEffect(key1 = fileSaverState.fileUri) {
		if (fileSaverState.isPreparing || fileSaverState.fileUri == null)
			return@LaunchedEffect
		exportedFilePreviewLauncher.launch(fileSaverState)
	}

	Scaffold(
		topBar = {
			EditScreenTopBar(
				isExportEnabled = isFilePrepared,
				onShare = { results.content?.let(shareResultsLauncher::launch) },
				onExport = { isExportDialogVisible = true },
				navigation = navigation,
			)

		},
		floatingActionButton = {
			FloatingActionButton(onClick = { isEditDialogVisible = true }) {
				Icon(
					painter = painterResource(id = R.drawable.ic_check_all),
					contentDescription = null
				)
			}
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
		Crossfade(
			targetState = results.isLoading,
			label = "Loading Transition",
			animationSpec = tween(durationMillis = 400, easing = LinearEasing),
			modifier = modifier
				.padding(scPadding)
				.padding(horizontal = dimensionResource(id = R.dimen.scaffold_padding))
				.fillMaxSize()
		) { isLoading ->
			when {
				isLoading || results.content == null -> Box(
					modifier = Modifier.fillMaxSize(),
					contentAlignment = Alignment.Center
				) {
					CircularProgressIndicator(
						color = MaterialTheme.colorScheme.secondary,
						trackColor = MaterialTheme.colorScheme.surfaceVariant,
					)
				}

				else -> Column(
					modifier = Modifier.fillMaxSize(),
					verticalArrangement = Arrangement.spacedBy(16.dp)
				) {
					ImagePreviewContainer(
						onPreviewClick = {
							results.content.imageUri?.toUri()
								?.let(imagePreviewLauncher::launch)
						},
						modifier = Modifier.aspectRatio(1.5f)
					) {
						AsyncImage(
							model = results.content.imageUri,
							contentDescription = null,
							imageLoader = context.imageLoader,
							contentScale = ContentScale.Crop,
							filterQuality = FilterQuality.Medium,
							modifier = Modifier
								.align(Alignment.Center)
								.alpha(.9f)
						)
					}
					OutlinedTextField(
						value = results.content.text,
						onValueChange = onTextChange,
						singleLine = false,
						maxLines = Int.MAX_VALUE,
						modifier = Modifier.fillMaxSize(),
						shape = MaterialTheme.shapes.large,
						textStyle = MaterialTheme.typography.bodyLarge,
						colors = OutlinedTextFieldDefaults
							.colors(focusedBorderColor = MaterialTheme.colorScheme.secondary)
					)
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
fun EditResultsScreenPreview(
	@PreviewParameter(ResultsShowContentSinglePreviewParams::class)
	results: ShowContent<ResultsModel?>,
) = Image2TextReaderTheme {
	EditResultScreen(
		results = results,
		fileSaverState = FileSaverState(),
		navigation = {
			Icon(
				imageVector = Icons.AutoMirrored.Filled.ArrowBack,
				contentDescription = null
			)
		},
		onTextChange = {},
		onEditComplete = {},
		onExport = {},
		snackBarHostState = SnackbarHostState()
	)
}