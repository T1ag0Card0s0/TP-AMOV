package pt.isec.amov.tp1.data

import androidx.compose.runtime.mutableStateOf
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
    private val locations = mutableListOf<Location>()
    private val placesOfInterest = mutableListOf<PlaceOfInterest>()
    val categories = mutableListOf<Category>()
    fun getLocations():List<Location>{
        return locations
    }
    fun getPlaceOfInterest(id:String): List<PlaceOfInterest> {
        return placesOfInterest.filter { it.locationId == id }
    }
    fun getSelectedLocalName(id:String):String?{
        val local= locations.find { it.id == id }
        return local?.name
    }
    fun addLocation(
        name: String,
        description: String,
        imagePath: String?
    ){
        val l = Location(
            UUID.randomUUID().toString(),
            user.value!!.email,
            name,
            description,
            imagePath)

        locations.add(l)
        if (imagePath!!.isNotEmpty()) {
            val file = File(imagePath)
            val inputStream = FileInputStream(file)
            FStorageUtil.uploadFile(inputStream, file.name)
        }
        FStorageUtil.addOrUpdateLocationToFirestore(l, onResult = {})
    }
    fun addPlaceOfInterest(
        name: String,
        description: String,
        imagePath: String?,
        category: Category,
        locationId: String
    ){
        val p =  PlaceOfInterest(
            UUID.randomUUID().toString(),
            user.value!!.email,
            name,
            description,
            imagePath,
            category.id,
            locationId
        )
        placesOfInterest.add(p)
        FStorageUtil.addOrUpdatePlaceOfInterestToFirestore(p, onResult = {})
    }
    fun addCategory(
        name: String,
        description: String
    ){
        val c: Category = Category(UUID.randomUUID().toString(),user.value!!.email,name,description)
        categories.add(c);
        FStorageUtil.addOrUpdateCategories(c, onResult = {})
    }

    fun getMyLocations(): List<Local> {
        return locations.filter { it.authorEmail == user.value!!.email }
    }

    fun getMyPlacesOfInterest(): List<Local> {
        return placesOfInterest.filter { it.authorEmail == user.value!!.email }
    }

    fun getMyCategories(): List<Category> {
        return categories.filter { it.authorEmail == user.value!!.email }
    }

}
