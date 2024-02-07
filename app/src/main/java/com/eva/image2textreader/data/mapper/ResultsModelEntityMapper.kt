package com.eva.image2textreader.data.mapper

import com.eva.ResultsEntity
import com.eva.image2textreader.domain.models.ResultsModel
import com.eva.image2textreader.util.now
import kotlinx.datetime.LocalDateTime

class ResultsModelEntityMapper {

	fun toModel(entity: ResultsEntity): ResultsModel {

		return ResultsModel(
			id = entity.id,
			text = entity.text,
			createdAt = entity.computed_at,
			lastUpdated = entity.updated_at,
			imageUri = entity.image_uri,
			languageCode = entity.language_code,
		)
	}

	fun toEntity(model: ResultsModel, imageUri: String?): ResultsEntity {
		return ResultsEntity(
			id = model.id ?: -1,
			text = model.text,
			computed_at = model.createdAt,
			updated_at = LocalDateTime.now(),
			image_uri = imageUri,
			language_code = model.languageCode,
		)
	}

	fun toEntity(model: ResultsModel): ResultsEntity {
		return ResultsEntity(
			id = model.id ?: -1,
			text = model.text,
			computed_at = model.createdAt,
			updated_at = LocalDateTime.now(),
			image_uri = model.imageUri,
			language_code = model.languageCode,
		)
	}
}