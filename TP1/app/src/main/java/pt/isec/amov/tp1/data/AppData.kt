package pt.isec.amov.tp1.data

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import pt.isec.amov.tp1.R
import pt.isec.amov.tp1.ui.viewmodels.ItemType
import pt.isec.amov.tp1.ui.viewmodels.toUser
import pt.isec.amov.tp1.utils.firebase.FAuthUtil

open class Local(
    open val id: Int,
    open val name: String,
    open val description: String,
    open val imagePath: String?
)
data class Location(
    override val id: Int,
    override val name: String,
    override val description: String,
    override val imagePath: String?,
    val placesOfInterest: MutableList<PlaceOfInterest> = mutableListOf() //alterar para id
):Local(id,name,description,imagePath)
data class PlaceOfInterest(
    override val id: Int,
    override val name: String,
    override val description: String,
    override val imagePath: String?,
    val category: Category//alterar para id
):Local(id,name,description,imagePath)
data class User(val name: String, val email: String, val picture: String?)
data class Category(val id: Int, val name: String,val description: String)
class AppData{
    val _user = mutableStateOf(FAuthUtil.currentUser?.toUser())
    private val locations = mutableListOf<Location>()
    val categories = listOf(
        Category(1,"Museus","") ,
        Category(2,"Monumentos&Locais de culto",""),
        Category(3,"Jardins",""),
        Category(4,"Miradouros",""),
        Category(5,"Restaurantes&Bares",""),
        Category(6,"Alojamento","")
    )
    fun getLocations():List<Location>{
        return locations
    }
    fun getPlaceOfInterest(id:Int): MutableList<PlaceOfInterest> {
        val location= locations.find { it.id == id } as Location
        return location.placesOfInterest
    }
    fun getSelectedLocalName(id:Int):String?{
        val local= locations.find { it.id == id }
        return local?.name
    }
    fun addLocation(
        name: String,
        description: String,
        imagePath: String?
    ){
        if(locations.find {  it.name==name }!=null) return
        locations.add(Location(locations.size,name,description,imagePath))
    }
    fun addPlaceOfInterest(
        name: String,
        description: String,
        imagePath: String?,
        category: Category,
        id:Int
    ){
        val location = locations.find { it.id==id } as Location
        location.placesOfInterest.add(
            PlaceOfInterest(
                location.placesOfInterest.size,
                name,
                description,
                imagePath,
                category
            )
        )
    }

}
