package com.eva.image2textreader.data.local

import com.eva.ResultsEntity
import kotlinx.coroutines.flow.Flow

interface ResultsDao {

	val resultsAsFlow: Flow<List<ResultsEntity>>

	val resultsAsList: List<ResultsEntity>

	suspend fun insertResult(entity: ResultsEntity)

	suspend fun updateResult(entity: ResultsEntity)

	suspend fun deleteResult(entity: ResultsEntity)

	suspend fun deleteResults(vararg entities: ResultsEntity)

	suspend fun isUriSHared(uri: String): Boolean
}