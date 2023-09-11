package com.example.mysliding

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.spk.sliding.SlidingUtils

class SlidingTestView @JvmOverloads constructor(
    context: Context, content: String, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {
    private var mOnCloseClick: (() -> Unit)? = null

    init {
        inflate(context, R.layout.home_view_sliding_test, this)
        findViewById<TextView>(R.id.tv_test).apply {
            text = "$content 悬浮窗"
            setOnClickListener {
                Toast.makeText(context, "点击了悬浮窗", Toast.LENGTH_SHORT).show()
            }
        }
        findViewById<ImageView>(R.id.btn_close).setOnClickListener {
            SlidingUtils.removeView(this)
            mOnCloseClick?.invoke()
        }
    }

    fun setOnCloseClickListener(onCloseClick: () -> Unit) {
        this.mOnCloseClick = onCloseClick
    }
}