package com.example.appapiexterna

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

        buttonAddTask.setOnClickListener {
            val task = editTextTask.text.toString()

            if (task.isNotEmpty()) {
                // Aqui você pode adicionar a tarefa ao banco de dados ou lista
                addTaskToList(task)

                // Limpar o campo e mostrar a mensagem de sucesso
                editTextTask.text.clear()
                Toast.makeText(this, "Tarefa adicionada com sucesso!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Por favor, insira uma tarefa.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addTaskToList(task: String) {
        // Aqui você pode salvar a tarefa no banco de dados ou em uma lista
    }
}
