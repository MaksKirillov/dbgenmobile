package com.example.databasegen

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.json.JSONObject

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

        val currentUser = getUsername(this)

        var selectedTable = "None"
        var selectedFormat = "csv"

        commandAPI("ls users/${currentUser}/feather/") { successls, resultls ->
            if (successls) {
                val jsonObject = JSONObject(resultls)
                val outputString = jsonObject.getString("output")
                val outputList = outputString.split("\n").filter { it.isNotEmpty() }
                val cleanedList = outputList.map { it.removeSuffix(".feather") }

                runOnUiThread {
                    val arrayAdapterTables = ArrayAdapter(this, android.R.layout.simple_spinner_item, cleanedList)
                    arrayAdapterTables.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerTable.adapter = arrayAdapterTables
                }

            } else {
                runOnUiThread {
                    Toast.makeText(this@TablesDownloadActivity, "Ошибка сервера", Toast.LENGTH_SHORT).show()
                }
            }
        }

        spinnerTable.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                Toast.makeText(this@TablesDownloadActivity, "Таблица \"$selectedItem\"", Toast.LENGTH_SHORT).show()
                selectedTable = selectedItem
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
                Toast.makeText(this@TablesDownloadActivity, "Формат \"$selectedItem\"", Toast.LENGTH_SHORT).show()
                selectedFormat = selectedItem
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }


        linkToMenu.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }

        button.setOnClickListener {
            commandAPI("python dbsaver.py -i users/${currentUser}/feather/${selectedTable}.feather -o saves -f $selectedFormat") { success, _ ->
                if (success) {
                    runOnUiThread {
                        Toast.makeText(this, "Успешное создание $selectedTable в формате $selectedFormat", Toast.LENGTH_SHORT).show()
                    }

                    downloadFile("$selectedTable.$selectedFormat", this) { successDownload ->
                        if (successDownload) {
                            runOnUiThread {
                                Toast.makeText(this, "Успешная загрузка $selectedTable в формате $selectedFormat", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            runOnUiThread {
                                Toast.makeText(this, "Ошибка загрузки", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                } else {
                    runOnUiThread {
                        Toast.makeText(this, "Ошибка сервера", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }
}