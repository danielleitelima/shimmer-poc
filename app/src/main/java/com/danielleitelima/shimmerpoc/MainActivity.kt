package com.danielleitelima.shimmerpoc

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
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
                binding.btnText.isVisible = false
                binding.progressCircular.isVisible = true
                binding.btnNext.isEnabled = false
                val lightGrayColor = ContextCompat.getColor(this@MainActivity, R.color.light_gray) // Replace 'your_color' with your color resource name
                val blackColor = ContextCompat.getColor(this@MainActivity, R.color.black) // Replace 'your_color' with your color resource name
                binding.btnNext.setCardBackgroundColor(ColorStateList.valueOf(lightGrayColor))
                contentSkeleton.showSkeleton()
                delay(3000)
                binding.btnNext.setCardBackgroundColor(ColorStateList.valueOf(blackColor))
                binding.btnNext.isEnabled = true
                contentSkeleton.showOriginal()
                binding.btnText.isVisible = true
                binding.progressCircular.isVisible = false
            }
        }
    }
}