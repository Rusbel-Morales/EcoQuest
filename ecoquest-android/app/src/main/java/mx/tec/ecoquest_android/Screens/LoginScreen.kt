package mx.tec.ecoquest_android.functionGoogle.sign_in

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.tec.ecoquest_android.R
import mx.tec.ecoquest_android.Screens.LoadingScreen

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    state: SignInState,
    onSignInClick: () -> Unit
) {
    var isLoading by remember { mutableStateOf(false) }

    // Implementación de funcionalidad de inicio de sesión
    val context = LocalContext.current
    LaunchedEffect(key1 = state.signInError) {
        state.signInError?.let { error ->
            Toast.makeText(
                context,
                error,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    val googlelogin = painterResource(id = R.drawable.googlelogin)
    val tree = painterResource(id = R.drawable.arbol)
    val logo = painterResource(id = R.drawable.logo)
    val logonegro = painterResource(id = R.drawable.logonegro)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 5.dp)
            .padding(top = 15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        //if (isLoading) {
        //    LoadingScreen()
        //} else {
            Spacer(
                modifier = Modifier
                    .height(30.dp)
            )
            Image(
                painter = logonegro,
                contentDescription = null,
                modifier = Modifier
                    .graphicsLayer(alpha = 0.15f) // Nos ayuda a implementar opacidad directamente
                    .size(width = 110.dp, height = 60.dp)
                    .padding(start = 10.dp)
                    .align(Alignment.Start)
            )
            Spacer(
                modifier = Modifier
                    .height(50.dp)
            )
            Image(
                painter = logo,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .size(width = 100.dp, height = 70.dp)
            )
            Spacer(
                modifier = Modifier
                    .height(50.dp)
            )
            Text(
                text = "¡Ayúdanos a hacer\n un mundo mejor!",
                color = Color(0xFF5C5C5C),
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(
                modifier = Modifier
                    .height(60.dp)
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFBEDC74))
                    .fillMaxWidth(0.95f) // format
                    .height(160.dp),
            ) {
                Text(
                    text = "Iniciar Sesión",
                    fontSize = 24.sp,
                    color = Color(0, 0, 0),
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .padding(20.dp)
                        .align(Alignment.TopCenter)
                )
                IconButton(
                    onClick = {
                        //isLoading = true
                        onSignInClick()
                    },
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(top = 10.dp)
                        .size(width = 200.dp, height = 45.dp)
                        .clip(RoundedCornerShape(16.dp))
                ) {
                    Image(
                        painter = googlelogin,
                        contentDescription = null,
                    )
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxHeight()
            ) {
                Image(
                    painter = tree,
                    contentDescription = null,
                    modifier = Modifier
                        .size(175.dp)
                        .align(Alignment.BottomCenter)
                )
            }
        //}
    }
}