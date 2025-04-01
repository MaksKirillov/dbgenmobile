package com.example.databasegen

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class TablesGenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tables_gen)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val linkToHint: TextView = findViewById(R.id.main_label)
        val nameText: EditText = findViewById(R.id.name_text)
        val commandText: EditText = findViewById(R.id.command_text)
        val button: Button = findViewById(R.id.button_gen)
        val linkToMenu: Button = findViewById(R.id.link_to_menu)

        linkToHint.setOnClickListener {
            val intent = Intent(this, HintActivity::class.java)
            startActivity(intent)
        }

        linkToMenu.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }

        //TODO Отправить запрос на сервер для генерации таблиц
        button.setOnClickListener {
            val name = nameText.text.toString().trim()
            val command = commandText.text.toString().trim()

            if (name == "" || command == "")
                Toast.makeText(this, "Не все поля заполнены", Toast.LENGTH_LONG).show()
            else {
                Toast.makeText(this, "Таблица $name сгенерирована", Toast.LENGTH_LONG).show()
            }

        }

    }
}