package com.eva.image2textreader.presentation.feature_results

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eva.image2textreader.domain.models.ResultsModel
import com.eva.image2textreader.domain.repository.ResultHistoryRepo
import com.eva.image2textreader.presentation.feature_results.util.ResultsState
import com.eva.image2textreader.presentation.feature_results.util.SortResultsOption
import com.eva.image2textreader.presentation.util.ShowContent
import com.eva.image2textreader.util.Resource
import com.eva.image2textreader.util.UiEvents
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class ResultsViewModel(
	private val historyRepo: ResultHistoryRepo
) : ViewModel() {

	private val _savedResultsState = MutableStateFlow<List<ResultsState>>(emptyList())
	private val _isLoading = MutableStateFlow(true)

	private val _sortOption = MutableStateFlow(SortResultsOption.TIME_OF_CREATE)
	val sortOrder = _sortOption.asStateFlow()

	val savedResults =
		combine(_savedResultsState, _isLoading, _sortOption) { results, loading, sortOrder ->
			val sortedResults = when (sortOrder) {
				SortResultsOption.TIME_OF_CREATE -> results.sortedByDescending { it.model.createdAt }
				SortResultsOption.TIME_OF_UPDATE -> results.sortedByDescending { it.model.lastUpdated }
			}
			ShowContent(isLoading = loading, content = sortedResults)
		}.stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(2000L),
			initialValue = ShowContent(content = emptyList())
		)

	val selectedResults = _savedResultsState
		.map { savedResults -> savedResults.filter(ResultsState::isSelected) }
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.Lazily,
			initialValue = emptyList()
		)

	private val _uiEvents = MutableSharedFlow<UiEvents>()
	val uiEvents = _uiEvents.asSharedFlow()


	fun clearAllSelection() = _savedResultsState.update { states ->
		states.map { state -> state.copy(isSelected = false) }
	}

	fun onSelectAll() = _savedResultsState.update { states ->
		states.map { state -> state.copy(isSelected = true) }
	}

	fun onSelectResult(model: ResultsModel) = _savedResultsState.update { states ->
		states.map { state ->
			if (state.model == model) state.copy(isSelected = !state.isSelected)
			else state
		}
	}

	fun onSortOrderChange(order: SortResultsOption) = _sortOption.update { order }

	init {
		setUpHistory()
	}

	private fun setUpHistory() = viewModelScope.launch {
		historyRepo.resultsFlow().onEach { res ->
			// Is Content Loading
			_isLoading.update { res is Resource.Loading }

			when (res) {
				is Resource.Error -> _uiEvents.emit(UiEvents.ShowSnackBar(text = res.message))

				is Resource.Success -> _savedResultsState.update { res.data.map(::ResultsState) }

				else -> {}
			}

		}.launchIn(this)
	}

	fun onDeleteResult(model: ResultsModel) = viewModelScope.launch {
		when (val results = historyRepo.deleteResult(model)) {
			is Resource.Error -> _uiEvents.emit(UiEvents.ShowToast(results.message))

			is Resource.Success -> _uiEvents.emit(
				UiEvents.ShowSnackBar(
					text = "Deleted Result Successfully",
				)
			)

			else -> {}
		}
	}


	fun onDeleteSelected() = viewModelScope.launch {

		val resultsTobedeleted =
			_savedResultsState.value.filter(ResultsState::isSelected).map(ResultsState::model)

		when (val results = historyRepo.deleteResults(resultsTobedeleted)) {
			is Resource.Error -> _uiEvents.emit(UiEvents.ShowSnackBar(text = results.message))

			is Resource.Success -> _uiEvents.emit(UiEvents.ShowToast("Deleted Results Successfully"))

			else -> {}
		}
	}

}