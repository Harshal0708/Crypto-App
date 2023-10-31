package com.example.cryptoapp

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable

class TextDrawable (context: Context, private val text: String) : Drawable() {

    private val paint = Paint()

    init {
        paint.color = Color.BLACK
        paint.textSize = 48f // Set the text size
    }

    override fun draw(canvas: Canvas) {
        canvas.drawColor(Color.WHITE) // Set the background color

        // Calculate the position to center the text on the canvas
        val x = (canvas.width - paint.measureText(text)) / 2
        val y = (canvas.height + paint.textSize) / 2

        canvas.drawText(text, x, y, paint)
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun setColorFilter(colorFilter: android.graphics.ColorFilter?) {
        // Not needed for simple text drawable
    }

    override fun getOpacity(): Int {
        return android.graphics.PixelFormat.OPAQUE
    }
}
