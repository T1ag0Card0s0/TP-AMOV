package pt.isec.amov.tp1.ui.viewmodels.location


import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pt.isec.amov.tp1.utils.location.LocationHandler

class LocalViewModel(private val locationHandler: LocationHandler) : ViewModel() {
    // Permissions
    var coarseLocationPermission = false
    var fineLocationPermission = false
    var backgroundLocationPermission = false

    private val _currentLocation = MutableLiveData(Location(null))
    val currentLocation : LiveData<Location>
        get() = _currentLocation

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

    private fun stopLocationUpdates() {
        locationHandler.stopLocationUpdates()
    }

    override fun onCleared() {
        super.onCleared()
        stopLocationUpdates()
    }
}

