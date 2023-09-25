package com.example.spk.sliding

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.content.Intent
import android.graphics.PixelFormat
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.ContentFrameLayout
import androidx.core.view.children
import androidx.fragment.app.Fragment


object SlidingUtils {
    var isShowApplicationSliding = true

    private fun createdView(
        context: Context,
        view: View,
        windowManager: WindowManager? = null,
        params: WindowManager.LayoutParams? = null
    ): SlidingSuspensionView {
        val slidingView = SlidingSuspensionView(context, windowManager, params).apply {
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
     * 添加系统级悬浮窗
     * @param test 权限申请回调 参数示例: val test = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {}
     */
    fun showSystemSliding(
        activity: AppCompatActivity, view: View, test: ActivityResultLauncher<Intent>
    ) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return
        }
        isShowApplicationSliding = true
        if (!Settings.canDrawOverlays(activity)) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:${activity.packageName}")
            )
            test.launch(intent)
        } else {
            // 已经获得了SYSTEM_ALERT_WINDOW权限，执行相关操作
            val windowManager = activity.getSystemService(WINDOW_SERVICE) as WindowManager?
            val params = WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,  // 悬浮窗类型
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,  // 不获取焦点
                PixelFormat.TRANSLUCENT
            )
            // 设置悬浮窗的位置、大小、布局等属性
            params.gravity = Gravity.TOP or Gravity.START
            params.x = 0
            params.y = 0
            // 添加悬浮窗视图到窗口
            windowManager?.addView(createdView(activity, view, windowManager, params), params)
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
            if (view.parent.parent is ViewGroup) {
                for (child in (view.parent.parent as ViewGroup).children) {
                    if (child is SlidingSuspensionView) {
                        (view.parent.parent as ViewGroup).removeView(child)
                    }
                }
            } else {
                (view.parent as SlidingSuspensionView).windowManager?.removeView(view.parent as SlidingSuspensionView)
            }
        }
    }
}