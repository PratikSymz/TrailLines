package com.neu.madcourse.mad_team4_finalproject.utils

import android.content.Context
import android.text.StaticLayout
import android.text.TextUtils.TruncateAt
import android.text.TextUtils.ellipsize
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class ResizableTextView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : AppCompatTextView(context, attrs, defStyleAttr) {
    companion object {
        const val LIMIT_LINE_COUNT: Int = 4
        const val TEXT: String = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
        const val TEXT_EXPAND_ACTION: String = "read more"
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        // Extract the view's given width
        val givenWidth = MeasureSpec.getSize(widthMeasureSpec)
        // Remove horizontal padding and drawable space
        val maxLineWidth = givenWidth - compoundPaddingStart - compoundPaddingEnd

        // Create a static layout
        val staticLayout = StaticLayout.Builder
                // Provide the original text and the maximum line width
                .obtain(TEXT, 0, TEXT.length, paint, maxLineWidth)
                // Provide the limited line count
                .setMaxLines(LIMIT_LINE_COUNT)
                // Provide all other text configurations
                .setIncludePad(false)
                .setEllipsize(TruncateAt.END)
                .setLineSpacing(lineSpacingExtra, lineSpacingMultiplier)
                .build()

        // Calculate the text using the formula -> // TODO:
        val sumOfLw = (0 until staticLayout.lineCount).sumOf { staticLayout.getLineWidth(it).toInt() }
        val expandActionWidth = paint.measureText(TEXT_EXPAND_ACTION)
        val truncatedTextWidth = sumOfLw - expandActionWidth
        val truncatedText = ellipsize(TEXT, paint, truncatedTextWidth, TruncateAt.END)

        // Append expand action to the truncated text
        val finalDisplayedText = "$truncatedText$TEXT_EXPAND_ACTION"

    }
}