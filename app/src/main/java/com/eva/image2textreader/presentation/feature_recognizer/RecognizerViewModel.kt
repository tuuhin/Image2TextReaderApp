package com.eva.image2textreader.presentation.feature_recognizer

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eva.image2textreader.data.mapper.RecognizerResultMapper
import com.eva.image2textreader.domain.models.RecognizedTextModel
import com.eva.image2textreader.domain.repository.RecognizerRepo
import com.eva.image2textreader.domain.repository.ResultHistoryRepo
import com.eva.image2textreader.presentation.navigation.NavParams
import com.eva.image2textreader.util.Resource
import com.eva.image2textreader.util.UiEvents
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RecognizerViewModel(
	private val recognizerRepo: RecognizerRepo,
	private val historyRepo: ResultHistoryRepo,
	private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

	private val _recognizedText = MutableStateFlow<RecognizedTextModel?>(null)
	val recognizedText = _recognizedText.asStateFlow()

	private val _isContentReady = MutableStateFlow(false)
	val isContentReady = _isContentReady.asStateFlow()

	private val _uiEvents = MutableSharedFlow<UiEvents>()
	val uiEvents = _uiEvents.asSharedFlow()

	private val imageUriFlow: StateFlow<String?>
		get() = savedStateHandle.getStateFlow<String?>(NavParams.URI_PATH_PARAM, null)

	private val imageUri: String?
		get() = savedStateHandle.get<String?>(NavParams.URI_PATH_PARAM)

	private var recognizerJob: Job? = null

	init {
		imageUriFlow.onEach { image ->
			image?.let(::recognizeText)
		}.launchIn(viewModelScope)
	}

	fun onBackPressEvent() = viewModelScope.launch {
		if (_isContentReady.value) return@launch
		_uiEvents.emit(UiEvents.ShowToast("Preparing output please wait"))
	}

	private fun saveResults() {
		val textModel = _recognizedText.value ?: return
		val resultsModel = RecognizerResultMapper.toResultModel(textModel, imageUri)
		viewModelScope.launch {
			when (val saveResult = historyRepo.addNewResult(resultsModel)) {
				is Resource.Success -> _uiEvents.emit(UiEvents.ShowToast("Saved"))
				is Resource.Error -> _uiEvents.emit(
					UiEvents.ShowSnackBar(
						text = saveResult.message,
						action = ::saveResults,
						actionLabel = "Retry"
					)
				)

				else -> {}
			}
		}
	}

	private fun recognizeText(uri: String) {
		recognizerJob?.cancel()
		recognizerJob = viewModelScope.launch {
			recognizerRepo.recognizeTextFromImageUri(uri = uri)
				.onEach { res ->
					// Change events according to the res
					when (res) {
						is Resource.Error -> _uiEvents.emit(UiEvents.ShowSnackBar(text = res.message))
						is Resource.Success -> _recognizedText.update { res.data }
						else -> {}
					}
					//change the loading state according to res
					val isLoaded = res !is Resource.Loading
					_isContentReady.update { isLoaded }
					// finally save this result if it's a success
					if (res is Resource.Success) saveResults()
				}
				.launchIn(this)
		}
	}


}