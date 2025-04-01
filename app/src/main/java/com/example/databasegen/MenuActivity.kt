package com.example.databasegen

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val linkToTablesView: Button = findViewById(R.id.link_to_tables_view)
        val linkToTablesGen: Button = findViewById(R.id.link_to_tables_gen)
        val linkToTablesRel: Button = findViewById(R.id.link_to_tables_rel)
        val linkToTablesDownload: Button = findViewById(R.id.link_to_tables_download)
        val linkToAuth: Button = findViewById(R.id.link_to_auth)

        linkToTablesView.setOnClickListener {
            val intent = Intent(this, TablesViewActivity::class.java)
            startActivity(intent)
        }

        linkToTablesGen.setOnClickListener {
            val intent = Intent(this, TablesGenActivity::class.java)
            startActivity(intent)
        }

        linkToTablesRel.setOnClickListener {
            val intent = Intent(this, TablesRelActivity::class.java)
            startActivity(intent)
        }

        linkToTablesDownload.setOnClickListener {
            val intent = Intent(this, TablesDownloadActivity::class.java)
            startActivity(intent)
        }

        linkToAuth.setOnClickListener {
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
        }

    }
}