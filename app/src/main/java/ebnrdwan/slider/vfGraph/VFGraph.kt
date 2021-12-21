package ebnrdwan.slider.vfGraph

import alirezat775.sliderview.R
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import ebnrdwan.lib.slider.SliderLayoutManager
import ebnrdwan.lib.slider.SliderRecyclerView
import ebnrdwan.lib.slider.slider_listener.SliderListener
import kotlinx.android.synthetic.main.voda_graph_layout.view.*
import kotlin.math.abs
import kotlin.math.roundToInt

class VFGraph @JvmOverloads constructor(
    private val mContext: Context,

    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(mContext, attrs, defStyleAttr), LifecycleObserver {
    var root: View = inflate(mContext, R.layout.voda_graph_layout, this)
    lateinit var sliderAdapter: SampleAdapter
    lateinit var itemsList: List<GraphModel>
    var baseLineMargin: Int = 0
    fun init(testList: List<GraphModel>) {
        this.itemsList = testList
        initSliderComponent(testList)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun registerNetworkCallback() {
        root.slider_view.addSliderListener(sliderListener)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun unregisterNetworkCallback() {
        root.slider_view.removeSliderListener()
    }

    private fun initSliderComponent(testList: List<GraphModel>) {
        //calculate negative shift and shift up base line
        // send height of each bar to recycler
        //show highlight card
        val sliderLayoutManager = SliderLayoutManager(mContext, SliderRecyclerView.HORIZONTAL, true)
        sliderAdapter = SampleAdapter(clickOnSlider,50)
        root.slider_view.sliderLayoutManager = sliderLayoutManager
        root.slider_view.adapter = sliderAdapter
        root.slider_view.setCalculateCenterThreshold(true)
        sliderAdapter.addAll(testList.toMutableList())

        handleChart()
    }


    private val sliderListener: SliderListener = object :
        SliderListener {
        override fun onPositionChange(position: Int) {
            Log.d("GRAPH", "onPositionChange: $position")
            sliderAdapter.setSliderPosition(position)
            handleScrollingToEmptyItems(position)

        }
    }

    /** @param position current selected item position
     * {Case position=0 --> user scrolling to first empty item which is invisible
     * handling: scroll back to first visible item}
     * {Case position > itemsList.size --> user scrolling to last empty item which is invisible
     * handling: scroll back to last visible item}
     * */
    private fun handleScrollingToEmptyItems(position: Int) {

        if (position == 0) {
            slider_view.smoothScrollToPosition(1)
            return
        }
        if (position > itemsList.size) {
            slider_view.smoothScrollToPosition(itemsList.size)
            return
        }
    }

    private val clickOnSlider = object : SampleAdapter.OnItemClickListener {
        override fun onSliderItemClick(position: Int, model: GraphModel) {
            slider_view.smoothScrollToPosition(position)
        }
    }

    private fun handleChart() {
        chartSpike.viewTreeObserver.addOnGlobalLayoutListener(object :
            OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                // Ensure you call it only once :
                drawChart()
                chartSpike.viewTreeObserver.removeGlobalOnLayoutListener(this)

            }
        })
    }

    private fun drawChart() {
        val height = context.pxToDp(chartSpike.height)
        var positives = itemsList.filter { it.value > 0 }
        var negatives = itemsList.filter { it.value < 0 }
        // draw negative
        val highestNegative = negatives.maxBy { abs(it.value) }?.value ?: 0;
        if (highestNegative == 0) return

        val negativeHeightPercentageOfChart = (abs(highestNegative).toFloat() / height.toFloat())
        val baseLineMargin: Float = negativeHeightPercentageOfChart * height.toFloat()
        baseLine.setMargins(bottom = baseLineMargin.roundToInt())
        this.baseLineMargin = baseLineMargin.roundToInt()

//        val negBar1Height = negBar1 / highestNegative * (negativeHeightPercentageOfChart * height)
//        negative_bar1.setHeight(context.dpToPx(negBar1Height.roundToInt()))
//
//        val negBar2Height =
//            abs(negBar2).toFloat() / abs(highestNegative).toFloat() * (negativeHeightPercentageOfChart * height)
//        negative_bar2.setHeight(dpToPx(negBar2Height.roundToInt()))
//
//


//        val highestPositive = posBar1;
//
//        val negativeHeightPercentageOfChart = if (highestNegative>0) abs(highestNegative).toFloat() / height.toFloat() else 0f
//        val positiveHeightPercentageOfChart = 1-negativeHeightPercentageOfChart
//
//        val posBar1Height = posBar1 / highestPositive * (positiveHeightPercentageOfChart * height)
//        positive_bar1.setHeight(dpToPx(posBar1Height.roundToInt()))
//
//        val posBar2Height = abs(posBar2).toFloat() / abs(highestPositive).toFloat() * (positiveHeightPercentageOfChart * height)
//        positive_bar2.setHeight(dpToPx(posBar2Height.roundToInt()))

    }


}


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
//    this.layoutParams = ConstraintLayout.LayoutParams(this.width, height)
}


