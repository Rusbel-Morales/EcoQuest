package mx.tec.ecoquest_android.Screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.*
import androidx.compose.runtime.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogInCode(
    modifier: Modifier = Modifier,
    sendCode: (String?) -> Unit,
    onCancel: () -> Unit
) {
    var text by remember { mutableStateOf("") } // Estado para almacenar el texto ingresado
    BasicAlertDialog(
        onDismissRequest = { onCancel() },
        modifier = modifier
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(),
            tonalElevation = AlertDialogDefaults.TonalElevation,
            color = Color(0xFFBEDC74),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = modifier
                    .padding(5.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "¡Bienvenido!",
                        fontSize = 27.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                        lineHeight = 40.sp,
                        color = Color(0xFF000000)
                    )
                }
                Spacer(
                    modifier = Modifier
                        .height(10.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Si alguien te lo recomendó, puedes ingresar su codigo aqui",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center,
                        lineHeight = 24.sp,
                        color = Color.Black
                    )
                }
                Spacer(
                    modifier = Modifier
                        .height(10.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    TextField(
                        value = text,
                        onValueChange = { newText -> text = newText }, // Actualiza el texto
                        label = { Text("Escribe el código aquí") }, // Etiqueta del campo de texto
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp)
                    )
                }
                Spacer(
                    modifier = Modifier
                        .height(15.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                )
                {

                    Button(
                        onClick = {sendCode(text) },
                        shape = RoundedCornerShape(16.dp),
                        //elevation = ButtonDefaults.elevation(8.dp),
                        modifier = Modifier
                            .padding(5.dp)
                            .fillMaxWidth(.7f)
                            .height(60.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,  // Color de fondo
                            contentColor = Color.Black    // Color del texto
                        ),

                        contentPadding = PaddingValues(12.dp)
                    ) {
                        Text(
                            text = "¡Enviar!",
                            fontSize = 22.sp
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                )
                {
                    Button(
                        onClick = onCancel,
                        shape = RoundedCornerShape(16.dp),
                        //elevation = ButtonDefaults.elevation(8.dp),
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth(.7f)
                            .height(60.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFF69D6B),  // Color de fondo
                            contentColor = Color.Black   // Color del texto
                        ),

                        contentPadding = PaddingValues(12.dp)
                    ) {
                        Text(
                            text = "No tengo codigo",
                            fontSize = 22.sp
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDescriptionBlock() {
    LogInCode(
        sendCode = {},
        onCancel = {}
    )
}

