package com.denizk0461.weserplaner.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.denizk0461.weserplaner.databinding.FragmentExamOverviewBinding
import com.denizk0461.weserplaner.viewmodel.ExamOverviewViewModel

/**
 * Fragment that shows the user an overview of their exams.
 */
class ExamOverviewFragment : AppFragment<FragmentExamOverviewBinding>() {

    // View model reference for providing access to the database
    private val viewModel: ExamOverviewViewModel by viewModels()

    // Instantiate the view binding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentExamOverviewBinding.inflate(inflater, container, false)
        return binding.root
    }
}