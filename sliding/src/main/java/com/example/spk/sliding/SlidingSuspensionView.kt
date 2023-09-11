package com.example.spk.sliding

import android.animation.LayoutTransition
import android.animation.ObjectAnimator
import android.app.Activity
import android.app.Application
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import android.widget.LinearLayout
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.ScreenUtils
import kotlin.math.abs
import kotlin.math.max

internal class SlidingSuspensionView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    /**
     * 判断用户是点击事件还是滑动事件
     */
    private var mOldX = 0f
    private var mOldY = 0f

    /**
     * 记录当前点击位置
     */
    private var mOldRawX = 0f
    private var mOldRawY = 0f

    /**
     * 记录当前View移动位置
     */
    private var mOldTranslationX = 0f
    private var mOldTranslationY = 0f

    /**
     * 父布局的宽高，及悬浮窗View的最大展示范围
     */
    private var mMaxWidth = 0
    private var mMaxHeight = 0

    /**
     * 滑动阻力系数，越界之后阻力系数越来越大
     */
    private var mCoefficient = 0f

    /**
     * 边距
     */
    private var mMargin = dp2px(8f)


    init {

        layoutTransition = LayoutTransition()
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        var interceptor = false
        when (ev?.action) {
            MotionEvent.ACTION_DOWN -> {
                mOldX = ev.x
                mOldY = ev.y
                mMaxWidth = (parent as? ViewGroup)?.width ?: 0
                mMaxHeight = (parent as? ViewGroup)?.height ?: 0
                mOldRawX = ev.rawX
                mOldRawY = ev.rawY
                mOldTranslationX = this.translationX
                mOldTranslationY = this.translationY
            }

            MotionEvent.ACTION_MOVE -> {
                // 误差10以内视为点击事件处理
                interceptor = abs(mOldX - ev.x) > 10 || abs(mOldY - ev.y) > 10
            }
        }
        return interceptor
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {}

            MotionEvent.ACTION_MOVE -> {
                checkOut {
                    return true
                }
                this.translationX = (event.rawX - mOldRawX + mOldTranslationX)
                this.translationY = (event.rawY - mOldRawY + mOldTranslationY)
            }

            MotionEvent.ACTION_UP -> {
                showAnim(this.x, this.y, event.rawY)
            }
        }
        return true
    }

    /**
     * View回弹事件，高度增加状态栏及导航栏判断
     */
    private fun showAnim(rawX: Float, rawY: Float, rawY1: Float?) {
        val margin = if (childCount > 1) 0f else mMargin
        val viewWidth = getChildAt(childCount - 1).width
        if ((rawX + viewWidth / 2) <= mMaxWidth / 2) {
            startAnim(margin, true)
        } else {
            startAnim(mMaxWidth - max(viewWidth, 132) - margin, true)
        }
        val barHeight =
            if (rawY1 == null) 0
            else if ((rawY1 - this.height / 2) <= BarUtils.getStatusBarHeight() * 2)
                BarUtils.getStatusBarHeight()
            else if (BarUtils.isNavBarVisible(ActivityUtils.getTopActivity())
                && (rawY1 + this.height / 2 + BarUtils.getNavBarHeight()) >= getScreenHeight()
            )
                BarUtils.getNavBarHeight()
            else 0
        if (rawY >= mMaxHeight - margin - this.height - barHeight) {
            startAnim(mMaxHeight - this.bottom - margin, false)
        } else if (rawY <= margin + barHeight) {
            startAnim(-(this.top - margin - barHeight), false)
        }
    }

    /**
     * 检查是否越界及控制越界阻力系数
     */
    private inline fun checkOut(checkOut: () -> Unit) {
        if (this.x <= -this.width / 2 || this.x >= mMaxWidth - this.width / 2) {
            if (childCount > 1) {
                checkOut()
            } else {
                getChildAt(childCount - 1).toGone()
                addView(SlidingSuspensionExpandView(
                    context, this.x >= mMaxWidth - this.width / 2
                ).apply {
                    setOnClickListener {
                        removeView(this)
                        getChildAt(childCount - 1).toVisible()
                        showAnim(
                            this@SlidingSuspensionView.x,
                            this@SlidingSuspensionView.y,
                            null
                        )
                    }
                })
                checkOut()
            }
        } else if (childCount > 1) {
            if (this.x > this.width && this.x < mMaxWidth - this.width * 2 && this.width >= 132) {
                removeViewAt(childCount - 1)
                getChildAt(childCount - 1).toVisible()
            }
        }

    }

    /**
     * 越界回弹动画
     */
    private fun startAnim(startLocation: Float, isX: Boolean) {
        val anim =
            ObjectAnimator.ofFloat(this, if (isX) "translationX" else "translationY", startLocation)
        anim.duration = 500
        anim.interpolator = OvershootInterpolator()
        anim.start()
    }
}

internal fun dp2px(dp: Float) = ConvertUtils.dp2px(dp).toFloat()

internal fun getScreenHeight() = ScreenUtils.getScreenHeight().toFloat()

internal fun View.toGone() {
    this.visibility = View.GONE
}

internal fun View.toVisible() {
    this.visibility = View.VISIBLE
}
