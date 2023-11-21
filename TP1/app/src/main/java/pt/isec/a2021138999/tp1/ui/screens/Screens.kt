package pt.isec.a2021138999.tp1.ui.screens

enum class Screens (val display: String, val showAppBar: Boolean){
    LOGIN("Sign in",false),
    REGISTER("Sign up",false),
    CREDITS("Credits",true),
    HOME("Home",false);
    val route : String
        get() = this.toString()
}