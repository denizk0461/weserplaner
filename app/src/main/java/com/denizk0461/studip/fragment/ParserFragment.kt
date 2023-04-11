package com.denizk0461.studip.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.denizk0461.studip.data.StudIPParser
import com.denizk0461.studip.databinding.FragmentParserBinding
import com.denizk0461.studip.viewmodel.ParserViewModel
import java.net.URLDecoder

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class ParserFragment : Fragment() {

    private var _binding: FragmentParserBinding? = null
    private var html = "" // temporary storage for the website HTML

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val viewModel: ParserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentParserBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding.buttonSecond.setOnClickListener {
//            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
//        }

        binding.webview.settings.apply {
            javaScriptEnabled = true
        }
        binding.webview.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView,
                request: WebResourceRequest
            ): Boolean {
                return false
            }
            override fun onPageFinished(view: WebView, url: String) {
                binding.webview.loadUrl(
                    "javascript:window.HtmlViewer.showHTML" +
                            "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');"
                )
            }
        }

        binding.webview.loadUrl("https://elearning.uni-bremen.de/index.php?again=yes")

        binding.fab.setOnClickListener { view ->
            binding.webview.evaluateJavascript(
                "(function(){return encodeURI(document.getElementsByTagName('html')[0].innerHTML)})();"
            ) { p0 ->
                viewModel.nukeEvents()
                StudIPParser().parse(URLDecoder.decode(p0, "UTF-8")) { events ->
                    viewModel.insertEvents(events)
                }
            }
//            html.chunked(3000).forEach {
//                Log.d("HELLO", it)
//            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}