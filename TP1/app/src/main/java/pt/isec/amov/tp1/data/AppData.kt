package pt.isec.amov.tp1.data

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import pt.isec.amov.tp1.ui.viewmodels.toUser
import pt.isec.amov.tp1.utils.firebase.FAuthUtil
import pt.isec.amov.tp1.utils.firebase.FStorageUtil
import java.io.File
import java.io.FileInputStream
import java.util.UUID

open class Contribution(
    open val authorEmail: String
)

open class Local(
    open val id: String,
    open val name: String,
    override val authorEmail: String,
    open val description: String,
    open val imagePath: String?
):Contribution(authorEmail)

data class Location(
    override val id: String,
    override val authorEmail: String,
    override val name: String,
    override val description: String,
    override val imagePath: String?,
):Local(authorEmail,id,name,description,imagePath)

data class PlaceOfInterest(
    override val id: String,
    override val authorEmail: String,
    override val name: String,
    override val description: String,
    override val imagePath: String?,
    val categoryId: String,
    val locationId: String
):Local(authorEmail,id,name,description,imagePath)

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
):Contribution(authorEmail)

class AppData{
    val user = mutableStateOf(FAuthUtil.currentUser?.toUser())
    private val _locations = MutableLiveData<List<Location>?>()
    private val _placesOfInterest = MutableLiveData<List<PlaceOfInterest>?>()
    private val _categories = MutableLiveData<List<Category>?>()
    val categories: LiveData<List<Category>?>
        get() = _categories
    val locations: LiveData<List<Location>?>
        get() = _locations
    val placesOfInterest: LiveData<List<PlaceOfInterest>?>
        get() = _placesOfInterest

    fun setCategories(c: List<Category>){
        _categories.value = c
    }
    fun setLocations(l: List<Location>){
        _locations.value = l
    }
    fun setPlacesOfInterest(p: List<PlaceOfInterest>){
        _placesOfInterest.value = p
    }

}
