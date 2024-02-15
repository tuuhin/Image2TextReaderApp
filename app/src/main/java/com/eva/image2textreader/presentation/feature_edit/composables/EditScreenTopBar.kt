package com.eva.image2textreader.presentation.feature_edit.composables

import android.content.res.Configuration
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.eva.image2textreader.R
import com.eva.image2textreader.presentation.util.preview.BooleanPreviewParams
import com.eva.image2textreader.ui.theme.Image2TextReaderTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScreenTopBar(
	isExportEnabled: Boolean,
	onShare: () -> Unit,
	onExport: () -> Unit,
	modifier: Modifier = Modifier,
	navigation: @Composable () -> Unit = {},
	colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(),
	windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
	scrollBehavior: TopAppBarScrollBehavior? = null,
) {
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
					onClick = onShare,
				) {
					Icon(
						painter = painterResource(id = R.drawable.ic_share),
						contentDescription = stringResource(id = R.string.share_icon_desc)
					)
				}
			}
			TooltipBox(
				positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
				tooltip = {
					PlainTooltip(caretProperties = TooltipDefaults.caretProperties) {
						Text(text = stringResource(id = R.string.download_icon_text))
					}
				},
				state = rememberTooltipState()
			) {
				IconButton(
					onClick = onExport,
					enabled = isExportEnabled
				) {
					Icon(
						painter = painterResource(id = R.drawable.ic_export),
						contentDescription = null
					)
				}
			}
		},
		navigationIcon = navigation,
		windowInsets = windowInsets,
		colors = colors,
		scrollBehavior = scrollBehavior,
		modifier = modifier,
	)
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(
	apiLevel = 33,
	uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Preview(
	apiLevel = 33,
	uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun EditScreenTopBarPreview(
	@PreviewParameter(BooleanPreviewParams::class)
	isExportEnabled: Boolean
) = Image2TextReaderTheme {
	EditScreenTopBar(
		isExportEnabled = isExportEnabled, onShare = {}, onExport = { },
		navigation = {
			Icon(
				imageVector = Icons.AutoMirrored.Filled.ArrowBack,
				contentDescription = null
			)
		},
	)
}