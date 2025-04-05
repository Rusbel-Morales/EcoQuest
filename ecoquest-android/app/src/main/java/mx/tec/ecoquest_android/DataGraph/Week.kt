package mx.tec.ecoquest_android.DataGraph

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DinamycWeek(
    isLastSubList: Boolean,
    weekNumber: Int
) {
    val number = if (weekNumber < 10) "0$weekNumber" else "$weekNumber"
    Row (
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .background(Color(0xFF000000))
                .fillMaxHeight(0.8f)
                .width(1.dp)
        )
        Text(
            text = "Semana\n$number",
            textAlign = TextAlign.Center,
            color = Color(0xFF000000),
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .padding(horizontal = 20.dp)
        )
    }
    if (isLastSubList) {
        Box(
            modifier = Modifier
                .background(Color(0xFF000000))
                .fillMaxHeight(0.8f)
                .width(1.dp)
        )
    }
}