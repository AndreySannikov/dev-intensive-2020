package ru.skillbranch.devintensive.ui.custom

import android.content.Context
import android.util.AttributeSet

class AvatarImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): CircleImageView(context, attrs, defStyleAttr) {

    fun setInitials(initials: String) {
        if (!initials.isNullOrBlank()) {
            textDrawable.text = initials
            textDrawable.setTextSize(45f)
            setImageDrawable(textDrawable)
        }
    }
}