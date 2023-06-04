package com.denizk0461.weserplaner.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.denizk0461.weserplaner.databinding.ActivityIntroductionBinding

class IntroductionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIntroductionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate view binding and bind to this activity
        binding = ActivityIntroductionBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}