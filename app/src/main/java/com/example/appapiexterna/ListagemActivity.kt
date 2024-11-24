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

        // obtendo a lista de tarefas passada pela intent
        val taskList = intent.getStringArrayListExtra("TASK_LIST") ?: arrayListOf()

        if (taskList.isEmpty()) {
            Toast.makeText(this, "Nenhuma tarefa cadastrada!", Toast.LENGTH_SHORT).show()
        }

        val recyclerViewTasks = findViewById<RecyclerView>(R.id.recyclerViewTasks)
        recyclerViewTasks.layoutManager = LinearLayoutManager(this)
        recyclerViewTasks.adapter = TaskAdapter(taskList)

        val buttonAddMoreTasks = findViewById<Button>(R.id.buttonAddMoreTasks)
        buttonAddMoreTasks.setOnClickListener {
            val intent = Intent(this, CadastroActivity::class.java)
            startActivity(intent)
        }

    }

}

