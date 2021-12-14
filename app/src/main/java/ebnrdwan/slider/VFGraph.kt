package ebnrdwan.slider

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


    fun init(testList: List<SampleModel>) {

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

    private fun initSliderComponent(testList: List<SampleModel>) {
        val sliderLayoutManager = SliderLayoutManager(mContext, SliderRecyclerView.HORIZONTAL, true)
        val sliderAdapter = SampleAdapter(clickOnSlider)
        root.slider_view.sliderLayoutManager = sliderLayoutManager
        root.slider_view.adapter = sliderAdapter
        root.slider_view.setCalculateCenterThreshold(true)
        sliderAdapter.addAll(testList.toMutableList())


    }

    private val sliderListener: SliderListener = object :
        SliderListener {
        override fun onPositionChange(position: Int) {
            Log.d("GRAPH", "onPositionChange: $position")
        }
    }

    private val clickOnSlider = object : SampleAdapter.OnItemClickListener {
        override fun onSliderItemClick(position: Int, model: SampleModel) {
            slider_view.smoothScrollToPosition(position)
        }
    }


}