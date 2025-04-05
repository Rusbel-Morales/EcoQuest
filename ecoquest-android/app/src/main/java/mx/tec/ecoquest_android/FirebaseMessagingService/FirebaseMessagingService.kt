package mx.tec.ecoquest_android.FirebaseMessagingService

import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

fun getDeviceToken(onTokenReceived: (String) -> Unit) {

    // Recibimos el token de cada dispositivo asignado por Firebase Cloud Messaging (FCM)
    FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
        if (!task.isSuccessful) {
            Log.d("Firebase ID token", "Fetching FCM registration token failed", task.exception)
            return@OnCompleteListener
        }

        val deviceToken = task.result
        Log.d("Firebase ID token", "Token: $deviceToken")

        onTokenReceived(deviceToken)
    })
}