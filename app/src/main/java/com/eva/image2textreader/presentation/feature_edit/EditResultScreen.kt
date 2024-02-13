package com.eva.image2textreader.presentation.feature_edit

import android.content.Context
import android.net.Uri
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
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.imageLoader
import com.eva.image2textreader.R
import com.eva.image2textreader.domain.models.ResultsModel
import com.eva.image2textreader.presentation.feature_edit.composables.EditConfirmDialog
import com.eva.image2textreader.presentation.feature_edit.composables.ImagePreviewContainer
import com.eva.image2textreader.presentation.util.ShowContent
import com.eva.image2textreader.presentation.util.contracts.ImagePreviewFromUriContracts
import com.eva.image2textreader.presentation.util.contracts.ShareResultsActivityContracts
import com.eva.image2textreader.presentation.util.preview.ResultsShowContentSinglePreviewParams
import com.eva.image2textreader.ui.theme.Image2TextReaderTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditResultScreen(
	results: ShowContent<ResultsModel?>,
	onTextChange: (String) -> Unit,
	onEditComplete: () -> Unit,
	modifier: Modifier = Modifier,
	navigation: @Composable () -> Unit = {},
	context: Context = LocalContext.current,
) {

	var isDialogVisible by remember { mutableStateOf(false) }

	EditConfirmDialog(
		isVisible = isDialogVisible,
		onConfirm = onEditComplete,
		onDismiss = { isDialogVisible = false },
	)

	val shareResultsLauncher = rememberLauncherForActivityResult(
		contract = ShareResultsActivityContracts(),
		onResult = {},
	)

	val imagePreviewLauncher = rememberLauncherForActivityResult(
		contract = ImagePreviewFromUriContracts(),
		onResult = {},
	)

	Scaffold(
		topBar = {
			TopAppBar(
				title = { Text(text = stringResource(id = R.string.edit_screen)) },
				actions = {
					TooltipBox(
						positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
						tooltip = {
							PlainTooltip(caretProperties = TooltipDefaults.caretProperties) {
								Text(text = stringResource(id = R.string.share_icon_text))
							}
						},
						state = rememberTooltipState()
					) {
						IconButton(
							onClick = { results.content?.let(shareResultsLauncher::launch) },
						) {
							Icon(
								painter = painterResource(id = R.drawable.ic_share),
								contentDescription = stringResource(id = R.string.share_icon_desc)
							)
						}
					}
				},
				navigationIcon = navigation,
			)
		},
		floatingActionButton = {
			FloatingActionButton(onClick = { isDialogVisible = true }) {
				Icon(
					painter = painterResource(id = R.drawable.ic_check_all),
					contentDescription = null
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
							val uri = Uri.parse(results.content.imageUri)
							imagePreviewLauncher.launch(uri)
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


@PreviewLightDark
@Composable
fun EditResultsScreenPreview(
	@PreviewParameter(ResultsShowContentSinglePreviewParams::class)
	results: ShowContent<ResultsModel?>,
) = Image2TextReaderTheme {
	EditResultScreen(
		results = results,
		navigation = {
			Icon(
				imageVector = Icons.AutoMirrored.Filled.ArrowBack,
				contentDescription = null
			)
		},
		onTextChange = {},
		onEditComplete = {}
	)
}