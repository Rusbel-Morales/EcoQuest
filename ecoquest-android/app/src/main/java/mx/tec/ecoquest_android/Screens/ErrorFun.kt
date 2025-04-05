package mx.tec.ecoquest_android.Screens

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.text.font.FontWeight

@Composable
fun ErrorDialog(showDialog: Boolean, onDismiss: () -> Unit, errorMessage: String) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { onDismiss() },  // Se invoca cuando el usuario intenta cerrar el diálogo
            title = {
                Text(
                    text = "Error",
                    fontWeight = FontWeight.SemiBold
                )
            },
            text = {
                Text(text = errorMessage)
            },
            confirmButton = {
                TextButton(onClick = { onDismiss() }) {  // Se invoca cuando el usuario hace clic en OK
                    Text("OK")
                }
            }
        )
    }
}