package ebnrdwan.lib.slider.slider_listener


interface SliderListener {
    /**
     * @param position current position
     */
    fun onPositionChange(position: Int)

    /**
     * @param dx delta x
     * @param dy delta y
     */
    fun onScroll(dx: Int, dy: Int){

    }
}
