package mx.tec.ecoquest_android.CurrentUserProgress

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import mx.tec.ecoquest_android.HttpRequest.getCurrentUserInformationForProgress

class CurrentUserViewModel: ViewModel() {
    private val _CurrentUserState = mutableStateOf<CurrentUser?>(null)
    val CurrentUserState: State<CurrentUser?> = _CurrentUserState

    fun fetchCurrentUserInformation(userId: String) {
        getCurrentUserInformationForProgress(userId) { success, user ->
            if (success && user != null) {
                _CurrentUserState.value = user
            }
            else {
                Log.e("Captura de usuario actual", "Ha ocurrido un error al capturar el usuario actual")
            }
        }
    }
}