package com.eva.image2textreader.presentation.feature_results

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eva.image2textreader.domain.models.ResultsModel
import com.eva.image2textreader.presentation.feature_results.util.ResultsState
import com.eva.image2textreader.util.UiEvents
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update


class ResultsViewModel : ViewModel() {

	private val _savedResultsState = MutableStateFlow<List<ResultsState>>(emptyList())
	val savedResults = _savedResultsState.asStateFlow()

	val selectedResults = _savedResultsState
		.map { savedResults -> savedResults.filter { it.isSelected } }
		.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

	private val _uiEvents = MutableSharedFlow<UiEvents>()
	val uiEvents = _uiEvents.asSharedFlow()


	fun clearAllSelection() = _savedResultsState.update { states ->
		states.map { state -> state.copy(isSelected = false) }
	}

	fun onSelectResult(model: ResultsModel) = _savedResultsState.update { states ->
		states.map { state ->
			if (state.model == model) state.copy(isSelected = !state.isSelected)
			else state
		}
	}

	fun onDeleteSelected() {

	}


}