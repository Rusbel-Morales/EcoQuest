package mx.tec.ecoquest_android.UsersForLeaderboard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun UserListItem(
    userId: String?,
    user: User?,
    index: Int,
    onCurrenUserFound: (Triple<Int, String, Pair<Color, Color>>?) -> Unit
) {

    var username by remember { mutableStateOf("") }
    var totalPoints by remember { mutableStateOf("") }
    var userIdFromAllUsers by remember { mutableStateOf("") }
    var flag by remember { mutableStateOf(true) }

    // Obtención del ID del usuario
    if (user?.id_usuario != null) {
        userIdFromAllUsers = user.id_usuario
    } else {
        userIdFromAllUsers = ""
    }

    // Obtención del nombre
    if (user?.nombre != null) {
        username = user.nombre
    } else {
        username = ""
    }

    if (user?.total_puntos != null) {
        totalPoints = user.total_puntos
    } else {
        totalPoints = ""
    }

    // Dependiendo de la posición del usuario, va a escoger un par de colores
    val surfaceColor: Pair<Color, Color> = when (index) {
        1 -> Pair(Color(0xFFFFD700), Color(0xFFD4B200)) // Oro
        2 -> Pair(Color(0xFFC0C0C0), Color(0xFF9A9A9A)) // Plata
        3 -> Pair(Color(0xFFCD7F32), Color(0xFFB7712C)) // Bronce
        else -> Pair(Color(0xFFE0E0E0), Color(0xFFE0E0E0)) // Color por defecto
    }

    // Verificamos que el usuario actual exista en el leaderboard
    if (flag) {
        if (userIdFromAllUsers == userId) {
            onCurrenUserFound(Triple(index, totalPoints, surfaceColor))
            flag = false
        }
    }

    Column (
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .height(50.dp)
                .background(if (userIdFromAllUsers == userId) Color(0xFFA2CA71) else Color(0xFFFFFFFF)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Superficie para la posición actual del usuario
            Surface (
                shadowElevation = 8.dp,
                color = surfaceColor.first,
                shape = RoundedCornerShape(6.dp),
                border = BorderStroke(2.dp, surfaceColor.second),
                modifier = Modifier
                    .padding(start = 10.dp)
                    .size(width = 36.dp, height = 36.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${index}.",
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            // Permitir máximo 15 caracteres en el nombre

            Text(
                text = if (username.length <= 15) {
                    username
                }
                else {
                    username.substring(0, 15).plus("...")
                },
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .padding(start = 20.dp)
            )
            Spacer(
                modifier = Modifier
                    .weight(1f)
            )

            // Superficie para los puntos actuales del usuario
            Surface (
                color = Color(0xFFD0D0D0),
                shape = RoundedCornerShape(6.dp),
                border = BorderStroke(1.dp, Color(0xFF9A9A9A)),
                modifier = Modifier
                    .padding(end = 10.dp)
                    .size(width = 60.dp, height = 36.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = totalPoints,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

            }
        }
        Spacer(
            modifier = Modifier
                .height(4.dp)
        )
        // Linea negra (debajo de cada usuario en el Leaderboard)
        Box(
            modifier = Modifier
                .background(Color(0xFF000000))
                .fillMaxWidth(0.95f)
                .height(1.dp)
            )
        }

        Spacer(
            modifier = Modifier
                .height(10.dp)
        )
}