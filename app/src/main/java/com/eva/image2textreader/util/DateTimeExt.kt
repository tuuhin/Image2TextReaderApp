package com.eva.image2textreader.util

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun LocalDateTime.Companion.now() = Clock.System.now()
	.toLocalDateTime(TimeZone.currentSystemDefault())
