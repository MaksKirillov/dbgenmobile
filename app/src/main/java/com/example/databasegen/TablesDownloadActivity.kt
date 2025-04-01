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

class TablesDownloadActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tables_download)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val spinnerTable: Spinner = findViewById(R.id.spinner_table)
        val spinnerFormat: Spinner = findViewById(R.id.spinner_format)
        val button: Button = findViewById(R.id.button_download)
        val linkToMenu: Button = findViewById(R.id.link_to_menu)


        //TODO Добавить загрузку с сервера
        val listItemsTables = listOf("Табица 1", "Табица 2", "Табица 3")
        val arrayAdapterTables = ArrayAdapter(this, android.R.layout.simple_spinner_item, listItemsTables)
        arrayAdapterTables.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTable.adapter = arrayAdapterTables

        spinnerTable.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                Toast.makeText(this@TablesDownloadActivity, "Таблица \"$selectedItem\"", Toast.LENGTH_LONG).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }


        val listItemsFormats = listOf("csv", "xlsx", "xml", "json", "html", "sql")
        val arrayAdapterFormats = ArrayAdapter(this, android.R.layout.simple_spinner_item, listItemsFormats)
        arrayAdapterFormats.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerFormat.adapter = arrayAdapterFormats

        spinnerFormat.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                Toast.makeText(this@TablesDownloadActivity, "Таблица \"$selectedItem\"", Toast.LENGTH_LONG).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }


        linkToMenu.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }

        //TODO Отправить запрос на сервер для загрузки таблицы
        button.setOnClickListener {
            Toast.makeText(this, "Загрузка таблицы...", Toast.LENGTH_LONG).show()
        }

    }
}