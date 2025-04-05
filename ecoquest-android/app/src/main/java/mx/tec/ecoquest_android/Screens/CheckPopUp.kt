import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import mx.tec.ecoquest_android.R
import mx.tec.ecoquest_android.ui.theme.Ecoquest_androidTheme

@Composable
fun CheckPopUp(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    Log.d("Debuggear", "Debug")
    val planetImg = painterResource(id = R.drawable.verificado)
    val shareImg = painterResource(id = R.drawable.share)
    val xImg = painterResource(id = R.drawable.x_img)
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFBEDC74)) //Color.Green
            .padding(32.dp)
    ) {
        Spacer(
            modifier = Modifier
                .height(60.dp)
        )
        Image(
            painter = planetImg,
            contentDescription = "Planet",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth(),
            colorFilter = ColorFilter.tint(Color.White)
        )

        Text(
            text = "Misión completada\n" + "¡Buen Trabajo!",
            fontSize = 36.sp,
            color = Color.White,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.SemiBold,
            lineHeight = 40.sp
        )
        Spacer(
            modifier = Modifier
                .height(40.dp)
        )
        /*
        Text(
            text = "Tus contribuciones \n" +
                    "Hasta el día de hoy lograron modificar" +
                    " a 105 arboles",
            fontSize = 22.sp,
            color = Color.White,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.SemiBold
        )*/
        Spacer(
            modifier = Modifier
                .height(30.dp)
        )
        Box(

            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = {
                        navController.navigate("completeScreen") {
                            popUpTo("checkPopUp") { inclusive = true }
                    }
                },
                shape = RoundedCornerShape(16.dp),
                //elevation = ButtonDefaults.elevation(8.dp),
                modifier = Modifier.padding(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,  // Color de fondo
                    contentColor = Color(0xFFBEDC74)  // Color del texto
                ),

                contentPadding = PaddingValues(12.dp)
            ) {
                Image(
                    painter = shareImg,
                    contentDescription = "Forward",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .width(40.dp),
                    colorFilter = ColorFilter.tint(Color(0xFFBEDC74))
                )
                Text(
                    text = "Compartir",
                    fontSize = 22.sp
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = {
                    navController.navigate("mainScreen") {
                        popUpTo("checkPopUp") { inclusive = true }
                    }
                },
                shape = RoundedCornerShape(16.dp),
                //elevation = ButtonDefaults.elevation(8.dp),
                modifier = Modifier.padding(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,  // Color de fondo
                    contentColor = Color(0xFFBEDC74)  // Color del texto
                ),

                contentPadding = PaddingValues(12.dp)

            ) {
                Image(
                    painter = xImg,
                    contentDescription = "Close",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .width(40.dp),
                    colorFilter = ColorFilter.tint(Color(0xFFBEDC74))
                )
                Text(
                    text = "Cerrar",
                    fontSize = 22.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PlanetPreview() {
    val navController = rememberNavController()
    Ecoquest_androidTheme {
        CheckPopUp(
            navController = navController
        )
    }
}