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

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_auth)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val userLogin: EditText = findViewById(R.id.user_login_auth)
        val userPass: EditText = findViewById(R.id.user_pass_auth)
        val button: Button = findViewById(R.id.button_auth)
        val linkToReg: Button = findViewById(R.id.link_to_reg)

        linkToReg.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
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
                        var jsonObject = JSONObject(resultls)
                        var outputString = jsonObject.getString("output")
                        var outputList = outputString.split("\n").filter { it.isNotEmpty() }

                        if (login !in outputList) {
                            runOnUiThread {
                                Toast.makeText(this@AuthActivity, "Данного пользователя нет", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            commandAPI("ls users/${login}/password") { successls2, resultls2 ->
                                if (successls2) {
                                    jsonObject = JSONObject(resultls2)
                                    outputString = jsonObject.getString("output")
                                    outputList = outputString.split("\n").filter { it.isNotEmpty() }

                                    if (pass !in outputList) {
                                        runOnUiThread {
                                            Toast.makeText(this@AuthActivity, "Неправильный пароль", Toast.LENGTH_SHORT).show()
                                        }
                                    } else {
                                        runOnUiThread {
                                            Toast.makeText(this@AuthActivity, "Пользователь $login авторизован", Toast.LENGTH_SHORT).show()
                                            saveUsername(this, login)
                                            userLogin.text.clear()
                                            userPass.text.clear()
                                            val intent = Intent(this, MenuActivity::class.java)
                                            startActivity(intent)
                                        }
                                    }
                                } else {
                                    runOnUiThread {
                                        Toast.makeText(this@AuthActivity, "Ошибка сервера", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }
                    } else {
                        runOnUiThread {
                            Toast.makeText(this@AuthActivity, "Ошибка сервера", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}