package pt.isec.amov.tp1.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
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

fun FirebaseUser.toUser(): User {
    val username = this.displayName?:""
    val strEmail = this.email?:""
    val photoUrl = this.photoUrl.toString()
    return User(username,strEmail,photoUrl)
}
class FireBaseViewModelFactory(
    private val appData: AppData
): ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass:Class<T>):T{
        return FireBaseViewModel(appData = appData) as T
    }
}
class FireBaseViewModel(private val appData: AppData): ViewModel() {
    val user : MutableState<User?>
        get() = appData.user

    private val _error = mutableStateOf<String?>(null)
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
    //Adds
    fun addCategoryToFireStore(category: Category){
        viewModelScope.launch {
            FStorageUtil.addCategoryToFirestore(category){exception->
                _error.value = exception?.message
            }
        }
    }
    fun addPlaceOfInterestToFireStore(placeOfInterest: PlaceOfInterest){
        viewModelScope.launch {
            FStorageUtil.addPlaceOfInterestToFirestore(placeOfInterest){exception->
                _error.value = exception?.message
            }
        }
    }
    fun addLocationToFireStore(location: Location){
        viewModelScope.launch {
            FStorageUtil.addLocationToFirestore(location){exception->
                _error.value = exception?.message
            }
        }
    }
    //Updates
    fun updateCategoryInFireStore(category: Category){
        viewModelScope.launch {
            FStorageUtil.updateCategoryInFirestore(category) { exp ->
                _error.value = exp?.message
            }
        }
    }
    fun updatePlaceOfInterestInFireStore(placeOfInterest: PlaceOfInterest){
        viewModelScope.launch {
            FStorageUtil.updatePlaceOfInterestInFirestore(placeOfInterest) { exp ->
                _error.value = exp?.message
            }
        }
    }
    fun updateLocationInFireStore(location: Location){
        viewModelScope.launch {
            FStorageUtil.updateLocationInFirestore(location) { exp ->
                _error.value = exp?.message
            }
        }
    }
    //Removes
    fun removeCategoryFromFireStore(category: Category){
        viewModelScope.launch {
            FStorageUtil.removeCategoryFromFireStone(category){ exp ->
                _error.value = exp?.message
            }
        }
    }
    fun removePlaceOfInterestFromFireStore(placeOfInterest: PlaceOfInterest){
        viewModelScope.launch {
            FStorageUtil.removePlaceOfInterestFromFireStone(placeOfInterest){ exp ->
                _error.value = exp?.message
            }
        }
    }
    fun removeLocationFromFireStore(location: Location){
        viewModelScope.launch {
            FStorageUtil.removeLocationFromFireStone(location){ exp ->
                _error.value = exp?.message
            }
        }
    }

    fun startCategoryObserver() {
        viewModelScope.launch {
            FStorageUtil.startCategoryObserver {categories->
                if(categories!=null) {
                    appData.categories.value = categories
                }
            }
        }
    }
    fun startLocationObserver() {
        viewModelScope.launch {
            FStorageUtil.startLocationObserver {locations->
                if(locations!=null) {
                   // appData.locations.value = locations
                }
            }
        }
    }
    fun startPlaceOfInterestObserver() {
        viewModelScope.launch {
            FStorageUtil.startPlacesOfInterestObserver {placesOfInterest->
                if(placesOfInterest!=null) {
                   // appData.placesOfInterest.value = placesOfInterest
                }
            }
        }
    }

    fun startObserver() {
        startCategoryObserver()
        startLocationObserver()
        startPlaceOfInterestObserver()
    }

}