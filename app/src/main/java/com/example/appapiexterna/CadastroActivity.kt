package com.example.appapiexterna

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class CadastroActivity : AppCompatActivity() {

    // Lista de tarefas (armazenamento temporário na memória)
    private val taskList = mutableListOf<String>()

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
                taskList.add(task)

                editTextTask.text.clear()

                Toast.makeText(this, "Tarefa adicionada com sucesso!", Toast.LENGTH_SHORT).show()
                println("Tarefas cadastradas: $taskList")
            } else {
                Toast.makeText(this, "Por favor, insira uma tarefa.", Toast.LENGTH_SHORT).show()
            }
        }

        buttonViewTasks.setOnClickListener {
            val intent = Intent(this, ListagemActivity::class.java)
            intent.putStringArrayListExtra("TASK_LIST", ArrayList(taskList))
            startActivity(intent)
        }

        btnGoToApi.setOnClickListener {
            val intent = Intent(this, ApiActivity::class.java)
            startActivity(intent)
        }

    }
}
