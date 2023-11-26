package pt.isec.amov.tp1.ui.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pt.isec.amov.tp1.data.AppData
import pt.isec.amov.tp1.ui.screens.Screens
import pt.isec.amov.tp1.ui.screens.home.BackgroundType

class AppViewModelFactory(
    val appData: AppData
): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AppViewModel(appData) as T
    }
}
open class AppViewModel(val appData: AppData): ViewModel() {
    val orderByOpt = mutableStateOf("")
    val search = mutableStateOf("")
    val imagePath : MutableState<String?> = mutableStateOf(null)
    val name: MutableState<String> = mutableStateOf("")
    val desc: MutableState<String> = mutableStateOf("")
    val typeOfDataToAdd: MutableState<String> = mutableStateOf("")
    fun addData(){
        when(typeOfDataToAdd.value){
            Screens.ADD_NEW_LOCATION.route->appData.addLocation(name.value,desc.value, imagePath.value )
            Screens.ADD_NEW_PLACE_OF_INTEREST.route->appData.addPlaceOfInterest(name.value,desc.value, imagePath.value )
        }

    }
}
