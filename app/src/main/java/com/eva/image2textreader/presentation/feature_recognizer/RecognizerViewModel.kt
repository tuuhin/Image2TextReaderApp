package com.eva.image2textreader.presentation.feature_recognizer

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eva.image2textreader.domain.models.RecognizedTextModel
import com.eva.image2textreader.domain.repository.RecognizerRepo
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
	private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

	private val _uiEvents = MutableSharedFlow<UiEvents>()
	val uiEvents = _uiEvents.asSharedFlow()

	private val _recognizedText = MutableStateFlow<RecognizedTextModel?>(null)
	val recognizedText = _recognizedText.asStateFlow()

	private val imageUriFlow: StateFlow<String?>
		get() = savedStateHandle.getStateFlow<String?>(NavParams.URI_PATH_PARAM, null)

	private var recognizerJob: Job? = null

	init {
		imageUriFlow.onEach { image ->
			image?.let(::recognizeText)
		}.launchIn(viewModelScope)
	}

	private fun recognizeText(uri: String) {
		recognizerJob?.cancel()
		recognizerJob = viewModelScope.launch {
			recognizerRepo.recognizeTextFromImageUri(uri)
				.onEach { res ->
					when (res) {
						is Resource.Error -> _uiEvents.emit(UiEvents.ShowSnackBar(text = res.message))
						Resource.Loading -> _uiEvents.emit(UiEvents.ShowToast("Preparing"))
						is Resource.Success -> _recognizedText.update { res.data }
					}
				}.launchIn(this)
		}
	}


}