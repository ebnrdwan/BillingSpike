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

        //1 calculate view height
        val height =
            context.pxToDp(context.resources.getDimension(R.dimen.graph_height).roundToInt())

        // filter positives and negatives
        var positives = itemsList.filter { it.value > 0 }
        var negatives = itemsList.filter { it.value < 0 }
        // get max values
        val highestNegative = negatives.maxBy { abs(it.value) }?.value;
        val highestPositive = positives.maxBy { abs(it.value) }?.value;
        val negativeHeightPercentageOfChart =
            (abs(highestNegative ?: 0).toFloat() / height.toFloat())
        var baseLineMargin = (negativeHeightPercentageOfChart * height.toFloat())
        var margin: Int = baseLineMargin.roundToInt()
        baseLine.setMargins(bottom = margin)


        val sliderLayoutManager = SliderLayoutManager(mContext, SliderRecyclerView.HORIZONTAL, true)
        sliderAdapter = SampleAdapter(clickOnSlider, 50 + margin)
        root.slider_view.sliderLayoutManager = sliderLayoutManager
        root.slider_view.adapter = sliderAdapter
        root.slider_view.setCalculateCenterThreshold(true)
        testList.map {
            getHeightValueForModel(
                context.pxToDp(    context.resources.getDimension(R.dimen.graph_height).roundToInt()),
                it,
                highestNegative ?: 0,
                highestPositive ?: 0
            )
        }
        sliderAdapter.addAll(testList.toMutableList())
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

    private fun handleChart(testList: List<GraphModel>) {
        chartSpike.viewTreeObserver.addOnGlobalLayoutListener(object :
            OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                // Ensure you call it only once :
                drawChart(testList)
                chartSpike.viewTreeObserver.removeGlobalOnLayoutListener(this)

            }
        })
    }


    private fun drawChart(testList: List<GraphModel>) {
        var positives = itemsList.filter { it.value > 0 }
        var negatives = itemsList.filter { it.value < 0 }
        val height = context.resources.getDimension(R.dimen.graph_height).roundToInt()
//        getHeightValueForModel(height, negatives)
//        drawPositiveBars(height,positives)

    }

    private fun getHeightValueForModel(
        height: Int,
        model: GraphModel,
        highestNegative: Int,
        highestPositive: Int
    ): GraphModel {
        if (model.isNegative()) {
            if (highestNegative == 0) return model

            val negativeHeightPercentageOfChart =
                (abs(highestNegative ?: 0).toFloat() / height.toFloat())
            val negBar1Height =
                (model.value / abs(highestNegative).toFloat() * (negativeHeightPercentageOfChart * height)).roundToInt()
            model.value = negBar1Height;

            return model;
        } else {
            val negativeHeightPercentageOfChart =
                (abs(highestNegative ?: 0).toFloat() / height.toFloat())
            val positiveHeightPercentageOfChart = 1 - negativeHeightPercentageOfChart

            var persentageBar:Float = model.value.toFloat() / highestPositive.toFloat()
          var  persentageHieght= positiveHeightPercentageOfChart * height.toFloat()

//            val posBar1Height =
//                ((model.value / highestPositive) * (positiveHeightPercentageOfChart * height)).roundToInt()
            val posBar1Height = persentageBar* persentageHieght;
            model.height = posBar1Height.roundToInt();

            return model;
        }

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


