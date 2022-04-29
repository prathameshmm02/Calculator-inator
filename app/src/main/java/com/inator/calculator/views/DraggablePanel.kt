package com.inator.calculator.views

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.accessibility.AccessibilityEvent
import android.view.animation.DecelerateInterpolator
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout

class DraggablePanel(context: Context?, attrs: AttributeSet?) : LinearLayout(context, attrs) {
    private var isOpen = false
    private var initialX = 0f
    private var initialY = 0f
    private var originalHeight = -1
    private var fullHeight = -1
    private var diffHeight = 0

    /**
     * How far the panel is offset from its closed position.
     * range [0, 1] where 0 = closed, 1 = open.
     */
    private var dragOffset = 0f

    //From where after Action Up the panel should go i.e. open or close
    private val centerPoint = 0.5f
    private var mPanelSlideListener: PanelSlideListener? = null
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (originalHeight == -1) {
            originalHeight = measuredHeight
            diffHeight = fullHeight - originalHeight
        }
        if (fullHeight == -1) {
            fullHeight = (parent as ViewGroup).height
            diffHeight = fullHeight - originalHeight
        }
        super.onLayout(changed, l, t, r, b)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val action = event.actionMasked
        if (action == MotionEvent.ACTION_DOWN) {
            val x = event.x
            val y = event.y
            initialX = x
            initialY = y
        } else if (action == MotionEvent.ACTION_UP) {
            val x = event.x
            val y = event.y
            val dx = x - initialX
            val dy = y - initialY
            if (isOpen) {
                if (-dy > diffHeight * centerPoint) {
                    isOpen = false
                    smoothPanelClose(300)
                } else {
                    smoothPanelOpen(300)
                }
            } else {
                if (dy > diffHeight * centerPoint) {
                    isOpen = true
                    smoothPanelOpen(300)
                } else {
                    smoothPanelClose(300)
                }
            }
        } else if (action == MotionEvent.ACTION_MOVE) {
            val x = event.x
            val y = event.y
            val dx = x - initialX
            val dy = y - initialY
            if (isOpen) {
                if (y > initialY) {
                    return true
                } else if (fullHeight + dy > originalHeight) {
                    layoutParams = ConstraintLayout.LayoutParams(width, (fullHeight + dy).toInt())
                }
            } else {
                if (y < initialY) {
                    return true
                } else if (originalHeight + dy < fullHeight) {
                    layoutParams =
                        ConstraintLayout.LayoutParams(width, (originalHeight + dy).toInt())
                }
            }
            dragOffset =
                (height - originalHeight).toFloat() / (fullHeight - originalHeight).toFloat()
            dispatchOnPanelSlide(this)
        }
        this.performClick()
        return true
    }

    private fun smoothPanelOpen(duration: Int) {
        val animator = ValueAnimator.ofInt(height, fullHeight)
        animator.addUpdateListener {
            layoutParams = ConstraintLayout.LayoutParams(
                width, (animator.animatedValue as Int)
            )
        }
        animator.duration = duration.toLong()
        animator.interpolator = DecelerateInterpolator()
        animator.start()
        dragOffset = 1.0f
        dispatchOnPanelOpened(this)
    }

    fun smoothPanelClose(duration: Int) {
        val animator = ValueAnimator.ofInt(height, originalHeight)
        animator.addUpdateListener {
            layoutParams = ConstraintLayout.LayoutParams(
                width, (animator.animatedValue as Int)
            )
        }
        animator.duration = duration.toLong()
        animator.interpolator = DecelerateInterpolator()
        animator.start()
        dragOffset = 0.0f
        dispatchOnPanelClosed(this)
    }

    fun isOpen(): Boolean {
        return isOpen
    }

    fun setPanelSlideListener(listener: PanelSlideListener?) {
        mPanelSlideListener = listener
    }

    interface PanelSlideListener {
        fun onPanelSlide(view: View, mDragOffset: Float)
        fun onPanelOpened(view: View)
        fun onPanelClosed(view: View)
    }

    private fun dispatchOnPanelSlide(view: View) {
        if (mPanelSlideListener != null) {
            mPanelSlideListener!!.onPanelSlide(view, dragOffset)
        }
    }

    private fun dispatchOnPanelOpened(view: View) {
        if (mPanelSlideListener != null) {
            mPanelSlideListener!!.onPanelOpened(view)
        }
        sendAccessibilityEvent(AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED)
    }

    private fun dispatchOnPanelClosed(view: View) {
        if (mPanelSlideListener != null) {
            mPanelSlideListener!!.onPanelClosed(view)
        }
        sendAccessibilityEvent(AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED)
    }
}