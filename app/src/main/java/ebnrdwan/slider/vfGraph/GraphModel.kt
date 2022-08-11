package ebnrdwan.slider.vfGraph

import ebnrdwan.lib.slider.ISliderModel

class GraphModel constructor(
    private var id: Int,
    val month: String,
    var value: Int,
    var height: Int = 0
) :
    ISliderModel {

    fun imageId(): Int {
        return id
    }

    fun isNegative() = value < 0
}
