package mx.tec.ecoquest_android.Achievements

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import mx.tec.ecoquest_android.HttpRequest.getTotalTrophiesFromServer

class TrophiesViewModel: ViewModel() {
    private val _trophiesState = MutableStateFlow<Trophies?>(null)
    val trophiesState: StateFlow<Trophies?> = _trophiesState

    fun fetchTotalTrophies(userId: String) {
        getTotalTrophiesFromServer(userId) { success, trophies ->
            if (success && trophies != null) {
                _trophiesState.value = trophies
            }
        }
    }

}