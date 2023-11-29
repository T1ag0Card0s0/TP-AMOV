package pt.isec.amov.tp1.data

import androidx.compose.runtime.mutableIntStateOf
import pt.isec.amov.tp1.R
import pt.isec.amov.tp1.ui.viewmodels.ItemType

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
    val placesOfInterest: MutableList<PlaceOfInterest> = mutableListOf()
):Local(id,name,description,imagePath)
data class PlaceOfInterest(
    override val id: Int,
    override val name: String,
    override val description: String,
    override val imagePath: String?
):Local(id,name,description,imagePath)

class AppData{
    private val locations = mutableListOf<Location>()
    init{
        /*val tmp =Location(1,"Coimbra","Cidade dos estudantes",null)
        locations.add(tmp)
        tmp.placesOfInterest.add(PlaceOfInterest(1,"Cantina do ISEC","Melhor cantina de coimbra",null))*/
    }
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
        id:Int
    ){
        val location = locations.find { it.id==id } as Location
        location.placesOfInterest.add(
            PlaceOfInterest(
                location.placesOfInterest.size,
                name,
                description,
                imagePath
            )
        )
    }

}