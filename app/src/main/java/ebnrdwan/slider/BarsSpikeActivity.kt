package ebnrdwan.slider

import ebnrdwan.slider.R
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.activity_bars_spike.*
import kotlin.math.abs
import kotlin.math.roundToInt


class BarsSpikeActivity : AppCompatActivity() {
    val negBar1 = -50;
    val negBar2 = -20;
    val posBar1 = 100;
    val posBar2 = 50;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bars_spike)


        handleChart()

    }

    private fun handleChart() {
        chartSpike.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                // Ensure you call it only once :
                onDraw()
                chartSpike.viewTreeObserver.removeGlobalOnLayoutListener(this)

            }
        })
    }

    private fun onDraw() {
        val height = pxToDp(chartSpike.height)
        drawNegativeBars(height)
        drawPositiveBars(height)

    }

    private fun drawNegativeBars(height: Int) {
        // draw negative
        val highestNegative = negBar1;
        if (highestNegative==0) return

        val negativeHeightPercentageOfChart = (abs(highestNegative).toFloat() / height.toFloat())
        val baseLineMargin: Float = negativeHeightPercentageOfChart * height.toFloat()
        baseLine.setMargins(bottom = baseLineMargin.roundToInt())

        val negBar1Height = negBar1 / highestNegative * (negativeHeightPercentageOfChart * height)
        negative_bar1.setHeight(dpToPx(negBar1Height.roundToInt()))

        val negBar2Height =
            abs(negBar2).toFloat() / abs(highestNegative).toFloat() * (negativeHeightPercentageOfChart * height)
        negative_bar2.setHeight(dpToPx(negBar2Height.roundToInt()))
    }


    private fun drawPositiveBars(height: Int) {
        // draw positive
        val highestPositive = posBar1;
        val highestNegative = negBar1;
        val negativeHeightPercentageOfChart = if (highestNegative>0) abs(highestNegative).toFloat() / height.toFloat() else 0f
        val positiveHeightPercentageOfChart = 1-negativeHeightPercentageOfChart

        val posBar1Height = posBar1 / highestPositive * (positiveHeightPercentageOfChart * height)
        positive_bar1.setHeight(dpToPx(posBar1Height.roundToInt()))

        val posBar2Height = abs(posBar2).toFloat() / abs(highestPositive).toFloat() * (positiveHeightPercentageOfChart * height)
        positive_bar2.setHeight(dpToPx(posBar2Height.roundToInt()))
    }


}


private fun View.setMargins(left: Int = 0, top: Int = 0, right: Int = 0, bottom: Int = 0) {
    if (this.layoutParams is MarginLayoutParams) {
        val p = this.layoutParams as MarginLayoutParams
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
    this.layoutParams.height = height-4
//    this.layoutParams = ConstraintLayout.LayoutParams(this.width, height)
}