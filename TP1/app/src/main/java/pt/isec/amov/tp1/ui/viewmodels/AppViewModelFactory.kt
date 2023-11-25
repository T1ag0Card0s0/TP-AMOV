package pt.isec.amov.tp1.ui.viewmodels

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pt.isec.amov.tp1.data.AppData
import pt.isec.amov.tp1.data.Local

class AppViewModelFactory(
    val appData: AppData
): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AppViewModel(appData) as T
    }
}
open class AppViewModel(val appData: AppData): ViewModel() {
    val pageTite = mutableStateOf("")
    val orderByOpt = mutableStateOf("")
    val search = mutableStateOf("")
}
