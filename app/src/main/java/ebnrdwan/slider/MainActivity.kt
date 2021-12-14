package ebnrdwan.slider

import alirezat775.sliderview.R
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import ebnrdwan.lib.slider.SliderLayoutManager
import ebnrdwan.lib.slider.SliderRecyclerView
import ebnrdwan.lib.slider.slider_listener.SliderListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val TAG: String = this::class.java.name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
initSliderComponent(getFakeData())
    }


    private fun initSliderComponent(testList:List<SampleModel>) {
        lifecycle.addObserver(vfGraph)
        vfGraph.init(testList)



    }

    private val sliderListener: SliderListener = object :
        SliderListener {
        override fun onPositionChange(position: Int) {
            Log.d(TAG, "onPositionChange: $position")
        }
    }

    private fun getFakeData():List<SampleModel> {
        val sliderList = ArrayList<SampleModel>()
        sliderList.add(SampleModel(R.drawable.ic_rec2))
        sliderList.add(SampleModel(R.drawable.ic_rec2))
        sliderList.add(SampleModel(R.drawable.ic_rec2))
        sliderList.add(SampleModel(R.drawable.ic_rec2))
        sliderList.add(SampleModel(R.drawable.ic_rec2))
        sliderList.add(SampleModel(R.drawable.ic_rec2))
        sliderList.add(SampleModel(R.drawable.ic_rec2))
        sliderList.add(SampleModel(R.drawable.ic_rec2))
        sliderList.add(SampleModel(R.drawable.ic_rec2))
        sliderList.add(SampleModel(R.drawable.ic_rectangle_1))
        return sliderList.reversed()
    }
}
