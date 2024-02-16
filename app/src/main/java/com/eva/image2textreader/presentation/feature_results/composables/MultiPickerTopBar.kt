package com.eva.image2textreader.presentation.feature_results.composables

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTooltipState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.eva.image2textreader.R
import com.eva.image2textreader.presentation.feature_results.util.SortResultsOption
import com.eva.image2textreader.presentation.util.preview.BooleanPreviewParams
import com.eva.image2textreader.ui.theme.Image2TextReaderTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MultiPickerTopBar(
	isItemSelected: Boolean,
	sortOrder: SortResultsOption,
	modifier: Modifier = Modifier,
	selectedCount: Int = 0,
	onClearSelected: () -> Unit,
	onDeleteSelected: () -> Unit,
	onSelectAll: () -> Unit,
	onSortOrderChange: (SortResultsOption) -> Unit,
	scrollBehavior: TopAppBarScrollBehavior? = null,
	selectedTopBarContainerColor: Color = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp),
	scrollableContainerColor: Color = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp),
) {

	val selectedResultsCount by remember(selectedCount) {
		derivedStateOf { "$selectedCount" }
	}

	var showDialog by remember { mutableStateOf(false) }
	var showSortDialog by remember { mutableStateOf(false) }

	DeleteResultsDialog(
		isVisible = showDialog,
		title = stringResource(id = R.string.delete_selected_confirm_title),
		text = stringResource(id = R.string.delete_selected_confirm_text),
		onConfirm = {
			onDeleteSelected()
			showDialog = false
		},
		onDisMiss = { showDialog = false },
	)

	SortResultsDialog(
		isVisible = showSortDialog,
		order = sortOrder,
		onOrderChange = onSortOrderChange,
		onDismiss = { showSortDialog = false },
	)

	AnimatedContent(
		targetState = isItemSelected,
		label = stringResource(id = R.string.animation_label_top_bar),
		modifier = modifier,
		transitionSpec = {

			val enterIn = expandIn(
				animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
				expandFrom = Alignment.TopCenter
			) + slideInVertically(
				animationSpec = tween(durationMillis = 400),
				initialOffsetY = { height -> height },
			)

			val exitOut = shrinkOut(
				animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
				shrinkTowards = Alignment.TopCenter
			) + slideOutVertically(
				animationSpec = tween(durationMillis = 400),
				targetOffsetY = { height -> -height },
			)

			enterIn togetherWith exitOut
		},
		contentAlignment = Alignment.Center,
	) { isMultiSelectTopBar ->
		if (isMultiSelectTopBar) TopAppBar(
			title = {
				Text(
					text = selectedResultsCount,
					style = MaterialTheme.typography.headlineSmall
				)
			},
			navigationIcon = {
				IconButton(
					onClick = onClearSelected,
				) {
					Icon(
						imageVector = Icons.Default.Close,
						contentDescription = stringResource(id = R.string.action_clear_selection)
					)
				}

			},
			actions = {
				TooltipBox(
					positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
					tooltip = {
						PlainTooltip {
							Text(text = stringResource(id = R.string.select_all))
						}
					},
					state = rememberTooltipState()
				) {

					IconButton(
						onClick = onSelectAll,
					) {
						Icon(
							painter = painterResource(id = R.drawable.ic_check_all),
							contentDescription = stringResource(R.string.select_all)
						)
					}
				}

				TooltipBox(
					positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
					tooltip = {
						PlainTooltip {
							Text(text = stringResource(id = R.string.delete_selected))
						}
					},
					state = rememberTooltipState()
				) {
					IconButton(
						onClick = { showDialog = true },
					) {
						Icon(
							painter = painterResource(id = R.drawable.ic_delete),
							contentDescription = stringResource(R.string.delete_icon_desc)
						)
					}
				}

			},
			scrollBehavior = scrollBehavior,
			colors = TopAppBarDefaults
				.topAppBarColors(containerColor = selectedTopBarContainerColor),
		)
		else CenterAlignedTopAppBar(
			title = { Text(text = stringResource(id = R.string.app_name)) },
			scrollBehavior = scrollBehavior,
			colors = TopAppBarDefaults
				.topAppBarColors(scrolledContainerColor = scrollableContainerColor),
			actions = {
				TooltipBox(
					positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
					tooltip = {
						PlainTooltip {
							Text(text = stringResource(R.string.sort_results))
						}
					},
					state = rememberTooltipState()
				) {
					IconButton(
						onClick = { showSortDialog = true },
					) {
						Icon(
							painter = painterResource(id = R.drawable.ic_sort_order),
							contentDescription = stringResource(R.string.sort_results)
						)
					}
				}
			}
		)
	}
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
fun MultiPickerTopBarPreview(
	@PreviewParameter(BooleanPreviewParams::class)
	showSelected: Boolean
) = Image2TextReaderTheme {
	MultiPickerTopBar(
		selectedCount = 10,
		sortOrder = SortResultsOption.TIME_OF_CREATE,
		isItemSelected = showSelected,
		onClearSelected = { },
		onDeleteSelected = { },
		onSelectAll = {},
		onSortOrderChange = {}
	)
}