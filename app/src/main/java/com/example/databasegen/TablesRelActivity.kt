package com.example.databasegen

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class TablesRelActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tables_rel)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val nameText: EditText = findViewById(R.id.name_text)
        val spinnerOne: Spinner = findViewById(R.id.spinner_table_one)
        val spinnerTwo: Spinner = findViewById(R.id.spinner_table_two)
        val spinnerRel: Spinner = findViewById(R.id.spinner_rel)
        val button: Button = findViewById(R.id.button_gen)
        val linkToMenu: Button = findViewById(R.id.link_to_menu)


        //TODO Добавить загрузку с сервера
        val listItemsOne = listOf("Табица 1", "Табица 2", "Табица 3")
        val arrayAdapterOne = ArrayAdapter(this, android.R.layout.simple_spinner_item, listItemsOne)
        arrayAdapterOne.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerOne.adapter = arrayAdapterOne

        spinnerOne.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                Toast.makeText(this@TablesRelActivity, "Таблица \"$selectedItem\"", Toast.LENGTH_LONG).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }


        val listItemsTwo = listOf("Табица 1", "Табица 2", "Табица 3")
        val arrayAdapterTwo = ArrayAdapter(this, android.R.layout.simple_spinner_item, listItemsTwo)
        arrayAdapterTwo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTwo.adapter = arrayAdapterTwo

        spinnerTwo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                Toast.makeText(this@TablesRelActivity, "Таблица \"$selectedItem\"", Toast.LENGTH_LONG).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }


        val listItemsRel = listOf("Один к одному", "Один ко многим", "Многие к одному", "Многие ко многим")
        val arrayAdapterRel = ArrayAdapter(this, android.R.layout.simple_spinner_item, listItemsRel)
        arrayAdapterRel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerRel.adapter = arrayAdapterRel

        spinnerRel.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                Toast.makeText(this@TablesRelActivity, "Связь \"$selectedItem\"", Toast.LENGTH_LONG).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }


        linkToMenu.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }

        //TODO Отправить запрос на сервер для генерации таблиц
        button.setOnClickListener {
            val name = nameText.text.toString().trim()

            if (name == "")
                Toast.makeText(this, "Не все поля заполнены", Toast.LENGTH_LONG).show()
            else {
                Toast.makeText(this, "Таблица $name сгенерирована", Toast.LENGTH_LONG).show()
            }

        }

    }
}