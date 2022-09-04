package ebnrdwan.slider.vfGraph

import android.content.Context
import android.view.View
import android.view.ViewGroup


public fun View.setMargins(left: Int = 0, top: Int = 0, right: Int = 0, bottom: Int = 0) {
    if (this.layoutParams is ViewGroup.MarginLayoutParams) {
        val p = this.layoutParams as ViewGroup.MarginLayoutParams
        val scale = context.resources.displayMetrics.density
        // convert the DP into pixel
        val l = (left * scale + 0.5f).toInt()
        val r = (right * scale + 0.5f).toInt()
        val t = (top * scale + 0.5f).toInt()
        val b = (bottom * scale + 0.5f).toInt()
        p.setMargins(l, t, r, b)
        this.requestLayout()
    }
}

fun Context.dpToPx(dp: Int): Int {
    return (dp * resources.displayMetrics.density).toInt()
}

fun Context.pxToDp(px: Int): Int {
    return (px / resources.displayMetrics.density).toInt()
}

fun View.setHeight(height: Int) {
    this.layoutParams.height = height - 4
}
