package com.neu.madcourse.mad_team4_finalproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupActionBarWithNavController(findNavController(R.id.fragment_nav_host))
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragment_nav_host)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}