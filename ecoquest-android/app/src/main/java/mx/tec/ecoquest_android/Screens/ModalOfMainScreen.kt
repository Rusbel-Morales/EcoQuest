package mx.tec.ecoquest_android.Screens

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.navigation.NavController
import mx.tec.ecoquest_android.Mission.ChangeMission
import mx.tec.ecoquest_android.Mission.GetNewOptionalMissionViewModel
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import mx.tec.ecoquest_android.HttpRequest.sendMissionAsCompletedToServer
import mx.tec.ecoquest_android.Mission.MarkMissionAsCompleted
import mx.tec.ecoquest_android.R

// Dialogo de invitación
@Composable
fun ModalOfInvitation(onDismiss: () -> Unit, formattedUserId: String) {
    val shareLogo = painterResource(id = R.drawable.share)
    val context = LocalContext.current

    Dialog(
        onDismissRequest = onDismiss
    ) {
        Surface(
            modifier = Modifier
                .padding(8.dp)
                .size(370.dp),
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFFBEDC74)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .align(Alignment.Start)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        tint = Color(0xFF000000)
                    )
                }
                Text(
                    text = "¡Invita a tus amigos\na EcoQuest!",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    color = Color(0xFF000000),
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                )
                Text(
                    text = formattedUserId,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    color = Color(0xFF000000),
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                )
                Text(
                    text = "Envía este enlace a tus amigos\npara invitarlos a descargar la App y\ncompletar su primera mision para\nambos ganar EcoXP",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    color = Color(0xFF000000)
                )
                Button(
                    onClick = {
                        val sendIntent: Intent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TITLE, "¡Únete a EcoQuest!") // Título para el contenido compartido
                            putExtra(Intent.EXTRA_TEXT, "https://ecoquest.page.link/invite") // TODO: Cambiar por el enlace de la app
                            type = "text/plain"
                        }

                        val shareIntent = Intent.createChooser(sendIntent, "Token de invitación EcoQuest")
                        context.startActivity(shareIntent)
                    },
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .size(176.dp, 48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFFFFF),
                        contentColor = Color(0xFF000000)
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 8.dp
                    ),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = shareLogo,
                            contentDescription = null,
                            modifier = Modifier
                                .size(32.dp)
                        )
                        Spacer(
                            modifier = Modifier
                                .width(8.dp)
                        )
                        Text (
                            text = "Compartir",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

// Dialogo de confirmación para marcar misión opcional como completada
@Composable
fun CompletedDialog(
    onDismiss: () -> Unit,
    dataChange: () -> Unit,
    navController: NavController,
    missionNumber: Int,
    changeMission: ChangeMission,
    getNewOptionalMissionViewModel: GetNewOptionalMissionViewModel,
    currentmissionId: Int,
    userId: String
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Marcar misión como\ncompletada",
                lineHeight = 28.sp
            )
        },
        text = { Text(text = "¿Estás seguro de realizar esta acción?") },
        confirmButton = {
            Button(
                onClick = {
                    onDismiss()
                    dataChange()
                    changeMission.updateMissionState(missionNumber, true)
                    getNewOptionalMissionViewModel.fetchNewOptionalMission(
                        userId,
                        currentmissionId
                    )
                    navController.navigate("checkPopUp")
                }
            ) {
                Text(
                    text = "Confirmar"
                )
            }
        },
        dismissButton = {
            TextButton(onClick = {
                onDismiss()
            }) {
                Text(
                    text = "Rechazar"
                )
            }
        }
    )
}

// Dialogo de confirmación para marcar misión como relooleada
@Composable
fun RerollDialog(
    onDismiss: () -> Unit,
    dataChange: () -> Unit,
    missionNumber: Int,
    changeMission: ChangeMission,
    getNewOptionalMissionViewModel: GetNewOptionalMissionViewModel,
    currentmissionId: Int,
    userId: String
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Cambiar misión",
            )
        },
        text = { Text(text = "¿Estás seguro de realizar esta acción?") },
        confirmButton = {
            Button(
                onClick = {
                    onDismiss()
                    dataChange()
                    changeMission.updateMissionState(missionNumber, true)
                    getNewOptionalMissionViewModel.fetchRerollOptionalMission(
                        userId,
                        currentmissionId
                    )
                }
            ) {
                Text(
                    text = "Confirmar"
                )
            }
        },
        dismissButton = {
            TextButton(onClick = {
                onDismiss()
            }) {
                Text(
                    text = "Rechazar"
                )
            }
        }
    )
}

// Dialogo de confirmación para marcar misión diaria como completada
@Composable
fun DailyMissionCompletedDialog(
    onDismiss: () -> Unit,
    navController: NavController,
    markMissionAsCompleted: MarkMissionAsCompleted
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Marcar misión como\ncompletada",
                lineHeight = 28.sp
            )
        },
        text = { Text(text = "¿Estás seguro de realizar esta acción?") },
        confirmButton = {
            Button(
                onClick = {
                    onDismiss()
                    sendMissionAsCompletedToServer(markMissionAsCompleted) { success ->
                        if (success) {
                            Log.d("DailyMissionCompletedDialog", "Misión completada")
                        }
                    }
                    navController.navigate("checkPopUp")
                }
            ) {
                Text(
                    text = "Confirmar"
                )
            }
        },
        dismissButton = {
            TextButton(onClick = {
                onDismiss()
            }) {
                Text(
                    text = "Rechazar"
                )
            }
        }
    )
}