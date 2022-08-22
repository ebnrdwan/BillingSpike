package ebnrdwan.lib.slider

import android.content.Context
import android.os.CountDownTimer
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import androidx.annotation.IntDef
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import ebnrdwan.lib.slider.helper.EndlessListener
import ebnrdwan.lib.slider.helper.ViewHelper
import ebnrdwan.lib.slider.slider_listener.SliderLazyLoadListener
import ebnrdwan.lib.slider.slider_listener.SliderListener
import kotlin.math.abs


class SliderRecyclerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) :
    RecyclerView(context, attrs, defStyle) {


    var sliderListener: SliderListener? = null
    var sliderLazyLoadListener: SliderLazyLoadListener? = null
    var gestureDetector: GestureDetector ? = null


    private var velocityTracker: VelocityTracker? = null
    var currentPosition: Int = 0
    private var actionDown = true
    var isAutoScroll = false
    var isLoopMode: Boolean = false
    var delayMillis: Long = 5000
    private var reverseLoop = true
    private var scheduler: Scheduler? = null
    private var scrolling = true
var isMove=false;
    var anchor: Int = 0
        set(anchor) {
            if (this.anchor != anchor) {
                field = anchor
                sliderLayoutManager.anchor = anchor
                requestLayout()
            }
        }

    /**
     * @return support RTL view
     */
    private val isRTL: Boolean
        get() = ViewCompat.getLayoutDirection(this@SliderRecyclerView) == ViewCompat.LAYOUT_DIRECTION_RTL


    private val isTrustLayout: Boolean
        get() {
            if (isRTL && sliderLayoutManager.reverseLayout) {
                return true
            } else if (!isRTL && sliderLayoutManager.reverseLayout) {
                return false
            } else if (isRTL && !sliderLayoutManager.reverseLayout) {
                return false
            } else if (!isRTL && !sliderLayoutManager.reverseLayout) {
                return true
            }
            return false
        }

    /**
     * @return layoutManager
     */
    var sliderLayoutManager: BaseSliderLayoutManager
        get() = layoutManager as BaseSliderLayoutManager
        set(value) {
            layoutManager = value
            initializeManager(value.orientation)
        }

    /**
     * @param adapter the new adapter to set, or null to set no adapter
     */
    override fun setAdapter(adapter: Adapter<*>?) {
        super.setAdapter(adapter)
        if (adapter!!.itemCount >= 0)
            initSnap()
    }

    /**
     * initialize
     */
    private fun initSnap() {
        clipToPadding = false
        overScrollMode = View.OVER_SCROLL_NEVER
        anchor = CENTER
        addOnItemTouchListener(onItemTouchListener())
        post { scrolling(0); if (isAutoScroll) getScheduler() }
    }

    /**
     * @return scheduler scroll item
     */
    private fun getScheduler(): Scheduler? {
        if (scheduler == null) {
            scheduler = Scheduler(delayMillis, 1)
        }
        return scheduler
    }

    /**
     * pause auto scroll
     */
    fun pauseAutoScroll() {
        getScheduler()?.cancel()
        scrolling = false
    }

    /**
     * resume auto scroll
     */
    fun resumeAutoScroll() {
        getScheduler()?.start()
        scrolling = true
    }




    /**
     * @param dx delta x
     * @param dy delta y
     */
    override fun onScrolled(dx: Int, dy: Int) {
        super.onScrolled(dx, dy)
        if (sliderListener != null)
            sliderListener!!.onScroll(dx, dy)
    }

    /**
     * @param position scrolling to specific position
     */
    override fun scrollToPosition(position: Int) {
        super.scrollToPosition(position)
        post { smoothScrollToPosition(position) }
    }

    /**
     * @param state called when the scroll state of this RecyclerView changes
     */
    override fun onScrollStateChanged(state: Int) {
        super.onScrollStateChanged(state)
        if (state == SCROLL_STATE_IDLE) {
            if (currentPosition == 0)
                reverseLoop = true
            else if (currentPosition == adapter!!.itemCount - 1)
                reverseLoop = false
        }
    }

    override fun smoothScrollToPosition(position: Int) {

        post { super.smoothScrollToPosition(position) }
            sliderListener?.onPositionChange(position)
            currentPosition = position
    }

    /**
     * @param positionPlus scroll to new position from previous position
     */
    fun scrolling(positionPlus: Int) {
        if (calculateSnapViewPosition() > -1) {
            var centerViewPosition = calculateSnapViewPosition() + positionPlus
            if (centerViewPosition <= 0)
                centerViewPosition = 0
            else if (centerViewPosition >= adapter!!.itemCount - 1)
                centerViewPosition = adapter!!.itemCount - 1

            smoothScrollToPosition(centerViewPosition)
            if (sliderListener != null) {
                sliderListener!!.onPositionChange(centerViewPosition)
            }
            currentPosition = centerViewPosition
        }
    }

    /**
     * @return position fit in screen for parent list
     */
    private val parentAnchor: Int
        get() = (if (sliderLayoutManager.orientation == VERTICAL) height else width) / 2

    /**
     * @param view item view
     * @return position fit in screen specific view in parent
     */
    private fun getViewAnchor(view: View?): Int {
        return if (sliderLayoutManager.orientation == VERTICAL) view?.top!! + view.height / 2
        else view?.left!! + view.width / 2
    }

    /**
     * @return calculate snapping position relation anchor
     */
    private fun calculateSnapViewPosition(): Int {
        val parentAnchor = parentAnchor
        val lastVisibleItemPosition = sliderLayoutManager.findLastVisibleItemPosition()
        val firstVisibleItemPosition = sliderLayoutManager.findFirstVisibleItemPosition()

        if (firstVisibleItemPosition > -1) {
            val currentViewClosestToAnchor = sliderLayoutManager.findViewByPosition(firstVisibleItemPosition)
            var currentViewClosestToAnchorDistance = parentAnchor - getViewAnchor(currentViewClosestToAnchor)
            var currentViewClosestToAnchorPosition = firstVisibleItemPosition

            for (i in firstVisibleItemPosition + 1..lastVisibleItemPosition) {
                val view = sliderLayoutManager.findViewByPosition(i)
                val distanceToCenter = parentAnchor - getViewAnchor(view)
                if (abs(distanceToCenter) < abs(currentViewClosestToAnchorDistance)) {
                    currentViewClosestToAnchorPosition = i
                    currentViewClosestToAnchorDistance = distanceToCenter
                }
            }
            return currentViewClosestToAnchorPosition
        } else {
            return -1
        }
    }

    inner class Scheduler(millisInFuture: Long, countDownInterval: Long) :
        CountDownTimer(millisInFuture, countDownInterval) {

        override fun onTick(millisUntilFinished: Long) {}

        override fun onFinish() {
            if (isLoopMode) {
                if (reverseLoop)
                    scrolling(+1)
                else
                    scrolling(-1)

            } else {
                if (currentPosition >= adapter!!.itemCount - 1)
                    scrollToPosition(0)
            }
            cancel()
            if (scrolling) start()
        }
    }

    fun addSliderListener(listener: SliderListener) {
        this.sliderListener = listener
    }

    fun removeSliderListener() {
        this.sliderListener = null
    }

    /**
     * @param centerThreshold enable calculating the distance from center point threshold item
     */ /*Rec*/
    fun setCalculateCenterThreshold(centerThreshold: Boolean) {
        sliderLayoutManager.setCalculateCenterThreshold(centerThreshold)
    }


    /**
     * @param scrollSpeed change speed scrolling item
     */
    fun scrollSpeed(scrollSpeed: Float) {
        sliderLayoutManager.setScrollSpeed(scrollSpeed)
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
            this.addOnScrollListener(object : EndlessListener(sliderLayoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                    sliderLazyLoadListener?.onLoadMore(
                        page,
                        totalItemsCount,
                        view as SliderRecyclerView
                    )
                }
            })
        else
            this.clearOnScrollListeners()
    }

    /**
     * @param orientation set VERTICAL/HORIZONTAL
     */
    private fun initializeManager(
        @SliderRecyclerView.SliderOrientation orientation: Int,
        enablePadding: Boolean = true
    ) {

        this.layoutManager = sliderLayoutManager
        val padding: Int
        when (orientation) {
            HORIZONTAL -> {
                padding = if (enablePadding) ViewHelper.getScreenWidth() / 4 else 1
                this.setPadding(padding, 0, padding, 0)
            }
            SliderRecyclerView.VERTICAL -> {
                padding = if (enablePadding) ViewHelper.getScreenHeight() / 4 else 1
                this.setPadding(0, padding, 0, padding)
            }
        }
    }


    companion object {

        //slider orientation
        const val HORIZONTAL = 0
        const val VERTICAL = 1

        //anchor default
        private val CENTER = 0

    }

    @IntDef(
        VERTICAL,
        HORIZONTAL
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class SliderOrientation




    /**
     * @return onItemTouchListener for calculate velocity and position fix view center
     */
    private fun onItemTouchListener(): OnItemTouchListener {
        return object : OnItemTouchListener {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                val action = e.actionMasked
                gestureDetector?.onTouchEvent(e);
                Log.d("Track_action_Action", "action -- $action ")
                return false
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
        }
    }

    private val gestureListener = object : GestureDetector.SimpleOnGestureListener() {
        override fun onScroll(
            e1: MotionEvent?,
            e2: MotionEvent?,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            val motion = e2;
            Log.d(TAG, "GestureDetector-onScroll: ")
            if (velocityTracker == null) {
                velocityTracker = VelocityTracker.obtain()
                velocityTracker?.addMovement(motion)
                velocityTracker?.computeCurrentVelocity(1000)

            } else {
                velocityTracker!!.clear()
                velocityTracker?.addMovement(motion)
                velocityTracker?.computeCurrentVelocity(1000)
            }
            velocityTracker!!.addMovement(motion)


            if (velocityTracker != null) {
                Log.d(TAG, "onScroll: xVelocity ${velocityTracker!!.xVelocity}")
                when (sliderLayoutManager.orientation) {
                    HORIZONTAL -> if (velocityTracker!!.xVelocity < 0) {
                        if (!isTrustLayout){
                            scrolling(-1)// rtl or reverse mode
                            Log.d(TAG, "onScroll: rtl or reverse mode -1 -- xVelocity<0  ")
                        }

                        else
                        {
                            scrolling(1)//scroll to right
                            Log.d(TAG, "onScroll: scroll to right +1 -- xVelocity<0  ")
                        }
                    } else if (velocityTracker!!.xVelocity > 0) {
                        if (!isTrustLayout){
                            scrolling(1)// rtl or reverse mode
                            Log.d(TAG, "onScroll: rtl or reverse mode +1 Veclocity ${velocityTracker!!.xVelocity}")
                        }

                        else{
                            scrolling(-1)//scroll to left
                            Log.d(TAG, "onScroll: scroll to left -1 ${velocityTracker!!.xVelocity} ")
                        }
                    }
                    VERTICAL -> if (velocityTracker!!.yVelocity <= 0) {
                        if (sliderLayoutManager.reverseLayout)
                            scrolling(-1)// rtl or reverse mode
                        else
                            scrolling(1)//scroll to up
                    } else if (velocityTracker!!.yVelocity > 0) {
                        if (sliderLayoutManager.reverseLayout)
                            scrolling(1)// rtl or reverse mode
                        else
                            scrolling(-1)//scroll to down
                    }

                }
//                velocityTracker?.computeCurrentVelocity(1000)
                velocityTracker!!.recycle()
                velocityTracker = null
            }
//            return super.onScroll(e1, e2, distanceX, distanceY)
            return true;
        }

        override fun onSingleTapUp(e: MotionEvent?): Boolean {
            Log.d(TAG, "GestureDetector-onSingleTapUp: ")
            return super.onSingleTapUp(e)
        }
    }
    init {
        gestureDetector = GestureDetector(context, gestureListener)
    }

}

var TAG = "Track_action"