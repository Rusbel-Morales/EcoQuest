package com.example.check

import StatsViewModel
import android.os.Build
import android.util.Log
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import mx.tec.ecoquest_android.Achievements.AchievementsViewModel
import mx.tec.ecoquest_android.Achievements.TrophiesViewModel
import mx.tec.ecoquest_android.Mission.ChangeMission
import mx.tec.ecoquest_android.Notification.NotificationPermission
import mx.tec.ecoquest_android.R
import mx.tec.ecoquest_android.Screens.DescriptionBlock
import mx.tec.ecoquest_android.functionGoogle.sign_in.GoogleAuthUiClient
import mx.tec.ecoquest_android.Mission.MarkMissionAsCompleted
import mx.tec.ecoquest_android.Mission.MissionViewModel
import mx.tec.ecoquest_android.CurrentUserProgress.CurrentUserViewModel
import mx.tec.ecoquest_android.DataGraph.DataGraphViewModel
import mx.tec.ecoquest_android.Mission.OptionalMissionViewModel
import mx.tec.ecoquest_android.Mission.GetNewOptionalMissionViewModel
import mx.tec.ecoquest_android.Screens.CompletedDialog
import mx.tec.ecoquest_android.Screens.DailyMissionCompletedDialog
import mx.tec.ecoquest_android.Screens.ModalOfInvitation
import mx.tec.ecoquest_android.Screens.RerollDialog
import mx.tec.ecoquest_android.UsersForLeaderboard.LeaderboardViewModel
import kotlinx.coroutines.delay

// Sigleton
object AppState {
    var hasRun = false
}

object Once {
    var hasRun = false
}

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    googleAuthUiClient: GoogleAuthUiClient,
    navController: NavController,
    missionViewModel: MissionViewModel,
    leaderboardViewModel: LeaderboardViewModel,
    currentUserViewModel: CurrentUserViewModel,
    statsViewModel: StatsViewModel,
    optionalMissionViewModel: OptionalMissionViewModel,
    getNewOptionalMissionViewModel: GetNewOptionalMissionViewModel,
    changeMission: ChangeMission,
    dataGraphViewModel: DataGraphViewModel,
    achievementsViewModel: AchievementsViewModel,
    trophiesViewModel: TrophiesViewModel
) {
    val friends = painterResource(id = R.drawable.social)
    val logo = painterResource(id = R.drawable.logo)
    val bLogo = painterResource(id = R.drawable.logonegro)
    val quest = painterResource(id = R.drawable.objetivo)
    val sidequest = painterResource(id = R.drawable.brujula)
    val reroll = painterResource(id = R.drawable.flecha_circulo)
    val ncalendar = painterResource(id = R.drawable.nav_calendar)
    val ntarget = painterResource(id = R.drawable.nav_target)
    val ntrophy = painterResource(id = R.drawable.nav_trophy)
    val nleaderboard = painterResource(id = R.drawable.nav_leaderboards)

    // Cargamos todas los campos de la mision que recibimos
    val missionState by missionViewModel.missionState
    val currentUserState by currentUserViewModel.CurrentUserState

    // Cargamos las misiones opcionales DE PRIMER INSTANCIA
    val missionListState by optionalMissionViewModel.optionalMissionState.collectAsState()

    val isLoading by getNewOptionalMissionViewModel.isLoading

    // Cargamos las misiones rerooleadas
    val rerollOptionalMission by getNewOptionalMissionViewModel.rerollOptionalMissionState

    val missionToReroll by changeMission.missionToReroll
    val hasRerolled by changeMission.hasRerolled

    // Cargamos las nuevas misiones
    val newOptinalMission by getNewOptionalMissionViewModel.newOptionalMissionState

    val userId = googleAuthUiClient.getSignedInUser()?.uid

    val stateOnce by remember { mutableStateOf(!Once.hasRun)  }

    // Llamamos a la función de obtener misiones opcionales
    LaunchedEffect(Unit) {
        if (!AppState.hasRun) {
            AppState.hasRun = true
            if (userId != null) {
                optionalMissionViewModel.fetchOptionalMissions(userId)
            }
        }
    }

    // Simplificacion de los LaunchEffect
    LaunchedEffect(Unit) {
        if (userId != null) {
            missionViewModel.fetchDailyMission(userId) // 1
            leaderboardViewModel.fetchAllUserForLeaderboard() // 2
            trophiesViewModel.fetchTotalTrophies(userId) // 3
            achievementsViewModel.fetchAchievementsCompleted(userId) // 4
            statsViewModel.fetchStatsInformation(userId) // 5
            currentUserViewModel.fetchCurrentUserInformation(userId) // 6
            dataGraphViewModel.fetchDataGraph(userId) // 7
        }
    }

    // Verificar si estamos en un dispositivo con versión Android 13 o superior
    if (stateOnce && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        NotificationPermission()

        Once.hasRun = true
    }

    var showDescription by remember { mutableStateOf(false) }
    var showDescription2 by remember { mutableStateOf(false) }
    var showDescription3 by remember { mutableStateOf(false) }
    var showMainDescription by remember { mutableStateOf(false) }
    var showModalForInvitation by remember { mutableStateOf(false) }
    var showModalForCompletedDialog by remember { mutableStateOf(false) }
    var showModalForRerollDialog by remember { mutableStateOf(false) }
    var load by remember { mutableStateOf(false) }
    var showSimpleModalForCompletedDialog by remember { mutableStateOf(false) }

    var missionNumber by remember { mutableIntStateOf(0) }
    var currentmissionId by remember { mutableIntStateOf(0) }

    LaunchedEffect(rerollOptionalMission, isLoading) {
        Log.d("Cambio de mision", "Cambio de mision")

        // Fuente de obtención de la nueva misión
        val missionSource = rerollOptionalMission ?: newOptinalMission
        if (hasRerolled && missionSource != null) {
            load = true
            missionSource.let { newMission ->
                newMission.mission?.let { mission ->
                    optionalMissionViewModel.updateOptionalMissionState(missionToReroll, mission)
                    changeMission.resetMissionState()
                    getNewOptionalMissionViewModel.resetState()
                }
            }
        }
    }

    val formattedUserId = if ((userId != null) and (userId?.length!! >= 5)) {
        "${userId.take(3)}${userId.takeLast(2)}"
    } else {
        "No hay codigo"
    }

    // Despliegue de modal para invitación
    if (showModalForInvitation) {
        ModalOfInvitation(onDismiss = { showModalForInvitation = false }, formattedUserId)
    }

    // Despliegue de modal de confirmacion de mision opcional a completar
    if (showModalForCompletedDialog) {
        CompletedDialog(
            onDismiss = { showModalForCompletedDialog = false },
            dataChange = {load = true},
            navController = navController,
            missionNumber = missionNumber,
            changeMission = changeMission,
            getNewOptionalMissionViewModel = getNewOptionalMissionViewModel,
            currentmissionId = currentmissionId,
            userId = userId
        )
    }

    // Despliegue de modal de confirmacion de mision a rerooleada
    if (showModalForRerollDialog) {
        RerollDialog(
            onDismiss = { showModalForRerollDialog = false },
            dataChange = {load = true},
            missionNumber = missionNumber,
            changeMission = changeMission,
            getNewOptionalMissionViewModel = getNewOptionalMissionViewModel,
            currentmissionId = currentmissionId,
            userId = userId
        )
    }

    // Despliege de modal de mision diaria a completar
    if (showSimpleModalForCompletedDialog) {
        DailyMissionCompletedDialog(onDismiss = { showSimpleModalForCompletedDialog = false },
            navController = navController,
            markMissionAsCompleted = MarkMissionAsCompleted(userId, currentmissionId)
        )
    }

    // Para la barra xp
    var userXp by remember { mutableIntStateOf(0) }
    var nextUserXp by remember { mutableIntStateOf(0) }
    var position by remember { mutableIntStateOf(0) }
    var porcent by remember { mutableFloatStateOf(0f) }

    // Para cargar
    var op1 by remember {mutableStateOf(false) }
    var op2 by remember {mutableStateOf(false) }
    var op3 by remember {mutableStateOf(false) }
    // Simula la carga
    LaunchedEffect((op1 || op2 || op3) && load) {
        if (op1) { // Solo actúa si startDelay es true
            delay(5000) // Espera 3 segundos
            op1 = false // Cambia el valor a false
            load = false
        } else if (op2) {
            delay(5000) // Espera 3 segundos
            op2 = false
            load = false
        } else if (op3) {
            delay(5000) // Espera 3 segundos
            op3 = false
            load = false
        }
    }


    // Asignamos los valores obtenidos de currentUserState
    currentUserState?.let { currentUser ->
        userXp = currentUser.misPuntos ?: 0
        nextUserXp = currentUser.puntosSiguienteUsuario ?: 0
        position = currentUser.miPosicion ?: 0
    }

    val xp = (userXp).toFloat()
    val xptotal = (nextUserXp).toFloat()
    if (nextUserXp != 0) {
        porcent = ((xp) / nextUserXp)
    }

    // Guardamos el texto de la mision
    var missionText by remember { mutableStateOf("") }

    missionText = if (missionState?.mission?.titulo?.isNotEmpty() == true) {
        // ?: Utilizado para proporcionar un valor predeterminado cuando una expresión es nula
        missionState?.mission?.titulo ?: "Cargando misión..."
    } else {
        "No hay mision disponible"
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
                onClick = { /* Handle the button click here */ },
                //elevation = ButtonDefaults.elevation(8.dp),
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
                    /*
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .background(Color(0xFFBEDC74))
                    )*/
                    // Parte derecha
                    if (nextUserXp != 0) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(porcent*1f)
                                .fillMaxHeight()
                                .align(Alignment.CenterStart)
                                .clip(RoundedCornerShape(16.dp))
                                .border(1.dp, Color(0xFF000000), RoundedCornerShape(16.dp))
                                .background(Color(0xFFB2EBF2))
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                                .align(Alignment.CenterStart)
                                .clip(RoundedCornerShape(16.dp))
                                .border(1.dp, Color(0xFF000000), RoundedCornerShape(16.dp))
                                .background(Color(0xFFB2EBF2))
                        )
                    }
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
        // Logo
        Image(
            painter = logo,
            contentDescription = "Logo",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp),

            )
        // Spacer
        Spacer(
            modifier = Modifier
                .height(30.dp)
        )
        // Mision del Dia
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = { /* Handle the button click here */ },
                shape = RoundedCornerShape(16.dp),
                //elevation = ButtonDefaults.elevation(8.dp),
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF6E96B),  // Color de fondo
                    contentColor = Color(0xFF000000) // Color del texto
                ),

                contentPadding = PaddingValues(17.dp)

            ) {
                Column(
                    modifier = modifier
                        //.fillMaxSize()
                        .padding(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Image(
                            painter = quest,
                            contentDescription = "Objetivo Icon",
                            contentScale = ContentScale.FillWidth,
                            modifier = Modifier
                                .width(40.dp),
                            colorFilter = ColorFilter.tint(Color.Black)
                        )
                        Spacer(
                            modifier = Modifier
                                .width(4.dp)
                        )
                        Text(
                            text = "Mision Principal",
                            fontSize = 27.sp,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center,
                            lineHeight = 40.sp
                        )
                    }
                    Spacer(
                        modifier = Modifier
                            .height(10.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = {
                                if (missionState?.isMissionCompleted == 0) {
                                    showSimpleModalForCompletedDialog = true
                                    currentmissionId = missionState?.mission?.id_mision ?: 0
//                                    val missionId = missionState?.mission?.id_mision
//                                    if (missionId != null) {
//                                        val markMissionAsCompleted = MarkMissionAsCompleted(userId, missionId)
//                                        sendMissionAsCompletedToServer(markMissionAsCompleted) { success ->
//                                            if (success) {
//                                                Log.d(
//                                                    "SendMission",
//                                                    "Mision completada enviada con exito"
//                                                )
//                                            } else {
//                                                Log.e(
//                                                    "SendMission",
//                                                    "Error al enviar la mision completada"
//                                                )
//                                            }
//                                        }
//                                    }
                                }
                            },
                            enabled = missionState?.isMissionCompleted != 1,

                            //elevation = ButtonDefaults.elevation(8.dp),
                            modifier = Modifier
                                .height(30.dp)
                                .width(30.dp),
                            shape = RectangleShape,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Gray
                            ),
                            contentPadding = PaddingValues(12.dp)
                        ) {}
                        Button(
                            onClick = { showMainDescription = true },
                            modifier = Modifier
                                .fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent // Fondo transparente
                            ),
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically // Alinea verticalmente al centro si es necesario
                            ) {
                                Text(
                                    text = missionText,
                                    color = Color.Black,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Normal,
                                    textAlign = TextAlign.Start,
                                    lineHeight = 24.sp
                                )
                                if (showMainDescription) {
                                    DescriptionBlock(
                                        title = missionText,
                                        descp = if (missionState?.mission?.descripcion?.isNotEmpty() == true) {
                                            missionState?.mission?.descripcion
                                                ?: "Cargando descripción..."
                                        } else {
                                            "No hay descripción disponible"
                                        },
                                        onCancel = { showMainDescription = false }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        // Spacer
        Spacer(
            modifier = Modifier
                .height(20.dp)
        )
        // Misiones secundarias
        Column(
            modifier = Modifier
                .padding(horizontal = 18.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start

            ) {
                Spacer(
                    modifier = Modifier
                        .width(10.dp)
                )
                Column(
                    modifier = Modifier
                        .height(40.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = sidequest,
                        contentDescription = "Objetivo Secundario Icon",
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .width(30.dp),
                        colorFilter = ColorFilter.tint(Color.Black)
                    )
                }
                Spacer(
                    modifier = Modifier
                        .width(4.dp)
                )
                Column(
                    modifier = Modifier
                        .height(40.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Misiones Secundarias",
                        fontSize = 22.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.SemiBold,
                        lineHeight = 40.sp
                    )
                }
            }
            // Sec 1
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,

                ) {
                Button(
                    onClick = {
                        op1 = true
                        missionListState?.get(0)?.let {
                            missionNumber = 0
                            currentmissionId = it.id_mision
                            showModalForRerollDialog = true
                        }
                    },
                    modifier = Modifier
                        .height(50.dp)
                        .width(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    contentPadding = PaddingValues(0.dp)

                ) {
                    Image(
                        painter = reroll,
                        contentDescription = "Boton de reintentar",
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .height(30.dp)
                            .width(30.dp),
                    )
                }
                Column(
                    modifier = Modifier
                        .height(50.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = {
                            op1 = true
                            missionListState?.get(0)?.let {
                                missionNumber = 0
                                currentmissionId = it.id_mision
                                showModalForCompletedDialog = true
                            }
                        },
                        modifier = Modifier
                            .height(30.dp)
                            .width(30.dp),
                        shape = RectangleShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Gray
                        ),
                        contentPadding = PaddingValues(12.dp)
                    ) {}
                }
                Spacer(
                    modifier = Modifier
                        .width(5.dp)
                )
                Column(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Button(
                        onClick = { showDescription = true },
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent // Fondo transparente
                        ),
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically // Alinea verticalmente al centro si es necesario
                        ) {
                            if (op1 && load){
                                Text(
                                    text = "Cargando siguiente mision...",
                                    fontSize = 22.sp,
                                    color = Color.Black,
                                    textAlign = TextAlign.Start,
                                    fontWeight = FontWeight.Normal,
                                    lineHeight = 24.sp
                                )
                            }else {
                                Text(
                                    text = missionListState?.get(0)?.titulo ?: "Cargando...",
                                    fontSize = 22.sp,
                                    color = Color.Black,
                                    textAlign = TextAlign.Start,
                                    fontWeight = FontWeight.Normal,
                                    lineHeight = 24.sp
                                )
                            }
                            if (showDescription) {
                                DescriptionBlock(
                                    title = missionListState?.get(0)?.titulo ?: "Cargando...",
                                    descp = missionListState?.get(0)?.descripcion ?: "Cargando...",
                                    onCancel = { showDescription = false }
                                )
                            }
                        }
                    }
                }

            }
            // Sec 2
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,

                )
            {
                Button(
                    onClick = {
                        op2 = true
                        missionListState?.get(1)?.let {
                            missionNumber = 1
                            currentmissionId = it.id_mision
                            showModalForRerollDialog = true
                        }
                    },
                    modifier = Modifier
                        .height(50.dp)
                        .width(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    contentPadding = PaddingValues(0.dp)

                ) {

                    Image(
                        painter = reroll,
                        contentDescription = "Boton de reintentar",
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .height(30.dp)
                            .width(30.dp),
                    )
                }
                Column(
                    modifier = Modifier
                        .height(50.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = {
                            op2 = true
                            missionListState?.get(1)?.let {
                                missionNumber = 1
                                currentmissionId = it.id_mision
                                showModalForCompletedDialog = true
                            }
                        },
                        modifier = Modifier
                            .height(30.dp)
                            .width(30.dp),
                        shape = RectangleShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Gray
                        ),
                        contentPadding = PaddingValues(12.dp)

                    ) {}
                }
                Spacer(
                    modifier = Modifier
                        .width(5.dp)
                )
                Column(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Button(
                        onClick = { showDescription2 = true },
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent // Fondo transparente
                        ),
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(), // Row ocupa todo el ancho del botón
                            verticalAlignment = Alignment.CenterVertically // Alinea verticalmente al centro si es necesario
                        ) {
                            if (op2 && load){
                                Text(
                                    text = "Cargando siguiente mision...",
                                    fontSize = 22.sp,
                                    color = Color.Black,
                                    textAlign = TextAlign.Start,
                                    fontWeight = FontWeight.Normal,
                                    lineHeight = 24.sp
                                )
                            }else {
                                Text(
                                    text = missionListState?.get(1)?.titulo ?: "Cargando...",
                                    fontSize = 22.sp,
                                    color = Color.Black,
                                    textAlign = TextAlign.Start,
                                    fontWeight = FontWeight.Normal,
                                    lineHeight = 24.sp
                                )
                            }
                            if (showDescription2) {
                                DescriptionBlock(
                                    title = missionListState?.get(1)?.titulo ?: "Cargando...",
                                    descp = missionListState?.get(1)?.descripcion ?: "Cargando...",
                                    onCancel = { showDescription2 = false }
                                )
                            }
                        }
                    }
                }

            }
            // Sec 3
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,

                )
            {
                Button(
                    onClick = {
                        op3 = true
                        missionListState?.get(2)?.let {
                            missionNumber = 2
                            currentmissionId = it.id_mision
                            showModalForRerollDialog = true
                        }
                    },
                    modifier = Modifier
                        .height(50.dp)
                        .width(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    contentPadding = PaddingValues(0.dp)

                ) {

                    Image(
                        painter = reroll,
                        contentDescription = "Boton de reintentar",
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .height(30.dp)
                            .width(30.dp),
                    )
                }
                Column(
                    modifier = Modifier
                        .height(50.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = {
                            op3 = true
                            missionListState?.get(2)?.let {
                                missionNumber = 2
                                currentmissionId = it.id_mision
                                showModalForCompletedDialog = true
                            }
                        },
                        modifier = Modifier
                            .height(30.dp)
                            .width(30.dp),
                        shape = RectangleShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Gray
                        ),
                        contentPadding = PaddingValues(12.dp)

                    ) {}
                }
                Spacer(
                    modifier = Modifier
                        .width(5.dp)
                )
                Column(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Button(
                        onClick = { showDescription3 = true },
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent // Fondo transparente
                        ),
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(), // Row ocupa todo el ancho del botón
                            verticalAlignment = Alignment.CenterVertically // Alinea verticalmente al centro si es necesario
                        ) {
                            if (op3 && load){
                                Text(
                                    text = "Cargando siguiente mision...",
                                    fontSize = 22.sp,
                                    color = Color.Black,
                                    textAlign = TextAlign.Start,
                                    fontWeight = FontWeight.Normal,
                                    lineHeight = 24.sp
                                )
                            }else {
                                Text(
                                    text = missionListState?.get(2)?.titulo ?: "Cargando...",
                                    fontSize = 22.sp,
                                    color = Color.Black,
                                    textAlign = TextAlign.Start,
                                    fontWeight = FontWeight.Normal,
                                    lineHeight = 24.sp
                                )
                            }

                            if (showDescription3) {
                                DescriptionBlock(
                                    title = missionListState?.get(2)?.titulo ?: "Cargando...",
                                    descp = missionListState?.get(2)?.descripcion ?: "Cargando...",
                                    onCancel = { showDescription3 = false }
                                )
                            }
                        }
                    }
                }
            }
        }
        Spacer(
            modifier = Modifier
                .weight(1f)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
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
                    colorFilter = ColorFilter.tint(
                        Color.Black
                    )
                )
            }
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
                        painter = ntarget,
                        contentDescription = "Objetivos Icon",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(5.dp),
                        colorFilter = ColorFilter.tint(Color.Black)
                    )
                }
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
