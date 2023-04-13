package com.denizk0461.studip.fragment

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.denizk0461.studip.BuildConfig
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

        var toast2 = makeToast("2")
        var toast22 = makeToast("22")
        var toast222 = makeToast("222")

//        binding.layoutQuarter.setOnClickListener {
//            isQuarterChecked = !isQuarterChecked
//            binding.switchQuarter.isChecked = isQuarterChecked
//        }

        binding.switchQuarter.setOnClickListener {
            isQuarterChecked = !isQuarterChecked
        }

        binding.buttonAppVersion.setOnClickListener {
            when (appVersionClick) {
                19 -> {
                    toast2.show()
                    appVersionClick += 1
                }
                20 -> {
                    toast2.cancel()
                    toast22.show()
                    appVersionClick += 1
                }
                21 -> {
                    toast22.cancel()
                    toast222.show()
                    appVersionClick += 1
                }
                22 -> {
                    toast222.cancel()
                    toast2 = makeToast("2")
                    toast22 = makeToast("22")
                    toast222 = makeToast("222")
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(Misc.mysteryLink)))
                    appVersionClick = 0
                }
                else -> appVersionClick += 1
            }
        }

        binding.appVersionText.text = BuildConfig.VERSION_NAME
    }

    private fun makeToast(text: String): Toast = Toast.makeText(context, text, Toast.LENGTH_SHORT)

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}