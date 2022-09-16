package com.sector.graph_1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sector.graph_1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnClear.setOnClickListener {
                drawView.clearCanvas()
            }
        }
    }
}