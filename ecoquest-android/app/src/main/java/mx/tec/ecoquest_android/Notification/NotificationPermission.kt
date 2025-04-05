package mx.tec.ecoquest_android.Notification

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@RequiresApi(Build.VERSION_CODES.TIRAMISU) // Solo se ejecuta cuando se procesa en un dispositivo que tenga una versión igual o superior a Android 13 (API 33)
@Composable
fun NotificationPermission() {
    val context = LocalContext.current
    var showPermissionDialog by remember { mutableStateOf(false) }

    // Inicialización de una instancia de la solicitud de permisos
    val requestPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            // El usuario acepta el permiso
            if (isGranted) {
                // El usuario niega el permiso
            } else {
            }
        }

    // Verificamos si el permiso ha sido concedido
    LaunchedEffect(Unit) {
        // Verificamos la versión actual de nuestro nivel de API de Android
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Verificar si el usuariuo ya otorgó el permiso de notificaciones
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                showPermissionDialog = true
            }
        }
    }

    if (showPermissionDialog) {
        AlertDialog(
            onDismissRequest = { showPermissionDialog = false },
            title = { Text(text = "Permiso de notificaciones") },
            text = { Text(text = "Por favor, activa las notificaciones para estar al tanto de las nuevas misiones diarias") },
            confirmButton = {
                Button(
                    onClick = {
                        showPermissionDialog = false
                        // Solicitar el permiso cuando el usuario presione "Aceptar"
                        requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                ) {
                    Text(
                        text = "Aceptar"
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showPermissionDialog = false
                }) {
                    Text(
                        text = "Rechazar"
                    )
                }
            }
        )
    }
}
