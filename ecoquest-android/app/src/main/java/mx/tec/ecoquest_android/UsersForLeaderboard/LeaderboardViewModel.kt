package mx.tec.ecoquest_android.UsersForLeaderboard

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import mx.tec.ecoquest_android.HttpRequest.getAllUsersForLeaderboard

class LeaderboardViewModel: ViewModel() {
    private val _leaderboardState = mutableStateOf<LeaderboardResponse?>(null)
    val leaderboardState: State<LeaderboardResponse?> = _leaderboardState

    // Obtener todos los usuarios disponibles
    fun fetchAllUserForLeaderboard() {
        getAllUsersForLeaderboard { success, users ->
            if (success && users != null) {
                _leaderboardState.value = users
            }
            else {
                Log.e("Captura de usuarios", "Ha ocurrdio un error al capturar los usuarios")
            }
        }
    }
}