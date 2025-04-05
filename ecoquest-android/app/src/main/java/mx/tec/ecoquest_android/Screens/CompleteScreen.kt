package com.example.check

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
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

@Composable
fun CompleteScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    googleAuthUiClient: GoogleAuthUiClient,
    missionViewModel: MissionViewModel,
    currentUserViewModel: CurrentUserViewModel
) {
    val friends = painterResource(id = R.drawable.social)
    val logo = painterResource(id = R.drawable.logo)
    val quest = painterResource(id = R.drawable.objetivo)
    val image = painterResource(id = R.drawable.imagen)

    // Imagenes
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    // Lanza el Intent para seleccionar una imagen de la galería
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri // Guarda el URI de la imagen seleccionada
    }
    // Para compartir
    val context = LocalContext.current
    val shareText = "¡Completé esta mision de ecoquest!"

    // Tamano de la pantalla
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    // Texto de escritura:
    var textdesc by remember { mutableStateOf("") }

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

    // Guardamos el texto de la mision
    var missionText by remember { mutableStateOf("") }

    if (missionState?.mission?.titulo?.isNotEmpty() == true) {
        // ?: Utilizado para proporcionar un valor predeterminado cuando una expresión es nula
        missionText = missionState?.mission?.titulo ?: "Cargando misión..."
    } else {
        missionText = "Cargando misión..."
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
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
                    text = if (position < 10) "#0$position" else "#$position",
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
                .height(20.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    navController.popBackStack()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color(0xFF000000)
                ),
                contentPadding = PaddingValues(0.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Regresar",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

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
                .height(10.dp)
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
                modifier = Modifier
                    .padding(horizontal = 10.dp, vertical = 50.dp)
                    .fillMaxWidth()
                    .height(screenHeight * 0.55f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF6E96B),  // Color de fondo
                    contentColor = Color.Black  // Color del texto
                ),
                contentPadding = PaddingValues(17.dp)

            ) {
                Column(
                    modifier = modifier
                        .padding(8.dp),
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "¿Marcar misión como completada?",
                            fontSize = 17.sp,
                            textAlign = TextAlign.Center,
                            lineHeight = 40.sp
                        )
                    }
                    Spacer(
                        modifier = Modifier
                            .height(7.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Cuenta tu experiencia de la misión",
                            fontSize = 17.sp,
                            textAlign = TextAlign.Center,
                            lineHeight = 40.sp
                        )
                    }
                    Spacer(
                        modifier = Modifier
                            .height(10.dp)
                    )
                    OutlinedTextField(
                        value = textdesc,
                        onValueChange = { textdesc = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .height(200.dp), // Ajusta la altura según el espacio que desees
                        placeholder = { Text("Escribe aquí tus párrafos...") },
                        maxLines = Int.MAX_VALUE, // Permite múltiples líneas
                        singleLine = false // Permite que el texto haga saltos de línea
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = { launcher.launch("image/*") },
                            shape = RoundedCornerShape(16.dp),
                            //elevation = ButtonDefaults.elevation(8.dp),
                            modifier = Modifier
                                .padding(10.dp)
                                .fillMaxWidth(.5f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,  // Color de fondo
                                contentColor = Color.Black  // Color del texto
                            ),

                            contentPadding = PaddingValues(5.dp)

                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "Añadir imagen",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    textAlign = TextAlign.Center,
                                    lineHeight = 20.sp
                                )
                                Spacer(
                                    modifier = Modifier
                                        .width(4.dp)
                                )
                                Image(
                                    painter = image,
                                    contentDescription = "Objetivo Icon",
                                    contentScale = ContentScale.FillWidth,
                                    modifier = Modifier
                                        .width(20.dp),
                                    colorFilter = ColorFilter.tint(Color.Black)
                                )


                            }
                        }
                    }
                }
            }
            Button(
                onClick = { /* Handle the button click here */ },
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(16.dp)
                    .fillMaxWidth(.7f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFA2CA71),  // Color de fondo
                    contentColor = Color(0xFF000000)  // Color del texto
                ),
                contentPadding = PaddingValues(10.dp)
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
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                        lineHeight = 40.sp
                    )
                }
            }
            Button(
                onClick = {
                    // Crear un intent de tipo ACTION_SEND
                    val shareIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        type =
                            if (selectedImageUri != null) "image/*" else "text/plain" // Tipo MIME según el caso

                        putExtra(Intent.EXTRA_TEXT, shareText) // Añadir texto

                        if (textdesc.isNotEmpty()) {
                            // Añadir texto adicional si textdesc no está vacío
                            putExtra(Intent.EXTRA_TEXT, "$shareText\n$textdesc")
                        }

                        if (selectedImageUri != null) {
                            putExtra(
                                Intent.EXTRA_STREAM,
                                selectedImageUri
                            ) // Añadir URI de la imagen solo si existe
                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        }
                    }
                    context.startActivity(
                        Intent.createChooser(
                            shareIntent,
                            "Compartir a través de"
                        )
                    )
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(10.dp)
                    .fillMaxWidth(.7f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFA2CA71),  // Color de fondo
                    contentColor = Color.Black  // Color del texto
                ),
            ) {
                Text("Compartir contenido")
            }
        }
    }
}