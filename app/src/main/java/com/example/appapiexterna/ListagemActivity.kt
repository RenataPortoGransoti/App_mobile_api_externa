package com.example.appapiexterna

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ListagemActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listagem)

        val taskList = loadTasks() // Carregar tarefas salvas

        val recyclerViewTasks = findViewById<RecyclerView>(R.id.recyclerViewTasks)
        val textViewEmptyMessage = findViewById<TextView>(R.id.textViewEmptyMessage)
        val buttonAddMoreTasks = findViewById<Button>(R.id.buttonAddMoreTasks)

        recyclerViewTasks.layoutManager = LinearLayoutManager(this)

        // Atualiza a visibilidade da mensagem de "nenhuma tarefa"
        fun updateViewVisibility() {
            if (taskList.isEmpty()) {
                textViewEmptyMessage.visibility = TextView.VISIBLE
                recyclerViewTasks.visibility = RecyclerView.GONE
            } else {
                textViewEmptyMessage.visibility = TextView.GONE
                recyclerViewTasks.visibility = RecyclerView.VISIBLE
            }
        }

        val adapter = TaskAdapter(taskList) { removedTask ->
            // Remove a tarefa e salva as mudanças
            saveTasks(taskList)
            updateViewVisibility() // Atualiza a visibilidade ao remover uma tarefa
            Toast.makeText(this, "Tarefa removida: $removedTask", Toast.LENGTH_SHORT).show()
        }

        recyclerViewTasks.adapter = adapter
        updateViewVisibility() // Chama ao carregar a lista inicialmente

        buttonAddMoreTasks.setOnClickListener {
            val intent = Intent(this, CadastroActivity::class.java)
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
