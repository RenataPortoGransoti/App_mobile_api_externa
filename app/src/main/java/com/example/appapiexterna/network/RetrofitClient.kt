package com.example.appapiexterna.network

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor  // Importando o interceptor de log
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import com.example.appapiexterna.api.ApiService


object RetrofitClient {

    private const val BASE_URL = "https://trabalho-final-api-production.up.railway.app"

    // Adicionando o HttpLoggingInterceptor
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .addInterceptor { chain ->
            val request: Request = chain.request().newBuilder()
                .build()
            chain.proceed(request)
        }
        .addInterceptor(loggingInterceptor)  // Adiciona o interceptor de log
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getApiService(): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}
