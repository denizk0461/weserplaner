package com.denizk0461.studip.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.denizk0461.studip.BuildConfig
import com.denizk0461.studip.activity.FetcherActivity
import com.denizk0461.studip.data.Misc
import com.denizk0461.studip.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var appVersionClick = 0

    private var isQuarterChecked = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding.layoutQuarter.setOnClickListener {
//            isQuarterChecked = !isQuarterChecked
//            binding.switchQuarter.isChecked = isQuarterChecked
//        }

        binding.buttonRefreshSchedule.setOnClickListener {
            launchWebView()
        }

        binding.switchQuarter.setOnClickListener {
            isQuarterChecked = !isQuarterChecked
        }

        binding.buttonAppVersion.setOnClickListener {
            when (appVersionClick) {
                19 -> {
                    appVersionClick += 1
                }
                20 -> {
                    appVersionClick += 1
                }
                21 -> {
                    appVersionClick += 1
                }
                22 -> {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(Misc.mysteryLink)))
                    appVersionClick = 0
                }
                else -> appVersionClick += 1
            }
        }

        @SuppressLint("SetTextI18n")
        binding.appVersionText.text = "${BuildConfig.VERSION_NAME}-${if (BuildConfig.DEBUG) "debug" else "release"}"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun launchWebView() {
        startActivity(Intent(context, FetcherActivity::class.java))
    }
}