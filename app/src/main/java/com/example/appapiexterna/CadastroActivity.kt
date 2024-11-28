package com.example.appapiexterna

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class CadastroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        val editTextTask = findViewById<EditText>(R.id.editTextTask)
        val buttonAddTask = findViewById<Button>(R.id.buttonAddTask)
        val buttonViewTasks = findViewById<Button>(R.id.buttonViewTasks)
        val btnGoToApi = findViewById<Button>(R.id.btnGoToApi)


        buttonAddTask.setOnClickListener {
            val task = editTextTask.text.toString()

            if (task.isNotEmpty()) {
                val taskList = loadTasks()
                taskList.add(task)
                saveTasks(taskList)

                editTextTask.text.clear()
                Toast.makeText(this, "Tarefa adicionada com sucesso!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Por favor, insira uma tarefa.", Toast.LENGTH_SHORT).show()
            }
        }

        buttonViewTasks.setOnClickListener {
            val intent = Intent(this, ListagemActivity::class.java)
            startActivity(intent)
        }

        btnGoToApi.setOnClickListener {
            val intent = Intent(this, ApiActivity::class.java)
            startActivity(intent)
        }

    }

    private fun saveTasks(taskList: MutableList<String>) {
        val sharedPreferences = getSharedPreferences("TASK_PREFS", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putStringSet("TASK_LIST", taskList.toSet())
        editor.apply()
    }

    private fun loadTasks(): MutableList<String> {
        val sharedPreferences = getSharedPreferences("TASK_PREFS", MODE_PRIVATE)
        val taskSet = sharedPreferences.getStringSet("TASK_LIST", emptySet())
        return taskSet?.toMutableList() ?: mutableListOf()
    }
}
