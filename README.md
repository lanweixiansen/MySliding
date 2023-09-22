# MySliding

依赖方式：

AGP 8+  settings.gradle下：

dependencyResolutionManagement {
    repositories {
        ...
        maven { url = uri("https://jitpack.io") }
    }
}

AGP 8- 根目录build.gradle下:

allprojects {
    repositories {
    ...
    maven { url "https://jitpack.io"}
}

2：implementation("com.github.lanweixiansen:MySliding:1.0.0")


## 使用方式

    /**
     * 添加Activity悬浮窗
     */
     SlidingUtils.showSliding(activity: Activity, view: View)

    /**
     * 添加Fragment悬浮窗
     */
    SlidingUtils.showSliding(fragment: Fragment, view: View)

    /**
     * 添加View悬浮窗
     */
     SlidingUtils.showSliding(view: View, showView: View)

    /**
     * 添加APP悬浮窗
     * @param view : view的context必须为Application
     */
    SlidingUtils.showApplicationSliding(activity: Activity, applicationContext: Application, view: View)

## Demo体验

![image](https://github.com/lanweixiansen/MySliding/assets/46479511/2b8792d3-c63d-42f0-9ff9-ad99d721837e)



