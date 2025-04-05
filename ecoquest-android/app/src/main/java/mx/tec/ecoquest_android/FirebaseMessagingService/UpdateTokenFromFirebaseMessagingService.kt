package mx.tec.ecoquest_android.FirebaseMessagingService

import android.util.Log
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.messaging.FirebaseMessagingService
import mx.tec.ecoquest_android.functionGoogle.sign_in.GoogleAuthUiClient
import mx.tec.ecoquest_android.functionGoogle.sign_in.UpdateDeviceToken
import mx.tec.ecoquest_android.HttpRequest.sendUpdateDeviceTokenToServer

class UpdateTokenFromFirebaseMessagingService: FirebaseMessagingService() {

    // Con este método, Firebase se comunica cuando el token se actualiza
    override fun onNewToken(token: String) {
        super.onNewToken(token)
         val googleAuthUiClient = GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext))

        val userId = googleAuthUiClient.getSignedInUser()?.uid

        // Construimos nuestra clase de datos
        if (userId != null) {
            val updateDeviceToken = UpdateDeviceToken(userId, token)
            // Enviar el token de dispositivo actualizado al servidor
            sendUpdateDeviceTokenToServer(updateDeviceToken) { success ->
                if (success) {
                    Log.d("UpdateTokenFromFirebaseMessagingService", "Token actualizado")
                }
                else {
                    Log.e("UpdateTokenFromFirebaseMessagingService", "Error al actualizar el token")
                }
            }
        }
    }
}