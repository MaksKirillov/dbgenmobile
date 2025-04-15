package com.example.databasegen

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.json.JSONObject

class TablesViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tables_view)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val spinner: Spinner = findViewById(R.id.spinner_tables)
        val linkToMenu: Button = findViewById(R.id.link_to_menu)
        val tableLayout: TableLayout = findViewById(R.id.table)

        val currentUser = getUsername(this)

        linkToMenu.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }

        commandAPI("ls users/${currentUser}/feather/") { successls, resultls ->
            if (successls) {
                val jsonObject = JSONObject(resultls)
                val outputString = jsonObject.getString("output")
                val outputList = outputString.split("\n").filter { it.isNotEmpty() }
                val cleanedList = outputList.map { it.removeSuffix(".feather") }

                runOnUiThread {
                    val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, cleanedList)
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinner.adapter = arrayAdapter
                }

            } else {
                runOnUiThread {
                    Toast.makeText(this@TablesViewActivity, "Ошибка сервера", Toast.LENGTH_SHORT).show()
                }
            }
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent.getItemAtPosition(position).toString()

                commandAPI("python dbviewer.py -i users/${currentUser}/feather/${selectedItem}.feather") { success, result ->
                    if (success) {
                        // Извлечение данных из JSON
                        val jsonObject = JSONObject(result)
                        val output = jsonObject.getString("output")

                        // Разделение заголовков и данных
                        val lines = output.split("\n")
                        val headers = lines[0].split("|")
                        val dataRows = lines.drop(1)

                        runOnUiThread {
                            //Очищаем таблицу
                            tableLayout.removeAllViews()

                            // Создаем строку заголовка
                            val headerRow = TableRow(this@TablesViewActivity)
                            for (header in headers) {
                                val textView = TextView(this@TablesViewActivity)
                                textView.text = header.trim()
                                headerRow.addView(textView)
                            }
                            tableLayout.addView(headerRow)

                            // Заполняем таблицу данными
                            for (row in dataRows) {
                                if (row.isNotBlank()) { // Проверяем на пустую строку
                                    val dataColumns = row.split("|")
                                    val dataRow = TableRow(this@TablesViewActivity)
                                    for (column in dataColumns) {
                                        val textView = TextView(this@TablesViewActivity)
                                        textView.text = column.trim()
                                        dataRow.addView(textView)
                                    }
                                    tableLayout.addView(dataRow)
                                }
                            }

                            Toast.makeText(this@TablesViewActivity, "Таблица $selectedItem создана", Toast.LENGTH_SHORT).show()
                        }

                    } else {
                        runOnUiThread {
                            Toast.makeText(this@TablesViewActivity, "Ошибка сервера", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }
}