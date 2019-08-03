package ru.skillbranch.devintensive.ui.custom

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.ImageView.ScaleType.CENTER_CROP
import android.widget.ImageView.ScaleType.CENTER_INSIDE
import androidx.annotation.ColorRes
import androidx.annotation.Dimension
import androidx.annotation.Dimension.DP
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import ru.skillbranch.devintensive.R
import android.graphics.RectF
import kotlin.math.min

class CircleImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {
    companion object {
        private const val DEFAULT_BORDER_COLOR = Color.WHITE
        private const val DEFAULT_BORDER_WIDTH_DP = 2
    }

    private var borderColor = DEFAULT_BORDER_COLOR
    private var borderWidthDp = DEFAULT_BORDER_WIDTH_DP


    private var bitmap: Bitmap? = null

    private var paintBorder: Paint = Paint().apply { isAntiAlias = true }
    private var paint: Paint = Paint().apply { isAntiAlias = true }

    private var innerCircleRadius: Float = 0F

    init {
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView, defStyleAttr, 0)
            borderColor = a.getColor(R.styleable.CircleImageView_cv_borderColor, DEFAULT_BORDER_COLOR)
            borderWidthDp = a.getDimensionPixelSize(R.styleable.CircleImageView_cv_borderWidth, DEFAULT_BORDER_WIDTH_DP)
            a.recycle()
        }
    }

    @Dimension(unit = DP)
    fun getBorderWidth(): Int = borderWidthDp

    fun setBorderWidth(@Dimension(unit = DP) dp: Int) {
        borderWidthDp = dp
    }

    fun getBorderColor(): Int = borderColor

    fun setBorderColor(hex: String) {
        borderColor = Color.parseColor(hex)
    }

    fun setBorderColor(@ColorRes colorId: Int) {
        borderColor = ContextCompat.getColor(context, colorId)
    }

    override fun getScaleType(): ScaleType =
        super.getScaleType().let { if (it == null || it != CENTER_INSIDE) CENTER_CROP else it }

    override fun onDraw(canvas: Canvas) {
        loadBitmap(drawable)

        if (bitmap == null) return

        val rect = calculateBounds()

        val outerCircleRadius = innerCircleRadius + widthInPixels()

        canvas.drawCircle(rect.centerX(), rect.centerY(), outerCircleRadius, paintBorder)
        canvas.drawCircle(rect.centerX(), rect.centerY(), innerCircleRadius, paint)
    }

    private fun loadBitmap(drawable: Drawable?) {
        bitmap = when (drawable) {
            null -> null
            is BitmapDrawable -> drawable.bitmap
            else -> Bitmap.createBitmap(
                drawable.intrinsicWidth,
                drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            ).also {
                val canvas = Canvas(it)
                drawable.setBounds(0, 0, canvas.width, canvas.height)
                drawable.draw(canvas)
            }
        }
        updateShader()
    }

    private fun updateShader() {
        bitmap?.also {
            val shader = BitmapShader(it, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            var scale = 0F
            var dx = 0F
            var dy = 0F

            when (scaleType) {
                CENTER_CROP -> if (it.width * height > width * it.height) {
                    scale = height / it.height.toFloat()
                    dx = (width - it.width * scale) * 0.5f
                } else {
                    scale = width / it.width.toFloat()
                    dy = (height - it.height * scale) * 0.5f
                }
                CENTER_INSIDE -> if (it.width * height < width * it.height) {
                    scale = height / it.height.toFloat()
                    dx = (width - it.width * scale) * 0.5f
                } else {
                    scale = width / it.width.toFloat()
                    dy = (height - it.height * scale) * 0.5f
                }
            }

            shader.setLocalMatrix(Matrix().apply {
                setScale(scale, scale)
                postTranslate(dx, dy)
            })

            paint.shader = shader
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        update()
    }

    private fun update() {
        if (bitmap != null) {
            updateShader()
        }

        innerCircleRadius = ((calculateBounds().width() - widthInPixels() * 2) / 2)
        paintBorder.color = borderColor
        invalidate()
    }

    private fun calculateBounds(): RectF {
        val availableWidth = width - paddingLeft - paddingRight
        val availableHeight = height - paddingTop - paddingBottom

        val sideLength = min(availableWidth, availableHeight)

        val left = paddingLeft + (availableWidth - sideLength) / 2f
        val top = paddingTop + (availableHeight - sideLength) / 2f

        return RectF(left, top, left + sideLength, top + sideLength)
    }


    private fun widthInPixels(): Int = (borderWidthDp * Resources.getSystem().displayMetrics.density).toInt()

}

