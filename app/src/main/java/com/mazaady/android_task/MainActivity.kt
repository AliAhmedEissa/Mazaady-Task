package com.mazaady.android_task

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mazaady.android_task.databinding.ActivityMainBinding
import com.mazaady.android_task.util.LoadingViewManager
import com.mazaady.android_task.util.extention.gone
import com.mazaady.android_task.util.extention.visible
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MainActivity : AppCompatActivity(),LoadingViewManager {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    override fun showLoading() {
        binding.viewLoading.root.visible()
    }

    override fun hideLoading() {
        binding.viewLoading.root.gone()
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(updateLocale(newBase, Locale("ar")))
    }

    private fun updateLocale(context: Context, locale: Locale): Context {
        Locale.setDefault(locale)
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        return context.createConfigurationContext(config)
    }



}