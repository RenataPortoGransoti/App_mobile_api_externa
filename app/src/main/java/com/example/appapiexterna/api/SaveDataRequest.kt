package com.example.appapiexterna.api

data class SaveDataRequest(
    val data: Data
)

data class Data(
    val nome: String,
    val email: String,
    val telefone: String
)
