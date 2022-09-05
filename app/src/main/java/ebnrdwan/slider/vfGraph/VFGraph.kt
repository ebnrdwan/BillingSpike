package ebnrdwan.slider.vfGraph

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
import ebnrdwan.lib.slider.OnScrollFadeViews
import ebnrdwan.lib.slider.SliderLayoutManager
import ebnrdwan.lib.slider.SliderRecyclerView
import ebnrdwan.lib.slider.slider_listener.SliderListener
import ebnrdwan.slider.R
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
    var onScrollFadeViews:OnScrollFadeViews?=null
    var baseLineMargin: Int = 0
    fun init(testList: List<GraphModel>, onScrollFadeViews:OnScrollFadeViews?) {
        this.itemsList = testList
        this.onScrollFadeViews = onScrollFadeViews;
        initSliderComponent(testList)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun registerNetworkCallback() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun unregisterNetworkCallback() {
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


        val sliderLayoutManager = SliderLayoutManager(mContext, SliderRecyclerView.HORIZONTAL, true, onScrollFadeViews =  onScrollFadeViews)
        sliderAdapter = SampleAdapter(listener, 50 + margin)
        val padding: Int = ScreenUtils.getScreenWidth(context) / 2-ScreenUtils.dpToPx(context, 40)
        root.slider_view.setPadding(padding, 0, padding, 0)
        root.slider_view.layoutManager = GraphBarsLayoutManager(context).apply {
            callback = object : GraphBarsLayoutManager.OnItemSelectedListener {
                override fun onItemSelected(layoutPosition: Int) {
                    sliderAdapter.setSliderPosition(layoutPosition)
                    Log.d("onItemSelected", "onItemSelected: $layoutPosition")
                }
            }
        }
        root.slider_view.adapter = sliderAdapter
        testList.map {
            getHeightValueForModel(
                context.pxToDp(    context.resources.getDimension(R.dimen.graph_height).roundToInt()),
                it,
                highestNegative ?: 0,
                highestPositive ?: 0
            )
        }
        sliderAdapter.addAll(testList.toMutableList())
        root.slider_view.smoothScrollToPosition(0)
        if ( testList.size <= 1)
            root.slider_view.setOnTouchListener { _, _ -> true }
    }

    val listener = object : SampleAdapter.OnItemClickListener {
        override fun onSliderItemClick(position: Int, model: GraphModel) {
            if (position != sliderAdapter.getCurrentSliderPosition()){
                root.slider_view.smoothScrollToPosition(position)
            }
        }
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


