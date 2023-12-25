package pt.isec.amov.tp1.ui.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch
import pt.isec.amov.tp1.data.AppData
import pt.isec.amov.tp1.data.Category
import pt.isec.amov.tp1.data.Location
import pt.isec.amov.tp1.data.PlaceOfInterest
import pt.isec.amov.tp1.data.User
import pt.isec.amov.tp1.utils.firebase.FAuthUtil
import pt.isec.amov.tp1.utils.firebase.FStorageUtil
import java.util.UUID

class AppViewModelFactory(
    private val appData: AppData
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AppViewModel(appData) as T
    }
}


class AppViewModel(val appData: AppData) : ViewModel() {
    var addLocalForm: AddLocalForm? = null
    val selectedLocation: MutableState<Location?> = mutableStateOf(null)
    val selecedPlaceOfInterest: MutableState<PlaceOfInterest?> = mutableStateOf(null)
    val isMyContributions = mutableStateOf(false)
    private val _error = mutableStateOf<String?>(null)
    val user : MutableState<User?>
        get() = appData.user
    val categories : LiveData<List<Category>?>
        get() = appData.categories
    val locations : LiveData<List<Location>?>
        get() = appData.locations
    val placesOfInterest : LiveData<List<PlaceOfInterest>?>
        get() = appData.placesOfInterest
    val error : MutableState<String?>
        get() = _error
    fun createUserWithEmail(email:String, password:String){
        if(email.isBlank() || password.isBlank()) return
        viewModelScope.launch {
            FAuthUtil.createUserWithEmail(email, password) { exception ->
                if (exception == null) {//Success
                    appData.user.value = FAuthUtil.currentUser?.toUser()
                }
                _error.value = exception?.message
            }
        }
    }
    fun signInWithEmail(email:String, password:String){
        if(email.isBlank() || password.isBlank()) return
        viewModelScope.launch {
            FAuthUtil.signInWithEmail(email, password) { exception ->
                if (exception == null) {//Success
                    appData.user.value = FAuthUtil.currentUser?.toUser()
                }
                _error.value = exception?.message
            }
        }
    }
    fun signOut(){
        FAuthUtil.signOut()
        appData.user.value = null
        _error.value = null
    }

    fun addCategory(name: String, description:String){
        viewModelScope.launch {
            FStorageUtil.addCategoryToFirestore(
                Category(
                    UUID.randomUUID().toString(),
                    user.value!!.email,
                    name,
                    description
                )
            ){exception->
                _error.value = exception?.message
            }
        }
    }
    fun addLocation(){
        if (addLocalForm == null) return
        if (addLocalForm!!.name.value.isEmpty() ||
            addLocalForm!!.descrition.value.isEmpty() ||
            addLocalForm!!.imagePath.value.isEmpty()
        ) return
        viewModelScope.launch {
            FStorageUtil.addLocationToFirestore(
                Location(
                    id = UUID.randomUUID().toString(),
                    authorEmail = appData.user.value!!.email,
                    name = addLocalForm!!.name.value,
                    description = addLocalForm!!.descrition.value,
                    imageName = addLocalForm!!.imagePath.value,
                    imageUri = null,
                    latitude = addLocalForm!!.latitude!!,
                    longitude = addLocalForm!!.longitude!!
                )
            ){exception->
                _error.value = exception?.message
            }
        }
    }
    fun addPlaceOfInterest(){
        if (addLocalForm == null) return
        if (addLocalForm!!.name.value.isEmpty() ||
            addLocalForm!!.descrition.value.isEmpty() ||
            addLocalForm!!.imagePath.value.isEmpty()
        ) return
        if (addLocalForm!!.category.value == null||
            addLocalForm!!.latitude==null||
            addLocalForm!!.longitude==null) return
        viewModelScope.launch {
            FStorageUtil.addPlaceOfInterestToFirestore(
                PlaceOfInterest(
                    id = UUID.randomUUID().toString(),
                    authorEmail = appData.user.value!!.email,
                    name = addLocalForm!!.name.value,
                    description = addLocalForm!!.descrition.value,
                    imageName =  addLocalForm!!.imagePath.value,
                    categoryId = addLocalForm!!.category.value!!.id,
                    locationId = selectedLocation.value!!.id,
                    imageUri = null,
                    latitude = addLocalForm!!.latitude!!,
                    longitude = addLocalForm!!.longitude!!
                )
            ){exception->
                _error.value = exception?.message
            }
        }
    }
    fun updateLocation(){

    }
    fun updatePlaceOfInterest(){

    }
    fun updateCategory(){

    }
    fun removeLocation(l: Location){
        if(placesOfInterest.value!!.find { it.locationId==l.id }!=null) return
        viewModelScope.launch {
            FStorageUtil.removeLocationFromFireStone(l){ exp ->
                _error.value = exp?.message
            }
        }
    }
    fun removePlaceOfInterest(poi: PlaceOfInterest){
        viewModelScope.launch {
            FStorageUtil.removePlaceOfInterestFromFireStone(poi){ exp ->
                _error.value = exp?.message
            }
        }
    }
    fun removeCategory(c: Category){
        viewModelScope.launch {
            FStorageUtil.removeCategoryFromFireStone(c){ exp ->
                _error.value = exp?.message
            }
        }
    }
    fun startObserver(){
        //Categories
        viewModelScope.launch {
            FStorageUtil.startCategoryObserver {categories->
                appData.setCategories(categories!!)
            }
        }
        //Locations
        viewModelScope.launch {
            FStorageUtil.startLocationObserver {locations->
                appData.setLocations(locations!!)
            }
        }
        //PlacesOfInterest
        viewModelScope.launch {
            FStorageUtil.startPlacesOfInterestObserver { placesOfInterest->
                appData.setPlacesOfInterest(placesOfInterest!!)
            }
        }
    }

    fun getCategoryById(categoryId: String): Category? {
        if(appData.categories.value==null) return null
        return appData.categories.value!!.find { it.id == categoryId }
    }

}

class AddLocalForm {
    val name: MutableState<String> = mutableStateOf("")
    val descrition: MutableState<String> = mutableStateOf("")
    val imagePath: MutableState<String> = mutableStateOf("")
    val category: MutableState<Category?> = mutableStateOf(null)
    var latitude : Double? = null
    var longitude : Double? = null
}

fun FirebaseUser.toUser(): User {
    val username = this.displayName?:""
    val strEmail = this.email?:""
    val photoUrl = this.photoUrl.toString()
    return User(username, strEmail, photoUrl)
}