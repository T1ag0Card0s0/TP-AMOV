package pt.isec.a2021138999.tp1.data

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf

open class Local(
    open val id: Int,
    open val name: String,
    open val description: String
)
data class Location(
    override val id: Int,
    override val name: String,
    override val description: String,
    val placesOfInterest: MutableList<PlaceOfInterest> = mutableListOf()
):Local(id,name,description)
data class PlaceOfInterest(
    override val id: Int,
    override val name: String,
    override val description: String
):Local(id,name,description)

class AppData{
    private val locations = mutableListOf<Local>()
    val selectedLocal = mutableIntStateOf(-1)
    init{
        for (i in 1..20) {
            val tmp =Location(i,"Coimbra","Cidade dos estudantes")
            locations.add(tmp)
            for(j in 1..20)
                tmp.placesOfInterest.add(PlaceOfInterest(j,"Cantina do ISEC","Melhor cantina de coimbra"))
        }
    }
    fun getLocations():List<Local>{
        return locations
    }
    fun getPlaceOfInterest():List<Local>{
        val location= locations.find { it.id == selectedLocal.intValue } as Location
        return location.placesOfInterest
    }
    fun getSelectedLocalName():String?{
        val local= locations.find { it.id == selectedLocal.intValue }
        return local?.name
    }

}