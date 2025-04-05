package mx.tec.ecoquest_android.functionGoogle.sign_in

data class SignInState (
    // Nos verifica si el inicio de sesión fue exitoso
    var isSignInSuccessful: Boolean = false,
    val signInError: String? = null,
)