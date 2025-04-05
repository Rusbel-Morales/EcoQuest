package mx.tec.ecoquest_android.HttpRequest

import mx.tec.ecoquest_android.Achievements.Achievements
import mx.tec.ecoquest_android.Achievements.Trophies
import mx.tec.ecoquest_android.Achievements.Trophy
import mx.tec.ecoquest_android.CurrentUserProgress.CurrentUser
import mx.tec.ecoquest_android.DataGraph.DataGraph
import mx.tec.ecoquest_android.functionGoogle.sign_in.DataHttpRequest
import mx.tec.ecoquest_android.functionGoogle.sign_in.UpdateDeviceToken
import mx.tec.ecoquest_android.Mission.MarkMissionAsCompleted
import mx.tec.ecoquest_android.Mission.Mission
import mx.tec.ecoquest_android.Mission.MissionResponse
import mx.tec.ecoquest_android.Mission.MissionResponseChanged
import mx.tec.ecoquest_android.Mission.SendUserIdAndIdMission
import mx.tec.ecoquest_android.UsersForLeaderboard.LeaderboardResponse
import mx.tec.ecoquest_android.statsModel.Stats
import retrofit2.Call
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

// Declaramos el conjunto de métodos que se deben implementar en la clase que se encargará de realizar la petición
interface APIInterface {
    @POST("/auth/auth-user") // Reemplazamos con el endpoint real
    fun sendToken(@Body dataHttpRequest: DataHttpRequest): Call<ResponseBody>

    @POST("/missions/complete-mission") // Reemplazamos con el endpoint real
    fun markMissionAsCompleted(@Body markMissionAsCompleted: MarkMissionAsCompleted): Call<ResponseBody>

    // TODO: Pendiente por probar
    @PUT("") // Reemplazamos con el endpoint real
    fun sendUpdateDeviceToken(@Body updateDeviceToken: UpdateDeviceToken): Call<ResponseBody>

    @GET("/missions/get-daily-mission")
    fun sendUserIdForMission(@Query("userId") userId: String): Call<MissionResponse>

    @GET("/stats/get-leaderboard")
    fun getUsersForLeaderboard(): Call<LeaderboardResponse>

    @GET("/stats/get-xp-bar-data")
    fun getCurrentUserInformation(@Query("userId") userId: String): Call<CurrentUser>

    @GET("/stats/get-user-stats")
    fun getUsersForStats(@Query("userId") userId: String): Call<Stats>

    @GET("/missions/get-opt-missions")
    fun getOptionalMissions(@Query("userId") userId: String): Call<List<Mission>>

    @POST("/missions/reroll-opt-mission")
    fun getRerollOptionalMission(@Body sendUserIdAndIdMission: SendUserIdAndIdMission): Call<MissionResponseChanged>

    @POST("/missions/complete-opt-mission")
    suspend fun markOptionalMissionAsCompleted(@Body sendUserIdAndIdMission: SendUserIdAndIdMission): Response<MissionResponseChanged>

    @GET("/stats/get-points-day")
    fun getPointsByDate(@Query("userId") userId: String): Call<List<DataGraph>>

    @GET("/stats/get-user-achievements")
    fun getAchievementsCompleted(@Query("userId") userId: String): Call<Achievements>

    @GET("stats/get-user-trophies")
    fun getTotalTrophies(@Query("userId") userId: String): Call<Trophies>
}