package ca.usherbrooke.degel.controllers

import ca.usherbrooke.degel.config.Constants.API
import ca.usherbrooke.degel.config.Permissions
import ca.usherbrooke.degel.models.Feed
import ca.usherbrooke.degel.models.News
import ca.usherbrooke.degel.services.FeedsService
import mu.KLogging
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping(API)
class FeedsController(val feedsService: FeedsService) {
    companion object: KLogging()

    @GetMapping("/feeds")
    fun getFeeds() = feedsService.getFeeds()

    @PreAuthorize(Permissions.HAS_ADMIN_ROLE)
    @PostMapping("/feeds")
    fun upsertFeed(@RequestBody feed: Feed) = feedsService.upsertFeed(feed)

    @PreAuthorize("${Permissions.USER_OWN_RESSOURCE} or ${Permissions.HAS_ADMIN_ROLE}")
    @GetMapping("/user/{id}/news")
    fun getNews(@PathVariable id: UUID) : List<News> {
        logger.debug("User $id requests his news")
        return feedsService.getNews(id)
    }
}