package com.example.appapiexterna

import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.appapiexterna.api.Data
import com.example.appapiexterna.api.GetDataResponse
import com.example.appapiexterna.api.SaveDataRequest
import com.example.appapiexterna.api.SaveDataResponse
import com.example.appapiexterna.api.UserData
import com.example.appapiexterna.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiActivity : AppCompatActivity() {

    private lateinit var etNome: EditText
    private lateinit var etEmail: EditText
    private lateinit var etTelefone: EditText
    private lateinit var btnSalvar: Button
    private lateinit var btnCarregar: Button
    private lateinit var listView: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private val userDataList: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_api)

        // Inicializando os componentes da interface
        etNome = findViewById(R.id.etNome)
        etEmail = findViewById(R.id.etEmail)
        etTelefone = findViewById(R.id.etTelefone)
        btnSalvar = findViewById(R.id.btnSalvar)
        btnCarregar = findViewById(R.id.btnCarregar)
        listView = findViewById(R.id.listView)

        // Configurando o ArrayAdapter para exibir os dados na ListView
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, userDataList)
        listView.adapter = adapter

        // Ações dos botões
        btnSalvar.setOnClickListener { salvarDados() }
        btnCarregar.setOnClickListener { carregarDados() }
    }

    private fun salvarDados() {
        val nome = etNome.text.toString()
        val email = etEmail.text.toString()
        val telefone = etTelefone.text.toString()

        if (nome.isEmpty() || email.isEmpty() || telefone.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            return
        }

        val data = Data(nome, email, telefone)
        val saveDataRequest = SaveDataRequest(data)
        val token = getSavedToken()

        if (token != null) {
            val apiService = RetrofitClient.getApiService()
            apiService.saveData(saveDataRequest, "Bearer $token").enqueue(object : Callback<SaveDataResponse> {
                override fun onResponse(call: Call<SaveDataResponse>, response: Response<SaveDataResponse>) {
                    if (response.isSuccessful) {
                        Log.d("ApiActivity", "Dados salvos: ${response.body()}")
                        Toast.makeText(this@ApiActivity, "Dados salvos com sucesso!", Toast.LENGTH_SHORT).show()
                    } else {
                        Log.e("ApiActivity", "Erro ao salvar dados: ${response.code()}")
                        Toast.makeText(this@ApiActivity, "Erro ao salvar dados: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<SaveDataResponse>, t: Throwable) {
                    Log.e("ApiActivity", "Falha ao salvar dados: ${t.message}")
                    Toast.makeText(this@ApiActivity, "Falha ao salvar dados: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(this, "Token não encontrado!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun carregarDados() {
        val token = getSavedToken()

        if (token != null) {
            val apiService = RetrofitClient.getApiService()
            apiService.getData("Bearer $token").enqueue(object : Callback<List<UserData>> {
                override fun onResponse(call: Call<List<UserData>>, response: Response<List<UserData>>) {
                    if (response.isSuccessful) {
                        val dados = response.body()
                        if (dados != null) {
                            userDataList.clear()
                            for (userData in dados) {
                                val item = "Nome: ${userData.nome}\nEmail: ${userData.email}\nTelefone: ${userData.telefone}"
                                userDataList.add(item)
                            }
                            adapter.notifyDataSetChanged()
                            Toast.makeText(this@ApiActivity, "Dados carregados com sucesso!", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Log.e("ApiActivity", "Erro ao carregar dados: ${response.code()}")
                        Toast.makeText(this@ApiActivity, "Erro ao carregar dados: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<UserData>>, t: Throwable) {
                    Log.e("ApiActivity", "Falha ao carregar dados: ${t.message}")
                    Toast.makeText(this@ApiActivity, "Falha ao carregar dados: ${t.message}", Toast.LENGTH_SHORT).show()
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
