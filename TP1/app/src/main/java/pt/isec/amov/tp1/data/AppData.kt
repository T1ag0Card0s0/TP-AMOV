package pt.isec.amov.tp1.data

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import pt.isec.amov.tp1.ui.viewmodels.toUser
import pt.isec.amov.tp1.utils.firebase.FAuthUtil

open class Contribution(
    open val authorEmail: String
)

open class Local(
    open val id: String,
    open val name: String,
    override val authorEmail: String,
    open val description: String,
    open val imageName: String?,
    open val imageUri: String?,
    open val latitude: Double,
    open val longitude: Double,
    open var approved: Boolean = false,
    open var user1: String?,
    open var user2: String?
):Contribution(authorEmail){
    fun numberOfValidations():Int{
        var retValue = 0
        if(user1!=null) retValue++
        if(user2!=null) retValue++
        return retValue
    }

    fun assignValidation(email: String) {
        if(user1!=null&&user2!=null) return
        if(email == user1||email == user2) return
        if(user1.isNullOrBlank()) user1 = email
        else if(user2.isNullOrBlank()) user2 = email
    }
}


data class Location(
    override val id: String,
    override val authorEmail: String,
    override val name: String,
    override val description: String,
    override var imageName: String?,
    override var imageUri: String?,
    override val latitude: Double,
    override val longitude: Double,
    override var user1: String?,
    override var user2: String?
):Local(authorEmail,id,name,description,imageName,imageUri,latitude,longitude,false,user1,user2)

data class PlaceOfInterest(
    override val id: String,
    override val authorEmail: String,
    override val name: String,
    override val description: String,
    override var imageName: String?,
    val categoryId: String,
    val locationId: String,
    override var imageUri: String?,
    override val latitude: Double,
    override val longitude: Double,
    override var user1: String?,
    override var user2: String?
):Local(authorEmail,id,name,description,imageName,imageUri,latitude,longitude,false,user1,user2)

data class User(
    val userName: String,
    val email: String,
    val picture: String?
)

data class Category(
    val id: String,
    override val authorEmail: String,
    val name: String,
    val description: String,
    val imageVector: ImageVector?
):Contribution(authorEmail)

data class Classification(
    val id: String,
    override val authorEmail: String,
    val placeOfInterestId: String,
    var value: Int,
    var comment: String,
    var imageUri:String?,
    var imageName: String?
):Contribution(authorEmail)

class AppData{
    val user = mutableStateOf(FAuthUtil.currentUser?.toUser())
    private val _locations = MutableLiveData<List<Location>?>()
    private val _placesOfInterest = MutableLiveData<List<PlaceOfInterest>?>()
    private val _categories = MutableLiveData<List<Category>?>()
    private val _classifications = MutableLiveData<List<Classification>?>()
    val categories: LiveData<List<Category>?>
        get() = _categories
    val locations: LiveData<List<Location>?>
        get() = _locations
    val placesOfInterest: LiveData<List<PlaceOfInterest>?>
        get() = _placesOfInterest
    val classifications: LiveData<List<Classification>?>
        get() = _classifications

    fun setCategories(c: List<Category>){
        _categories.value = c
    }
    fun setLocations(l: List<Location>){
        _locations.value = l
    }
    fun setPlacesOfInterest(p: List<PlaceOfInterest>){
        _placesOfInterest.value = p
    }

    fun setClassifications(c: List<Classification>?) {
        _classifications.value = c
    }

}
