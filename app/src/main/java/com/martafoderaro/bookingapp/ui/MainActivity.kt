package com.martafoderaro.bookingapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.martafoderaro.bookingapp.R
import com.martafoderaro.bookingapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity: AppCompatActivity() {

    private var _binding: ActivityMainBinding ? = null
    val binding: ActivityMainBinding
    get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainer, MainFragment.instance(), MainFragment.TAG)
            .commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}