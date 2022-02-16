package com.nasa.astronomy.presentation.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.fragment.NavHostFragment
import com.nasa.astronomy.presentation.R
import com.nasa.astronomy.presentation.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val navHostFragment: NavHostFragment by lazy {
        (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment)
    }
    val navController: NavController by lazy {
        navHostFragment.navController
    }
    val navGraph: NavGraph by lazy {
        navController.navInflater.inflate(R.navigation.presentation_nav)
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    }
}
