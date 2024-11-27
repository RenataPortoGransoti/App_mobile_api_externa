package com.example.appapiexterna.api

// Representa os dados de um usuário
data class UserData(
    val nome: String,
    val email: String,
    val telefone: String
)

data class GetDataResponse(
    val data: List<UserData> // Aqui, a lista agora contém objetos do tipo UserData
)