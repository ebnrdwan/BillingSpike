package ebnrdwan.slider.vfGraph

import alirezat775.sliderview.R
import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleObserver
import ebnrdwan.lib.slider.OnScrollFadeViews
import ebnrdwan.lib.slider.discretescrollview.DiscreteScrollView
import kotlinx.android.synthetic.main.vf_graph_layout.view.*
import kotlin.math.abs
import kotlin.math.roundToInt

class VFGraph @JvmOverloads constructor(
    private val mContext: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(mContext, attrs, defStyleAttr), LifecycleObserver,
    DiscreteScrollView.ScrollStateChangeListener<GraphAdapter.GraphViewHolder>,
    DiscreteScrollView.OnItemChangedListener<GraphAdapter.GraphViewHolder> {
    var root: View = inflate(mContext, R.layout.vf_graph_layout, this)
    lateinit var sliderAdapter: GraphAdapter
    lateinit var itemsList: List<GraphModel>
    var onScrollFadeViews: OnScrollFadeViews? = null
    var baseLineMargin: Int = 0
    fun init(testList: List<GraphModel>, onScrollFadeViews: OnScrollFadeViews?) {
        this.itemsList = testList
        this.onScrollFadeViews = onScrollFadeViews;
        initSliderComponent(testList)
    }

    private fun initSliderComponent(barsList: List<GraphModel>) {
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
        sliderAdapter = GraphAdapter(clickOnSlider, 50 + margin)
        root.slider_view.adapter = sliderAdapter
        root.slider_view.setSlideOnFling(true)
        root.slider_view.setOverScrollEnabled(true);
        root.slider_view.setSlideOnFlingThreshold(1100);
        root.slider_view.addOnItemChangedListener(this)
        root.slider_view.setItemTransitionTimeMillis(100)
        root.slider_view.addScrollStateChangeListener(this)

        barsList.map {
            getHeightValueForModel(
                context.pxToDp(context.resources.getDimension(R.dimen.graph_height).roundToInt()),
                it,
                highestNegative ?: 0,
                highestPositive ?: 0
            )
        }
        sliderAdapter.addAll(barsList.toMutableList())
        root.slider_view.scrollToPosition(barsList.size-1)
    }



    private val clickOnSlider = object : GraphAdapter.OnItemClickListener {
        override fun onSliderItemClick(position: Int, model: GraphModel) {
            slider_view.smoothScrollToPosition(position)
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

            var persentageBar: Float = model.value.toFloat() / highestPositive.toFloat()
            var persentageHieght = positiveHeightPercentageOfChart * height.toFloat()

            val posBar1Height = persentageBar * persentageHieght;
            model.height = posBar1Height.roundToInt();

            return model;
        }

    }


    override fun onScrollEnd(p0: GraphAdapter.GraphViewHolder, p1: Int) {
        sliderAdapter.setSliderPosition(p1)
    }

    override fun onScrollStart(p0: GraphAdapter.GraphViewHolder, p1: Int) {
    }

    override fun onScroll(
        p0: Float,
        p1: Int,
        p2: Int,
        p3: GraphAdapter.GraphViewHolder?,
        p4: GraphAdapter.GraphViewHolder?
    ) {
    }

    override fun onCurrentItemChanged(p0: GraphAdapter.GraphViewHolder?, position: Int) {
    }


}


