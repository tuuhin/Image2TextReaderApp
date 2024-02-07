package com.eva.image2textreader.data.local

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.eva.AppDataBase
import com.eva.ResultsEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ResultsDaoImpl(database: AppDataBase) : ResultsDao {

	private val dbQueries = database.resultsDaoQueries

	override val resultsAsFlow: Flow<List<ResultsEntity>>
		get() = dbQueries.selectAllResults()
			.asFlow()
			.mapToList(Dispatchers.IO)

	override val resultsAsList: List<ResultsEntity>
		get() = dbQueries.selectAllResults()
			.executeAsList()

	override suspend fun insertResult(entity: ResultsEntity) =
		withContext(Dispatchers.IO) {
			dbQueries.insetResult(
				id = null,
				text = entity.text,
				image_uri = entity.image_uri,
				computed_at = entity.computed_at,
				updated_at = entity.updated_at,
				language_code = entity.language_code
			)
		}


	override suspend fun updateResult(entity: ResultsEntity) =
		withContext(Dispatchers.IO) {
			dbQueries.updateResults(entity)
		}


	override suspend fun deleteResult(entity: ResultsEntity) =
		withContext(Dispatchers.IO) {
			dbQueries.deleteResult(entity.id)
		}


	override suspend fun deleteResults(vararg entities: ResultsEntity) =
		withContext(Dispatchers.IO) {
			val idsCollection = entities.map { it.id }
			dbQueries.deleteResults(idsCollection)
		}


	override suspend fun isUriSHared(uri: String): Boolean =
		withContext(Dispatchers.IO) {
			dbQueries.countSharedUriNumber(uri)
				.executeAsOneOrNull()
				?.let { result -> result > 1 } ?: false
		}


}