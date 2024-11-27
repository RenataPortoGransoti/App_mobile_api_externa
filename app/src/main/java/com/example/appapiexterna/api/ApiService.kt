package com.example.appapiexterna.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Header

interface ApiService {

    // Endpoint para login
    @POST("/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    // Endpoint para salvar dados
    @POST("/saveData")
    fun saveData(
        @Body request: SaveDataRequest,
        @Header("Authorization") token: String // Passando o token como header
    ): Call<SaveDataResponse>

    @GET("/getData")  // Endpoint para pegar os dados
    fun getData(@Header("Authorization") token: String): Call<List<UserData>>

}

