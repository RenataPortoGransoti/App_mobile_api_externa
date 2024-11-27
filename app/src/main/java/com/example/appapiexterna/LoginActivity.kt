package com.example.appapiexterna

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appapiexterna.api.ApiService
import com.example.appapiexterna.api.LoginRequest
import com.example.appapiexterna.api.LoginResponse
import com.example.appapiexterna.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var loginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        usernameEditText = findViewById(R.id.usernameEditText)
        loginButton = findViewById(R.id.loginButton)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            if (username.isNotEmpty()) {
                login(username)
            } else {
                Toast.makeText(this, "Por favor, insira um nome de usuário", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun login(username: String) {
        val loginRequest = LoginRequest(username = username) // Envia apenas o nome de usuário
        val apiService = RetrofitClient.getApiService()

        apiService.login(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                Log.d("LoginActivity", "Response Code: ${response.code()}")

                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    val token = loginResponse?.token

                    if (token != null) {
                        // Salvando o token no SharedPreferences
                        saveToken(token)

                        // Exibindo uma mensagem de sucesso
                        Toast.makeText(this@LoginActivity, "Login bem-sucedido", Toast.LENGTH_SHORT).show()

                        // Redirecionando para a tela de cadastro
                        val intent = Intent(this@LoginActivity, CadastroActivity::class.java)
                        startActivity(intent)
                        finish() // Finaliza a LoginActivity para não permitir voltar para ela

                        Log.d("LoginActivity", "Token recebido: $token")  // Verifica se o token foi recebido
                    } else {
                        // Se não houver token, exibe erro
                        Toast.makeText(this@LoginActivity, "Token inválido", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Se a resposta da API não for bem-sucedida
                    Toast.makeText(this@LoginActivity, "Erro ao fazer login: ${response.code()}", Toast.LENGTH_SHORT).show()
                    Log.e("LoginActivity", "Erro ao fazer login: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                // Se ocorrer um erro na requisição
                Toast.makeText(this@LoginActivity, "Erro ao conectar: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("LoginActivity", "Falha na requisição: ${t.message}")
            }
        })
    }

    private fun saveToken(token: String) {
        val sharedPreferences = getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("auth_token", token)
        editor.apply()
    }

    private fun getSavedToken(): String? {
        val sharedPreferences = getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("auth_token", null)
        Log.d("LoginActivity", "Token recuperado: $token") // Log para verificar
        return token
    }
}
