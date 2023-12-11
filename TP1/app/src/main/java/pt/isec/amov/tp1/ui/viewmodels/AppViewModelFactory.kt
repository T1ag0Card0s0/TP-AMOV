package pt.isec.amov.tp1.ui.viewmodels

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
    var searchForm: SearchForm?=null
    var addLocalForm: AddLocalForm?=null
    val selectedLocationId: MutableState<Int> = mutableIntStateOf(-1)
    fun addLocal():Boolean{
        if(addLocalForm==null) return false
        if( addLocalForm!!.name.value.isEmpty()||
            addLocalForm!!.descrition.value.isEmpty()||
            addLocalForm!!.imagePath.value.isEmpty()) return false
        when(addLocalForm!!.itemType){
            ItemType.LOCATION-> appData.addLocation(
                                                addLocalForm!!.name.value,
                                                addLocalForm!!.descrition.value,
                                                addLocalForm!!.imagePath.value
                                )
            ItemType.PLACE_OF_INTEREST->{
                if(addLocalForm!!.category.value==null) return false
                appData.addPlaceOfInterest(
                    addLocalForm!!.name.value,
                    addLocalForm!!.descrition.value,
                    addLocalForm!!.imagePath.value,
                    addLocalForm!!.category.value!!,
                    selectedLocationId.value

                )
            }
        }
        return true
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

    fun getCategories(): List<Category> {
        return appData.categories
    }
    fun filterByCategory(category: Category):List<PlaceOfInterest>{
        return appData.getPlaceOfInterest(selectedLocationId.value).filter { it.category==category }
    }
    fun orderBy(orderBy: String): List<Local>? {
        return null
    }
}
// Isto provavelmente tem de se tirar e juntar o var itemType e os valores que estão no addLocalForm todos no appViewModel
class SearchForm(var itemType: ItemType){

}
class AddLocalForm(val itemType: ItemType){
    val name: MutableState<String> = mutableStateOf("")
    val descrition: MutableState<String> = mutableStateOf("")
    val imagePath : MutableState<String> = mutableStateOf("")
    val category: MutableState<Category?> = mutableStateOf(null)
}
