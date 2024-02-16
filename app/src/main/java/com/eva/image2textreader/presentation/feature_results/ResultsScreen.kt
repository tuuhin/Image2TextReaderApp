package com.eva.image2textreader.presentation.feature_results

import android.content.res.Configuration
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.areNavigationBarsVisible
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.eva.image2textreader.R
import com.eva.image2textreader.domain.models.ResultsModel
import com.eva.image2textreader.presentation.feature_results.composables.MultiPickerTopBar
import com.eva.image2textreader.presentation.feature_results.composables.ResultsContent
import com.eva.image2textreader.presentation.feature_results.composables.SavedResultsCard
import com.eva.image2textreader.presentation.feature_results.util.ResultsState
import com.eva.image2textreader.presentation.feature_results.util.SortResultsOption
import com.eva.image2textreader.presentation.util.ShowContent
import com.eva.image2textreader.presentation.util.compLocal.LocalSnackBarProvider
import com.eva.image2textreader.presentation.util.contracts.ShareResultsActivityContracts
import com.eva.image2textreader.presentation.util.preview.ResultsShowContentPreviewParams
import com.eva.image2textreader.ui.theme.Image2TextReaderTheme

@OptIn(
	ExperimentalMaterial3Api::class,
	ExperimentalLayoutApi::class,
	ExperimentalFoundationApi::class,
)
@Composable
fun ResultsScreen(
	results: ShowContent<List<ResultsState>>,
	sortOrder: SortResultsOption,
	onPickImage: (Uri?) -> Unit,
	onLongClick: (ResultsModel) -> Unit,
	onClick: (ResultsModel) -> Unit,
	onDeleteResult: (ResultsModel) -> Unit,
	onDeleteSelected: () -> Unit,
	onUnSelectAll: () -> Unit,
	onSelectAll: () -> Unit,
	onSortOptionChange: (SortResultsOption) -> Unit,
	modifier: Modifier = Modifier,
	selectedCount: Int = 0,
	snackBarHostState: SnackbarHostState = LocalSnackBarProvider.current,
) {
	val isAnyItemSelected by remember(selectedCount) {
		derivedStateOf { selectedCount > 0 }
	}

	BackHandler(
		enabled = isAnyItemSelected,
		onBack = onUnSelectAll,
	)

	val pickVisualImages = rememberLauncherForActivityResult(
		contract = ActivityResultContracts.PickVisualMedia(),
		onResult = onPickImage,
	)

	val shareResultsContract = rememberLauncherForActivityResult(
		contract = ShareResultsActivityContracts(),
		onResult = {}
	)

	val launchImagePicker: () -> Unit = {
		val mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
		pickVisualImages.launch(PickVisualMediaRequest(mediaType))
	}

	val snackBarInserts = if (WindowInsets.areNavigationBarsVisible)
		WindowInsets.systemBars else WindowInsets.statusBars

	Scaffold(
		topBar = {
			MultiPickerTopBar(
				sortOrder = sortOrder,
				isItemSelected = isAnyItemSelected,
				selectedCount = selectedCount,
				onClearSelected = onUnSelectAll,
				onDeleteSelected = onDeleteSelected,
				onSelectAll = onSelectAll,
				onSortOrderChange = onSortOptionChange,
			)
		},
		floatingActionButton = {
			AnimatedVisibility(
				visible = !isAnyItemSelected,
				enter = slideInVertically() + fadeIn(),
				exit = slideOutVertically() + fadeOut(),
				label = "Is Pick Image Option Visible"
			) {
				ExtendedFloatingActionButton(
					onClick = launchImagePicker,
					contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
					elevation = FloatingActionButtonDefaults.elevation()
				) {
					Icon(
						painter = painterResource(id = R.drawable.ic_image),
						contentDescription = null,
					)
					Spacer(modifier = Modifier.width(6.dp))
					Text(
						text = stringResource(id = R.string.pick_image),
						style = MaterialTheme.typography.titleMedium
					)
				}
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
		contentWindowInsets = snackBarInserts,
	) { scPadding ->
		ResultsContent(
			results = results,
			modifier = Modifier.padding(scPadding)
		) { content ->
			LazyColumn(
				contentPadding = PaddingValues(dimensionResource(R.dimen.scaffold_padding)),
				verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.results_spacing)),
				modifier = Modifier.fillMaxSize()
			) {
				itemsIndexed(
					items = content,
					key = { _, item -> item.model.id ?: -1L },
				) { _, item ->
					SavedResultsCard(
						model = item.model,
						isSelected = item.isSelected,
						onLongClick = { onLongClick(item.model) },
						onClick = {
							// callback for deselecting the selected item
							if (isAnyItemSelected) onLongClick(item.model)
							//callback for onclick on the model
							else onClick(item.model)
						},
						onShare = { shareResultsContract.launch(item.model) },
						onDelete = { onDeleteResult(item.model) },
						modifier = Modifier
							.animateContentSize()
							.animateItemPlacement(),
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
fun ResultsScreenPreview(
	@PreviewParameter(ResultsShowContentPreviewParams::class)
	results: ShowContent<List<ResultsState>>
) = Image2TextReaderTheme {
	ResultsScreen(
		results = results,
		sortOrder = SortResultsOption.TIME_OF_CREATE,
		onPickImage = {},
		onClick = {},
		onLongClick = {},
		onUnSelectAll = {},
		onDeleteSelected = {},
		onDeleteResult = {},
		onSelectAll = {},
		onSortOptionChange = {},
		snackBarHostState = SnackbarHostState()
	)
}

