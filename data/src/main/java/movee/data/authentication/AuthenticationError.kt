package movee.data.authentication

enum class AuthenticationError(val statusCode: Int, val statusMessage: String) {
    RESOURCE_NOT_FOUND(34, "The resource you requested could not be found.")
}