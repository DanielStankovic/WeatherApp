package com.example.weatherapp.ui.help

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentHelpBinding
import com.example.weatherapp.util.Constants.Companion.TUTORIAL_URL
import com.example.weatherapp.util.viewBinding

class HelpFragment : Fragment(R.layout.fragment_help) {

    private val binding:FragmentHelpBinding by viewBinding(FragmentHelpBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if(binding.webView.canGoBack()){
                   binding.webView.goBack()
                }else{
                    isEnabled = false
                    activity?.onBackPressed()
                }
            }
        })
    }
    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.webView.apply {
            webViewClient = WebViewClient()
            loadUrl(TUTORIAL_URL)
            settings.javaScriptEnabled = true

        }

    }

}

