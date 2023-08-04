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
import kotlin.math.cos
import kotlin.math.sin

class MaskableTextView(context: Context, attrs: AttributeSet) : AppCompatTextView(context, attrs) {
    private var isMasked: Boolean = false
    private val paint: Paint = Paint()
    private var shimmerColor: Int = ContextCompat.getColor(context, R.color.skeleton_mask)
    private var color: Int = ContextCompat.getColor(context, R.color.skeleton_shimmer)
    private var shimmerAngle: Int = 45
    private var animationFactor: Float = 0.0f
    private var animator: ValueAnimator? = null
    private var shimmerGradient: LinearGradient? = null

    init {
        paint.isAntiAlias = true // Adding anti-aliasing
        paint.color = color
    }

    private fun updateGradient() {
        val radians = Math.toRadians(shimmerAngle.toDouble())
        val endX = cos(radians.toFloat()) * width + animationFactor
        val endY = sin(radians.toFloat()) * height + animationFactor
        shimmerGradient = LinearGradient(
            animationFactor,
            animationFactor,
            endX,
            endY,
            intArrayOf(color, shimmerColor, color),
            floatArrayOf(0.1f, 0.2f, 0.1f),
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

    fun mask() {
        isMasked = true
        if (animator != null) {
            animator!!.cancel()
        }
        animator = ValueAnimator.ofFloat(-width.toFloat(), width.toFloat()).apply {
            duration = 2000 // Modify this for controlling the speed. 3000ms = 3 seconds
            repeatMode = ValueAnimator.RESTART
            repeatCount = ValueAnimator.INFINITE
            addUpdateListener { animation ->
                animationFactor = animation.animatedValue as Float
                updateGradient()
                invalidate()
            }
            start()
        }
    }

    fun unmask() {
        isMasked = false
        animator?.cancel()
        animator = null
        paint.shader = null
        invalidate()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animator?.cancel()
    }
}

