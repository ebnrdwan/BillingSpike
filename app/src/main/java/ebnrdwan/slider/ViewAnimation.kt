package ebnrdwan.slider

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.os.Build
import android.view.View
import android.view.animation.BaseInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import androidx.annotation.RequiresApi

object Animations {
    const val Duration: Long = 500
    const val Delay: Long = 500
    const val XLeftStart: Float = -500f
    const val XRightStart: Float = 500f
    const val YTopStart: Float = -1000f
    const val YBottomStart: Float = 100000f
    const val EndPosition: Float = 0f
    const val fadedOut: Float = 0f
    const val fadedIn: Float = 1f
    const val reverse: Boolean = false
    const val repeatCount: Int = 0
    const val lottieStart: Float = 0.5f
}

@RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
fun View.translateFromStart(
    interpolator: BaseInterpolator = OvershootInterpolator(),
    reverse: Boolean = Animations.reverse,
    repeatCount: Int = Animations.repeatCount,
    duration: Long = Animations.Duration,
    delay: Long = Animations.Delay,
    xStart: Float = Animations.XLeftStart,
    xEnd: Float = Animations.EndPosition

) {
    val animator = ObjectAnimator.ofFloat(this, View.TRANSLATION_X, xStart, xEnd)
    val view = this
    view.visibility = View.INVISIBLE
    animator?.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationStart(animation: Animator?) {
            view.visibility = View.VISIBLE
            super.onAnimationStart(animation)
        }
    })
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
        animator.interpolator = interpolator
    } else animator.interpolator = OvershootInterpolator()
    if (reverse) {

        animator.repeatMode = ObjectAnimator.REVERSE
    }
    animator.repeatCount = repeatCount
    animator.duration = duration
    animator?.startDelay = delay
    animator.start()
}


fun View.translaterFromEnd(
    interpolator: BaseInterpolator = OvershootInterpolator(),
    reverse: Boolean = Animations.reverse,
    repeatCount: Int = Animations.repeatCount,
    duration: Long = Animations.Duration,
    delay: Long = Animations.Delay,
    xStart: Float = Animations.XRightStart,
    xEnd: Float = Animations.EndPosition
) {


    val animator = ObjectAnimator.ofFloat(this, View.TRANSLATION_X, xStart, xEnd)
    animator.duration = 1000
    val view = this
    view.visibility = View.INVISIBLE
    animator?.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationStart(animation: Animator?) {
            view.visibility = View.VISIBLE
            super.onAnimationStart(animation)
        }
    })
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
        animator.interpolator = interpolator
    } else animator.interpolator = OvershootInterpolator()
    animator.repeatCount = repeatCount
    if (reverse) {
        animator.repeatMode = ObjectAnimator.REVERSE
    }

    animator.duration = duration
    animator?.startDelay = delay
    animator.start()
}


fun View.translaterFromUp(
    interpolator: BaseInterpolator = OvershootInterpolator(),
    reverse: Boolean = Animations.reverse,
    repeatCount: Int = Animations.repeatCount,
    duration: Long = Animations.Duration,
    delay: Long = Animations.Delay,
    start: Float = Animations.YTopStart,
    end: Float = Animations.EndPosition
) {
    val animator = ObjectAnimator.ofFloat(this, View.TRANSLATION_Y, start, end)
    val view = this
    view.visibility = View.INVISIBLE
    animator?.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationStart(animation: Animator?) {
            view.visibility = View.VISIBLE
            super.onAnimationStart(animation)
        }
    })
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
        animator.interpolator = interpolator
    } else animator.interpolator = OvershootInterpolator()
    if (reverse) {

        animator.repeatMode = ObjectAnimator.REVERSE
    }
    animator.repeatCount = repeatCount
    animator.duration = duration
    animator?.startDelay = delay
    animator.start()
}


fun View.translaterFromBottom(
    interpolator: BaseInterpolator = OvershootInterpolator(),
    reverse: Boolean = Animations.reverse,
    repeatCount: Int = Animations.repeatCount,
    duration: Long = Animations.Duration,
    delay: Long = Animations.Delay,
    start: Float = Animations.YBottomStart,
    end: Float = Animations.EndPosition
) {
    val animator = ObjectAnimator.ofFloat(this, View.TRANSLATION_Y, start, end)
    val view = this
    view.visibility = View.INVISIBLE
    animator.duration = duration
    animator?.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationStart(animation: Animator?) {
            view.visibility = View.VISIBLE
            super.onAnimationStart(animation)
        }
    })
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
        animator.interpolator = interpolator
    } else animator.interpolator = OvershootInterpolator()
    if (reverse) {

        animator.repeatMode = ObjectAnimator.REVERSE
    }
    animator.repeatCount = repeatCount

    animator?.startDelay = delay
    animator.start()
}


fun View.fade(
    fadeout: Boolean = false,
    interpolator: BaseInterpolator = DecelerateInterpolator(),
    reverse: Boolean = false,
    repeatCount: Int = Animations.repeatCount,
    duration: Long = Animations.Duration,
    delay: Long = Animations.Delay,
    fadedOut: Float = Animations.fadedOut,
    fadedIn: Float = Animations.fadedIn
) {

    val view = this
    view.visibility = View.INVISIBLE
    val fade: ObjectAnimator? = if (fadeout) {
        ObjectAnimator.ofFloat(this, View.ALPHA, fadedIn, fadedOut)
    } else ObjectAnimator.ofFloat(this, View.ALPHA, fadedOut, fadedIn)

    fade?.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationStart(animation: Animator?) {
            view.visibility = View.VISIBLE
            super.onAnimationStart(animation)
        }

        override fun onAnimationEnd(animation: Animator?) {
            super.onAnimationEnd(animation)
            if (fadeout) view.visibility = View.GONE
        }
    })
    fade?.apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            fade.interpolator = interpolator
        } else fade.interpolator = OvershootInterpolator()
        if (reverse) {
            fade.repeatMode = ObjectAnimator.REVERSE
        }
        fade.repeatCount = repeatCount
        fade.duration = duration
    }
    fade?.startDelay = delay
    fade?.start()
}
