package com.example.databasegen

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val userLogin: EditText = findViewById(R.id.user_login)
        val userPass: EditText = findViewById(R.id.user_pass)
        val button: Button = findViewById(R.id.button_reg)
        val linkToAuth: Button = findViewById(R.id.link_to_auth)

        linkToAuth.setOnClickListener {
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
        }

        button.setOnClickListener {
            val login = userLogin.text.toString().trim()
            val pass = userPass.text.toString().trim()

            if (login == "" || pass == "")
                Toast.makeText(this, "Не все поля заполнены", Toast.LENGTH_SHORT).show()
            else {
                commandAPI("ls users/") { successls, resultls ->
                    if (successls) {

                        val jsonObject = JSONObject(resultls)
                        val outputString = jsonObject.getString("output")
                        val outputList = outputString.split("\n").filter { it.isNotEmpty() }

                        if (login !in outputList) {
                            commandAPI("mkdir users/${login}") { success, _ ->
                                if (success) {
                                    commandAPI("mkdir users/${login}/password") { success2, _ ->
                                        if (success2) {
                                            commandAPI("mkdir users/${login}/password/${pass}") { success3, _ ->
                                                if (success3) {
                                                    commandAPI("mkdir users/${login}/feather") { success4, _ ->
                                                        if (success4) {
                                                            runOnUiThread {
                                                                Toast.makeText(this@MainActivity, "Пользователь $login зарегистрирован", Toast.LENGTH_SHORT).show()
                                                            }
                                                        } else {
                                                            runOnUiThread {
                                                                Toast.makeText(this@MainActivity, "Ошибка сервера", Toast.LENGTH_SHORT).show()
                                                            }
                                                        }
                                                    }
                                                } else {
                                                    runOnUiThread {
                                                        Toast.makeText(this@MainActivity, "Ошибка сервера", Toast.LENGTH_SHORT).show()
                                                    }
                                                }
                                            }
                                        } else {
                                            runOnUiThread {
                                                Toast.makeText(this@MainActivity, "Ошибка сервера", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    }
                                } else {
                                    runOnUiThread {
                                        Toast.makeText(this@MainActivity, "Ошибка сервера", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        } else {
                            runOnUiThread {
                                Toast.makeText(this@MainActivity, "Данный пользователь уже существует", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        runOnUiThread {
                            Toast.makeText(this@MainActivity, "Ошибка сервера", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                userLogin.text.clear()
                userPass.text.clear()

            }
        }
    }
}