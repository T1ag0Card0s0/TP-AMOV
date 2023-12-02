package pt.isec.amov.tp1.ui.viewmodels.login

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import pt.isec.amov.tp1.data.AppData

class LoginViewModel(val appData: AppData): ViewModel(){
    val email: MutableState<String> = mutableStateOf("")
    val password: MutableState<String> = mutableStateOf("")
    fun login():Boolean{
        if(email.value.isEmpty()||password.value.isEmpty()) return false
        //TODO: Implementar login
        return true
    }
}