package ebnrdwan.lib.slider



interface SliderLazyLoadListener {

    fun onLoadMore(page: Int, totalItemsCount: Int, view: SliderRecyclerView)

}
