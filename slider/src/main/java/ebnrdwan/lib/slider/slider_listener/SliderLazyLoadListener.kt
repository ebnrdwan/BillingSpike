package ebnrdwan.lib.slider.slider_listener

import ebnrdwan.lib.slider.SliderRecyclerView


interface SliderLazyLoadListener {

    fun onLoadMore(page: Int, totalItemsCount: Int, view: SliderRecyclerView)

}
