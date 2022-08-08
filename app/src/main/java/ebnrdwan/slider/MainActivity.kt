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

    private fun initSliderComponent(testList: List<GraphModel>) {
        lifecycle.addObserver(vfGraph)
        vfGraph.init(testList)


    }

    private fun getFakeData(): List<GraphModel> {
        val sliderList = ArrayList<GraphModel>()
        sliderList.add(GraphModel(R.drawable.ic_rec2, "Jan", 100))
        sliderList.add(GraphModel(R.drawable.ic_rec2, "feb", 50))
        sliderList.add(GraphModel(R.drawable.ic_rec2, "mar", 50))
//        sliderList.add(GraphModel(R.drawable.ic_rec2, "apr", 100))
//        sliderList.add(GraphModel(R.drawable.ic_rec2, "may", -50))
//        sliderList.add(GraphModel(R.drawable.ic_rec2, "jun", -25))
//        sliderList.add(GraphModel(R.drawable.ic_rec2, "jul", 100))
//        sliderList.add(GraphModel(R.drawable.ic_rec2, "aug", 100))
//        sliderList.add(GraphModel(R.drawable.ic_rec2, "oct", 100))
//        sliderList.add(GraphModel(R.drawable.ic_rec2, "nov", 100))
//        sliderList.add(GraphModel(R.drawable.ic_rectangle_1, "dec", 100))
        return sliderList.reversed()
    }
}
