package mx.tec.ecoquest_android.UsersForLeaderboard

// Obtenemos la lista de todos los usuarios
data class LeaderboardResponse (
    val leaderboard: List<User?>
)

// Obtenemos los atributos del usuario
data class User (
    val id_usuario: String?,
    val nombre: String?,
    val total_puntos: String?
)