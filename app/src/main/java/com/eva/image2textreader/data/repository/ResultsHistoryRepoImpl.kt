package com.eva.image2textreader.data.repository

import android.database.sqlite.SQLiteException
import com.eva.image2textreader.data.local.ResultsDao
import com.eva.image2textreader.data.mapper.ResultsModelEntityMapper
import com.eva.image2textreader.domain.fs.ImageFileSaver
import com.eva.image2textreader.domain.models.ResultsModel
import com.eva.image2textreader.domain.repository.ResultHistoryRepo
import com.eva.image2textreader.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class ResultsHistoryRepoImpl(
	private val resultsDao: ResultsDao,
	private val fileSaver: ImageFileSaver,
) : ResultHistoryRepo {

	override fun resultsFlow(): Flow<Resource<List<ResultsModel>>> {
		return flow {
			emit(Resource.Loading)
			try {
				val results = resultsDao.resultsAsFlow.map { entities ->
					val models = entities.map(ResultsModelEntityMapper::toModel)
					Resource.Success(data = models)
				}
				emitAll(results)
			} catch (e: SQLiteException) {
				e.printStackTrace()
				emit(Resource.Error(message = e.message ?: "SQL_INTERNAL_ERROR"))
			} catch (e: Exception) {
				e.printStackTrace()
				emit(Resource.Error(message = e.message ?: ""))
			}
		}.flowOn(Dispatchers.IO)
	}

	override suspend fun addNewResult(result: ResultsModel): Resource<Boolean> {
		return withContext(Dispatchers.IO) {
			try {
				// save the image
				val uri = result.imageUri?.let {
					val deferredSave = async {
						fileSaver.saveFileFromContentUri(result.imageUri)
					}
					deferredSave.await()
				}
				//save the entity
				val entity = ResultsModelEntityMapper.toEntity(model = result, imageUri = uri)
				resultsDao.insertResult(entity)
				Resource.Success(true)
			} catch (e: SQLiteException) {
				e.printStackTrace()
				Resource.Error(message = e.message ?: "SQL_INTERNAL_ERROR")
			} catch (e: Exception) {
				e.printStackTrace()
				Resource.Error(message = e.message ?: "")
			}
		}
	}

	override suspend fun updateResult(result: ResultsModel): Resource<Boolean> {
		return try {
			// update the content but images are constant
			val entity = ResultsModelEntityMapper.toEntity(model = result)
			resultsDao.updateResult(entity)
			Resource.Success(true)
		} catch (e: SQLiteException) {
			e.printStackTrace()
			Resource.Error(message = e.message ?: "SQL_INTERNAL_ERROR")
		} catch (e: Exception) {
			e.printStackTrace()
			Resource.Error(message = e.message ?: "")
		}
	}

	override suspend fun deleteResult(result: ResultsModel): Resource<Boolean> {
		return withContext(Dispatchers.IO) {
			try {
				val entity = ResultsModelEntityMapper.toEntity(result)
				//Calculate if SharedUri then return url
				val isShared = result.imageUri?.let { resultsDao.isUriSHared(it) }
				// delete the entity
				resultsDao.deleteResult(entity)
				//Delete the file if it's not shared otherwise do nothing
				if (isShared == false) fileSaver.deleteFile(result.imageUri)
				Resource.Success(true)
			} catch (e: SQLiteException) {
				e.printStackTrace()
				Resource.Error(message = e.message ?: "SQL_INTERNAL_ERROR")
			} catch (e: Exception) {
				e.printStackTrace()
				Resource.Error(message = e.message ?: "")
			}
		}
	}

	override suspend fun deleteResults(results: List<ResultsModel>): Resource<Boolean> {
		return withContext(Dispatchers.IO) {
			try {
				val entities = results.map(ResultsModelEntityMapper::toEntity)
				val entitiesArray = entities.toTypedArray()
				// Calculate the uris to be deleted
				val uris = async { resultsDao.calculateCommonUriToDelete(entities) }.await()
				// Then delete the results
				resultsDao.deleteResults(*entitiesArray)
				//then delete the files
				val operation = async { fileSaver.deleteFiles(uris) }
				operation.await()
				// Then it's a success
				Resource.Success(true)
			} catch (e: SQLiteException) {
				e.printStackTrace()
				Resource.Error(message = e.message ?: "SQL_INTERNAL_ERROR")
			} catch (e: Exception) {
				e.printStackTrace()
				Resource.Error(message = e.message ?: "")
			}
		}
	}

	override suspend fun resultsFromId(id: Long): Flow<Resource<ResultsModel?>> {
		return flow {
			emit(Resource.Loading)
			try {
				val model = resultsDao.resultFromId(id)?.let(ResultsModelEntityMapper::toModel)
				emit(Resource.Success(data = model))
			} catch (e: SQLiteException) {
				e.printStackTrace()
				emit(Resource.Error(message = e.message ?: "SQL_INTERNAL_ERROR"))
			} catch (e: Exception) {
				e.printStackTrace()
				emit(Resource.Error(message = e.message ?: ""))
			}
		}.flowOn(Dispatchers.IO)

	}


}