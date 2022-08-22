package ebnrdwan.lib.slider

import android.content.Context
import android.view.View

open class SliderLayoutManager(
    context: Context?, orientation: Int, reverseLayout: Boolean,
    _shrinkAmount: Float = 0.4f,
    _shrinkDistance: Float = 0.9f,
    _defaultScrollSpeed: Float = 150f,
    private val reduceFadeBy: Float = 0.3f,
    val onScrollFadeViews: OnScrollFadeViews?
) :
    BaseSliderLayoutManager(
        context,
        orientation,
        reverseLayout,
        _shrinkAmount,
        _shrinkDistance,
        _defaultScrollSpeed
    ) {


    private val wideRangeAroundCenter = 0.75f..1.0f
    private val tightRangeAroundCenter = 0.90f..1.0f


    override fun onCenterThresholdChange(child: View, centerThreshold: Float) {
        fadeView(child, centerThreshold, reduceFadeBy)
    }

    private fun scaleView(child: View, centerThreshold: Float) {
        child.scaleX = centerThreshold
        child.scaleY = centerThreshold
    }

    private fun fadeBorder(view: View, centerThreshold: Float) {


    }

    private fun fadeView(view: View, centerThreshold: Float, reduceFadeBy: Float) {
        var float: Float = 0.0f;
        float = if (centerThreshold in tightRangeAroundCenter)
            centerThreshold
        else centerThreshold - reduceFadeBy

        view.alpha = float;
        onScrollFadeViews?.onFadeView(float)
    }

}