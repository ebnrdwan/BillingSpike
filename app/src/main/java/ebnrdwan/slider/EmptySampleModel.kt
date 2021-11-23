package ebnrdwan.slider

import ebnrdwan.lib.slider.ISliderModel

class EmptySampleModel constructor(private val text: String) : ISliderModel {

    fun getText(): String {
        return text
    }
}