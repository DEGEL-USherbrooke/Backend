package ca.usherbrooke.degel.controllers

import biweekly.Biweekly
import ca.usherbrooke.degel.config.Constants.API
import ca.usherbrooke.degel.config.Permissions
import ca.usherbrooke.degel.exceptions.DegelException
import ca.usherbrooke.degel.models.Value
import ca.usherbrooke.degel.services.CalendarService
import mu.KLogging
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping(API)
class CalendarController(val calendarService: CalendarService) {
    companion object: KLogging()

    @PreAuthorize("${Permissions.USER_OWN_RESSOURCE} or ${Permissions.HAS_ADMIN_ROLE}")
    @GetMapping("/user/{id}/calendar")
    @Throws(DegelException::class)
    fun getCalendar(@PathVariable id: UUID) : String {
        logger.info("User $id requests his calendar")
        return Biweekly.writeJson(calendarService.getCalendar(id)).go()
    }

    @PreAuthorize("${Permissions.USER_OWN_RESSOURCE} or ${Permissions.HAS_ADMIN_ROLE}")
    @PostMapping("/user/{id}/calendar/key")
    fun setCalendarKey(@PathVariable id: UUID, @RequestBody key: Value<String>) : Value<String> {
        logger.info("New calendar key for user $id")
        return calendarService.upsertCalendarKey(id, key.value)
    }
}