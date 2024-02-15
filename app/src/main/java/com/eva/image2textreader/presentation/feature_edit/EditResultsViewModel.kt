package com.eva.image2textreader.presentation.feature_edit

import android.app.Application
import androidx.core.net.toUri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.eva.image2textreader.domain.models.ResultsModel
import com.eva.image2textreader.domain.repository.ResultHistoryRepo
import com.eva.image2textreader.presentation.feature_edit.utils.FileSaverState
import com.eva.image2textreader.presentation.navigation.NavParams
import com.eva.image2textreader.presentation.util.ShowContent
import com.eva.image2textreader.util.Resource
import com.eva.image2textreader.util.UiEvents
import com.eva.image2textreader.worker.ContentSaveWorker
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditResultsViewModel(
	private val application: Application,
	private val historyRepo: ResultHistoryRepo,
	private val savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

	private val _resultModel = MutableStateFlow<ResultsModel?>(null)
	private val _isLoading = MutableStateFlow(true)

	val resultAsContent: StateFlow<ShowContent<ResultsModel?>> =
		combine(_resultModel, _isLoading) { model, loading ->
			ShowContent(
				isLoading = loading, content = model
			)
		}.stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(800),
			initialValue = ShowContent(content = null, isLoading = true)
		)

	private val _fileDownloadState = MutableStateFlow(FileSaverState())
	val fileDownloadState = _fileDownloadState.asStateFlow()

	private val _uiEvents = MutableSharedFlow<UiEvents>()
	val uiEvents = _uiEvents.asSharedFlow()

	private val resultIdFlow: StateFlow<Long?>
		get() = savedStateHandle.getStateFlow(NavParams.RESULT_ID, initialValue = null)

	private var _downloadJob: Job? = null

	init {
		resultIdFlow.onEach { resultId ->
			resultId?.let(::setResult)
		}.launchIn(viewModelScope)
	}

	private fun setResult(resultId: Long) = viewModelScope.launch {
		historyRepo.resultsFromId(resultId)
			.onEach { res ->
				_isLoading.update { res is Resource.Loading }
				when (res) {
					is Resource.Error -> _uiEvents
						.emit(UiEvents.ShowSnackBar(text = "Cannot load the result"))

					is Resource.Success -> _resultModel.update { res.data }
					else -> {}
				}

			}.launchIn(this)
	}


	fun onExportFile() {
		val resultModel = _resultModel.value ?: return

		val context = application.applicationContext
		val workerId = ContentSaveWorker.startSaveWorker(context, resultModel)

		_downloadJob?.cancel()
		_downloadJob = ContentSaveWorker.observeWorkerState(context, workerId)
			.onEach { res ->
				//change the ready state
				_fileDownloadState.update { it.copy(isPreparing = res is Resource.Loading) }
				//handle the data returned
				when (res) {
					is Resource.Error -> _uiEvents.emit(UiEvents.ShowSnackBar(text = res.message))
					is Resource.Success -> {
						_fileDownloadState.update { state ->
							state.copy(
								fileUri = res.data.toUri(),
								result = resultModel
							)
						}
						_uiEvents.emit(UiEvents.ShowToast("File Saved Successfully"))
					}

					else -> {}
				}
			}
			.launchIn(viewModelScope)

	}


	fun onEditComplete() = _resultModel.value?.let { model ->
		viewModelScope.launch {
			when (val result = historyRepo.updateResult(model)) {
				is Resource.Error -> _uiEvents.emit(
					UiEvents.ShowSnackBar(
						result.message, action = ::onEditComplete, actionLabel = "Retry"
					)
				)

				is Resource.Success -> _uiEvents.emit(UiEvents.ShowToast("Content Updated Successfully"))
				else -> {}
			}
		}
	}

	fun onContentChange(newText: String) = _resultModel.update { it?.copy(text = newText) }
}
