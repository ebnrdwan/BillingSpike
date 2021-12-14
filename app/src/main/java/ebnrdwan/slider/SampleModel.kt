package ebnrdwan.slider

import ebnrdwan.lib.slider.ISliderModel

class SampleModel constructor(private var id: Int, val month:String) : ISliderModel {

    fun imageId(): Int {
        return id
    }
}
