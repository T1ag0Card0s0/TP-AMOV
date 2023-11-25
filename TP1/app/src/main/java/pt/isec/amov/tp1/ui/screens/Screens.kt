package pt.isec.amov.tp1.ui.screens

enum class Screens (val display: String, val showAppBar: Boolean){
    LOGIN("Sign in",false),
    REGISTER("Sign up",false),
    CREDITS("Credits",true),
    LOCATION_SEARCH("Locations",true),
    PLACE_OF_INTEREST_SEARCH("Places of Interest",true),
    ADD_NEW_LOCATION("Add new location", true),
    ADD_NEW_PLACE_OF_INTEREST("Add new place of interest", true);
    val route : String
        get() = this.toString()
}