package mx.tec.ecoquest_android

import CheckPopUp
import StatsViewModel
import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.activity.viewModels
import androidx.compose.runtime.remember
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.example.check.CompleteScreen
import com.example.check.ImpactScreen
import com.example.check.MainScreen
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.CoroutineScope
import mx.tec.ecoquest_android.Achievements.AchievementsViewModel
import mx.tec.ecoquest_android.Achievements.TrophiesViewModel
import mx.tec.ecoquest_android.Login.LoginScreenContent.LoginScreenContent
import mx.tec.ecoquest_android.Screens.LeaderboardScreen
import mx.tec.ecoquest_android.functionGoogle.sign_in.GoogleAuthUiClient
import mx.tec.ecoquest_android.Mission.MissionViewModel
import mx.tec.ecoquest_android.CurrentUserProgress.CurrentUserViewModel
import mx.tec.ecoquest_android.DataGraph.DataGraphViewModel
import mx.tec.ecoquest_android.Mission.ChangeMission
import mx.tec.ecoquest_android.Mission.GetNewOptionalMissionViewModel
import mx.tec.ecoquest_android.Mission.OptionalMissionViewModel
import mx.tec.ecoquest_android.Screens.AchievementsScreen
import mx.tec.ecoquest_android.Screens.StatisticsScreen
import mx.tec.ecoquest_android.UsersForLeaderboard.LeaderboardViewModel
import mx.tec.ecoquest_android.ui.theme.Ecoquest_androidTheme

class MainActivity : ComponentActivity() {
    // Inicializamos el ViewModel
    private val missionViewModel: MissionViewModel by viewModels()
    private val leaderboardViewModel: LeaderboardViewModel by viewModels()
    private val currentUserViewModel: CurrentUserViewModel by viewModels()
    private val statsViewModel: StatsViewModel by viewModels()
    private val optionalMissionViewModel: OptionalMissionViewModel by viewModels()
    private val getNewOptionalMissionViewModel: GetNewOptionalMissionViewModel by viewModels()
    private val changeMission: ChangeMission by viewModels()
    private val dataGraphViewModel: DataGraphViewModel by viewModels()
    private val achievementsViewModel: AchievementsViewModel by viewModels()
    private val trophiesViewModel: TrophiesViewModel by viewModels()

    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext),
        )
    }

    private lateinit var sharedPreferences: SharedPreferences

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        // Permitir personalizar cómo se ajustan las barras del sistema
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // Ocultar las barras de navegación y de estado (modo inmersivo)
        window.decorView.windowInsetsController!!.hide(
            android.view.WindowInsets.Type.statusBars()
                    or android.view.WindowInsets.Type.navigationBars()
        )

        // Inicializamos el SharedPreferences
        sharedPreferences = getSharedPreferences("USER_UID", Context.MODE_PRIVATE)

        setContent {
            Ecoquest_androidTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MyApp(
                        modifier = Modifier.padding(innerPadding),
                        sharedPreferences = sharedPreferences,
                        lifecycleScope = lifecycleScope,
                        googleAuthUiClient = googleAuthUiClient,
                        missionViewModel = missionViewModel,
                        leaderboardViewModel = leaderboardViewModel,
                        currentUserViewModel = currentUserViewModel,
                        statsViewModel = statsViewModel,
                        optionalMissionViewModel = optionalMissionViewModel,
                        getNewOptionalMissionViewModel = getNewOptionalMissionViewModel,
                        changeMission = changeMission,
                        dataGraphViewModel = dataGraphViewModel,
                        achievementsViewModel = achievementsViewModel,
                        trophiesViewModel = trophiesViewModel
                    )
                }
            }
        }
    }
}

@Composable
fun MyApp(
    modifier: Modifier = Modifier,
    sharedPreferences: SharedPreferences,
    lifecycleScope: CoroutineScope,
    googleAuthUiClient: GoogleAuthUiClient,
    missionViewModel: MissionViewModel,
    leaderboardViewModel: LeaderboardViewModel,
    currentUserViewModel: CurrentUserViewModel,
    statsViewModel: StatsViewModel,
    optionalMissionViewModel: OptionalMissionViewModel,
    getNewOptionalMissionViewModel: GetNewOptionalMissionViewModel,
    changeMission: ChangeMission,
    dataGraphViewModel: DataGraphViewModel,
    achievementsViewModel: AchievementsViewModel,
    trophiesViewModel: TrophiesViewModel
) {

    // Verificar si el usuario ya inició sesión
    val uid = remember { sharedPreferences.getString("USER_UID", null) }
    val startDestination = if (uid != null) "mainScreen" else "login"

    // Limpiar SharedPreferences
//    val editor = sharedPreferences.edit()
//    editor.clear()
//    editor.apply()

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = startDestination) {
        composable("login") {
            LoginScreenContent(
                sharedPreferences = sharedPreferences,
                lifecycleScope = lifecycleScope,
                navController = navController,
                googleAuthUiClient = googleAuthUiClient,
            )
        }

        // Pantalla de misiones
        composable("mainScreen") {
            MainScreen(
                googleAuthUiClient = googleAuthUiClient,
                navController = navController,
                missionViewModel = missionViewModel,
                leaderboardViewModel = leaderboardViewModel,
                currentUserViewModel = currentUserViewModel,
                statsViewModel = statsViewModel,
                optionalMissionViewModel = optionalMissionViewModel,
                getNewOptionalMissionViewModel = getNewOptionalMissionViewModel,
                changeMission = changeMission,
                dataGraphViewModel = dataGraphViewModel,
                achievementsViewModel = achievementsViewModel,
                trophiesViewModel = trophiesViewModel
            )
        }

        // Pantalla de estadística
        composable("statisticScreen") {
            StatisticsScreen(
                navController = navController,
                dataGraphViewModel = dataGraphViewModel,
                statsViewModel = statsViewModel
            )
        }

        // Pantalla de logros
        composable("achievements") {
            AchievementsScreen(
                statsViewModel = statsViewModel,
                achievementsViewModel = achievementsViewModel,
                trophiesViewModel = trophiesViewModel,
                navController = navController
            )
        }

        // Pantalla de leaderboard
        composable("leaderboardScreen") {
            LeaderboardScreen(
                navController = navController,
                leaderboardViewModel = leaderboardViewModel,
                googleAuthUiClient = googleAuthUiClient
            )
        }

        // Pantalla que se despliega cuando se completa una misión
        composable("checkPopUp") {
            CheckPopUp(
                navController = navController
            )
        }

        // Pantalla de impacto
        composable("impactScreen") {
            ImpactScreen(
                googleAuthUiClient = googleAuthUiClient,
                navController = navController,
                missionViewModel = missionViewModel,
                currentUserViewModel = currentUserViewModel,
            )
        }

        // Pantalla de compartir contenido
        composable("completeScreen") {
            CompleteScreen(
                navController = navController,
                googleAuthUiClient = googleAuthUiClient,
                missionViewModel = missionViewModel,
                currentUserViewModel = currentUserViewModel,
            )
        }
    }
}