package com.example.appapiexterna

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appapiexterna.api.Data // Importa a classe Data
import com.example.appapiexterna.api.GetDataResponse
import com.example.appapiexterna.api.SaveDataRequest // Importa a classe SaveDataRequest
import com.example.appapiexterna.api.SaveDataResponse // Importa a classe SaveDataResponse
import com.example.appapiexterna.api.UserData
import com.example.appapiexterna.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView




class ApiActivity : AppCompatActivity() {

    private lateinit var etNome: EditText
    private lateinit var etEmail: EditText
    private lateinit var etTelefone: EditText
    private lateinit var btnSalvar: Button
    private lateinit var btnCarregar: Button // Botão para carregar os dados

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Defina o layout da activity
        setContentView(R.layout.activity_api)

        // Inicializando os campos
        etNome = findViewById(R.id.etNome)
        etEmail = findViewById(R.id.etEmail)
        etTelefone = findViewById(R.id.etTelefone)
        btnSalvar = findViewById(R.id.btnSalvar)
        btnCarregar = findViewById(R.id.btnCarregar)

        // Configurando o botão de salvar
        btnSalvar.setOnClickListener {
            salvarDados()
        }

        // Configurando o botão de carregar
        btnCarregar.setOnClickListener {
            carregarDados()
        }
    }

    // Função para salvar os dados
    private fun salvarDados() {
        val nome = etNome.text.toString()
        val email = etEmail.text.toString()
        val telefone = etTelefone.text.toString()

        // Verificar se os campos estão preenchidos
        if (nome.isEmpty() || email.isEmpty() || telefone.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            return
        }

        // Criar o objeto Data com os valores dos campos
        val data = Data(nome, email, telefone)

        // Criar o objeto SaveDataRequest com o Data
        val saveDataRequest = SaveDataRequest(data)

        // Obter o token (este é o token que você obteve após o login)
        val token = getSavedToken()  // Função que você já deve ter implementado para pegar o token salvo

        // Verificar se o token foi encontrado
        if (token != null) {
            val apiService = RetrofitClient.getApiService()

            // Fazer a requisição para salvar os dados
            apiService.saveData(saveDataRequest, "Bearer $token").enqueue(object : Callback<SaveDataResponse> {
                override fun onResponse(call: Call<SaveDataResponse>, response: Response<SaveDataResponse>) {
                    if (response.isSuccessful) {
                        // Adiciona log detalhado da resposta JSON
                        Log.d("ApiActivity", "Resposta da API (Salvar): ${response.body()}")
                        Toast.makeText(this@ApiActivity, "Dados salvos com sucesso!", Toast.LENGTH_SHORT).show()
                    } else {
                        // Log caso a resposta não seja bem-sucedida
                        Log.e("ApiActivity", "Erro ao salvar dados: ${response.code()}")
                        Toast.makeText(this@ApiActivity, "Erro ao salvar dados: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<SaveDataResponse>, t: Throwable) {
                    // Log caso a requisição falhe
                    Log.e("ApiActivity", "Falha na requisição: ${t.message}")
                    Toast.makeText(this@ApiActivity, "Falha na requisição: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })

        } else {
            // Se o token não for encontrado
            Toast.makeText(this, "Token não encontrado!", Toast.LENGTH_SHORT).show()
        }
    }

    // Função para carregar os dados
    private fun carregarDados() {
        val token = getSavedToken()  // Pega o token salvo anteriormente

        if (token != null) {
            val apiService = RetrofitClient.getApiService()

// Dentro da função carregarDados
            apiService.getData("Bearer $token").enqueue(object : Callback<List<UserData>> {
                override fun onResponse(call: Call<List<UserData>>, response: Response<List<UserData>>) {
                    if (response.isSuccessful) {
                        // Adiciona log detalhado da resposta JSON
                        Log.d("ApiActivity", "Resposta da API (Carregar): ${response.body()}")
                        val dados = response.body()
                        if (dados != null) {
                            for (userData in dados) {
                                val nome = userData.nome
                                val email = userData.email
                                val telefone = userData.telefone

                                // Exibe os dados no log
                                Log.d("ApiActivity", "Nome: $nome, Email: $email, Telefone: $telefone")
                            }

                            Toast.makeText(this@ApiActivity, "Dados recebidos com sucesso!", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Log.e("ApiActivity", "Erro ao carregar dados: ${response.code()}")
                        Toast.makeText(this@ApiActivity, "Erro ao carregar dados: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<UserData>>, t: Throwable) {
                    // Log caso a requisição falhe
                    Log.e("ApiActivity", "Falha na requisição: ${t.message}")
                    Toast.makeText(this@ApiActivity, "Falha na requisição: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })

        } else {
            Toast.makeText(this, "Token não encontrado!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getSavedToken(): String? {
        val sharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE)
        return sharedPreferences.getString("auth_token", null)
    }
}
