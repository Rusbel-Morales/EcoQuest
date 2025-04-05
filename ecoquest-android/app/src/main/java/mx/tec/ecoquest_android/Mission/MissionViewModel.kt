package mx.tec.ecoquest_android.Mission

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import mx.tec.ecoquest_android.HttpRequest.getDailyMissionFromServer

class MissionViewModel : ViewModel() {
    private val _missionState = mutableStateOf<MissionResponse?>(null)
    val missionState: State<MissionResponse?> = _missionState

    // Obtener la misión desde el servidor
    fun fetchDailyMission(userId: String?) {
        if (userId != null) {
            getDailyMissionFromServer(userId) { success, missionResponse ->
                if (success && missionResponse != null) {
                    _missionState.value = missionResponse
                } else {
                    Log.e("Captura de mision", "Ha ocurrido un error al capturar la mision")
                }
            }
        }
    }
}