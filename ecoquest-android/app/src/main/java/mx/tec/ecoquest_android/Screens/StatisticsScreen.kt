package mx.tec.ecoquest_android.Screens

import StatsViewModel
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import mx.tec.ecoquest_android.R
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter
import mx.tec.ecoquest_android.DataGraph.DataGraphViewModel
import android.graphics.Color as AndroidColor

class DayValueFormatter(private val days: List<String?>) : ValueFormatter() {
    override fun getAxisLabel(value: Float, axis: AxisBase?): String? {
        return if (value.toInt() in days.indices) {
            days[value.toInt()]
        } else {
            ""
        }
    }
}

@Composable
fun StatisticsScreen(
    navController: NavController,
    dataGraphViewModel: DataGraphViewModel,
    statsViewModel: StatsViewModel
) {

    // Convertir los datos a un día en específico
    val data by dataGraphViewModel.dataGraphState.collectAsState()

    // Obtener la racha actual del usuario
    val statistics by statsViewModel.stats

    // Crear una lista de los dias de la semana
    val xData = data?.map { it.dia } ?: emptyList() // Datos para el eje X (pueden ser fechas o cualquier valor)
    val yData = data?.map { it.puntos_totales?.toFloat() } ?: emptyList() // Datos para el eje Y (pueden ser puntuaciones o valores relacionados
    val dataLabel = "Progreso de la última semana" // Etiqueta de los datos
    val ncalendar = painterResource(id = R.drawable.nav_calendar)
    val ntrophy = painterResource(id = R.drawable.nav_trophy)
    val ntarget = painterResource(id = R.drawable.nav_target)
    val nleaderboard = painterResource(id = R.drawable.nav_leaderboards)
    val logonegro = painterResource(id = R.drawable.logonegro)

    // Obtener la racha actual del usuario
    val currentStreak: Int = statistics?.rachaActual ?: 0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFFFF))
            .padding(top = 44.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .background(Color(0xFFBEDC74))
                .fillMaxWidth()
                .height(80.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Estadísticas",
                fontSize = 28.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = logonegro,
                contentDescription = null,
                modifier = Modifier
                    .graphicsLayer(alpha = 0.15f)
                    .size(width = 80.dp, height = 50.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (xData.isNotEmpty() && yData.isNotEmpty()) {
                AndroidView(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(0.8f),
                    factory = { context ->
                        val chart = LineChart(context) // Inicilizar la gráfica
//                    val entries = xData.zip(yData) { x, y -> Entry(x, y) }
                        val entries = xData.indices.map { index ->
                            Entry(index.toFloat(), yData[index] ?: 0f)
                        }
                        val dataSet =
                            LineDataSet(
                                entries,
                                dataLabel
                            ) // Crear un conjunto de datos de entradas

                        // Configuración del aspecto de los datos
                        dataSet.color =
                            AndroidColor.parseColor("#08A4A7") // Color de la linea que une los puntos
                        dataSet.lineWidth = 3f // Grosor de la linea que une los puntos
                        dataSet.circleRadius = 7f // Grosor del circulo de puntos
                        dataSet.setCircleColors(AndroidColor.parseColor("#08A4A7"))
                        // Deshabilitar las etiquetas de los puntos
                        dataSet.setDrawValues(false)

                        // Configuración de los datos del gráfico
                        chart.data = LineData(dataSet)
                        chart.description.text = ""

                        // Separar la etiqueta de los gráfica
                        chart.extraBottomOffset = 24f
                        chart.legend.textSize = 16f
                        chart.legend.formSize = 16f
                        chart.legend.formToTextSpace = 12f

                        // Configuración del eje X
                        val xAxis = chart.xAxis
                        xAxis.position = XAxis.XAxisPosition.BOTTOM
                        xAxis.setDrawAxisLine(true)
                        xAxis.axisLineColor = AndroidColor.parseColor("#000000")
                        xAxis.axisLineWidth = 3f


                        // Configurar las entradas del eje X de la gráfica
                        xAxis.valueFormatter = DayValueFormatter(xData)
                        Log.d("xAxix", xAxis.valueFormatter.toString())

                        // Configuración del eje Y (lado izquierdo)
                        val leftAxis = chart.axisLeft
                        leftAxis.setDrawAxisLine(true)
                        leftAxis.axisLineColor = AndroidColor.parseColor("#000000")
                        leftAxis.axisLineWidth = 3f

                        // Configuración del eje Y (lado derecho)
                        val rightAxis = chart.axisRight
                        rightAxis.isEnabled = false

                        // Actualizar y devolver el gráfico
                        chart.invalidate()
                        chart
                    }
                )
                Button(
                    onClick = {
                        navController.navigate("achievements")
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFA2CA71),
                        contentColor = Color(0xFF000000)
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 4.dp
                    ),
                    modifier = Modifier
                        .padding(top = 40.dp, bottom = 16.dp)
                ) {
                    Text(
                        text = "Ver logros",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF000000)
                    )

                }

                Spacer(
                    modifier = Modifier
                        .height(12.dp)
                )
                // Barra negra
                Box(
                    modifier = Modifier
                        .background(Color(0xFF000000))
                        .fillMaxWidth(0.95f)
                        .height(1.dp)
                        .align(Alignment.CenterHorizontally)
                )

                // Apartado estático de la racha actual
                Row(
                    modifier = Modifier
                        .padding(
                            top = 10.dp,
                            bottom = 5.dp,
                            start = 20.dp,
                            end = 20.dp
                        )
                ) {
                    Text(
                        text = "Racha\nactual",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(
                        modifier = Modifier
                            .weight(1f)
                    )
                    Text(
                        text = "$currentStreak",
                        fontSize = 48.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            else {
                LoadingDataGraphScreen(
                    modifier = Modifier
                        .weight(1f)
                )
            }

            // Barra de navegación
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier
                    .background(Color(0xFFBEDC74))
            ) {
                Button(
                    onClick = {
                        navController.navigate("impactScreen")
                    },
                    modifier = Modifier
                        .height(80.dp)
                        .weight(1f),
                    shape = RectangleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFBEDC74)
                    ),
                    contentPadding = PaddingValues(5.dp)
                ) {
                    Image(
                        painter = ncalendar,
                        contentDescription = "Calendario Icon",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(5.dp),
                        colorFilter = ColorFilter.tint(Color.Black)
                    )
                }
                Button(
                    onClick = {
                        navController.navigate("mainScreen")
                    },
                    modifier = Modifier
                        .height(80.dp)
                        .weight(1f),
                    shape = RectangleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFBEDC74)
                    ),
                    contentPadding = PaddingValues(5.dp)

                ) {
                    Image(
                        painter = ntarget,
                        contentDescription = "Objetivos Icon",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(5.dp),
                        colorFilter = ColorFilter.tint(Color.Black)
                    )
                }
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .padding(2.dp),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 8.dp
                    )
                ) {
                    Button(
                        onClick = { /* Nothing */ },
                        modifier = Modifier
                            .height(80.dp)
                            .shadow(12.dp),
                        shape = RectangleShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFA2CA71)
                        ),
                        contentPadding = PaddingValues(5.dp)
                    ) {
                        Image(
                            painter = ntrophy,
                            contentDescription = "Logros Icon",
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(5.dp),
                            colorFilter = ColorFilter.tint(Color.Black)
                        )
                    }
                }
                Button(
                    onClick = {
                        navController.navigate("leaderboardScreen")
                    },
                    modifier = Modifier
                        .height(80.dp)
                        .weight(1f),
                    shape = RectangleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFBEDC74)
                    ),
                    contentPadding = PaddingValues(5.dp)

                ) {
                    Image(
                        painter = nleaderboard,
                        contentDescription = "Ranking Icon",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(5.dp),
                        colorFilter = ColorFilter.tint(Color.Black)
                    )
                }
            }
        }
    }
}