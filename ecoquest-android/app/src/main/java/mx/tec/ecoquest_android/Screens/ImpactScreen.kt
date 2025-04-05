package com.example.check

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import mx.tec.ecoquest_android.R
import mx.tec.ecoquest_android.functionGoogle.sign_in.GoogleAuthUiClient
import mx.tec.ecoquest_android.Mission.MissionViewModel
import mx.tec.ecoquest_android.CurrentUserProgress.CurrentUserViewModel
import mx.tec.ecoquest_android.Screens.ModalOfInvitation

@SuppressLint("DefaultLocale")
@Composable
fun ImpactScreen(
    modifier: Modifier = Modifier,
    googleAuthUiClient: GoogleAuthUiClient,
    navController: NavController,
    missionViewModel: MissionViewModel,
    currentUserViewModel: CurrentUserViewModel,
) {

    val friends = painterResource(id = R.drawable.social)
    val logo = painterResource(id = R.drawable.carretera)
    val bLogo = painterResource(id = R.drawable.logonegro)
    val ncalendar = painterResource(id = R.drawable.nav_calendar)
    val ntarget = painterResource(id = R.drawable.nav_target)
    val ntrophy = painterResource(id = R.drawable.nav_trophy)
    val nleaderboard = painterResource(id = R.drawable.nav_leaderboards)

    // Cargamos todas los campos de la mision que recibimos
    val missionState by missionViewModel.missionState
    val currentUserState by currentUserViewModel.CurrentUserState

    //var showMainDescription by remember { mutableStateOf(false) }
    var showModalForInvitation by remember { mutableStateOf(false) }

    val userId = googleAuthUiClient.getSignedInUser()?.uid
    val formattedUserId = if ((userId != null) and (userId?.length!! >= 5)) {
        "${userId.take(3)}${userId.takeLast(2)}"
    } else {
        "No hay codigo"
    }

    if (showModalForInvitation) {
        ModalOfInvitation(onDismiss = { showModalForInvitation = false }, formattedUserId)
    }


    // Para la barra xp
    var userXp by remember { mutableIntStateOf(0) }
    var nextUserXp by remember { mutableIntStateOf(0) }
    var position by remember { mutableIntStateOf(0) }
    var porcent by remember { mutableFloatStateOf(0f) }

    // Asignamos los valores obtenidos de currentUserState
    currentUserState?.let { currentUser ->
        userXp = currentUser.misPuntos ?: 0
        nextUserXp = currentUser.puntosSiguienteUsuario ?: 0
        position = currentUser.miPosicion ?: 0
    }

    val xp = (userXp).toFloat()
    val xptotal = (nextUserXp).toFloat()
    if (nextUserXp != 0) {
        porcent = ((xp) / xptotal)
    }

    val co = (userXp).toFloat()

    // Guardamos el texto de la mision
    var missionText by remember { mutableStateOf("") }

    missionText = if (missionState?.mission?.titulo?.isNotEmpty() == true) {
        // ?: Utilizado para proporcionar un valor predeterminado cuando una expresión es nula
        missionState?.mission?.titulo ?: "Cargando misión..."
    } else {
        "Cargando misión..."
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFFFFFF))
            .padding(top = 52.dp)
    ) {

        // XP
        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            Button(
                onClick = { showModalForInvitation = true },
                modifier = Modifier
                    .height(50.dp)
                    .weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFBEDC74)
                ),
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues(0.dp)

            ) {
                Image(
                    painter = friends,
                    contentDescription = "Boton de compartir",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .height(30.dp)
                        .width(30.dp)
                )
            }
            Spacer(
                modifier = Modifier
                    .weight(1f),
            )
            Column(
                modifier = Modifier
                    .height(30.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "#$position",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp,
                    color = Color.Black
                )
            }
            Spacer(
                modifier = Modifier
                    .width(8.dp),
            )
            Button(
                onClick = { /* Nothing */ },
                modifier = Modifier
                    .height(30.dp)
                    .weight(3f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFBEDC74)
                ),
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues(5.dp)

            ) {

                Box(
                    modifier = Modifier
                        .size(160.dp, 30.dp)
                ) {
                    // Parte izquierda
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .background(Color(0xFFBEDC74))
                    )
                    // Parte derecha
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(1f - porcent)
                            .fillMaxHeight()
                            .align(Alignment.CenterEnd)
                            .clip(RoundedCornerShape(16.dp))
                            .border(1.dp, Color(0xFF000000), RoundedCornerShape(16.dp))
                            .background(Color(0xFFB2EBF2))
                    )
                    // Texto en el centro

                    if (nextUserXp != 0) {
                        Text(
                            text = "$userXp/$nextUserXp",
                            modifier = Modifier
                                .align(Alignment.Center),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center,
                            lineHeight = 24.sp,
                            color = Color(0xFF000000)
                        )
                    } else {
                        Text(
                            text = "$userXp Nivel MAX",
                            modifier = Modifier
                                .align(Alignment.Center),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center,
                            lineHeight = 24.sp,
                            color = Color(0xFF000000)
                        )
                    }

                }
            }
        }
        // Spacer
        Spacer(
            modifier = Modifier
                .height(30.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Gracias a tus contribuciones\nevitamos:",
                modifier = Modifier,
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                lineHeight = 24.sp,
                color = Color(0xFF000000)
            )
        }
        // Spacer
        Spacer(
            modifier = Modifier
                .height(30.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = String.format("%.1f", (co * 0.01f)),
                fontSize = 98.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                lineHeight = 24.sp,
                color = Color.Black, // Cambia el color del texto según sea necesario
                modifier = Modifier

            )
            Spacer(
                modifier = Modifier
                    .width(8.dp)
            )
            Text(
                text = "kg",
                fontSize = 38.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                lineHeight = 24.sp,
                color = Color.Black // Cambia el color del texto según sea necesario
            )
        }

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Logo y Fondo
            Box(
                modifier = Modifier
                    .weight(1f) // Permite que el Box ocupe el espacio disponible sin excederse
                    .fillMaxWidth()
            ) {
                Image(
                    painter = logo,
                    contentDescription = "Carretera",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                )

                Text(
                    text = "de emisiones de CO2 en\nla atmósfera",
                    color = Color.Black,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(8.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomEnd)
                        .padding(end = 30.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Image(
                        painter = bLogo,
                        contentDescription = "Logo transparente",
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .height(70.dp)
                            .width(70.dp)
                            .padding(bottom = 16.dp),
                        alpha = 0.2f
                    )
                }
            }

            // Barra de navegación
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier
                    .background(Color(0xFFBEDC74))
            ) {
                Card(
                    modifier = Modifier
                        .weight(1f) // Asegúrate de que el botón tenga el mismo peso
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
                            painter = ncalendar,
                            contentDescription = "Calendario Icon",
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(5.dp),
                            colorFilter = ColorFilter.tint(Color.Black)
                        )
                    }
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
                Button(
                    onClick = {
                        navController.navigate("leaderboardScreen")
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