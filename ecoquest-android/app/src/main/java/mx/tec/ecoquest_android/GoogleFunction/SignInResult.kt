package mx.tec.ecoquest_android.functionGoogle.sign_in

data class SignInResult (
    val data: UserData?,
    val errorMessage: String?,
)

// Datos obtenidos por Firebase y cuenta de Google
data class UserData (
    val uid: String?, // Identificador único del usuario proporcionado por Firebase
    val idToken: String? // Token de identificación de usuario
)

// Enviamos los datos de identificación a la base de datos por primera vez (abierta la aplicación)
data class DataHttpRequest (
    val userId: String?, // Identificador único del usuario proporcionado por Firebase
    val idToken: String?, // Token de identificación de cuenta de google del usuario
    val deviceToken: String?, // Token de identificación del dispositivo
    val invitedBy: String? // Código de invitación
)

// Actualizamos nuestro token del dispositivo en caso de que
data class UpdateDeviceToken (
    val userId: String?, // Identificador único del usuario proporcionado por Firebase
    val deviceToken: String? // Token de identificación del dispositivo
)