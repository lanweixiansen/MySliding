# MySliding

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
