package mx.tec.ecoquest_android.Mission

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import mx.tec.ecoquest_android.HttpRequest.getNewMissionFromDatabase
import mx.tec.ecoquest_android.HttpRequest.getRerollOptionalMissionFromServer

class GetNewOptionalMissionViewModel: ViewModel() {
    // Obtener la misión rerolleada
    private val _rerollOptionalMissionState = mutableStateOf<MissionResponseChanged?>(null)
    val rerollOptionalMissionState: State<MissionResponseChanged?> = _rerollOptionalMissionState

    // Obtener la nueva misión
    private val  _newOptionalMissionState = mutableStateOf<MissionResponseChanged?>(null)
    val newOptionalMissionState: State<MissionResponseChanged?> = _newOptionalMissionState

    // Estado de la llamada a la API
    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    fun fetchRerollOptionalMission(userId: String, idMission: Int) {
        val sendData = SendUserIdAndIdMission(userId, idMission)
        getRerollOptionalMissionFromServer(sendData) { success, mission ->
            if (success && mission != null) {
                _rerollOptionalMissionState.value = mission
            }
        }
    }

    fun fetchNewOptionalMission(userId: String, idMission: Int) {
        viewModelScope.launch {
            val sendData = SendUserIdAndIdMission(userId, idMission)

            // Espera el resultado y actualiza el estado
            val (success, newMission) = getNewMissionFromDatabase(sendData)
            if (success && newMission != null) {
                _newOptionalMissionState.value = newMission
                _isLoading.value = true
            }
        }
    }

    fun resetState() {
        _rerollOptionalMissionState.value = null
        _newOptionalMissionState.value = null
        _isLoading.value = false
    }
}