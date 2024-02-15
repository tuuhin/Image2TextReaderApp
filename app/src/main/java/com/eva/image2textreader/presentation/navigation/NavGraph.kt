package com.eva.image2textreader.presentation.navigation

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarDuration
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
import androidx.navigation.compose.rememberNavController
import com.eva.image2textreader.presentation.feature_edit.EditResultScreen
import com.eva.image2textreader.presentation.feature_edit.EditResultsViewModel
import com.eva.image2textreader.presentation.feature_recognizer.RecognizerScreen
import com.eva.image2textreader.presentation.feature_recognizer.RecognizerViewModel
import com.eva.image2textreader.presentation.feature_results.ResultsScreen
import com.eva.image2textreader.presentation.feature_results.ResultsViewModel
import com.eva.image2textreader.presentation.util.compLocal.LocalSnackBarProvider
import com.eva.image2textreader.util.UiEvents
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavigationGraph(
	modifier: Modifier = Modifier,
	context: Context = LocalContext.current,
	lifeCycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
	snackBarHost: SnackbarHostState = LocalSnackBarProvider.current
) {
	val navController = rememberNavController()

	NavHost(
		navController = navController,
		startDestination = Screens.ResultsScreen.route,
		modifier = modifier,
	) {
		composableWithAnimation(
			route = Screens.ResultsScreen.route
		) {

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
									actionLabel = event.actionLabel,
									duration = SnackbarDuration.Short
								)
								when (results) {
									SnackbarResult.ActionPerformed -> event.action?.invoke()
									else -> {}
								}
							}

							is UiEvents.ShowToast -> {
								Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
							}
						}
					}
				}
			}

			ResultsScreen(
				results = savedResults,
				onPickImage = { uri ->
					uri?.let {
						Screens.ResultsScreen.navigateToRecognizeScreen(navController, uri)
					}
				},
				onClick = {
					// TODO: Check the Id Before moving
					Screens.ResultsScreen.navigateToEditScreen(navController, it.id ?: -1)
				},
				onLongClick = viewModel::onSelectResult,
				onUnSelectAll = viewModel::clearAllSelection,
				selectedCount = selectedResults.size,
				onDeleteSelected = viewModel::onDeleteSelected,
				onDeleteResult = viewModel::onDeleteResult,
				onSelectAll = viewModel::onSelectAll
			)
		}

		composableWithAnimation(
			route = Screens.RecognizeScreen.route,
			navArguments = Screens.RecognizeScreen.navArgs,
		) { backstack ->

			val viewModel = koinViewModel<RecognizerViewModel>()

			val recognizerModel by viewModel.recognizedText.collectAsStateWithLifecycle()
			val isComputationComplete by viewModel.isContentReady.collectAsStateWithLifecycle()

			LaunchedEffect(key1 = lifeCycleOwner) {
				lifeCycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
					viewModel.uiEvents.collect { event ->
						when (event) {
							is UiEvents.ShowSnackBar -> {
								val results = snackBarHost.showSnackbar(
									message = event.text,
									actionLabel = event.actionLabel,
									duration = SnackbarDuration.Short
								)
								when (results) {
									SnackbarResult.ActionPerformed -> event.action?.invoke()
									else -> {}
								}
							}

							is UiEvents.ShowToast -> {
								Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
							}
						}
					}
				}
			}

			val imageUri = backstack.arguments?.getString(NavParams.URI_PATH_PARAM)

			RecognizerScreen(
				imageUri = imageUri,
				model = recognizerModel,
				isContentReady = isComputationComplete,
				onBack = viewModel::onBackPressEvent,
				navigation = {
					IconButton(onClick = navController::navigateUp) {
						Icon(
							imageVector = Icons.AutoMirrored.Filled.ArrowBack,
							contentDescription = null
						)
					}
				},
			)
		}

		composableWithAnimation(
			route = Screens.EditScreen.route,
			navArguments = Screens.EditScreen.navArgs,
		) {

			val viewModel = koinViewModel<EditResultsViewModel>()

			val resultsAsContent by viewModel.resultAsContent.collectAsStateWithLifecycle()
			val fileSaverState by viewModel.fileDownloadState.collectAsStateWithLifecycle()

			LaunchedEffect(key1 = lifeCycleOwner) {
				lifeCycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
					viewModel.uiEvents.collect { event ->
						when (event) {
							is UiEvents.ShowSnackBar -> {
								val results = snackBarHost.showSnackbar(
									message = event.text,
									actionLabel = event.actionLabel,
									duration = SnackbarDuration.Short
								)
								when (results) {
									SnackbarResult.ActionPerformed -> event.action?.invoke()
									else -> {}
								}
							}

							is UiEvents.ShowToast -> {
								Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
							}
						}
					}
				}
			}

			EditResultScreen(
				results = resultsAsContent,
				onEditComplete = viewModel::onEditComplete,
				onTextChange = viewModel::onContentChange,
				onExport = viewModel::onExportFile,
				fileSaverState = fileSaverState,
				navigation = {
					IconButton(onClick = navController::navigateUp) {
						Icon(
							imageVector = Icons.AutoMirrored.Filled.ArrowBack,
							contentDescription = null
						)
					}
				},
			)
		}
	}
}
