package com.example.appapiexterna.network


import android.content.Context
import android.widget.Toast
import com.example.appapiexterna.api.LoginRequest
import com.example.appapiexterna.api.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// Modificando a assinatura do método para receber um contexto
fun callApi(context: Context) {
    // Crie a instância de LoginRequest
    val loginRequest = LoginRequest(username = "seu_username_aqui")

    // Instância do ApiService (via RetrofitClient)
    val apiService = RetrofitClient.getApiService()

    // Faz a chamada da API
    apiService.login(loginRequest).enqueue(object : Callback<LoginResponse> {
        override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
            if (response.isSuccessful) {
                val loginResponse = response.body()
                // Exibe o token recebido
                Toast.makeText(
                    context,  // Usando o contexto passado como parâmetro
                    "Token recebido: ${loginResponse?.token}",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                // Em caso de falha
                Toast.makeText(
                    context,  // Usando o contexto passado como parâmetro
                    "Erro no login: ${response.code()}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
            // Em caso de erro na requisição
            Toast.makeText(
                context,  // Usando o contexto passado como parâmetro
                "Erro ao conectar: ${t.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
    })
}
