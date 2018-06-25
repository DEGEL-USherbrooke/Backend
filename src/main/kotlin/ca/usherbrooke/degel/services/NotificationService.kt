package ca.usherbrooke.degel.services

import org.springframework.stereotype.Service

interface NotificationService {
    fun validateClient(title: String, description: String) : String
}

@Service
class NotificationServiceImpl(private val client: ExpoNotificationClient) : NotificationService {
    override fun validateClient(title: String, description: String) : String {

        val body = "{\n" +
                "  \"to\": \"ExponentPushToken[KjIqqZPDn4Hi0cPUzTqJfe]\",\n" +
                "  \"title\": \"" + title + "\",\n" +
                "  \"body\": \"" + description + "\"\n" +
                "}"

        return client.validateClient(body)
    }
}
