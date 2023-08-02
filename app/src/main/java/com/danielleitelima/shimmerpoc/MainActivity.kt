package com.danielleitelima.shimmerpoc

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.danielleitelima.shimmerpoc.databinding.ActivityMainBinding
import com.faltenreich.skeletonlayout.createSkeleton
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val contentSkeleton = binding.content.createSkeleton()

        binding.btnNext.setOnClickListener {
            lifecycleScope.launch {
                // Simulate long running task
                contentSkeleton.showSkeleton()
                delay(3000)
                contentSkeleton.showOriginal()
            }
        }
    }
}