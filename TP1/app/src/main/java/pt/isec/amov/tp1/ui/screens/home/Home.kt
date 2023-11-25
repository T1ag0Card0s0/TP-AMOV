package pt.isec.amov.tp1.ui.screens.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import pt.isec.amov.tp1.data.AppData
import pt.isec.amov.tp1.ui.viewmodels.AppViewModel

enum class BackgroundType{
    LOCATION,
    PLACE_OF_INTEREST,
}
@Composable
fun Home(
    appViewModel: AppViewModel,
    type: BackgroundType,
    navHostController: NavHostController?
) {

    when(type){
        BackgroundType.LOCATION-> LocationSearch( appViewModel = appViewModel, navHostController = navHostController)
        BackgroundType.PLACE_OF_INTEREST -> PlaceOfInterestSearch(appViewModel = appViewModel)
    }


}
@Preview
@Composable
fun HomePreview(){

}