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
        sliderList.add(GraphModel(R.drawable.ic_rec2, "feb", 80))
        sliderList.add(GraphModel(R.drawable.ic_rec2, "mar", 70))
        sliderList.add(GraphModel(R.drawable.ic_rec2, "apr", 80))
        sliderList.add(GraphModel(R.drawable.ic_rec2, "may", 50))
        sliderList.add(GraphModel(R.drawable.ic_rec2, "jun", 25))
        sliderList.add(GraphModel(R.drawable.ic_rec2, "jul", 70))
        sliderList.add(GraphModel(R.drawable.ic_rec2, "aug", 40))
        sliderList.add(GraphModel(R.drawable.ic_rec2, "oct", 30))
        sliderList.add(GraphModel(R.drawable.ic_rec2, "nov", 90))
        sliderList.add(GraphModel(R.drawable.ic_rectangle_1, "dec", 60))
        return sliderList.reversed()
    }
}
