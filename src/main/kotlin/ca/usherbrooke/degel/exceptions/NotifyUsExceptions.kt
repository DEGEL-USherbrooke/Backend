package ca.usherbrooke.degel.exceptions

class FailedRegisterNUNotificationTypeException(id: String): ServerSideException("Failed to register notification: $id")

class FailedSendNUNotificationException(cip: String): ServerSideException("Failed to send notification to user: $cip")