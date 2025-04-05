package mx.tec.ecoquest_android.Screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import mx.tec.ecoquest_android.R
import mx.tec.ecoquest_android.UsersForLeaderboard.LeaderboardViewModel
import mx.tec.ecoquest_android.UsersForLeaderboard.UserListItem
import mx.tec.ecoquest_android.functionGoogle.sign_in.GoogleAuthUiClient

@Composable
fun LeaderboardScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    leaderboardViewModel: LeaderboardViewModel,
    googleAuthUiClient: GoogleAuthUiClient
) {
    val ncalendar = painterResource(id = R.drawable.nav_calendar)
    val ntrophy = painterResource(id = R.drawable.nav_trophy)
    val ntarget = painterResource(id = R.drawable.nav_target)
    val nleaderboard = painterResource(id = R.drawable.nav_leaderboards)

    // Obtener el ID del usuario
    val userId = googleAuthUiClient.getSignedInUser()?.uid

    // Limitar la cantidad de usuarios
    val totalUsers by leaderboardViewModel.leaderboardState

    // Obtener valores del usuario actual (en el caso de que esté dentro del top 500)
    var currentUser by remember { mutableStateOf<Triple<Int, String, Pair<Color, Color>>?>(null) }

    val limiteduserList = if ((totalUsers?.leaderboard?.size ?: 0) > 500) {
        totalUsers?.leaderboard?.take(500)
    } else {
        totalUsers?.leaderboard
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFFFF))
            .padding(top = 44.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .background(Color(0xFFBEDC74))
                .fillMaxWidth()
                .height(80.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Clasificación",
                fontSize = 28.sp,
                fontWeight = FontWeight.SemiBold,
            )
        }
        Spacer(
            modifier = Modifier
                .height(32.dp)
        )

        // Linea amarilla - IniciaL
        Box(
            modifier = Modifier
                .background(Color(0xFFF6E96B))
                .fillMaxWidth(0.95f)
                .height(8.dp)
        )

        Spacer(
            modifier = Modifier
                .height(20.dp)
        )

        /////////////////////////

        // Condicional para mostrar la lista de usuarios o el mensaje de que no hay usuarios
        if (limiteduserList.isNullOrEmpty()) {
            Column (
                modifier = Modifier
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = "No hay usuarios disponibles",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
            ) {
                itemsIndexed(limiteduserList) { index, user ->
                    UserListItem(userId, user, index + 1) { onCurrentUserFound ->
                        currentUser = onCurrentUserFound
                    }
                }
            }
        }

        // Barra de navegación
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(
                modifier = Modifier
                    .height(8.dp)
            )

            // Linea amarilla - Final
            Box(
                modifier = Modifier
                    .background(Color(0xFFF6E96B))
                    .fillMaxWidth(0.95f)
                    .height(8.dp)
            )

            Spacer(
                modifier = Modifier
                    .height(8.dp)
            )

            // Posición del usuario actual
            //////////////////////////////////////////////////

            Row(
                modifier = Modifier
                    .fillMaxWidth(0.9f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Superficie para la posición actual del usuario
                Surface(
                    shadowElevation = 8.dp,
                    color = currentUser?.third?.first ?: Color(0xFFE0E0E0),
                    shape = RoundedCornerShape(6.dp),
                    border = currentUser?.third?.second?.let { BorderStroke(2.dp, it) }
                        ?: BorderStroke(2.dp, Color(0xFFE0E0E0)),
                    modifier = Modifier
                        .size(width = 36.dp, height = 36.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (currentUser?.first != null) {
                                "${currentUser?.first}"
                            } else {
                                "-"
                            },
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                // Permitir máximo 15 caracteres en el nombre
                Text(
                    text = "Tú",
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
                Surface(
                    color = Color(0xFFD0D0D0),
                    shape = RoundedCornerShape(6.dp),
                    border = BorderStroke(1.dp, Color(0xFF9A9A9A)),
                    modifier = Modifier
                        .size(width = 60.dp, height = 36.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (currentUser?.second != null) {
                                "${currentUser?.second}"
                            } else {
                                "-"
                            },
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                }
            }

            //////////////////////////////////////////////////
            Spacer(
                modifier = Modifier
                    .height(8.dp)
            )

            Box(
                modifier = Modifier
                    .background(Color(0xFFF6E96B))
                    .fillMaxWidth(0.95f)
                    .height(8.dp)
            )

            Spacer(
                modifier = Modifier
                    .height(40.dp)
            )

            // Barra de navegación
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier
                    .background(Color(0xFFBEDC74))
            ) {
                Button(
                    onClick = {
                        navController.navigate("impactScreen")
                    },
                    modifier = Modifier
                        .height(80.dp)
                        .weight(1f),
                    shape = RectangleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFBEDC74)
                    ),
                    contentPadding = PaddingValues(5.dp)

                ) {
                    Image(
                        painter = ncalendar,
                        contentDescription = "Calendario Icon",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(5.dp),
                        colorFilter = ColorFilter.tint(Color.Black)
                    )
                }
                Button(
                    onClick = {
                        navController.navigate("mainScreen")
                    },
                    modifier = Modifier
                        .height(80.dp)
                        .weight(1f),
                    shape = RectangleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFBEDC74)
                    ),
                    contentPadding = PaddingValues(5.dp)

                ) {
                    Image(
                        painter = ntarget,
                        contentDescription = "Objetivos Icon",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(5.dp),
                        colorFilter = ColorFilter.tint(Color.Black)
                    )
                }
                Button(
                    onClick = {
                        navController.navigate("statisticScreen")
                    },
                    modifier = Modifier
                        .height(80.dp)
                        .weight(1f),
                    shape = RectangleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFBEDC74)
                    ),
                    contentPadding = PaddingValues(5.dp)

                ) {
                    Image(
                        painter = ntrophy,
                        contentDescription = "Logros Icon",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(5.dp),
                        colorFilter = ColorFilter.tint(Color.Black)
                    )
                }
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .padding(2.dp),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 8.dp
                    )
                ) {
                    Button(
                        onClick = { /* Nothing */ },
                        modifier = Modifier
                            .height(80.dp)
                            .shadow(12.dp),
                        shape = RectangleShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFA2CA71)
                        ),
                        contentPadding = PaddingValues(5.dp)

                    ) {
                        Image(
                            painter = nleaderboard,
                            contentDescription = "Ranking Icon",
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(5.dp),
                            colorFilter = ColorFilter.tint(Color.Black)
                        )
                    }
                }
            }
        }
    }
}