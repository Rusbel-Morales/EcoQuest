package mx.tec.ecoquest_android.Screens

import StatsViewModel
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import mx.tec.ecoquest_android.Achievements.AchievementsViewModel
import mx.tec.ecoquest_android.Achievements.TrophiesViewModel
import mx.tec.ecoquest_android.R

@Composable
fun AchievementsScreen(
    modifier: Modifier = Modifier,
    statsViewModel: StatsViewModel,
    achievementsViewModel: AchievementsViewModel,
    trophiesViewModel: TrophiesViewModel,
    navController: NavController
) {

    // Cargamos las imágenes de los trofeos
    val goldTrophy = painterResource(id = R.drawable.trophy)
    val silverTrophy = painterResource(id = R.drawable.trophy)
    val bronzeTrophy = painterResource(id = R.drawable.trophy)

    // Cargamos las imágenes de los logros
    val firstAchievement = painterResource(id = R.drawable.leaf)
    val secondAchievement = painterResource(id = R.drawable.globe)
    val thirdAchievement = painterResource(id = R.drawable.star)
    val fourthAchievement = painterResource(id = R.drawable.crown)

    // Cargamos toda la información correspondiente a nuestro usuario actual
    val stats by statsViewModel.stats

    var xp by remember { mutableStateOf("") }
    var misiones by remember { mutableStateOf("") }
    var racha by remember { mutableStateOf("") }

    // Asignamos los valores obtenidos de currentUserState
    stats?.let { stat ->
        racha = stat.rachaMaxima.toString()
        xp = stat.totalPuntos.toString()
        misiones = stat.totalMisionesCompletadas.toString()
    }

    // Cargamos los datos de los trofeos
    val trophiesData by trophiesViewModel.trophiesState.collectAsState()

    var countGoldTrophy = 0
    var countSilverTrophy = 0
    var countBronzeTrophy = 0

    trophiesData?.trophyCount.let {
        if (it != null) {
            countGoldTrophy = it.total_oro
            countSilverTrophy = it.total_plata
            countBronzeTrophy = it.total_bronce
        }
    }

    // Cargamos los datos de los logros
    val achievementsData by achievementsViewModel.achievementsState.collectAsState()

    // Filtros de color para los logros
    var colorFilter01: Triple<Color, ColorFilter, Color>? = null
    var colorFilter02: Triple<Color, ColorFilter, Color>? = null
    var colorFilter03: Triple<Color, ColorFilter, Color>? = null
    var colorFilter04: Triple<Color, ColorFilter, Color>? = null

    achievementsData?.achievements.let {
        if (it != null) {
            colorFilter01 = if (it[0].isCompleted == 0) {
                Triple(Color(0xFFD9D9D9), ColorFilter.tint(Color(0xFF9B9B9B)), Color(0xFF9B9B9B))
            } else {
                null
            }
            colorFilter02 = if (it[1].isCompleted == 0) {
                Triple(Color(0xFFD9D9D9), ColorFilter.tint(Color(0xFF9B9B9B)), Color(0xFF9B9B9B))
            } else {
                null
            }
            colorFilter03 = if (it[2].isCompleted == 0) {
                Triple(Color(0xFFD9D9D9), ColorFilter.tint(Color(0xFF9B9B9B)), Color(0xFF9B9B9B))
            } else {
                null
            }
            colorFilter04 = if (it[3].isCompleted == 0) {
                Triple(Color(0xFFD9D9D9), ColorFilter.tint(Color(0xFF9B9B9B)), Color(0xFF9B9B9B))
            } else {
                null
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFFFF))
            .padding(top = 44.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(Color(0xFFBEDC74)),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        navController.popBackStack()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color(0xFF000000)
                    ),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Regresar",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            Text(
                text = "Trofeos",
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(bottom = 12.dp)
            )
        }

        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .height(170.dp),
            horizontalArrangement = Arrangement.Center
        ) {

            // 1er Trofeo
            Column(
                modifier = modifier
                    .padding(bottom = 8.dp, start = 4.dp)
                    .align(Alignment.Bottom),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 12.dp
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFFFFFFF))
                            .size(110.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = silverTrophy,
                            contentDescription = "Silver Trophy",
                            colorFilter = ColorFilter.tint(Color(0xFF808080)),
                            modifier = Modifier
                                .size(80.dp)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .background(Color(0xFF000000))
                            .width(110.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "$countSilverTrophy plata",
                            textAlign = TextAlign.Center,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFFFFFFFF),
                            modifier = Modifier
                                .padding(2.dp)
                        )
                    }
                }
            }

            // 2do Trofeo
            Column(
                modifier = modifier
                    .padding(8.dp)
                    .align(Alignment.Top),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 12.dp
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFFFFFFF))
                            .size(110.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = goldTrophy,
                            contentDescription = "Gold Trophy",
                            colorFilter = ColorFilter.tint(Color(0xFFEFB810)),
                            modifier = Modifier
                                .size(80.dp)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .background(Color(0xFF000000))
                            .width(110.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "$countGoldTrophy oro",
                            textAlign = TextAlign.Center,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFFFFFFFF),
                            modifier = Modifier
                                .padding(2.dp)
                        )
                    }
                }
            }

            // 3er Trofeo
            Column(
                modifier = modifier
                    .padding(bottom = 8.dp, end = 4.dp)
                    .align(Alignment.Bottom),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 12.dp
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFFFFFFF))
                            .size(110.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = bronzeTrophy,
                            contentDescription = "Bronze Trophy",
                            colorFilter = ColorFilter.tint(Color(0xFFBF8970)),
                            modifier = Modifier
                                .size(80.dp)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .background(Color(0xFF000000))
                            .width(110.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "$countBronzeTrophy bronce",
                            textAlign = TextAlign.Center,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFFFFFFFF),
                            modifier = Modifier
                                .padding(2.dp)
                        )
                    }
                }
            }
        }

        Row(
            modifier = modifier
                .fillMaxWidth()
                .background(Color(0xFFBEDC74)),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Logros",
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 8.dp)
            )
        }

        Spacer(
            modifier = Modifier
                .height(8.dp)
        )

        LazyColumn(
            modifier = modifier
                .weight(1f)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Column(
                    modifier = Modifier
                        .padding(top = 12.dp)
                ) {
                    Card(
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 12.dp
                        ),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFF7E96C)
                        )
                    ) {
                        Spacer(
                            modifier = Modifier
                                .height(8.dp)
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            // Logro 01
                            Card(
                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = 12.dp
                                ),
                                colors = colorFilter01?.let {
                                    CardDefaults.cardColors(
                                        containerColor = it.first
                                    )
                                } ?: CardDefaults.cardColors(
                                    containerColor = Color(0xFFBEDC74)
                                ),
                                border = BorderStroke(1.dp, Color(0xFF000000)),
                                modifier = Modifier
                                    .size(width = 150.dp, height = 160.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Image(
                                        painter = firstAchievement,
                                        colorFilter = colorFilter01?.second,
                                        contentDescription = "First Achievement",
                                        modifier = Modifier
                                            .size(70.dp)
                                    )
                                    Text(
                                        text = "EcoPrincipiante",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        textAlign = TextAlign.Center,
                                        color = colorFilter01?.third ?: Color(0xFF000000),
                                        modifier = Modifier
                                            .padding(horizontal = 8.dp)
                                    )

                                    Spacer(
                                        modifier = Modifier
                                            .height(4.dp)
                                    )

                                    Text(
                                        text = "Completaste tu\nprimera misión",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        textAlign = TextAlign.Center,
                                        color = colorFilter01?.third ?: Color(0xFF000000),
                                        lineHeight = 12.sp,
                                        modifier = Modifier
                                            .padding(bottom = 8.dp)
                                    )
                                }
                            }

                            Spacer(
                                modifier = Modifier
                                    .width(12.dp)
                            )

                            // Logro 02
                            Card(
                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = 12.dp
                                ),
                                colors = colorFilter02?.let {
                                    CardDefaults.cardColors(
                                        containerColor = it.first
                                    )
                                } ?: CardDefaults.cardColors(
                                    containerColor = Color(0xFFBEDC74)
                                ),
                                border = BorderStroke(1.dp, Color(0xFF000000)),
                                modifier = Modifier
                                    .size(width = 150.dp, height = 160.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Image(
                                        painter = secondAchievement,
                                        colorFilter = colorFilter02?.second,
                                        contentDescription = "Second Achievement",
                                        modifier = Modifier
                                            .size(70.dp)
                                    )
                                    Text(
                                        text = "EcoExplorador",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        textAlign = TextAlign.Center,
                                        color = colorFilter02?.third ?: Color(0xFF000000),
                                        modifier = Modifier
                                            .padding(horizontal = 8.dp)
                                    )

                                    Spacer(
                                        modifier = Modifier
                                            .height(4.dp)
                                    )

                                    Text(
                                        text = "Completaste un total\nde 5 misiones",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        textAlign = TextAlign.Center,
                                        color = colorFilter02?.third ?: Color(0xFF000000),
                                        lineHeight = 12.sp,
                                        modifier = Modifier
                                            .padding(bottom = 8.dp)
                                    )
                                }
                            }
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {

                            // Logro 03
                            Card(
                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = 12.dp
                                ),
                                colors = colorFilter03?.let {
                                    CardDefaults.cardColors(
                                        containerColor = it.first
                                    )
                                } ?: CardDefaults.cardColors(
                                    containerColor = Color(0xFFBEDC74)
                                ),
                                border = BorderStroke(1.dp, Color(0xFF000000)),
                                modifier = Modifier
                                    .size(width = 150.dp, height = 160.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Image(
                                        painter = thirdAchievement,
                                        colorFilter = colorFilter03?.second,
                                        contentDescription = "Third Achievement",
                                        modifier = Modifier
                                            .size(70.dp)
                                    )
                                    Text(
                                        text = "EcoHéroe",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        textAlign = TextAlign.Center,
                                        color = colorFilter03?.third ?: Color(0xFF000000),
                                        modifier = Modifier
                                            .padding(horizontal = 8.dp)
                                    )

                                    Spacer(
                                        modifier = Modifier
                                            .height(4.dp)
                                    )

                                    Text(
                                        text = "Completaste un total\nde 10 misiones",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        textAlign = TextAlign.Center,
                                        color = colorFilter03?.third ?: Color(0xFF000000),
                                        lineHeight = 12.sp,
                                        modifier = Modifier
                                            .padding(bottom = 8.dp)
                                    )
                                }
                            }

                            Spacer(
                                modifier = Modifier
                                    .width(12.dp)
                            )

                            // Logro 04
                            Card(
                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = 12.dp
                                ),
                                colors = colorFilter04?.let {
                                    CardDefaults.cardColors(
                                        containerColor = it.first
                                    )
                                } ?: CardDefaults.cardColors(
                                    containerColor = Color(0xFFBEDC74)
                                ),
                                border = BorderStroke(1.dp, Color(0xFF000000)),
                                modifier = Modifier
                                    .size(width = 150.dp, height = 160.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Image(
                                        painter = fourthAchievement,
                                        colorFilter = colorFilter04?.second,
                                        contentDescription = "Fourth Achievement",
                                        modifier = Modifier
                                            .size(70.dp)
                                    )
                                    Text(
                                        text = "EcoMaestro",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        textAlign = TextAlign.Center,
                                        color = colorFilter04?.third ?: Color(0xFF000000),
                                        modifier = Modifier
                                            .padding(horizontal = 8.dp)
                                    )

                                    Spacer(
                                        modifier = Modifier
                                            .height(4.dp)
                                    )

                                    Text(
                                        text = "Completaste un total\nde 20 misiones",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        textAlign = TextAlign.Center,
                                        color = colorFilter04?.third ?: Color(0xFF000000),
                                        lineHeight = 12.sp,
                                        modifier = Modifier
                                            .padding(bottom = 8.dp)
                                    )
                                }
                            }
                        }

                        Spacer(
                            modifier = Modifier
                                .height(8.dp)
                        )
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .padding(vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .background(Color(0xFFBEDC74)),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Estadísticas",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                )
            }

            Row(
                modifier = modifier
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "EcoXP total",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 12.dp, bottom = 8.dp)
                )

                Spacer(
                    modifier = Modifier
                        .weight(1f)
                )

                Text(
                    text = xp,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 12.dp, bottom = 8.dp)
                )
            }

            // Linea negra
            BlackLine()

            Row(
                modifier = modifier
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Misiones completadas",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 12.dp, bottom = 8.dp)
                )

                Spacer(
                    modifier = Modifier
                        .weight(1f)
                )

                Text(
                    text = misiones,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 12.dp, bottom = 8.dp)
                )
            }

            // Linea negra
            BlackLine()

            Row(
                modifier = modifier
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Racha más larga",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 12.dp, bottom = 8.dp)
                )

                Spacer(
                    modifier = Modifier
                        .weight(1f)
                )

                Text(
                    text = racha,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 12.dp, bottom = 8.dp)
                )
            }

            // Linea negra
            BlackLine()
        }
    }
}

@Composable
fun BlackLine() {
    Surface(
        modifier = Modifier
            .fillMaxWidth(1f)
            .height(1.dp),
        shadowElevation = 4.dp
    ) {
        Box(
            modifier = Modifier
                .background(Color(0xFF000000))
        )
    }
}