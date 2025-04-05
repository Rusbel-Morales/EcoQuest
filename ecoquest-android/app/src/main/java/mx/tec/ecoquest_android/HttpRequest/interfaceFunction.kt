package mx.tec.ecoquest_android.HttpRequest

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.util.Log
import com.google.gson.Gson
import mx.tec.ecoquest_android.Achievements.Achievements
import mx.tec.ecoquest_android.Achievements.Trophies
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

// Función que envía el token de cuenta de Google del usuario al servidor
fun sendIdAndTokenToServer(dataHttpRequest: DataHttpRequest, callback: (Boolean) -> Unit) {
    val apiService = HttpInstance.instance

    Log.d("JSON", Gson().toJson(dataHttpRequest))

    val call = apiService.sendToken(dataHttpRequest)

    call.enqueue(object : Callback<ResponseBody> {
        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
            val statusCode = response.code()

            if (response.isSuccessful) {
                val responseBody = response.body()?.string()
                Log.d("SendToken", "Token enviado con exito. Codigo de respuesta: $statusCode")
                Log.d("SendToken", "Respuesta del servidor: $responseBody")
                callback(true)
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("SendToken", "Error al enviar el token. Codigo de respuesta: $statusCode")
                Log.e("SendToken", "Error del servidor: $errorBody")
                callback(false)
            }
        }

        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
            Log.e("SendToken", "Error al enviar el token", t)
            callback(false)
        }
    })
}

// Función que envía la misión completada al servidor
fun sendMissionAsCompletedToServer(
    markMissionAsCompleted: MarkMissionAsCompleted,
    callback: (Boolean) -> Unit
) {
    val apiService = HttpInstance.instance

    Log.d("JSON", Gson().toJson(markMissionAsCompleted))

    val call = apiService.markMissionAsCompleted(markMissionAsCompleted)

    call.enqueue(object : Callback<ResponseBody> {
        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
            val statusCode = response.code()

            if (response.isSuccessful) {
                val responseBody = response.body()?.string()
                Log.d(
                    "SendMissionCompleted",
                    "Mision completada enviado con exito. Codigo de respuesta: $statusCode"
                )
                Log.d("SendMissionCompleted", "Respuesta del servidor: $responseBody")
                callback(true)
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e(
                    "SendMissionCompleted",
                    "Error al enviar la mision completada. Codigo de respuesta: $statusCode"
                )
                Log.e("SendMissionCompleted", "Error del servidor: $errorBody")
                callback(false)
            }
        }

        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
            Log.e("SendMissionCompleted", "Error al enviar la mision completada", t)
            callback(false)
        }
    })
}

// TODO: Función que está pendiente para probar, ya que depende de que Firebase asigne un token inactivo a un
//  dispositivo (despues de que no se haya comunicado con FCM en más de un mes) registrado en la aplicación.

fun sendUpdateDeviceTokenToServer(
    updateDeviceToken: UpdateDeviceToken,
    callback: (Boolean) -> Unit
) {
    val apiService = HttpInstance.instance

    val call = apiService.sendUpdateDeviceToken(updateDeviceToken)

    call.enqueue(object : Callback<ResponseBody> {
        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
            val statusCode = response.code()

            if (response.isSuccessful) {
                val responseBody = response.body()?.string()
                Log.d(
                    "SendUpdateDeviceToken",
                    "Token de dispositivo actualizado enviado con exito. Codigo de respuesta: $statusCode"
                )
                Log.d("SendUpdateDeviceToken", "Respuesta del servidor: $responseBody")
                callback(true)
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e(
                    "SendUpdateDeviceToken",
                    "Error al enviar el token de dispositivo actualizado. Codigo de respuesta: $statusCode"
                )
                Log.e("SendUpdateDeviceToken", "Error del servidor: $errorBody")
                callback(false)
            }
        }

        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
            Log.e("SendUpdateDeviceToken", "Error al enviar el token de dispositivo actualizado", t)
            callback(false)
        }
    })
}

fun getDailyMissionFromServer(userId: String, callback: (Boolean, MissionResponse?) -> Unit) {
    val apiService = HttpInstance.instance

    val call = apiService.sendUserIdForMission(userId)

    call.enqueue(object : Callback<MissionResponse> {
        override fun onResponse(call: Call<MissionResponse>, response: Response<MissionResponse>) {

            val statusCode = response.code()

            if (response.isSuccessful) {
                val responseBody = response.body()
                Log.d("Mision", "Mision obtenida desde base de datos con exito: $statusCode")

                val missionJson = Gson().toJson(responseBody)

                Log.d("Mision", missionJson)
                callback(true, responseBody)
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("Mision", "Error al recibir la mision. Codigo de respuesta: $statusCode")
                Log.e("Mision", "Respuesta del servidor: $errorBody")
                callback(false, null)
            }
        }

        override fun onFailure(call: Call<MissionResponse>, t: Throwable) {
            Log.e("Mision", "Error al recibir la mision", t)
            callback(false, null)
        }
    })
}

fun getAllUsersForLeaderboard(callback: (Boolean, LeaderboardResponse?) -> Unit) {
    val apiService = HttpInstance.instance

    val call = apiService.getUsersForLeaderboard()

    call.enqueue(object : Callback<LeaderboardResponse> {
        override fun onResponse(
            call: Call<LeaderboardResponse>,
            response: Response<LeaderboardResponse>
        ) {

            val statusCode = response.code()

            if (response.isSuccessful) {
                val responseBody = response.body()
                Log.d("Usuarios", "Usuarios obtenidos con éxito: $statusCode")

                Log.d("Lista de usuarios", responseBody.toString())
                callback(true, responseBody)
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("Usuarios", "Error al recibir la lista de usuarios: $statusCode")
                Log.e("Usuarios", "Respuesta del servidor: $errorBody")
                callback(false, null)
            }
        }

        override fun onFailure(call: Call<LeaderboardResponse>, t: Throwable) {
            Log.e("Usuarios", "Error al recibir los usuarios", t)
            callback(false, null)
        }
    })
}

fun getCurrentUserInformationForProgress(
    userId: String,
    callback: (Boolean, CurrentUser?) -> Unit
) {
    val apiService = HttpInstance.instance

    val call = apiService.getCurrentUserInformation(userId)

    call.enqueue(object : Callback<CurrentUser> {
        override fun onResponse(call: Call<CurrentUser>, response: Response<CurrentUser>) {

            val statusCode = response.code()

            if (response.isSuccessful) {
                val responseBody = response.body()
                Log.d("Usuario actual", "Usuario actual obtenido con exito: $statusCode")

//                val users = Gson().toJson(responseBody)

                Log.d("Usuarios actual", responseBody.toString())
                callback(true, responseBody)
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("Usuario actual", "Error al recibir el usuario actual: $statusCode")
                Log.e("Usuario actual", "Respuesta del servidor: $errorBody")
                callback(false, null)
            }
        }

        override fun onFailure(call: Call<CurrentUser>, t: Throwable) {
            Log.e("Usuario actual", "Error al recibir el usuario actual", t)
            callback(false, null)
        }
    })
}

fun getStatsInformationForProgress(userId: String, callback: (Boolean, Stats?) -> Unit) {
    val apiService = HttpInstance.instance

    val call = apiService.getUsersForStats(userId)

    call.enqueue(object : Callback<Stats> {
        override fun onResponse(call: Call<Stats>, response: Response<Stats>) {

            val statusCode = response.code()

            if (response.isSuccessful) {
                val responseBody = response.body()
                Log.d(
                    "Progreso del usuario actual",
                    "Progreso del usuario actual obtenido con exito: $statusCode"
                )


                Log.d("Progreso del usuario actual", responseBody.toString())
                callback(true, responseBody)
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e(
                    "Progreso del usuario actual",
                    "Error al recibir el progreso del usuario actual: $statusCode"
                )
                Log.e("Progreso del usuario actual", "Respuesta del servidor: $errorBody")
                callback(false, null)
            }
        }

        override fun onFailure(call: Call<Stats>, t: Throwable) {
            Log.e(
                "Progreso del usuario actual",
                "Error al recibir el progreso del usuario actual",
                t
            )
            callback(false, null)
        }
    })
}

fun getOptionalMissionsFromServer(userId: String, callback: (Boolean, List<Mission>?) -> Unit) {
    val apiService = HttpInstance.instance

    val call = apiService.getOptionalMissions(userId)

    call.enqueue(object : Callback<List<Mission>> {
        override fun onResponse(call: Call<List<Mission>>, response: Response<List<Mission>>) {

            val statusCode = response.code()

            if (response.isSuccessful) {
                val responseBody = response.body()
                Log.d("Captura de las misiones opcionales", "Obtenido con exito: $statusCode")


                Log.d("Misiones opcionales", responseBody.toString())
                callback(true, responseBody)
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e(
                    "Error de las misiones opcionales",
                    "Error al recibir las misiones opcionales: $statusCode"
                )
                Log.e("Error de las misiones opcionales", "Respuesta del servidor: $errorBody")
                callback(false, null)
            }
        }

        override fun onFailure(call: Call<List<Mission>>, t: Throwable) {
            Log.e("Progreso del usuario actual", "Error al recibir las misiones opcionales", t)
            callback(false, null)
        }
    })
}

fun getRerollOptionalMissionFromServer(
    sendUserIdAndIdMission: SendUserIdAndIdMission,
    callback: (Boolean, MissionResponseChanged?) -> Unit
) {
    val apiService = HttpInstance.instance

    val call = apiService.getRerollOptionalMission(sendUserIdAndIdMission)

    call.enqueue(object : Callback<MissionResponseChanged> {
        override fun onResponse(
            call: Call<MissionResponseChanged>,
            response: Response<MissionResponseChanged>
        ) {

            val statusCode = response.code()

            if (response.isSuccessful) {
                val responseBody = response.body()
                Log.d("Reroll", "Obtenido con exito: $statusCode")


                Log.d("Mision reroleada", responseBody.toString())
                callback(true, responseBody)
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("Error de reroll", "Error al relolear la mision: $statusCode")
                Log.e("Error de reroll", "Respuesta del servidor: $errorBody")
                callback(false, null)
            }
        }

        override fun onFailure(call: Call<MissionResponseChanged>, t: Throwable) {
            Log.e("Error de reroll", "Error al relolear la mision", t)
            callback(false, null)
        }
    })
}

suspend fun getNewMissionFromDatabase(sendUserIdAndIdMission: SendUserIdAndIdMission): Pair<Boolean, MissionResponseChanged?> {
    return try {
        val response = HttpInstance.instance.markOptionalMissionAsCompleted(sendUserIdAndIdMission)
        if (response.isSuccessful) {
            Log.d("API Call", "Respuesta exitosa: ${response.code()}")
            Log.d("API Call", "Cuerpo de respuesta: ${response.body()}")
            Pair(true, response.body())
        } else {
            Log.e("API Call", "Error en la respuesta: ${response.code()}")
            Pair(false, null)
        }
    } catch (e: Exception) {
        Log.e("API Call", "Error al llamar a la API", e)
        Pair(false, null)
    }
}

fun getDataToGraph(userId: String, callback: (Boolean, List<DataGraph>?) -> Unit) {
    val apiServie = HttpInstance.instance

    val call = apiServie.getPointsByDate(userId)

    call.enqueue(object : Callback<List<DataGraph>> {
        override fun onResponse(call: Call<List<DataGraph>>, response: Response<List<DataGraph>>) {
            val statusCode = response.code()

            if (response.isSuccessful) {
                val responseBody = response.body()
                Log.d("Grafica", "Datos obtenidos con exito: $statusCode")

                Log.d("Datos de la grafica", responseBody.toString())
                callback(true, responseBody)
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e(
                    "Error de la grafica",
                    "Error al recibir los datos de la grafica: $statusCode"
                )
                Log.e("Error de la grafica", "Respuesta del servidor: $errorBody")
                callback(false, null)
            }
        }

        override fun onFailure(call: Call<List<DataGraph>>, t: Throwable) {
            Log.e("Error de la grafica", "Error al recibir los datos de la grafica", t)
            callback(false, null)
        }
    })
}


fun getAchiementsCompletedFromServer(userId: String, callback: (Boolean, Achievements?) -> Unit) {
    val apiServie = HttpInstance.instance

    val call = apiServie.getAchievementsCompleted(userId)

    call.enqueue(object : Callback<Achievements> {
        override fun onResponse(call: Call<Achievements>, response: Response<Achievements>) {
            val statusCode = response.code()

            if (response.isSuccessful) {
                val responseBody = response.body()
                Log.d("Logros", "Logros obtenidos con exito: $statusCode")

                Log.d("Logros", responseBody.toString())
                callback(true, responseBody)
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("Error de los logros", "Error al recibir los logros: $statusCode")
                Log.e("Error de los logros", "Respuesta del servidor: $errorBody")
                callback(false, null)
            }
        }

        override fun onFailure(call: Call<Achievements>, t: Throwable) {
            Log.e("Error de los logros", "Error al recibir los logros", t)
            callback(false, null)
        }
    })
}

fun getTotalTrophiesFromServer(userId: String, callback: (Boolean, Trophies?) -> Unit) {
    val apiServie = HttpInstance.instance

    val call = apiServie.getTotalTrophies(userId)

    call.enqueue(object : Callback<Trophies> {
        override fun onResponse(call: Call<Trophies>, response: Response<Trophies>) {
            val statusCode = response.code()

            if (response.isSuccessful) {
                val responseBody = response.body()
                Log.d("Trofeos", "Trofeos obtenidos con exito: $statusCode")

                Log.d("Trofeos", responseBody.toString())
                callback(true, responseBody)
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("Error de los trofeos", "Error al recibir los trofeos: $statusCode")
                Log.e("Error de los trofeos", "Respuesta del servidor: $errorBody")
                callback(false, null)
            }
        }

        override fun onFailure(call: Call<Trophies>, t: Throwable) {
            Log.e("Error de los trofeos", "Error al recibir los trofeos", t)
            callback(false, null)
        }
    })
}