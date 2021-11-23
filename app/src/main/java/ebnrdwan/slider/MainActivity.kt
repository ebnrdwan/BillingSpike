package ebnrdwan.slider

import alirezat775.sliderview.R
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ebnrdwan.lib.slider.SliderComponent
import ebnrdwan.lib.slider.SliderListener
import ebnrdwan.lib.slider.SliderRecyclerView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val TAG: String = this::class.java.name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = SampleAdapter()
        val msliderManager = MSliderLayoutManager(this, SliderRecyclerView.HORIZONTAL, false)
        val sliderComponent =
            SliderComponent(msliderManager, slider_view, adapter)
        sliderComponent.setCalculateCenterThreshold(true)

        sliderComponent.addSliderListener(object :
            SliderListener {
            override fun onPositionChange(position: Int) {
                Log.d(TAG, "currentPosition : $position")
            }

            override fun onScroll(dx: Int, dy: Int) {
                Log.d(TAG, "onScroll dx : $dx -- dy : $dx")
            }
        })


        sliderComponent.add(SampleModel(R.drawable.ic_rec2))
        sliderComponent.add(SampleModel(R.drawable.ic_rec2))
        sliderComponent.add(SampleModel(R.drawable.ic_rec2))
        sliderComponent.add(SampleModel(R.drawable.ic_rec2))
        sliderComponent.add(SampleModel(R.drawable.ic_rec2))
        sliderComponent.add(SampleModel(R.drawable.ic_rec2))
        sliderComponent.add(SampleModel(R.drawable.ic_rec2))
        sliderComponent.add(SampleModel(R.drawable.ic_rec2))
        sliderComponent.add(SampleModel(R.drawable.ic_rectangle_1))


    }
}
