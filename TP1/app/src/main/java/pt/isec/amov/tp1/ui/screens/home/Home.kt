package pt.isec.amov.tp1.ui.screens.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import pt.isec.amov.tp1.data.AppData

enum class BackgroundType{
    LOCATION,
    PLACE_OF_INTEREST,
}
@Composable
fun Home(
    appData: AppData,
    type: BackgroundType,
    navHostController: NavHostController?
) {

    when(type){
        BackgroundType.LOCATION-> LocationSearch( appData = appData, navHostController = navHostController)
        BackgroundType.PLACE_OF_INTEREST -> PlaceOfInterestSearch(appData = appData)
    }


}
@Preview
@Composable
fun HomePreview(){

}