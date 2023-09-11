package com.example.spk.sliding

import android.app.Activity
import android.app.Application
import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.ContentFrameLayout
import androidx.core.view.children
import androidx.fragment.app.Fragment

object SlidingUtils {
    var isShowApplicationSliding = true

    private fun createdView(context: Context, view: View): SlidingSuspensionView {
        val slidingView = SlidingSuspensionView(context).apply {
            addView(view)
        }
        slidingView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        return slidingView
    }

    /**
     * 添加Activity悬浮窗
     */
    fun showSliding(activity: Activity, view: View) {
        activity.findViewById<ContentFrameLayout>(android.R.id.content)
            .addView(createdView(activity, view))
    }

    /**
     * 添加Fragment悬浮窗
     */
    fun showSliding(fragment: Fragment, view: View) {
        if (fragment.view is ViewGroup) {
            (fragment.view as ViewGroup).addView(createdView(fragment.requireContext(), view))
        }
    }

    /**
     * 添加View悬浮窗
     */
    fun showSliding(view: View, showView: View) {
        if (view is ViewGroup) {
            view.addView(createdView(view.context, showView))
        }
    }

    /**
     * 添加APP悬浮窗
     * @param view : view的context必须为Application
     */
    fun showApplicationSliding(activity: Activity, applicationContext: Application, view: View) {
        if (view.context !is Application) {
            throw IllegalArgumentException("view.context != Application,The global floating window must use application as context!")
        } else {
            isShowApplicationSliding = true
            applicationContext.registerActivityLifecycleCallbacks(SlidingLifecycleCallback.apply {
                attach(activity, view)
            })
        }
    }

    /**
     * 移除悬浮窗View
     */
    fun removeView(activity: Activity) {
        for (child in activity.findViewById<ContentFrameLayout>(android.R.id.content).children) {
            if (child is SlidingSuspensionView) {
                activity.findViewById<ContentFrameLayout>(android.R.id.content).removeView(child)
            }
        }
    }

    /**
     * 移除Fragment悬浮窗View
     */
    fun removeView(fragment: Fragment) {
        if (fragment.view is ViewGroup) {
            for (child in (fragment.view as ViewGroup).children) {
                if (child is SlidingSuspensionView) {
                    (fragment.view as ViewGroup).removeView(child)
                }
            }
        }
    }

    /**
     * 移除ViewGroup 悬浮窗View
     * @param view SlidingSuspensionView的上层ViewGroup
     */
    fun removeView(view: View) {
        if (view.context is Application) {
            removeApplicationView(view)
        } else {
            remove(view)
        }
    }

    /**
     * 移除Application 悬浮窗View
     * @param view SlidingSuspensionView的上层ViewGroup
     */
    private fun removeApplicationView(view: View) {
        remove(view)
        isShowApplicationSliding = false
    }


    private fun remove(view: View) {
        if (view.parent is SlidingSuspensionView) {
            for (child in (view.parent.parent as ViewGroup).children) {
                if (child is SlidingSuspensionView) {
                    (view.parent.parent as ViewGroup).removeView(child)
                }
            }
        }
    }
}