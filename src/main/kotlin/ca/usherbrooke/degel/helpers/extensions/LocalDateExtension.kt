package ca.usherbrooke.degel.helpers.extensions

import java.time.LocalDate
import java.time.ZoneId
import java.util.*

fun LocalDate.toDate() = Date
        .from(this.atStartOfDay()
        .atZone(ZoneId.systemDefault())
        .toInstant())