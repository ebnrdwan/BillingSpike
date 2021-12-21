package ebnrdwan.slider.vfGraph

import ebnrdwan.lib.slider.ISliderModel

class GraphModel constructor(private var id: Int, val month:String) : ISliderModel {

    fun imageId(): Int {
        return id
    }
}
