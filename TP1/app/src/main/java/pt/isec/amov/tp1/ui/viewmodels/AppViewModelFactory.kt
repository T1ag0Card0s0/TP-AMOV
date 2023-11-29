package pt.isec.amov.tp1.ui.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pt.isec.amov.tp1.data.AppData
import pt.isec.amov.tp1.data.Location
import pt.isec.amov.tp1.data.PlaceOfInterest

enum class ItemType{
    LOCATION,
    PLACE_OF_INTEREST
}
class AppViewModelFactory(
    val appData: AppData
): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AppViewModel(appData) as T
    }
}
class AppViewModel(val appData: AppData): ViewModel() {
    var loginForm: LoginForm? = null
    var searchForm: SearchForm?=null
    var registerForm: RegisterForm?=null
    var addLocalForm: AddLocalForm?=null
    val selectedLocationId: MutableState<Int> = mutableIntStateOf(-1)
    fun addLocal(){
        when(addLocalForm!!.itemType){
            ItemType.LOCATION-> appData.addLocation(addLocalForm!!.name.value,
                addLocalForm!!.descrition.value, addLocalForm!!.imagePath.value)
            ItemType.PLACE_OF_INTEREST-> appData.addPlaceOfInterest(addLocalForm!!.name.value,
                addLocalForm!!.descrition.value, addLocalForm!!.imagePath.value,selectedLocationId.value)
        }
    }
    fun getLocations(): List<Location>{
        return appData.getLocations()
    }
    fun getSelectedLocalName(): String? {
        return appData.getSelectedLocalName(selectedLocationId.value)
    }
    fun getPlaceOfInterest():List<PlaceOfInterest>{
        return appData.getPlaceOfInterest(selectedLocationId.value)
    }

    fun getCategories(): List<String> {
        return listOf(
            "Museus",
            "Monumentos&Locais de culto",
            "Jardins",
            "Miradouros",
            "Restaurantes&Bares",
            "Alojamento"
        )
    }
}
class LoginForm{
    val email: MutableState<String> = mutableStateOf("")
    val password: MutableState<String> = mutableStateOf("")
}

class SearchForm(var itemType: ItemType){
    val name: MutableState<String> = mutableStateOf("")
    val orderBy:MutableState<String> = mutableStateOf("")
}
class RegisterForm{
    val name: MutableState<String> = mutableStateOf("")
    val email: MutableState<String> = mutableStateOf("")
    val password: MutableState<String> = mutableStateOf("")
    val confirmPassword: MutableState<String> = mutableStateOf("")
}
class AddLocalForm(val itemType: ItemType){
    val name: MutableState<String> = mutableStateOf("")
    val descrition: MutableState<String> = mutableStateOf("")
    val imagePath : MutableState<String?> = mutableStateOf(null)
}