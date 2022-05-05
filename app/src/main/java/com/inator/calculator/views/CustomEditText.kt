package com.inator.calculator.views

import android.content.Context
import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import com.google.android.material.textfield.TextInputEditText


class CustomEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = -1
) : TextInputEditText(context, attrs, defStyleAttr) {
    private val initialSize = 30F

    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        if (context.resources.configuration.orientation != ORIENTATION_LANDSCAPE) {
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
                while (bounds.width() - 150 > width) {
                    setTextSize(
                        TypedValue.COMPLEX_UNIT_SP,
                        textSize / resources.displayMetrics.scaledDensity - 1
                    )
                    paint.textSize = textSize
                    paint.getTextBounds(myString, 0, myString.length, bounds)
                }
            }
        }
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
    }
}