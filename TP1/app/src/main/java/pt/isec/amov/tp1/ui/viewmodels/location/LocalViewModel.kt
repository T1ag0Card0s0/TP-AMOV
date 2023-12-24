package pt.isec.amov.tp1.ui.viewmodels.location


import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.osmdroid.util.GeoPoint
import pt.isec.amov.tp1.utils.location.LocationHandler

class LocalViewModel(private val locationHandler: LocationHandler) : ViewModel() {
    val POIs : MutableList<Coordinates>? = null
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

