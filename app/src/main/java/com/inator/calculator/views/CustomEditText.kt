package com.inator.calculator.views

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import androidx.appcompat.widget.AppCompatEditText


class CustomEditText : AppCompatEditText {
    private val mContext: Context
    private val initialSize = 30F

    constructor(context: Context) : super(context) {
        this.mContext = context
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        this.mContext = context
    }

    constructor (context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        this.mContext = context
    }

    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        if (text.isNullOrEmpty()) {
            setTextSize(
                TypedValue.COMPLEX_UNIT_SP,
                initialSize
            )
        } else {
            val paint = Paint()
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)

            paint.textSize = textSize

            val bounds = Rect()
            val myString = text.toString()
            paint.getTextBounds(myString, 0, myString.length, bounds)
            Log.d(TAG, "width: " + bounds.width().toString() + " height: " + bounds.height())
            Log.d(TAG, "width: $width")
            while (bounds.width() - 150 > width) {
                setTextSize(
                    TypedValue.COMPLEX_UNIT_SP,
                    textSize / resources.displayMetrics.scaledDensity - 1
                )
                paint.textSize = textSize
                paint.getTextBounds(myString, 0, myString.length, bounds)
            }

        }
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
    }

}