import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import mx.tec.ecoquest_android.CurrentUserProgress.CurrentUser
import mx.tec.ecoquest_android.HttpRequest.APIInterface
import mx.tec.ecoquest_android.HttpRequest.getCurrentUserInformationForProgress
import mx.tec.ecoquest_android.HttpRequest.getStatsInformationForProgress
import mx.tec.ecoquest_android.statsModel.Stats
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class StatsViewModel : ViewModel() {
    val _Stats = mutableStateOf<Stats?>(null)

    val stats: State<Stats?> = _Stats

    fun fetchStatsInformation(userId: String) {
        getStatsInformationForProgress(userId) { success, user ->
            if (success && user != null) {
                _Stats.value = user
            }
            else {
                Log.e("Captura de progreso del usuario actual", "Ha ocurrido un error en la captura del progreso del usuario actual")
            }
        }
    }
}

