package ebnrdwan.slider

import android.content.Context
import android.view.View
import ebnrdwan.lib.slider.SliderLayoutManager
import kotlinx.android.synthetic.main.item_slider.view.*

class MSliderLayoutManager(context: Context?, orientation: Int, reverseLayout: Boolean) :
    SliderLayoutManager(context, orientation, reverseLayout) {
    override val shrinkAmount: Float
        get() = 0.4f
    override val shrinkDistance: Float
        get() = 0.9f

    override fun onCenterThresholdChange(child: View, centerThreshold: Float) {
        scaleView(child, centerThreshold)
        fadeBorder(child, centerThreshold)
        fadeView(child, centerThreshold, 0.3f)
    }

    private fun scaleView(child: View, centerThreshold: Float) {
//        child.scaleX = centerThreshold
//        child.scaleY = centerThreshold
    }

    private fun fadeBorder(view: View, centerThreshold: Float) {
        val border = view.border_view
        if (centerThreshold in 0.75f..1.0f)
            border?.alpha = centerThreshold
        else border?.alpha = 0.0f

    }

    private fun fadeView(view: View, centerThreshold: Float, subtractFade: Float) {

        if (centerThreshold in 0.90f..1.0f)
            view.alpha = centerThreshold
        else view.alpha = centerThreshold - subtractFade

    }

}