package com.mazaady.android_task

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.LabelVisibilityMode
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
        initBottomNavigation()

    }

    private fun initBottomNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController
        binding.navView.labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_SELECTED

        binding.navView.setupWithNavController(navController)

        // Optional: Prevent re-selection actions for the BottomNavigationView
        binding.navView.setOnItemReselectedListener { }
    }

    override fun showLoading() {
        binding.viewLoading.root.visible()
    }

    override fun hideLoading() {
        binding.viewLoading.root.gone()
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(updateLocale(newBase, Locale("en")))
    }

    private fun updateLocale(context: Context, locale: Locale): Context {
        Locale.setDefault(locale)
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        return context.createConfigurationContext(config)
    }



}