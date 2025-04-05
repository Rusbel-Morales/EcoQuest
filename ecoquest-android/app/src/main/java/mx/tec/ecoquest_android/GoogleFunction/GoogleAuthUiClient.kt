package mx.tec.ecoquest_android.functionGoogle.sign_in

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.SignInClient
import mx.tec.ecoquest_android.R // Para la resolución de problema de la referencia R
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException

// Iniciar sesión, cerrar sesión y obtener datos del usuario
class GoogleAuthUiClient(
    private val context: Context,
    // oneTapClient: Muestra una ventana emergente de las cuentas disponibles en el dispositivo móvil en particular
    // SignInClient: API de Google que nos permite autenticarnos con la cuenta Google
    private val oneTapClient: SignInClient,
) {
    private val auth = Firebase.auth
    private var cachedToken: String? = null // Variable para almacenar el ID Token

    suspend fun signIn(): IntentSender? {
        val result = try {
            oneTapClient.beginSignIn(
                buildSignInRequest()
            ).await()
        } catch (e: Exception) {
            e.printStackTrace()
            // Verificar excepciones de cancelación
            if (e is CancellationException) throw e
            null
        }
        return result?.pendingIntent?.intentSender
    }

    suspend fun signInWithIntent(intent: Intent): SignInResult {
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
        return try {
            val user = auth.signInWithCredential(googleCredentials).await().user
            cachedToken = googleIdToken // Guardar el ID Token en la variable
            SignInResult(
                data = user?.run {
                    UserData(
                        uid = uid,
                        idToken = googleIdToken
                    )
                },
                errorMessage = null,
            )
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            SignInResult(
                data = null,
                errorMessage = e.message,
            )
        }
    }

    fun getSignedInUser(): UserData? = auth.currentUser?.run {
        UserData(
            uid = uid,
            idToken = cachedToken
        )
    }

    // Nos permite utilizar servicios de Google
    private fun buildSignInRequest(): BeginSignInRequest {
        return BeginSignInRequest.builder()
            // Configura las opciones para solicitar un ID de token de Google
            .setGoogleIdTokenRequestOptions(
                GoogleIdTokenRequestOptions.builder()
                    .setSupported(true) // Habilita la opción de iniciar sesión usando el ID de token de Google
                    .setFilterByAuthorizedAccounts(false) // Muestra todas las cuentas de Google, no solo las autorizadas previamente
                    .setServerClientId(context.getString(R.string.web_client_id)) // Especifica el ID del cliente del servidor para verificar el token
                    .build()
            )
            .setAutoSelectEnabled(true) // Selecciona automáticamente la cuenta de Google si solo hay una cuenta disponible
            .build()
    }
}
