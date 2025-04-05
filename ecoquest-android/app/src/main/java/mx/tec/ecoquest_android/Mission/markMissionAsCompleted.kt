package mx.tec.ecoquest_android.Mission

// Enviar datos a la base de datos para marcar una misión como completada
data class MarkMissionAsCompleted(
    val userId: String?,
    val idMission: Int,
)