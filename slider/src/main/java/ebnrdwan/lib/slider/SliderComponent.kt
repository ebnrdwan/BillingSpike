package ebnrdwan.lib.slider

import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import ebnrdwan.lib.slider.helper.EndlessListener
import ebnrdwan.lib.slider.helper.ViewHelper


class SliderComponent constructor(
    private var manager: SliderLayoutManager,
    @NonNull private var sliderRecyclerView: SliderRecyclerView,
    @NonNull private var adapter: SliderAdapter
) {


    private var sliderLazyLoadListener: SliderLazyLoadListener? = null

    init {
        initializeManager(orientation = manager.orientation)
        sliderRecyclerView.adapter = adapter
        sliderRecyclerView.isAutoScroll = false
    }

    /**
     * @param orientation set VERTICAL/HORIZONTAL
     */
    private fun initializeManager(
        @SliderRecyclerView.SliderOrientation orientation: Int,
        enablePadding: Boolean = true
    ) {

        sliderRecyclerView.layoutManager = manager
        val padding: Int
        when (orientation) {
            SliderRecyclerView.HORIZONTAL -> {
                padding = if (enablePadding) ViewHelper.getScreenWidth() / 4 else 1
                sliderRecyclerView.setPadding(padding, 0, padding, 0)
            }
            SliderRecyclerView.VERTICAL -> {
                padding = if (enablePadding) ViewHelper.getScreenHeight() / 4 else 1
                sliderRecyclerView.setPadding(0, padding, 0, padding)
            }
        }
    }

    fun addSliderListener(listener: SliderListener) {
        sliderRecyclerView.listener = listener
    }

    fun removeSliderListener() {
        sliderRecyclerView.listener = null
    }

    /**
     * lazyLoad load more item with infinity scroll.
     * for enable this feature should be pass true value in first parameter
     * and pass child of SliderLazyLoadListener for second parameter
     * for disable this feature should be pass false value in first argument
     * and pass null for second parameter
     *
     * @param lazy this flag enable or disable lazy loading view
     * @param sliderLazyLoadListener listener when need call load more item
     */
    fun lazyLoad(lazy: Boolean, sliderLazyLoadListener: SliderLazyLoadListener?) {
        this.sliderLazyLoadListener = sliderLazyLoadListener

        if (lazy)
            sliderRecyclerView.addOnScrollListener(object : EndlessListener(getManager()!!) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                    sliderLazyLoadListener?.onLoadMore(page, totalItemsCount, view as SliderRecyclerView)
                }
            })
        else
            sliderRecyclerView.clearOnScrollListeners()
    }

    /**
     * @param centerThreshold enable calculating the distance from center point threshold item
     */
    fun setCalculateCenterThreshold(centerThreshold: Boolean) {
        getManager()?.setCalculateCenterThreshold(centerThreshold)
    }

    /**
     * @return SliderLayoutManager
     */
    private fun getManager(): SliderLayoutManager? {
        return manager
    }

    /**
     * @param items list items should be add to slider
     */
    fun addAll(items: MutableList<ISliderModel>) {
        adapter.addAll(items)
    }

    /**
     * @param item one item should be add to slider
     */
    fun add(item: ISliderModel) {
        adapter.operation(item,
            SliderAdapter.ADD
        )
    }

    /**
     * @param item list items should be remove to slider
     */
    fun remove(item: ISliderModel) {
        adapter.operation(item,
            SliderAdapter.REMOVE
        )
    }

    /**
     * @param currentPosition
     */
    fun setCurrentPosition(currentPosition: Int) {
        sliderRecyclerView.scrollToPosition(currentPosition)
    }

    /**
     * @return current item position
     */
    fun getCurrentPosition(): Int {
        return sliderRecyclerView.currentPosition
    }

    /**
     * pause auto scrolling
     */
    fun pauseAutoScroll() {
        sliderRecyclerView.pauseAutoScroll()
    }

    /**
     * resume auto scrolling
     */
    fun resumeAutoScroll() {
        sliderRecyclerView.resumeAutoScroll()
    }

    /**
     * @param autoScroll
     * @param delayMillis
     * @param loopMode
     */
    fun autoScroll(autoScroll: Boolean, delayMillis: Long, loopMode: Boolean) {
        sliderRecyclerView.isAutoScroll = autoScroll
        sliderRecyclerView.delayMillis = delayMillis
        sliderRecyclerView.isLoopMode = loopMode
    }

    /**
     * @param enableSlider enable slider mode
     */
    fun enableSlider(enableSlider: Boolean) {
        if (getManager()?.orientation == SliderRecyclerView.VERTICAL)
            throw IllegalStateException("for using slider mode, orientation must be is HORIZONTAL")
        else
            adapter.enableSlider(enableSlider)
    }

    /**
     * @param scrollSpeed change speed scrolling item
     */
    fun scrollSpeed(scrollSpeed: Float) {
        getManager()?.setScrollSpeed(scrollSpeed)
    }

}