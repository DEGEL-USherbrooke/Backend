package ca.usherbrooke.degel.exceptions

class FailedRegisterNotificationTypeException(id: String): ServerSideException("Failed to register notification: $id")

class FailedSendNotificationException(cip: String): ServerSideException("Failed to send notification to user: $cip")