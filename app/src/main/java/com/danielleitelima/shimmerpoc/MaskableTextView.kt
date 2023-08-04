package com.danielleitelima.shimmerpoc

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat

class MaskableTextView(context: Context, attrs: AttributeSet) : AppCompatTextView(context, attrs), Maskable {
    private var isMasked: Boolean = false
    private val paint: Paint = Paint()
    private var shimmerColor: Int = ContextCompat.getColor(context, R.color.skeleton_mask)
    private var color: Int = ContextCompat.getColor(context, R.color.skeleton_shimmer)
    private var animationFactor: Float = 0.0f
    private var shimmerGradient: LinearGradient? = null
    private var maskAnimator: ValueAnimator? = null
    private var fadeAnimator: ValueAnimator? = null

    init {
        paint.isAntiAlias = true // Adding anti-aliasing
        paint.color = color
    }

    private fun updateGradient() {
        shimmerGradient = LinearGradient(
            animationFactor,
            0f,
            animationFactor + width,
            0f,
            intArrayOf(color, shimmerColor, color),
            null,
            Shader.TileMode.CLAMP
        )
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (isMasked) {
            shimmerGradient?.let {
                paint.shader = it
                canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
            }
        }
    }

    override fun mask() {
        isMasked = true
        if (maskAnimator != null) {
            maskAnimator!!.cancel()
        }
        maskAnimator = ValueAnimator.ofFloat(-width.toFloat(), width.toFloat()).apply {
            duration = 2000 // Modify this for controlling the speed. 3000ms = 3 seconds
            repeatMode = ValueAnimator.REVERSE
            repeatCount = ValueAnimator.INFINITE
            addUpdateListener { animation ->
                animationFactor = animation.animatedValue as Float
                updateGradient()
                invalidate()
            }
        }

        fadeAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 300
            addUpdateListener { animation ->
                val alpha = animation.animatedValue as Float
                paint.alpha = (alpha * 255).toInt()
                invalidate()
            }
        }

        fadeAnimator?.start()
        maskAnimator?.start()
    }

    override fun unmask() {
        fadeAnimator = ValueAnimator.ofFloat(1f, 0f).apply {
            duration = 300
            addUpdateListener { animation ->
                val alpha = animation.animatedValue as Float
                paint.alpha = (alpha * 255).toInt()
                invalidate()
                if (alpha == 0f) {
                    isMasked = false
                    maskAnimator?.cancel()
                    maskAnimator = null
                    paint.shader = null
                }
            }
            start()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        maskAnimator?.cancel()
    }
}

