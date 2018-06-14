package ca.usherbrooke.degel.exceptions

open class DegelException : Exception {
    val error: String

    constructor(error: String, message: String) : super(message) {
        this.error = error
    }
    constructor(error: String, cause: Throwable) : super(cause) {
        this.error = error
    }
    constructor(error: String, message: String, cause: Throwable) : super(message, cause) {
        this.error = error
    }
}

open class ServerSideException: DegelException {
    constructor(message: String) : super(SERVER_SIDE_EXCEPTION, message)
    constructor(cause: Throwable) : super(SERVER_SIDE_EXCEPTION, cause)
    constructor(message: String, cause: Throwable) : super(SERVER_SIDE_EXCEPTION, message, cause)

    companion object {
        const val SERVER_SIDE_EXCEPTION: String = "server_side_exception"
    }
}

open class ClientSideException: DegelException {
    constructor(message: String) : super(CLIENT_SIDE_EXCEPTION, message)
    constructor(cause: Throwable) : super(CLIENT_SIDE_EXCEPTION, cause)
    constructor(message: String, cause: Throwable) : super(CLIENT_SIDE_EXCEPTION, message, cause)

    companion object {
        const val CLIENT_SIDE_EXCEPTION: String = "client_side_exception"
    }
}