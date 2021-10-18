package com.inator.calculator.extensions

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.View
import android.widget.LinearLayout
import androidx.core.animation.doOnEnd
import androidx.core.view.updateLayoutParams

fun View.hide(doOnEnd: ((animator: Animator) -> Unit)? = null) {
    ObjectAnimator.ofFloat(this, "alpha", 1f, 0f).apply {
        duration = 250
        doOnEnd { doOnEnd?.invoke(this) }
        start()
    }
}

fun View.show(doOnEnd: ((animator: Animator) -> Unit)? = null) {
    ObjectAnimator.ofFloat(this, "alpha", 0f, 1f).apply {
        duration = 250
        doOnEnd { doOnEnd?.invoke(this) }
        start()
    }
}

fun View.updateWeight(weight: Float) {
    updateLayoutParams<LinearLayout.LayoutParams> {
        this.weight = weight
    }
}

fun View.animateWeight(fromWeight: Float, toWeight: Float) {
    ValueAnimator.ofFloat(fromWeight, toWeight).apply {
        addUpdateListener {
            updateWeight(it.animatedValue as Float)
        }
        duration = 300
        start()
    }
}

fun View.weight() = (layoutParams as LinearLayout.LayoutParams).weight