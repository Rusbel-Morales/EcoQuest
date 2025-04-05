package mx.tec.ecoquest_android.HttpRequest

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Declaramos una instancia de la librería "Retrofit" para realizar nuestras solicitudes http
object HttpInstance {
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.ApiDatabases.URL_API_GOOGLE_CLOUD)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Creamos una instancia de la interfaz que declaramos en el archivo "APIInterface.kt"
    val instance: APIInterface by lazy {
        retrofit.create(APIInterface::class.java)
    }
}