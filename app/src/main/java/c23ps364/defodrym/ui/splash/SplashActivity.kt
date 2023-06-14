package c23ps364.defodrym.ui.splash

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import c23ps364.defodrym.MainActivity
import c23ps364.defodrym.R

class SplashActivity : AppCompatActivity() {
    private val DELAY: Long = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val logo = findViewById<View>(R.id.imageView)

        val scaleXAnimator = ObjectAnimator.ofFloat(logo, "scaleX", 0.5f, 1f)
        val scaleYAnimator = ObjectAnimator.ofFloat(logo, "scaleY", 0.5f, 1f)
        val alphaAnimator = ObjectAnimator.ofFloat(logo, "alpha", 0f, 1f)

        scaleXAnimator.duration = 1000
        scaleYAnimator.duration = 1000
        alphaAnimator.duration = 1000

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(scaleXAnimator, scaleYAnimator, alphaAnimator)

        Handler(Looper.getMainLooper()).postDelayed({
            animatorSet.start()
        }, 500)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, DELAY)
    }
}
