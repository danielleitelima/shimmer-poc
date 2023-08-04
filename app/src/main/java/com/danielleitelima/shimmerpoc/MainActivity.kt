package com.danielleitelima.shimmerpoc

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.danielleitelima.shimmerpoc.databinding.ActivityMainBinding
import com.danielleitelima.shimmerpoc.databinding.ItemStoryBinding
import com.github.javafaker.Faker
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setTextContent()

        binding.stories.adapter = createAdapter()

//        TODO: Handle skeleton state
        binding.stories.isVisible = false

        binding.btnNext.setOnClickListener {
            lifecycleScope.launch {
                binding.root.mask()
                setButtonState(isLoading = true)
                delay(3000)
                setTextContent()
                setButtonState(isLoading = false)
                binding.root.unmask()
            }
        }

    }

    private fun setTextContent() {
        binding.tvTitle.setRandomLoremIpsum(10,50)
        binding.tvHeadline.setRandomLoremIpsum(40, 80)
        binding.tvArticleBody.setRandomLoremIpsum(300, 500)
        binding.tvFooter.setRandomLoremIpsum(50,100)
    }

    private fun setButtonState(isLoading: Boolean) {
        val lightGrayColor = ContextCompat.getColor(this@MainActivity, R.color.skeleton_mask) // Replace 'your_color' with your color resource name
        val blackColor = ContextCompat.getColor(this@MainActivity, R.color.black) // Replace 'your_color' with your color resource name

        binding.btnNext.isEnabled = !isLoading
        binding.btnText.isVisible = !isLoading
        binding.progressCircular.isVisible = isLoading

        binding.btnNext.setCardBackgroundColor(ColorStateList.valueOf(if (isLoading) lightGrayColor else blackColor))
    }

    fun ViewGroup.mask() {
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            when (child) {
                is Maskable -> child.mask()
                is ViewGroup -> child.mask()
            }
        }
    }

    fun ViewGroup.unmask() {
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            when (child) {
                is Maskable -> child.unmask()
                is ViewGroup -> child.unmask()
            }
        }
    }

    fun TextView.setRandomLoremIpsum(minimumLength: Int = 20, maximumLength: Int = 200) {
        val faker = Faker()
        val textLength = (minimumLength..maximumLength).random()
        val lorem = faker.lorem().fixedString(textLength)
        this.text = lorem
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