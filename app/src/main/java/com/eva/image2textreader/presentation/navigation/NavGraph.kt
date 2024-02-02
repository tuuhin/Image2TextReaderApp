package com.eva.image2textreader.presentation.navigation

import android.content.Context
import android.widget.Toast
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.eva.image2textreader.R
import com.eva.image2textreader.presentation.feature_results.ResultsScreen
import com.eva.image2textreader.presentation.feature_results.ResultsViewModel
import com.eva.image2textreader.presentation.util.compLocal.LocalSnackBarProvider
import com.eva.image2textreader.util.UiEvents
import org.koin.androidx.compose.koinViewModel

@Composable
fun NavigationGraph(
	modifier: Modifier = Modifier,
	context: Context = LocalContext.current,
	lifeCycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
	snackBarHost: SnackbarHostState = LocalSnackBarProvider.current
) {
	val controller = rememberNavController()

	NavHost(
		navController = controller,
		startDestination = Screens.Resents.route,
		modifier = modifier,
	) {
		composable(route = Screens.Resents.route) {

			val viewModel = koinViewModel<ResultsViewModel>()

			val savedResults by viewModel.savedResults.collectAsStateWithLifecycle()

			val selectedResults by viewModel.selectedResults.collectAsStateWithLifecycle()

			LaunchedEffect(key1 = lifeCycleOwner) {
				lifeCycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
					viewModel.uiEvents.collect { event ->
						when (event) {
							is UiEvents.ShowSnackBar -> {
								val results = snackBarHost.showSnackbar(
									message = event.text,
									actionLabel = context.getString(R.string.retry_text)
								)
								when (results) {
									SnackbarResult.ActionPerformed -> event.action.invoke()
									else -> {}
								}
							}

							is UiEvents.ShowToast -> {
								Toast.makeText(context, event.message, Toast.LENGTH_SHORT)
									.show()
							}
						}
					}
				}
			}

			ResultsScreen(
				results = savedResults,
				onPickImage = {},
				onClick = {},
				onLongClick = viewModel::onSelectResult,
				onUnSelect = viewModel::clearAllSelection,
				selectedCount = selectedResults.size,
				onDeleteSelected = viewModel::onDeleteSelected,
			)
		}
	}
}