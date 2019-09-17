package ru.skillbranch.devintensive.extensions

import android.app.Activity
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import ru.skillbranch.devintensive.R

fun Activity.hideKeyboard() {
    val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    //Find the currently focused view, so we can grab the correct window token from it.
    var view = currentFocus
    if (view == null) {
        view = View(this)
    }
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Activity.isKeyboardOpen() = keyBoardHeight(this) > 0

fun Activity.isKeyboardClosed() = keyBoardHeight(this) <= 0

private fun keyBoardHeight(activity: Activity): Int {
    val rootView = activity.findViewById<View>(android.R.id.content)
    val visibleBounds = Rect()
    rootView.getWindowVisibleDisplayFrame(visibleBounds)
    return rootView.height - visibleBounds.height()
}

fun Activity.convertDpToPx(dp: Float): Float = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    dp,
    resources.displayMetrics
)

fun Activity.convertPxToDp(px: Float): Float = px / resources.displayMetrics.density

fun Activity.snackBar(view: View, text: CharSequence, duration: Int): Snackbar =
    Snackbar.make(
        view,
        text,
        duration
    )
        .apply {
            this.view.setBackgroundResource(R.drawable.bg_snackbar)
            this.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
                .setTextColor(resolveAttr(R.attr.colorSnackbarText))
        }

fun Activity.resolveAttr(attrId: Int) =
    TypedValue().apply {
        theme.resolveAttribute(attrId, this, true)
    }.data