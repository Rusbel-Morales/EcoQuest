package mx.tec.ecoquest_android.Achievements

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import mx.tec.ecoquest_android.HttpRequest.getAchiementsCompletedFromServer

class AchievementsViewModel : ViewModel() {
    private val _achivementsState = MutableStateFlow<Achievements?>(null)
    val achievementsState: StateFlow<Achievements?> = _achivementsState

    fun fetchAchievementsCompleted(userId: String) {
        getAchiementsCompletedFromServer(userId) { success, achievements ->
            if (success && achievements != null) {
                _achivementsState.value = achievements
            }
        }
    }
}