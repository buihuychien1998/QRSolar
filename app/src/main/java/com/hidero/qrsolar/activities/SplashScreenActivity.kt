package com.hidero.qrsolar.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import androidx.dynamicanimation.animation.SpringForce.DAMPING_RATIO_LOW_BOUNCY
import androidx.dynamicanimation.animation.SpringForce.STIFFNESS_LOW
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.activity_splash_screen.*


class SplashScreenActivity : AppCompatActivity() {


    private val SPLASH_DISPLAY_LENGTH: Long = 5000

    @SuppressLint("ClickableViewAccessibility")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.hidero.qrsolar.R.layout.activity_splash_screen)
        val animation = AnimationUtils.loadAnimation(this, com.hidero.qrsolar.R.anim.scan)
        // 1
        happyButton.setOnClickListener {
            emotionalFaceView.happinessState = EmotionalFaceView.HAPPY
        }
// 2
        sadButton.setOnClickListener {
            emotionalFaceView.happinessState = EmotionalFaceView.SAD
        }

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
        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        Handler().postDelayed(Runnable {
            /* Create an Intent that will start the Menu-Activity. */
            val mainIntent = Intent(this, MainActivity::class.java)
            this.startActivity(mainIntent)
            this.finish()
        }, SPLASH_DISPLAY_LENGTH)
    }

    // Create a SpringAnimation with the stiffness of STIFFNESS_LOW and damping ratio of DAMPING_RATIO_LOW_BOUNCY
    fun createSpringAnim(view: View, property: DynamicAnimation.ViewProperty): SpringAnimation {
        return SpringAnimation(view, property).setSpring(SpringForce().apply {
            stiffness = STIFFNESS_LOW
            dampingRatio = DAMPING_RATIO_LOW_BOUNCY
        })
    }
}
