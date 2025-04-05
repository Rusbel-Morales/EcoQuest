package mx.tec.ecoquest_android.DataGraph

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import mx.tec.ecoquest_android.HttpRequest.getDataToGraph
import java.text.SimpleDateFormat
import java.util.Locale

class DataGraphViewModel: ViewModel() {
    private val _dataGraphState = MutableStateFlow<List<DataGraph>?>(null)
    val dataGraphState: StateFlow<List<DataGraph>?> = _dataGraphState

    fun fetchDataGraph(userId: String) {
        getDataToGraph(userId) { success, dataGraph ->
            if (success && dataGraph != null) {
                _dataGraphState.value = dataGraph
                convertDataToDay()
            }
        }
    }

    // Convertir los datos en días en específicos
    private fun convertDataToDay() {

        // Formato de entrada para la fecha original
        val entryFormat = SimpleDateFormat("dd-MM-yyyy", Locale("es"))

        // Formato de salida para el día de la semana
        val formatDayWeek = SimpleDateFormat("EEEE", Locale("es"))

        _dataGraphState.value = _dataGraphState.value?.map { dayPoint ->
            val date = dayPoint.dia?.let { entryFormat.parse(it) }
            val dayWeek = date?.let { formatDayWeek.format(it) }
            dayPoint.copy(dia = dayWeek?.let { it -> removeAccents(it.take(3)).replaceFirstChar { it.uppercase() } }) // Obtener solo las primeras 3 letras del nombre del día
        }
    }

    // Remover acentos de los días
    private fun removeAccents(input: String): String {
        return input
            .replace("á", "a")
            .replace("é", "e")
            .replace("í", "i")
            .replace("ó", "o")
            .replace("ú", "u")
    }
}

