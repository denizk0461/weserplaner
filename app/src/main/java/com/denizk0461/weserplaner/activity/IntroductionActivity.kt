package com.denizk0461.weserplaner.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.denizk0461.weserplaner.databinding.ActivityIntroductionBinding
import com.denizk0461.weserplaner.viewmodel.IntroductionViewModel

/**
 * Introductory activity for briefly telling the user about the app's features, and to allow quick
 * configuration of common settings.
 */
class IntroductionActivity : AppCompatActivity() {

    // View binding
    private lateinit var binding: ActivityIntroductionBinding

    // View model
    private lateinit var viewModel: IntroductionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Instantiate view model
        viewModel = ViewModelProvider.AndroidViewModelFactory(application)
            .create(IntroductionViewModel::class.java)

        // Inflate view binding and bind to this activity
        binding = ActivityIntroductionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonDownloadSchedule.setOnClickListener {
            // Save that the user has completed the tutorial
            viewModel.completedIntroduction()

            // Close the activity
            finish()

            /*
             * Start the main activity so that the user won't be thrown out of the app after they
             * complete the fetch or press the back button.
             */
            startActivity(Intent(this, MainActivity::class.java))

            // Start the fetcher activity
            startActivity(Intent(this, FetcherActivity::class.java))
        }

        binding.buttonContinue.setOnClickListener {
            // Save that the user has completed the tutorial
            viewModel.completedIntroduction()

            // Close the activity
            finish()

            // Start the main activity
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}