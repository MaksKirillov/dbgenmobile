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
import org.json.JSONObject

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

        val currentUser = getUsername(this)

        var selectedTable1 = "None"
        var selectedTable2 = "None"
        var selectedRel = "one_one"

        commandAPI("ls users/${currentUser}/feather/") { successls, resultls ->
            if (successls) {
                val jsonObject = JSONObject(resultls)
                val outputString = jsonObject.getString("output")
                val outputList = outputString.split("\n").filter { it.isNotEmpty() }
                val cleanedList = outputList.map { it.removeSuffix(".feather") }

                runOnUiThread {
                    val arrayAdapterOne = ArrayAdapter(this, android.R.layout.simple_spinner_item, cleanedList)
                    arrayAdapterOne.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerOne.adapter = arrayAdapterOne

                    val arrayAdapterTwo = ArrayAdapter(this, android.R.layout.simple_spinner_item, cleanedList)
                    arrayAdapterTwo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerTwo.adapter = arrayAdapterTwo
                }

            } else {
                runOnUiThread {
                    Toast.makeText(this@TablesRelActivity, "Ошибка сервера", Toast.LENGTH_SHORT).show()
                }
            }
        }

        spinnerOne.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                Toast.makeText(this@TablesRelActivity, "Таблица \"$selectedItem\"", Toast.LENGTH_SHORT).show()
                selectedTable1 = selectedItem
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        spinnerTwo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                Toast.makeText(this@TablesRelActivity, "Таблица \"$selectedItem\"", Toast.LENGTH_SHORT).show()
                selectedTable2 = selectedItem
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }


        val listItemsRel = listOf("one_one", "one_many", "many_one", "many_many")
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
                Toast.makeText(this@TablesRelActivity, "Связь \"$selectedItem\"", Toast.LENGTH_SHORT).show()
                selectedRel = selectedItem
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        linkToMenu.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }

        button.setOnClickListener {
            val name = nameText.text.toString().trim()

            if (name == "")
                Toast.makeText(this, "Не все поля заполнены", Toast.LENGTH_LONG).show()
            else {
                commandAPI("python dbrel.py -f users/${currentUser}/feather/${selectedTable1}.feather -s users/${currentUser}/feather/${selectedTable2}.feather -o users/${currentUser}/feather/${name} -t $selectedRel -k 20") { success, _ ->
                    if (success) {
                        runOnUiThread {
                            Toast.makeText(this, "Таблица $name создана", Toast.LENGTH_SHORT).show()
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
}