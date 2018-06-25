package ca.usherbrooke.degel.models

import biweekly.component.VEvent

data class CalendarDiff(val addedEvents: List<VEvent>,
                        val removedEvents: List<VEvent>,
                        val modifiedEvents: List<VEvent>)