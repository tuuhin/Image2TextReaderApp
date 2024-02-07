package com.eva.image2textreader.presentation.feature_results

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.eva.image2textreader.R
import com.eva.image2textreader.domain.models.ResultsModel
import com.eva.image2textreader.presentation.feature_results.composables.MultiPickerTopBar
import com.eva.image2textreader.presentation.feature_results.composables.ResultsContent
import com.eva.image2textreader.presentation.feature_results.composables.SavedResultsCard
import com.eva.image2textreader.presentation.feature_results.util.ResultsState
import com.eva.image2textreader.presentation.util.ShowContent
import com.eva.image2textreader.presentation.util.compLocal.LocalSnackBarProvider
import com.eva.image2textreader.presentation.util.preview.ResultsShowContentPreviewParams
import com.eva.image2textreader.ui.theme.Image2TextReaderTheme

@OptIn(
	ExperimentalMaterial3Api::class,
	ExperimentalLayoutApi::class,
	ExperimentalFoundationApi::class
)
@Composable
fun ResultsScreen(
	results: ShowContent<List<ResultsState>>,
	onPickImage: (Uri?) -> Unit,
	onLongClick: (ResultsModel) -> Unit,
	onClick: (ResultsModel) -> Unit,
	onDeleteResult: (ResultsModel) -> Unit,
	onDeleteSelected: () -> Unit,
	onUnSelectAll: () -> Unit,
	onSelectAll: () -> Unit,
	modifier: Modifier = Modifier,
	selectedCount: Int = 0,
	snackBarHostState: SnackbarHostState = LocalSnackBarProvider.current,
	context: Context = LocalContext.current
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

	val launchImagePicker: () -> Unit = {
		val mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
		pickVisualImages.launch(PickVisualMediaRequest(mediaType))
	}
	val onShareSelected: () -> Unit = {

		val selectedUri = arrayListOf(*results.content
			.filter { it.isSelected }
			.mapNotNull { it.model.imageUri?.toUri() }
			.toTypedArray()
		)

		val intent = Intent(Intent.ACTION_SEND_MULTIPLE).apply {
			putParcelableArrayListExtra(
				Intent.EXTRA_STREAM, selectedUri
			)
			type = "image/*"
		}
	}

	val onShareResult: (ResultsModel) -> Unit = { result ->

		val title = context.getString(R.string.computation_result, result.id ?: -1)

		val intent = Intent(Intent.ACTION_SEND).apply {
			putExtra(Intent.EXTRA_TITLE, title)
			putExtra(Intent.EXTRA_TEXT, result.text)
			flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
			setDataAndType(result.imageUri?.let(Uri::parse), "text/plain")
		}

		val intentChooser = Intent.createChooser(
			intent,
			context.getString(R.string.share_results_title)
		)
		context.startActivity(intentChooser)
	}

	Scaffold(
		topBar = {
			MultiPickerTopBar(
				isItemSelected = isAnyItemSelected,
				selectedCount = selectedCount,
				onClearSelected = onUnSelectAll,
				onDeleteSelected = onDeleteSelected,
				onSelectAll = onSelectAll,
			)
		},
		floatingActionButton = {
			ExtendedFloatingActionButton(
				onClick = launchImagePicker,
				contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
				elevation = FloatingActionButtonDefaults.elevation()
			) {
				Icon(
					painter = painterResource(id = R.drawable.ic_image),
					contentDescription = null,
				)
				Spacer(modifier = Modifier.width(4.dp))
				Text(
					text = stringResource(id = R.string.pick_image),
					style = MaterialTheme.typography.bodyMedium
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
		contentWindowInsets = if (WindowInsets.areNavigationBarsVisible)
			WindowInsets.systemBars else WindowInsets.statusBars,
	) { scPadding ->
		ResultsContent(
			results = results,
			modifier = Modifier.padding(scPadding)
		) { content ->
			// TODO: If later check if multiple screen size required
			LazyVerticalGrid(
				columns = GridCells.Adaptive(300.dp),
				contentPadding = PaddingValues(dimensionResource(R.dimen.scaffold_padding)),
				verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.results_spacing)),
				horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.results_spacing)),
				modifier = Modifier.fillMaxSize()
			) {
				itemsIndexed(
					items = content,
					key = { idx, _ -> idx },
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
						onShare = { onShareResult(item.model) },
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

@PreviewLightDark
@Composable
fun ResultsScreenPreview(
	@PreviewParameter(ResultsShowContentPreviewParams::class)
	results: ShowContent<List<ResultsState>>
) = Image2TextReaderTheme {
	ResultsScreen(
		results = results,
		onPickImage = {},
		onClick = {},
		onLongClick = {},
		onUnSelectAll = {},
		onDeleteSelected = {},
		onDeleteResult = {},
		onSelectAll = {},
		snackBarHostState = SnackbarHostState()
	)
}

