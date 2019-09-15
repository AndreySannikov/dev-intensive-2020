package ru.skillbranch.devintensive.ui.custom

import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.annotation.Dimension
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.extensions.convertDpToPx
import ru.skillbranch.devintensive.extensions.convertPxToDp
import kotlin.math.min

open class CircleImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ImageView(context, attrs, defStyleAttr) {

    companion object {
        private const val DEFAULT_BORDER_COLOR = Color.WHITE
        private const val DEFAULT_BORDER_WIDTH_DP = 2f
    }

    private var borderColor = DEFAULT_BORDER_COLOR
    private var borderWidth = (context as Activity).convertDpToPx(DEFAULT_BORDER_WIDTH_DP)

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val clipPath = Path()

    private var text: String? = null
    protected var textDrawable = TextDrawable().apply { setupTextDrawable(this) }

    init {
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView)

            borderColor = a.getColor(
                R.styleable.CircleImageView_cv_borderColor,
                DEFAULT_BORDER_COLOR
            )

            borderWidth = a.getDimension(
                R.styleable.CircleImageView_cv_borderWidth,
                borderWidth
            )

            a.recycle()
        }
    }

    private fun setupTextDrawable(textDrawable: TextDrawable) {
        val typedValue = TypedValue()
        context.theme.resolveAttribute(R.attr.colorAccent, typedValue, true)
        textDrawable.backgroundColor = typedValue.data
    }

    fun getBorderColor(): Int = borderColor

    @Dimension
    fun getBorderWidth(): Int = (context as Activity).convertPxToDp(borderWidth).toInt()

    fun setBorderColor(hex: String) {
        borderColor = Color.parseColor(hex)
        invalidate()
    }

    fun setBorderColor(@ColorRes colorId: Int) {
        borderColor = resources.getColor(colorId, context.theme)
        invalidate()
    }

    fun setBorderWidth(@Dimension dp: Int) {
        borderWidth = (context as Activity).convertDpToPx(dp.toFloat())
        invalidate()
    }

    fun getText(): String? = text

    fun setText(text: String?) {
        if (this.text == text) return

        this.text = text

        // If first name & last name is empty -> avatar is default image
        if (!text.isNullOrBlank()) {
            textDrawable.text = text
            setImageDrawable(textDrawable)
        } else setImageResource(R.drawable.avatar_default)
    }

    override fun onDraw(canvas: Canvas?) {
        val x = width / DEFAULT_BORDER_WIDTH_DP
        val y = height / DEFAULT_BORDER_WIDTH_DP
        val radius = min(x, y)

        clipPath.addCircle(x, y, radius, Path.Direction.CW)
        canvas?.clipPath(clipPath)

        super.onDraw(canvas)

        paint.apply {
            style = Paint.Style.STROKE
            color = borderColor
            strokeWidth = borderWidth
        }

        canvas?.drawCircle(x, y, radius, paint)
    }
}