package com.example.spk.sliding

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.example.spk.sliding.SlidingUtils.isShowApplicationSliding
import java.lang.ref.WeakReference

internal object SlidingLifecycleCallback : Application.ActivityLifecycleCallbacks {

    private var managerView: SlidingSuspensionView? = null
    private var mContainer: WeakReference<ViewGroup>? = null

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

    override fun onActivityStarted(activity: Activity) {}

    override fun onActivityResumed(activity: Activity) {
        if (isShowApplicationSliding)
            attach(activity)
    }

    fun attach(activity: Activity, view: View? = null) {
        activity.decorView?.let {
            if (managerView == null) {
                initManagerView(activity.applicationContext, view)
            } else {
                if (managerView?.visibility != View.VISIBLE) {
                    managerView?.toVisible()
                }
                detach()
            }
            setContainerGroup(it)
            mContainer?.get()?.addView(managerView)
        }
    }

    private fun setContainerGroup(it: FrameLayout) {
        mContainer = WeakReference(it)
    }

    private fun detach() {
        val containerGroup = mContainer?.get() ?: return
        if (managerView != null) {
            containerGroup.removeView(managerView)
        }
    }

    private fun initManagerView(activity: Context, mView: View?) {
        mView ?: return
        managerView = SlidingSuspensionView(activity).apply {
            addView(mView)
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
    }

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityStopped(activity: Activity) {}

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

    override fun onActivityDestroyed(activity: Activity) {}
}

internal val Activity.decorView: FrameLayout?
    get() = try {
        window.decorView as FrameLayout
    } catch (_: Exception) {
        null
    }