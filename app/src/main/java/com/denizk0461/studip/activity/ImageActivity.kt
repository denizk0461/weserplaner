package com.denizk0461.studip.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.denizk0461.studip.R
import com.denizk0461.studip.databinding.ActivityImageBinding

/**
 * Activity used solely to display a single image. Not a critical part of the app.
 */
class ImageActivity : AppCompatActivity() {

    // View binding
    private lateinit var binding: ActivityImageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        // Inflate view binding and bind to this activity
        binding = ActivityImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set the image of the view
        binding.imageView.setImageResource(when (intent.extras?.getString("img") ?: "") {
            "bojack" -> { R.drawable.man }
            "garbage" -> { R.drawable.garbage }
            else -> { R.drawable.engineering }
        })
    }
}