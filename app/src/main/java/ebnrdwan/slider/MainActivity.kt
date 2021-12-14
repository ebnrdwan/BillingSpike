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
        sliderList.add(SampleModel(R.drawable.ic_rec2,"Jan"))
        sliderList.add(SampleModel(R.drawable.ic_rec2,"feb"))
        sliderList.add(SampleModel(R.drawable.ic_rec2,"mar"))
        sliderList.add(SampleModel(R.drawable.ic_rec2,"apr"))
        sliderList.add(SampleModel(R.drawable.ic_rec2,"may"))
        sliderList.add(SampleModel(R.drawable.ic_rec2,"jun"))
        sliderList.add(SampleModel(R.drawable.ic_rec2,"jul"))
        sliderList.add(SampleModel(R.drawable.ic_rec2,"aug"))
        sliderList.add(SampleModel(R.drawable.ic_rec2,"oct"))
        sliderList.add(SampleModel(R.drawable.ic_rec2,"nov"))
        sliderList.add(SampleModel(R.drawable.ic_rectangle_1,"dec"))
        return sliderList.reversed()
    }
}
