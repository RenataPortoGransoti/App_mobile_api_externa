package com.example.appapiexterna

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ListagemActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listagem)

        val taskList = loadTasks().toMutableList()

        if (taskList.isEmpty()) {
            Toast.makeText(this, "Nenhuma tarefa cadastrada!", Toast.LENGTH_SHORT).show()
        }

        val recyclerViewTasks = findViewById<RecyclerView>(R.id.recyclerViewTasks)
        recyclerViewTasks.layoutManager = LinearLayoutManager(this)

        val adapter = TaskAdapter(taskList) { removedTask ->
            saveTasks(taskList)
            Toast.makeText(this, "Tarefa removida: $removedTask", Toast.LENGTH_SHORT).show()
        }

        recyclerViewTasks.adapter = adapter

        // Botão para adicionar mais tarefas
        val buttonAddMoreTasks = findViewById<Button>(R.id.buttonAddMoreTasks)
        buttonAddMoreTasks.setOnClickListener {
            val intent = Intent(this, CadastroActivity::class.java)
            startActivity(intent)
        }
    }

    // Salva a lista de tarefas no SharedPreferences
    private fun saveTasks(taskList: List<String>) {
        val sharedPreferences = getSharedPreferences("TASK_PREFS", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putStringSet("TASK_LIST", taskList.toSet())
        editor.apply()
    }

    // Carrega a lista de tarefas do SharedPreferences
    private fun loadTasks(): List<String> {
        val sharedPreferences = getSharedPreferences("TASK_PREFS", MODE_PRIVATE)
        val taskSet = sharedPreferences.getStringSet("TASK_LIST", emptySet())
        return taskSet?.toList() ?: emptyList()
    }
}
