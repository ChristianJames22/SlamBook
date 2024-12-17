package com.example.slambook

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check for the last visited activity
        val sharedPref = getSharedPreferences("app_state", Context.MODE_PRIVATE)
        val lastScreen = sharedPref.getString("LAST_SCREEN", "MainActivity")

        if (lastScreen != "MainActivity") {
            val intent = when (lastScreen) {
                "AboutMe" -> Intent(this, AboutMe::class.java)
                "UserActivity" -> Intent(this, UserActivity::class.java)
                else -> Intent(this, AboutMe::class.java) // Default fallback
            }
            startActivity(intent)
            finish() // Close MainActivity after redirection
            return
        }

        // Set layout if no previous screen
        setContentView(R.layout.activity_main)

        // Handle "Get Started" button click
        val btnGetStarted: Button = findViewById(R.id.btnGetStarted)
        btnGetStarted.setOnClickListener {
            with(sharedPref.edit()) {
                putString("LAST_SCREEN", "AboutMe")
                apply()
            }
            val intent = Intent(this, AboutMe::class.java)
            startActivity(intent)
        }
    }
}
