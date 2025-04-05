package mx.tec.ecoquest_android.Mission

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import mx.tec.ecoquest_android.HttpRequest.getOptionalMissionsFromServer

class OptionalMissionViewModel: ViewModel() {
    private val _optionalMissionState = MutableStateFlow<List<Mission>?>(null)
    val optionalMissionState: StateFlow<List<Mission>?> = _optionalMissionState

    fun fetchOptionalMissions(userId: String) {
        getOptionalMissionsFromServer(userId) { success, optionalMissions ->
            if (success && optionalMissions != null) {
                _optionalMissionState.value = optionalMissions
            }
        }
    }

    fun updateOptionalMissionState(index: Int, updateMission: Mission) {
        Log.d("Actualizando misiones opcionales", "Actualizando misiones opcionales")
        _optionalMissionState.value = _optionalMissionState.value?.let { currentMissions ->
            if (index in currentMissions.indices) {
                currentMissions.toMutableList().apply {
                    this[index] = updateMission // Actualiza la misión en el índice especificado
                }
            } else {
                currentMissions
            }
        }
    }
}