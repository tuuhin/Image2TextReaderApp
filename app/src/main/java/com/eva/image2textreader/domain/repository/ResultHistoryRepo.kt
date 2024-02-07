package com.eva.image2textreader.domain.repository

import com.eva.image2textreader.domain.models.ResultsModel
import com.eva.image2textreader.util.Resource
import kotlinx.coroutines.flow.Flow

interface ResultHistoryRepo {

	fun resultsFlow(): Flow<Resource<List<ResultsModel>>>

	suspend fun addNewResult(result: ResultsModel): Resource<Boolean>

	suspend fun updateResult(result: ResultsModel): Resource<Boolean>

	suspend fun deleteResult(result: ResultsModel): Resource<Boolean>

	suspend fun deleteResults(results: List<ResultsModel>): Resource<Boolean>
}