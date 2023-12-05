package pt.isec.amov.tp1.ui.viewmodels.location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pt.isec.amov.tp1.utils.location.LocationHandler

class LocalViewModelFactory(
    private val locationHandler: LocationHandler
): ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass:Class<T>):T{
        return LocalViewModel(locationHandler = locationHandler) as T
    }
}