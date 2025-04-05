package mx.tec.ecoquest_android.Login.LoginScreenContent

import android.app.Activity.RESULT_OK
import android.content.SharedPreferences
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import mx.tec.ecoquest_android.FirebaseMessagingService.getDeviceToken
import mx.tec.ecoquest_android.Screens.ErrorDialog
import mx.tec.ecoquest_android.functionGoogle.sign_in.DataHttpRequest
import mx.tec.ecoquest_android.functionGoogle.sign_in.GoogleAuthUiClient
import mx.tec.ecoquest_android.functionGoogle.sign_in.LoginScreen
import mx.tec.ecoquest_android.functionGoogle.sign_in.SignInViewModel
import mx.tec.ecoquest_android.HttpRequest.sendIdAndTokenToServer
import mx.tec.ecoquest_android.Screens.LoadingScreen
import mx.tec.ecoquest_android.Screens.LogInCode

@Composable
fun LoginScreenContent(
    sharedPreferences: SharedPreferences,
    lifecycleScope: CoroutineScope,
    navController: NavController,
    googleAuthUiClient: GoogleAuthUiClient,
) {
    var isLoading by remember { mutableStateOf(false) }
    val viewModel = viewModel<SignInViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            if (result.resultCode == RESULT_OK) {
                lifecycleScope.launch {
                    val signInResult = googleAuthUiClient.signInWithIntent(
                        intent = result.data ?: return@launch
                    )
                    viewModel.onSignInResult(signInResult)
                }
            }
        }
    )

    var showLogInCodeDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var canProceed by remember { mutableStateOf(false) }
    var showErrorCode by remember { mutableStateOf(false) }
    var sendCode by remember { mutableStateOf("") }

    LaunchedEffect(state.isSignInSuccessful) {
        if (state.isSignInSuccessful) {
            showLogInCodeDialog = true
        }
    }

    if (showLogInCodeDialog) {
        LogInCode(
            sendCode = { code ->
                if (!code.isNullOrEmpty()) {
                    sendCode = code
                    showLogInCodeDialog = false
                    canProceed = true
                }
                else {
                    showErrorCode = true

                }
                Log.d("LoginScreenContent", "Code: ${code?.uppercase()}")
            },
            onCancel = {
                showLogInCodeDialog = false
                canProceed = true
            }
        )
    }

    LaunchedEffect(canProceed) {
        if (canProceed) {
            // Resetear canProceed para evitar múltiples ejecuciones
            canProceed = false

            // Obtener el token de la cuenta de Google del usuario y el ID proporcionado por Firebase
            val token = googleAuthUiClient.getSignedInUser()?.idToken
            val uid = googleAuthUiClient.getSignedInUser()?.uid

            if (token != null && uid != null) {
                // Almacenar el UID en SharedPreferences
                sharedPreferences.edit().putString("USER_UID", uid).apply()

                // Obtener token del dispositivo de forma asíncrona
                getDeviceToken { deviceToken ->
                    val dataHttpRequest = DataHttpRequest(uid, token, deviceToken, sendCode)
                    sendIdAndTokenToServer(dataHttpRequest) { success ->
                        if (success) {
                            navController.navigate("mainScreen")
                        } else {
                            showErrorDialog = true

                        }
                    }
                }
            } else {
                showErrorDialog = true
            }
        }
    }

    if (showErrorCode) {
        ErrorDialog(
            showDialog = showErrorCode,
            onDismiss = { showErrorCode = false },
            errorMessage = "No puedes enviar un código vacío."
        )
    }

    if (showErrorDialog) {
        ErrorDialog(
            showDialog = showErrorDialog,
            onDismiss = { showErrorDialog = false },
            errorMessage = "Ha ocurrido un error inesperado \nIntente de nuevo."
        )
        state.isSignInSuccessful = false
        isLoading = false

    }

    if (isLoading) {
        LoadingScreen()
    } else {
        LoginScreen(
            state = state,
            onSignInClick = {
                isLoading = true
                lifecycleScope.launch {
                    val signInIntentSender = googleAuthUiClient.signIn()
                    launcher.launch(
                        IntentSenderRequest.Builder(
                            signInIntentSender ?: return@launch
                        ).build()
                    )
                }
            }
        )
    }
}