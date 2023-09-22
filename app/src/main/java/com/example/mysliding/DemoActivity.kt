package com.example.mysliding

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.spk.sliding.SlidingUtils

class DemoActivity : AppCompatActivity() {
    private var mFragment: SlidingDemoFragment? = null
    val test =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo)
        initDemo()
    }

    private fun initDemo() {
        findViewById<Button>(R.id.home_system).setOnClickListener {
            SlidingUtils.showSystemSliding(this, SlidingTestView(this@DemoActivity, "System"), test)
        }
        findViewById<Button>(R.id.home_activity).setOnClickListener {
            SlidingUtils.showSliding(
                this@DemoActivity,
                SlidingTestView(this@DemoActivity, "Activity")
            )
        }
        findViewById<Button>(R.id.home_app).setOnClickListener {
            SlidingUtils.showApplicationSliding(
                this@DemoActivity,
                application,
                SlidingTestView(applicationContext, "App")
            )
        }
        findViewById<Button>(R.id.home_fragment).setOnClickListener {
            mFragment = SlidingDemoFragment()
            supportFragmentManager.beginTransaction().add(R.id.fragment, mFragment!!).commit()
            R.id.btn_remove.buttonView().visibility = View.VISIBLE
        }
        R.id.home_view.buttonView().setOnClickListener {
            R.id.view_sliding.View().visibility = View.VISIBLE
            R.id.btn_remove_view.buttonView().visibility = View.VISIBLE
        }
        R.id.btn_remove.buttonView().setOnClickListener {
            R.id.btn_remove.buttonView().visibility = View.GONE
            supportFragmentManager.beginTransaction().remove(mFragment!!).commit()
            mFragment = null
        }
        R.id.btn_remove_view.buttonView().setOnClickListener {
            R.id.view_sliding.View().visibility = View.GONE
            R.id.btn_remove_view.buttonView().visibility = View.GONE
        }
    }


    private fun Int.buttonView(): Button = findViewById(this)
    private fun Int.View(): View = findViewById<View>(this)

}