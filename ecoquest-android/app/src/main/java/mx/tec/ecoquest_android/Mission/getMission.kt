package mx.tec.ecoquest_android.Mission

// Capturamos todos los datos en una sola clase de datos
data class MissionResponse(
    val mission: Mission?,
    var isMissionCompleted: Int?
)

data class MissionResponseChanged(
    val mission: Mission?
)

data class Mission (
    val id_mision: Int,
    val titulo: String,
    val descripcion: String,
    val categoria: String,
    val dificultad: Difficulty,
) {
    enum class Difficulty {
        facil,
        dificil
    }
}

data class SendUserIdAndIdMission(
    val userId: String,
    val idMission: Int
)
//data class OptionalMissions(
//    val id_mission: Int?,
//    val titulo: String?,
//    val descripcion: String?
//)