package pt.isec.amov.tp1.ui.screens

enum class Screens(val display: String, val showAppBar: Boolean) {
    LOGIN("Sign in", true),
    REGISTER("Sign up", true),
    CREDITS("Credits", true),
    SEARCH_LOCATIONS("Search Locations", true),
    SEARCH_PLACES_OF_INTEREST("Search Places Of Interest", true),
    ADD_LOCATIONS("Add Location", true),
    ADD_PLACE_OF_INTEREST("Add Place Of Interest", true),
    LOCATION_DETAILS("Location Details", true),
    PLACE_OF_INTEREST_DETAILS("Place of Interest Details", true),
    CHOOSE_LOCATION_COORDINATES("Choose Location Coordinates", true),
    CHOOSE_PLACE_OF_INTEREST_COORDINATES("Choose Place Of Interest Coordinates", true);

    val route: String
        get() = this.toString()
}