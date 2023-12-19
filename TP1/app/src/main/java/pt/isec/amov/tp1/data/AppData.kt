package pt.isec.amov.tp1.data

import androidx.compose.runtime.mutableStateOf
import pt.isec.amov.tp1.ui.viewmodels.toUser
import pt.isec.amov.tp1.utils.firebase.FAuthUtil
import pt.isec.amov.tp1.utils.firebase.FStorageUtil
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
    val placesOfInterest: MutableList<PlaceOfInterest> = mutableListOf() //alterar para id
):Local(authorEmail,id,name,description,imagePath)

data class PlaceOfInterest(
    override val id: String,
    override val authorEmail: String,
    override val name: String,
    override val description: String,
    override val imagePath: String?,
    val categoryId: String
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
    val _user = mutableStateOf(FAuthUtil.currentUser?.toUser())
    private val locations = mutableListOf<Location>()
    val categories = mutableListOf<Category>()
    fun getLocations():List<Location>{
        return locations
    }
    fun getPlaceOfInterest(id:String): MutableList<PlaceOfInterest> {
        val location= locations.find { it.id == id } as Location
        return location.placesOfInterest
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
        if(locations.find {  it.name==name }!=null) return
        locations.add(
            Location(
                UUID.randomUUID().toString(),
                _user.value!!.email,
                name,
                description,
                imagePath)
        )
    }
    fun addPlaceOfInterest(
        name: String,
        description: String,
        imagePath: String?,
        category: Category,
        id:String
    ){
        val location = locations.find { it.id==id } as Location
        location.placesOfInterest.add(
            PlaceOfInterest(
                UUID.randomUUID().toString(),
                _user.value!!.email,
                name,
                description,
                imagePath,
                category.id
            )
        )
    }
    fun addCategory(
        name: String,
        description: String
    ){
        val c: Category = Category(UUID.randomUUID().toString(),_user.value!!.email,name,description)
        categories.add(c);
        FStorageUtil.addCategoryToFirestore(c, onResult = {})
    }

}
