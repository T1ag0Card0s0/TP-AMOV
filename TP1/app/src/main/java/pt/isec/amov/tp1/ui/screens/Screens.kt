package pt.isec.amov.tp1.ui.screens

enum class Screens (val display: String, val showAppBar: Boolean){
    LOGIN("Sign in",false),
    REGISTER("Sign up",false),
    CREDITS("Credits",true),
    SEARCH_LOCATIONS("Search Locations", true),
    SEARCH_PLACES_OF_INTEREST("Search Places Of Interest", true),
    ADD_LOCATIONS("Add Location", true),
    ADD_PLACE_OF_INTEREST("Add Place Of Interest",true),
    DETAILS("Details",true),
    CHOOSE_COORDINATES("Choose Coordinates",true);
    val route : String
        get() = this.toString()
}