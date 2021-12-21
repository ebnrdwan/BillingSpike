package ebnrdwan.slider

import alirezat775.sliderview.R
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ebnrdwan.slider.vfGraph.GraphModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val TAG: String = this::class.java.name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
initSliderComponent(getFakeData())
    }


    private fun initSliderComponent(testList:List<GraphModel>) {
        lifecycle.addObserver(vfGraph)
        vfGraph.init(testList)



    }

    private fun getFakeData():List<GraphModel> {
        val sliderList = ArrayList<GraphModel>()
        sliderList.add(GraphModel(R.drawable.ic_rec2,"Jan"))
        sliderList.add(GraphModel(R.drawable.ic_rec2,"feb"))
        sliderList.add(GraphModel(R.drawable.ic_rec2,"mar"))
        sliderList.add(GraphModel(R.drawable.ic_rec2,"apr"))
        sliderList.add(GraphModel(R.drawable.ic_rec2,"may"))
        sliderList.add(GraphModel(R.drawable.ic_rec2,"jun"))
        sliderList.add(GraphModel(R.drawable.ic_rec2,"jul"))
        sliderList.add(GraphModel(R.drawable.ic_rec2,"aug"))
        sliderList.add(GraphModel(R.drawable.ic_rec2,"oct"))
        sliderList.add(GraphModel(R.drawable.ic_rec2,"nov"))
        sliderList.add(GraphModel(R.drawable.ic_rectangle_1,"dec"))
        return sliderList.reversed()
    }
}
