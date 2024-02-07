package com.eva.image2textreader.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.eva.AppDataBase
import com.eva.ResultsEntity
import com.eva.image2textreader.data.local.ResultsDao
import com.eva.image2textreader.data.local.ResultsDaoImpl
import com.eva.image2textreader.data.local.localDateTimeAdapter
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

private const val DB_NAME = "database.db"

val dbModule = module {
	single<SqlDriver> {
		AndroidSqliteDriver(
			schema = AppDataBase.Schema,
			context = androidContext(),
			name = DB_NAME
		)

	}
	single<AppDataBase> {
		AppDataBase(
			driver = get<SqlDriver>(),
			resultsEntityAdapter = ResultsEntity.Adapter(
				computed_atAdapter = localDateTimeAdapter,
				updated_atAdapter = localDateTimeAdapter
			)
		)
	}

	singleOf(::ResultsDaoImpl) bind ResultsDao::class

}