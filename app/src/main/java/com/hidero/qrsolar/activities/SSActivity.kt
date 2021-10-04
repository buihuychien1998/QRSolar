package com.hidero.qrsolar.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.github.ybq.android.spinkit.style.ThreeBounce
import com.hidero.qrsolar.R
import com.hidero.qrsolar.utils.Colors.colors
import kotlinx.android.synthetic.main.activity_ss.*

class SSActivity : AppCompatActivity() {

    private val SPLASH_DISPLAY_LENGTH: Long = 1000
    var mDrawable: ThreeBounce? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ss)
        val animation = AnimationUtils.loadAnimation(this, R.anim.reversedscan)
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                bar.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })

//        emotionalFaceView.setOnTouchListener { view, motionEvent ->
//            bar.visibility = View.VISIBLE
//            bar.startAnimation(animation)
//            false
//        }
        bar.visibility = View.VISIBLE
        bar.startAnimation(animation)


        //Textview
         mDrawable= ThreeBounce()
        mDrawable!!.setBounds(0, 0, 100, 100)
        mDrawable!!.setColor(Color.BLACK)
        tvLoading.setCompoundDrawables(null, null, mDrawable!!, null)

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            /* Create an Intent that will start the Menu-Activity. */
            val mainIntent = Intent(this, MainActivity::class.java)
            this.startActivity(mainIntent)
            this.finish()
        }, SPLASH_DISPLAY_LENGTH)
    }

    override fun onResume() {
        super.onResume()
        mDrawable!!.start()
    }

    override fun onStop() {
        super.onStop()
        mDrawable!!.stop()
    }
}
