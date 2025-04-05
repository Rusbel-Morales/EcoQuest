package mx.tec.ecoquest_android.Mission

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State

class ChangeMission: ViewModel() {
    private val _missionToReroll = mutableStateOf(0)
    val missionToReroll: State<Int> = _missionToReroll

    private val _hasRerolled = mutableStateOf(false)
    val hasRerolled: State<Boolean> = _hasRerolled

    fun updateMissionState(missionNumber: Int, rerolled: Boolean) {
        _missionToReroll.value = missionNumber
        _hasRerolled.value = rerolled
    }

    fun resetMissionState() {
        _missionToReroll.value = 0
        _hasRerolled.value = false
    }
}