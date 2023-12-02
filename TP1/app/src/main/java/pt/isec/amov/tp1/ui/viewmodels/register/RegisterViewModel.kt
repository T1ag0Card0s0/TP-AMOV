package pt.isec.amov.tp1.ui.viewmodels.register

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import pt.isec.amov.tp1.data.AppData

class RegisterViewModel(val appData: AppData): ViewModel(){
    val name: MutableState<String> = mutableStateOf("")
    val email: MutableState<String> = mutableStateOf("")
    val password: MutableState<String> = mutableStateOf("")
    val confirmPassword: MutableState<String> = mutableStateOf("")
    fun register():Boolean{
        if(
            email.value.isEmpty()||
            name.value.isEmpty()||
            password.value.isEmpty()||
            confirmPassword.value.isEmpty()
            ) return false
        //TODO: Implementar registo
        return true
    }
}