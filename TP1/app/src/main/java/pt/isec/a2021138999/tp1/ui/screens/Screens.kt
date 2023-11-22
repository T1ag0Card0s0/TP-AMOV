package pt.isec.a2021138999.tp1.ui.screens

enum class Screens (val display: String, val showAppBar: Boolean){
    LOGIN("Sign in",false),
    REGISTER("Sign up",false),
    CREDITS("Credits",true),
    LOCATION_SEARCH("Location Search",false),
    PLACE_OF_INTEREST_SEARCH("Place of Interest Search",true);
    val route : String
        get() = this.toString()
}