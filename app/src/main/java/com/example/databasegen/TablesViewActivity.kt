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
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.io.IOException

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

        fun addHeaderRowToTable(headerRow: Element) {
            val tableRow = TableRow(this)

            // Получение ячеек заголовка
            val headers = headerRow.select("th")
            for (header in headers) {
                val textView = TextView(this)
                textView.text = header.text()
                textView.setPadding(16, 16, 16, 16) // Установка отступов
                textView.setTypeface(null, android.graphics.Typeface.BOLD) // Установка жирного шрифта
                tableRow.addView(textView)
            }

            tableLayout.addView(tableRow)
        }

        fun addRowToTable(row: Element) {
            val tableRow = TableRow(this)

            // Получение ячеек строки
            val cells = row.select("td")
            for (cell in cells) {
                val textView = TextView(this)
                textView.text = cell.text()
                textView.setPadding(16, 16, 16, 16) // Установка отступов
                tableRow.addView(textView)
            }

            tableLayout.addView(tableRow)
        }

        fun loadHtmlFile(fileName: String) {
            try {
                tableLayout.removeAllViews()

                //TODO загрузка таблицы с сервера
                val inputStream = assets.open(fileName)
                val document: Document = Jsoup.parse(inputStream, "UTF-8", "")

                // Получение таблицы
                val table: Element = document.select("table").first() ?: return

                // Получение заголовков таблицы
                val headerRow = table.select("thead tr").first()
                headerRow?.let { addHeaderRowToTable(it) }

                // Получение строк таблицы
                val rows = table.select("tbody tr")

                // Отображение первых 20 строк
                for (i in 0 until minOf(rows.size, 20)) {
                    val row = rows[i]
                    addRowToTable(row)
                }

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        linkToMenu.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }

        //TODO Получаем табдицы с сервера
        val listItems = listOf("Табица 1", "Табица 2", "Табица 3")
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listItems)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = arrayAdapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                Toast.makeText(this@TablesViewActivity, "Табица \"$selectedItem\"", Toast.LENGTH_LONG).show()
                loadHtmlFile("animals.html")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

    }
}