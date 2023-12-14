package pt.isec.amov.tp1.ui.viewmodels

import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pt.isec.amov.tp1.data.AppData
import pt.isec.amov.tp1.data.Category
import pt.isec.amov.tp1.data.Local
import pt.isec.amov.tp1.data.Location
import pt.isec.amov.tp1.data.PlaceOfInterest

enum class ItemType {
    LOCATION,
    PLACE_OF_INTEREST
}

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
    fun addLocal(itemType: ItemType): Boolean {
        if (addLocalForm == null) return false
        if (addLocalForm!!.name.value.isEmpty() ||
            addLocalForm!!.descrition.value.isEmpty() ||
            addLocalForm!!.imagePath.value.isEmpty()
        ) return false
        when (itemType) {
            ItemType.LOCATION -> {
                appData.addLocation(
                    addLocalForm!!.name.value,
                    addLocalForm!!.descrition.value,
                    addLocalForm!!.imagePath.value
                )
            }

            ItemType.PLACE_OF_INTEREST -> {
                if (addLocalForm!!.category.value == null) return false
                appData.addPlaceOfInterest(
                    addLocalForm!!.name.value,
                    addLocalForm!!.descrition.value,
                    addLocalForm!!.imagePath.value,
                    addLocalForm!!.category.value!!,
                    selectedLocation.value!!.id
                )
            }
        }
        return true
    }

    fun getLocations(): List<Location> {
        return appData.getLocations()
    }

    fun getCategories(): List<Category> {
        return appData.categories
    }
}

class AddLocalForm {
    val name: MutableState<String> = mutableStateOf("")
    val descrition: MutableState<String> = mutableStateOf("")
    val imagePath: MutableState<String> = mutableStateOf("")
    val category: MutableState<Category?> = mutableStateOf(null)
}
