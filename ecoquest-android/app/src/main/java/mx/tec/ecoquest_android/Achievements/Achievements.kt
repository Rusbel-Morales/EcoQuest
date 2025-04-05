package mx.tec.ecoquest_android.Achievements

data class Achievements(
    val achievements: List<Achievement>?
)

data class Achievement(
    val id_achievement: Int,
    val isCompleted: Int,
)

data class Trophies(
    val trophyCount: Trophy?
)

data class Trophy(
    val total_oro: Int,
    val total_plata: Int,
    val total_bronce: Int,
)