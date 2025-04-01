package com.example.databasegen

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

fun APIhelper() {
    // Создаем клиент OkHttp
    val client = OkHttpClient()

    // URL вашего Flask API
    val url = "https://maximkirillov.pythonanywhere.com/dbgen"

    // Создаем JSON объект с параметрами
    val json = JSONObject().apply {
        put("names", arrayOf("Username", "HomeAddress")) // Пример названий атрибутов
        put("types", arrayOf("name_first_last", "place_street_num_0_20")) // Пример типов атрибутов
        put("language", "eng") // Язык таблицы
        put("number", 10) // Количество кортежей
        put("blank", 0) // Процент пустых данных
        put("save", "feather_df/table_2") // Путь для сохранения таблицы
    }

    // Создаем тело запроса
    val body = json.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

    // Создаем запрос
    val request = Request.Builder()
        .url(url)
        .post(body)
        .build()

    // Выполняем запрос
    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            e.printStackTrace()
        }

        override fun onResponse(call: Call, response: Response) {
            response.use {
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                // Обрабатываем ответ от сервера
                println(response.body?.string())
            }
        }
    })
}