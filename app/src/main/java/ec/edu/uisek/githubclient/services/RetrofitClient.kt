package ec.edu.uisek.githubclient.services

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import ec.edu.uisek.githubclient.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://api.github.com/"
    private lateinit var authService: AuthService

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    fun init (context: Context) {
        authService = AuthService(context)
    }

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .addInterceptor { chain ->
            val token = authService.getToken() ?: ""
            println("Token es vacio? ${token.isEmpty()}")
                //.replace("\"", "")
                //.trim()
            
            val request = chain.request().newBuilder()
                .addHeader(name = "Authorization", value= "Bearer $token")
                .header("Cache-Control", "no-cache, no-store, must-revalidate")
                .header("Pragma", "no-cache")
                .header("Expires", "0")
                .build()
                chain.proceed(request)
        }
        .cache(null)
        .build()

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
