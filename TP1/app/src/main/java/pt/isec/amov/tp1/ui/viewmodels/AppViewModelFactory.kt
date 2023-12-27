package pt.isec.amov.tp1.ui.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint
import pt.isec.amov.tp1.data.AppData
import pt.isec.amov.tp1.data.Category
import pt.isec.amov.tp1.data.Classification
import pt.isec.amov.tp1.data.Location
import pt.isec.amov.tp1.data.PlaceOfInterest
import pt.isec.amov.tp1.data.User
import pt.isec.amov.tp1.utils.firebase.FAuthUtil
import pt.isec.amov.tp1.utils.firebase.FStorageAdd
import pt.isec.amov.tp1.utils.firebase.FStorageObserver
import pt.isec.amov.tp1.utils.firebase.FStorageOrder
import pt.isec.amov.tp1.utils.firebase.FStorageRemove
import pt.isec.amov.tp1.utils.firebase.FStorageUpdate
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
    var evaluateForm: EvaluateForm? = null
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
    val classifications: LiveData<List<Classification>?>
        get() = appData.classifications
    val error : MutableState<String?>
        get() = _error
    val selecetLocationGeoPoint: GeoPoint?
        get() = if(selectedLocation.value==null) null
                else GeoPoint(
                    selectedLocation.value!!.latitude,
                    selectedLocation.value!!.longitude
                )
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

    fun addCategory(name: String, description:String, imageVector: ImageVector){
        viewModelScope.launch {
            FStorageAdd.category(
                Category(
                    UUID.randomUUID().toString(),
                    user.value!!.email,
                    name,
                    description,
                    imageVector
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
            addLocalForm!!.imagePath.value.isEmpty()||
            addLocalForm!!.latitude.value == null||
            addLocalForm!!.longitude.value == null
        ) return
        viewModelScope.launch {
            FStorageAdd.location(
                Location(
                    id = UUID.randomUUID().toString(),
                    authorEmail = appData.user.value!!.email,
                    name = addLocalForm!!.name.value,
                    description = addLocalForm!!.descrition.value,
                    imageName = addLocalForm!!.imagePath.value,
                    imageUri = null,
                    latitude = addLocalForm!!.latitude.value!!,
                    longitude = addLocalForm!!.longitude.value!!,
                    null,null
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
            addLocalForm!!.latitude.value==null||
            addLocalForm!!.longitude.value==null) return
        viewModelScope.launch {
            FStorageAdd.placeOfInterest(
                PlaceOfInterest(
                    id = UUID.randomUUID().toString(),
                    authorEmail = appData.user.value!!.email,
                    name = addLocalForm!!.name.value,
                    description = addLocalForm!!.descrition.value,
                    imageName =  addLocalForm!!.imagePath.value,
                    categoryId = addLocalForm!!.category.value!!.id,
                    locationId = selectedLocation.value!!.id,
                    imageUri = null,
                    latitude = addLocalForm!!.latitude.value!!,
                    longitude = addLocalForm!!.longitude.value!!,
                    null,null
                )
            ){exception->
                _error.value = exception?.message
            }
        }
    }
    fun addClassification(){
        if(evaluateForm==null) return
        viewModelScope.launch {
            FStorageAdd.classification(
                Classification(
                    id = UUID.randomUUID().toString(),
                    authorEmail = user.value!!.email,
                    placeOfInterestId = selecedPlaceOfInterest.value!!.id,
                    value = evaluateForm!!.value.value,
                    comment = evaluateForm!!.comment.value,
                    imageUri = null,
                    imageName = evaluateForm!!.imagePath.value
                )
            ){ exp->
                _error.value = exp?.message
            }
        }
    }
    fun updateLocation(l: Location){
        viewModelScope.launch {
            FStorageUpdate.location(l){exp->
                _error.value = exp?.message
            }
        }
    }
    fun updatePlaceOfInterest(){

    }
    fun updateCategory(){

    }
    fun removeLocation(l: Location){
        if(placesOfInterest.value!!.find { it.locationId==l.id }!=null) return
        viewModelScope.launch {
            FStorageRemove.location(l){ exp ->
                _error.value = exp?.message
            }
        }
    }
    fun removePlaceOfInterest(poi: PlaceOfInterest){
        viewModelScope.launch {
            FStorageRemove.placeOfInterest(poi){ exp ->
                _error.value = exp?.message
            }
        }
    }
    fun removeCategory(c: Category){
        viewModelScope.launch {
            FStorageRemove.category(c){ exp ->
                _error.value = exp?.message
            }
        }
    }
    fun removeClassification(c: Classification){
        viewModelScope.launch {
            FStorageRemove.classification(c){ exp ->
                _error.value = exp?.message
            }
        }
    }
    fun startAllObservers(){
        stopAllObservers()
        startCategoriesObserver()
        startLocationsObserver()
        startPlacesOfInterestObserver()
        startClassificationsObserver()
    }
    fun startCategoriesObserver(){
        viewModelScope.launch {
            FStorageObserver.observeCategory {categories->
                appData.setCategories(categories!!)
            }
        }
    }
    fun startLocationsObserver(){
        viewModelScope.launch {
            FStorageObserver.observeLocation {locations->
                appData.setLocations(locations!!)
            }
        }
    }
    fun startPlacesOfInterestObserver(){
        viewModelScope.launch {
            FStorageObserver.observePlaceOfInterest { placesOfInterest->
                appData.setPlacesOfInterest(placesOfInterest!!)
            }
        }
    }
    fun startClassificationsObserver(){
        viewModelScope.launch {
            FStorageObserver.observeClassifications { classifications ->
                appData.setClassifications(classifications)
            }
        }
    }
    fun stopAllObservers() {
        viewModelScope.launch {
            FStorageObserver.stopAllObservers()
        }
    }
    fun getCategoryById(categoryId: String): Category? {
        if(appData.categories.value==null) return null
        return appData.categories.value!!.find { it.id == categoryId }
    }

    fun locationsOrderByAlphabetically(ascendent: Boolean) {
        if(ascendent) {
            viewModelScope.launch {
                FStorageOrder.locationByNameAscending { locations ->
                    appData.setLocations(locations!!)
                }
            }
        }else{
            viewModelScope.launch {
                FStorageOrder.locationByNameDescending { locations ->
                    appData.setLocations(locations!!)
                }
            }
        }

    }
    fun placesOfInterestOrderByAlphabetically(ascendent: Boolean){
        if(ascendent) {
            viewModelScope.launch {
                FStorageOrder.placeOfInterestByNameAscending { placesOfInterest ->
                    appData.setPlacesOfInterest(placesOfInterest!!)
                }
            }
        }else{
            viewModelScope.launch {
                FStorageOrder.placeOfInterestByNameDescending { placesOfInterest ->
                    appData.setPlacesOfInterest(placesOfInterest!!)
                }
            }
        }
    }
    fun locationsOrderByDistance(latitude:Double,longitude:Double ,ascendent: Boolean){
        if(ascendent) {
            viewModelScope.launch {
                FStorageOrder.locationsByDistanceAscending(latitude,longitude) { locations ->
                    appData.setLocations(locations!!)
                }
            }
        }else{
            viewModelScope.launch {
                FStorageOrder.locationsByDistanceDescending(latitude,longitude) { locations ->
                    appData.setLocations(locations!!)
                }
            }
        }
    }
    fun placesOfInterestOrderByDistance(latitude:Double,longitude:Double ,ascendent: Boolean){
        if(ascendent) {
            viewModelScope.launch {
                FStorageOrder.placesOfInterestByDistanceAscending(latitude,longitude) { placesOfInterest ->
                    appData.setPlacesOfInterest(placesOfInterest!!)
                }
            }
        }else{
            viewModelScope.launch {
                FStorageOrder.placesOfInterestByDistanceDescending(latitude,longitude) { placesOfInterest ->
                    appData.setPlacesOfInterest(placesOfInterest!!)
                }
            }
        }
    }


}

class AddLocalForm {
    val name: MutableState<String> = mutableStateOf("")
    val descrition: MutableState<String> = mutableStateOf("")
    val imagePath: MutableState<String> = mutableStateOf("")
    val category: MutableState<Category?> = mutableStateOf(null)
    var latitude : MutableState<Double?> = mutableStateOf(null)
    var longitude : MutableState<Double?> = mutableStateOf(null)
}
class EvaluateForm{
    val comment: MutableState<String> = mutableStateOf("")
    val imagePath: MutableState<String> = mutableStateOf("")
    val value: MutableState<Int> = mutableIntStateOf(0)
}
fun FirebaseUser.toUser(): User {
    val username = this.displayName?:""
    val strEmail = this.email?:""
    val photoUrl = this.photoUrl.toString()
    return User(username, strEmail, photoUrl)
}