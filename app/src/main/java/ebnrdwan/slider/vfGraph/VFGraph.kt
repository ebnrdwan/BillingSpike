package ebnrdwan.slider.vfGraph

import alirezat775.sliderview.R
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import ebnrdwan.lib.slider.SliderLayoutManager
import ebnrdwan.lib.slider.SliderRecyclerView
import ebnrdwan.lib.slider.slider_listener.SliderListener
import kotlinx.android.synthetic.main.voda_graph_layout.view.*

class VFGraph @JvmOverloads constructor(
    private val mContext: Context,

    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(mContext, attrs, defStyleAttr), LifecycleObserver {
    var root: View = inflate(mContext, R.layout.voda_graph_layout, this)
    lateinit var sliderAdapter: SampleAdapter
    lateinit var itemsList: List<GraphModel>
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
        val sliderLayoutManager = SliderLayoutManager(mContext, SliderRecyclerView.HORIZONTAL, true)
        sliderAdapter = SampleAdapter(clickOnSlider)
        root.slider_view.sliderLayoutManager = sliderLayoutManager
        root.slider_view.adapter = sliderAdapter
        root.slider_view.setCalculateCenterThreshold(true)
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


}