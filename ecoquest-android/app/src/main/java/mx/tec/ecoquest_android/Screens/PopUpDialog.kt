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
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DescriptionBlock(
    modifier: Modifier = Modifier,
    title: String,
    descp: String,
    onCancel: () -> Unit
) {
    BasicAlertDialog(
        modifier = modifier,
        onDismissRequest = { onCancel() }

    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(),
            tonalElevation = AlertDialogDefaults.TonalElevation,
            color = Color(0xFFF69D6B),
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
                        text = title,
                        fontSize = 27.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        lineHeight = 40.sp,
                        color = Color(0xFF000000),
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
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
                        text = descp,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center,
                        lineHeight = 24.sp,
                        color = Color.Black
                    )
                }
                Spacer(
                    modifier = Modifier
                        .height(30.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = onCancel,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth(.7f)
                            .height(60.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFFFFFF),  // Color de fondo
                            contentColor = Color(0xFF000000)    // Color del texto
                        ),

                        contentPadding = PaddingValues(12.dp)
                    ) {
                        Text(
                            text = "Cerrar",
                            fontSize = 22.sp
                        )
                    }
                }
            }
        }
    }
}


