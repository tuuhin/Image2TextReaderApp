package com.eva.image2textreader.presentation.feature_results

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.eva.image2textreader.R
import com.eva.image2textreader.domain.models.ResultsModel
import com.eva.image2textreader.presentation.feature_results.composables.MultiPickerTopBar
import com.eva.image2textreader.presentation.feature_results.composables.SavedResultsCard
import com.eva.image2textreader.presentation.feature_results.util.ResultsState
import com.eva.image2textreader.presentation.util.compLocal.LocalSnackBarProvider
import com.eva.image2textreader.presentation.util.preview.PreviewFakes
import com.eva.image2textreader.ui.theme.Image2TextReaderTheme

@OptIn(
	ExperimentalMaterial3Api::class,
	ExperimentalFoundationApi::class
)
@Composable
fun ResultsScreen(
	results: List<ResultsState>,
	onPickImage: () -> Unit,
	onLongClick: (ResultsModel) -> Unit,
	onClick: (ResultsModel) -> Unit,
	onDeleteSelected: () -> Unit,
	onUnSelect: () -> Unit,
	modifier: Modifier = Modifier,
	selectedCount: Int = 0,
	snackBarHostState: SnackbarHostState = LocalSnackBarProvider.current
) {
	val isAnyItemSelected by remember(selectedCount) {
		derivedStateOf { selectedCount > 0 }
	}

	val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

	BackHandler(enabled = isAnyItemSelected, onBack = onUnSelect)

	Scaffold(
		topBar = {
			MultiPickerTopBar(
				isItemSelected = isAnyItemSelected,
				selectedCount = selectedCount,
				onClearSelected = onUnSelect,
				onDeleteSelected = onDeleteSelected,
				scrollBehavior = scrollBehavior,
			)
		},
		floatingActionButton = {
			ExtendedFloatingActionButton(
				onClick = onPickImage,
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
					shape = MaterialTheme.shapes.large,
				)
			}
		},
		modifier = modifier,
		contentWindowInsets = WindowInsets.systemBars
	) { scPadding ->
		LazyColumn(
			contentPadding = scPadding,
			verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.results_spacing)),
			modifier = Modifier
				.padding(horizontal = dimensionResource(R.dimen.scaffold_padding))
				.nestedScroll(scrollBehavior.nestedScrollConnection)
		) {
			itemsIndexed(
				items = results,
				key = { idx, _ -> idx },
			) { _, item ->
				SavedResultsCard(
					model = item.model,
					isSelected = item.isSelected,
					onLongClick = { onLongClick(item.model) },
					onClick = { onClick(item.model) },
					modifier = Modifier.animateItemPlacement(),
				)
			}
		}
	}
}

@PreviewLightDark
@Composable
fun ResultsScreenPreview() = Image2TextReaderTheme {
	ResultsScreen(
		results = PreviewFakes.fakeResultsModelList.map(::ResultsState),
		onPickImage = {},
		onClick = {},
		onLongClick = {},
		onUnSelect = {},
		onDeleteSelected = {}
	)
}

