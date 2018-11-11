package ca.usherbrooke.degel.controllers

import ca.usherbrooke.degel.config.Constants.API
import ca.usherbrooke.degel.config.Permissions
import ca.usherbrooke.degel.models.Feed
import ca.usherbrooke.degel.services.FeedsService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(API)
class FeedsController(val feedsService: FeedsService) {
    @GetMapping("/feeds")
    fun getFeeds() = feedsService.getFeeds()

    @PreAuthorize(Permissions.HAS_ADMIN_ROLE)
    @PostMapping("/feeds")
    fun upsertFeed(@RequestBody feed: Feed) = feedsService.upsertFeed(feed)
}