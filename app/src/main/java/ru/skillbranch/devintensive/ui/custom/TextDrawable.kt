package ru.skillbranch.devintensive.ui.custom

import android.graphics.*
import android.graphics.drawable.Drawable

class TextDrawable: Drawable() {

    companion object {
        private const val DEFAULT_BORDER_WIDTH_DP = 2f
        private const val TEXT_SIZE = 150f
        private const val TEXT_COLOR = Color.WHITE
        private const val BACKGROUND_COLOR = Color.BLACK
    }

    var text = ""
    private var paint = Paint().apply {
        style = Paint.Style.FILL
        textSize = TEXT_SIZE
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
        isFakeBoldText = true
    }
    var backgroundColor = BACKGROUND_COLOR

    override fun draw(canvas: Canvas) {
        if (text.isBlank()) return

        paint.color = backgroundColor
        canvas.drawRect(bounds, paint)

        val baseLineDistance = (paint.ascent() + paint.descent()) / 2
        val x = bounds.width() / DEFAULT_BORDER_WIDTH_DP
        val y = bounds.height() / DEFAULT_BORDER_WIDTH_DP - baseLineDistance

        paint.color = TEXT_COLOR
        canvas.drawText(text, x, y, paint)
    }

    override fun getOpacity(): Int = PixelFormat.TRANSPARENT

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun setColorFilter(cf: ColorFilter?) {
        paint.colorFilter = cf
    }

    fun setTextSize(size: Float) {
        paint.textSize = size
    }
}