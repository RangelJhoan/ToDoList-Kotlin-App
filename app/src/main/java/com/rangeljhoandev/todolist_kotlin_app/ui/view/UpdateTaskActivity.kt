package com.rangeljhoandev.todolist_kotlin_app.ui.view

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.rangeljhoandev.todolist_kotlin_app.R
import com.rangeljhoandev.todolist_kotlin_app.databinding.ActivityUpdateTaskBinding

class UpdateTaskActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUpdateTaskBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(R.layout.activity_update_task)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}