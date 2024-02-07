package com.eva.image2textreader.data.repository

import android.database.sqlite.SQLiteException
import com.eva.image2textreader.data.image.ImageFileSaver
import com.eva.image2textreader.data.local.ResultsDao
import com.eva.image2textreader.data.mapper.ResultsModelEntityMapper
import com.eva.image2textreader.domain.models.ResultsModel
import com.eva.image2textreader.domain.repository.ResultHistoryRepo
import com.eva.image2textreader.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class ResultsHistoryRepoImpl(
	private val resultsDao: ResultsDao,
	private val mapper: ResultsModelEntityMapper,
	private val fileSaver: ImageFileSaver,
) : ResultHistoryRepo {

	override fun resultsFlow(): Flow<Resource<List<ResultsModel>>> {
		return flow {
			emit(Resource.Loading)
			try {
				val results = resultsDao.resultsAsFlow.map { entities ->
					val models = entities.map(mapper::toModel)
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
		}
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
				val entity = mapper.toEntity(model = result, imageUri = uri)
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
			val entity = mapper.toEntity(model = result)
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
				val entity = mapper.toEntity(result)
				// delete the entity
				resultsDao.deleteResult(entity)
				//delete the file
				result.imageUri?.let {
					val isShared = resultsDao.isUriSHared(it)
					if (isShared) return@let
					fileSaver.deleteFile(it)
				}
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
		return try {
			val entities = results.map(mapper::toEntity).toTypedArray()
			resultsDao.deleteResults(*entities)
			// TODO: IMPLEMENT DELETE FILES FOR GROUP DELETE
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