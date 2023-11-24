package pt.isec.a2021138999.tp1.ui.viewmodels

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pt.isec.a2021138999.tp1.data.AppData
import pt.isec.a2021138999.tp1.data.Local

class AppViewModelFactory(
    private val appData: AppData
): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AppViewModel(appData) as T
    }
}
open class AppViewModel(protected val appData: AppData): ViewModel() {
}