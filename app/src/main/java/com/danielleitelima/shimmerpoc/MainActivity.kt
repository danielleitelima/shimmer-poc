package com.danielleitelima.shimmerpoc

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.danielleitelima.shimmerpoc.databinding.ActivityMainBinding
import com.danielleitelima.shimmerpoc.databinding.ItemStoryBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.stories.adapter = createAdapter()

//        TODO: Handle skeleton state
        binding.stories.isVisible = false

        val maskableViews = listOf(
            binding.tvHeadline,
            binding.tvFooter,
            binding.tvArticleBody,
            binding.tvTitle,
        )

        binding.btnNext.setOnClickListener {
            lifecycleScope.launch {
                maskableViews.forEach { it.mask() }
                setButtonState(true)
                delay(3000)
                setButtonState(false)
                maskableViews.forEach { it.unmask() }
            }
        }

    }

    private fun setButtonState(isLoading: Boolean) {
        val lightGrayColor = ContextCompat.getColor(this@MainActivity, R.color.light_gray) // Replace 'your_color' with your color resource name
        val blackColor = ContextCompat.getColor(this@MainActivity, R.color.black) // Replace 'your_color' with your color resource name

        binding.btnNext.isEnabled = !isLoading
        binding.btnText.isVisible = !isLoading
        binding.progressCircular.isVisible = isLoading

        binding.btnNext.setCardBackgroundColor(ColorStateList.valueOf(if (isLoading) lightGrayColor else blackColor))
    }

    private fun createAdapter() = SingleTypeGenericAdapter(
        ItemStoryBinding::inflate,
        listOf("One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten")
    ) { item, binding ->
        binding.apply {
            binding.content.text = item
        }
    }
}