package pt.isec.amov.tp1.ui.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pt.isec.amov.tp1.data.AppData
import pt.isec.amov.tp1.data.Category
import pt.isec.amov.tp1.data.Local
import pt.isec.amov.tp1.data.Location
import pt.isec.amov.tp1.data.PlaceOfInterest
import java.util.UUID

class AppViewModelFactory(
    val appData: AppData
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AppViewModel(appData) as T
    }
}


class AppViewModel(val appData: AppData) : ViewModel() {
    var addLocalForm: AddLocalForm? = null
    val selectedLocation: MutableState<Location?> = mutableStateOf(null)
    val selecedPlaceOfInterest: MutableState<PlaceOfInterest?> = mutableStateOf(null)

    fun buildLocationFromAddForm(): Location? {
        if (addLocalForm == null) return null
        if (addLocalForm!!.name.value.isEmpty() ||
            addLocalForm!!.descrition.value.isEmpty() ||
            addLocalForm!!.imagePath.value.isEmpty()
        ) return null

        return Location(
            UUID.randomUUID().toString(),
            appData.user.value!!.email,
            addLocalForm!!.name.value,
            addLocalForm!!.descrition.value,
            addLocalForm!!.imagePath.value
        )
    }
    fun buildPlaceOfInterestFromAddForm(): PlaceOfInterest?{
        if (addLocalForm == null) return null
        if (addLocalForm!!.name.value.isEmpty() ||
            addLocalForm!!.descrition.value.isEmpty() ||
            addLocalForm!!.imagePath.value.isEmpty()
        ) return null
        if (addLocalForm!!.category.value == null) return null
        return PlaceOfInterest(
            UUID.randomUUID().toString(),
            appData.user.value!!.email,
            addLocalForm!!.name.value,
            addLocalForm!!.descrition.value,
            addLocalForm!!.imagePath.value,
            addLocalForm!!.category.value!!.id,
            selectedLocation.value!!.id
        )
    }

    fun getLocations(): List<Location> {
        return appData.getLocations()
    }

    fun getCategories(): LiveData<List<Category>> {
        return appData.categories
    }
    fun addCategory(name: String, description:String){
        appData.addCategory(name,description)
    }

    fun getMyLocations(): List<Local> {
        return appData.getMyLocations()
    }

    fun getMyPlacesOfInterest(): List<Local> {
        return appData.getMyPlacesOfInterest()
    }
    fun getMyCategories(): List<Category>{
        return appData.getMyCategories()
    }

    fun getPlacesOfInterest(): List<PlaceOfInterest> {
        return appData.getPlaceOfInterest(selectedLocation.value!!.id)
    }
}

class AddLocalForm {
    val name: MutableState<String> = mutableStateOf("")
    val descrition: MutableState<String> = mutableStateOf("")
    val imagePath: MutableState<String> = mutableStateOf("")
    val category: MutableState<Category?> = mutableStateOf(null)
}
