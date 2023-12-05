package pt.isec.amov.tp1.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch
import pt.isec.amov.tp1.data.AppData
import pt.isec.amov.tp1.ui.viewmodels.location.LocalViewModel
import pt.isec.amov.tp1.utils.firebase.FAuthUtil
import pt.isec.amov.tp1.utils.firebase.FStorageUtil
import pt.isec.amov.tp1.utils.location.LocationHandler

data class User(val name: String, val email: String, val picture: String?)

fun FirebaseUser.toUser():User{
    val username = this.displayName?:""
    val strEmail = this.email?:""
    val photoUrl = this.photoUrl.toString()
    return User(username,strEmail,photoUrl)
}
class FireBaseViewModelFactory(
    private val appData: AppData
): ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass:Class<T>):T{
        return FireBaseViewModel(appData = appData) as T
    }
}
class FireBaseViewModel(private val appData: AppData): ViewModel() {

    private val _user = mutableStateOf(FAuthUtil.currentUser?.toUser())
    val user : MutableState<User?>
        get() = _user

    private val _error = mutableStateOf<String?>(null)
    val error : MutableState<String?>
        get() = _error

    fun createUserWithEmail(email:String, password:String){
        if(email.isBlank() || password.isBlank()) return
        viewModelScope.launch {
            FAuthUtil.createUserWithEmail(email, password) { exception ->
                if (exception == null) {//Success
                    _user.value = FAuthUtil.currentUser?.toUser()
                }
                _error.value = exception?.message
            }
        }
    }
    fun signInWithEmail(email:String, password:String){
        if(email.isBlank() || password.isBlank()) return
        viewModelScope.launch {
            FAuthUtil.signInWithEmail(email, password) { exception ->
                if (exception == null) {//Success
                    _user.value = FAuthUtil.currentUser?.toUser()
                }
                _error.value = exception?.message
            }
        }
    }
    fun signOut(){
        FAuthUtil.signOut()
        _user.value = null
        _error.value = null
    }
    fun addDataToFireStore(){
        viewModelScope.launch {
            FStorageUtil.addDataToFirestore { exception->
                _error.value = exception?.message
            }
        }
    }

    fun updateDataToFireStore() {
        viewModelScope.launch {
            FStorageUtil.updateDataInFirestoreTrans { exp ->
                _error.value = exp?.message
            }
        }
    }
    fun startOberver(){
        viewModelScope.launch {
            FStorageUtil.startObserver(){ games, topscores->
                Log.i("Teste","$games, $topscores")
            }
        }
    }

}