package com.example.databasegen

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.io.OutputStream

fun commandAPI(command: String, callback: (success: Boolean, result: String) -> Unit) {
    val client = OkHttpClient()
    val json = """{"command": "$command"}"""
    val body = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
    val request = Request.Builder()
        .url("https://maximkirillov.pythonanywhere.com/cmdex")
        .post(body)
        .build()

    client.newCall(request).enqueue(object : Callback {

        override fun onFailure(call: Call, e: IOException) {
            val errorMessage = "Error on Failure: ${e.message}"
            callback(false, errorMessage) // Передаем ошибку через коллбэк
        }

        override fun onResponse(call: Call, response: Response) {
            if (response.isSuccessful) {
                val responseBody = response.body?.string() ?: "Empty response"
                callback(true, responseBody) // Передаем успешный ответ через коллбэк
            } else {
                val errorMessage = "Error on Response: ${response.code}"
                callback(false, errorMessage) // Передаем ошибку через коллбэк
            }
        }

    })
}

fun downloadFile(fileName: String, context: Context, callback: (success: Boolean) -> Unit) {
    val url = "https://maximkirillov.pythonanywhere.com/files/$fileName"
    val client = OkHttpClient()

    val request = Request.Builder()
        .url(url)
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            e.printStackTrace() // Обработка ошибки
            callback(false)
        }

        override fun onResponse(call: Call, response: Response) {
            if (response.isSuccessful) {
                response.body?.let { responseBody ->
                    // Сохранение файла в папку "Downloads"
                    val values = ContentValues().apply {
                        put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                        put(MediaStore.MediaColumns.MIME_TYPE, "application/octet-stream")
                        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                    }

                    val uri: Uri? = context.contentResolver.insert(MediaStore.Files.getContentUri("external"), values)

                    uri?.let { contentUri ->
                        context.contentResolver.openOutputStream(contentUri)?.use { outputStream: OutputStream ->
                            outputStream.write(responseBody.bytes())
                        }
                        // Файл успешно скачан и сохранен в папке "Downloads"
                        callback(true)
                    }
                }
            } else {
                callback(false)
            }
        }
    })
}