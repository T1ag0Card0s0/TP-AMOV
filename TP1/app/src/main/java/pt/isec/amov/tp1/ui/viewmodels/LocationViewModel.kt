package pt.isec.amov.tp1.ui.viewmodels


import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pt.isec.amov.tp1.utils.location.LocationHandler

class LocationViewModelFactory(
    private val locationHandler: LocationHandler
):ViewModelProvider.Factory{
    override fun <T:ViewModel> create(modelClass:Class<T>):T{
        return LocationViewModel(locationHandler = locationHandler) as T
    }
}

class LocationViewModel(private val locationHandler: LocationHandler) : ViewModel() {
    val POIs = listOf(
        Coordinates("Liverpool",53.430819,-2.960828),
        Coordinates("Manchester City",53.482989,-2.200292),
        Coordinates("Manchester United",53.463056,-2.291389),
        Coordinates("Bayern Munich", 48.218775, 11.624753),
        Coordinates("Barcelona",41.38087,2.122802),
        Coordinates("Real Madrid",40.45306,-3.68835)
    )
    // Permissions
    var coarseLocationPermission = false
    var fineLocationPermission = false
    var backgroundLocationPermission = false

    private val _currentLocation = MutableLiveData(Location(null))
    val currentLocation : LiveData<Location>
        get() = _currentLocation


    private val locationEnabled : Boolean
        get() = locationHandler.locationEnabled

    init {
        locationHandler.onLocation = {location->
            _currentLocation.value = location
        }
    }

    fun startLocationUpdates() {
        if (fineLocationPermission && coarseLocationPermission) {
            locationHandler.startLocationUpdates()
        }

    }

    fun stopLocationUpdates() {
        locationHandler.stopLocationUpdates()
    }

    override fun onCleared() {
        super.onCleared()
        stopLocationUpdates()
    }
}
data class Coordinates(val team: String,val latitude : Double, val longitude: Double)

