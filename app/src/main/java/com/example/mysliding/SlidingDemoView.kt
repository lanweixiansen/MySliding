package com.example.mysliding

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.mysliding.databinding.HomeViewSlidingDemoBinding
import com.example.spk.sliding.SlidingUtils

class SlidingDemoView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    init {
        HomeViewSlidingDemoBinding.inflate(LayoutInflater.from(context))
    }

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        if (visibility == View.VISIBLE) {
            if (childCount == 0)
                SlidingUtils.showSliding(this, SlidingTestView(context, "View"))
        }
    }

}