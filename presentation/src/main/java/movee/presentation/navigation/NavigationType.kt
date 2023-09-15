package movee.presentation.navigation

sealed class NavigationType {

    class ToRoute(
        private val route: String,
        private val args: List<Pair<String, Any>> = emptyList()
    ) : NavigationType() {

        fun getRoute(): String {
            var routeWithArgs = route

            args.forEach { (key, value) ->
                routeWithArgs = routeWithArgs.replace("{$key}", value.toString())
            }

            return routeWithArgs
        }
    }

    class Up(val route: String? = null, val isInclusive: Boolean = false) : NavigationType()

    class Back() : NavigationType()

    class ToTab(val route: String) : NavigationType()
}