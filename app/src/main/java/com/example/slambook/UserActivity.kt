package com.example.slambook

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class UserActivity : AppCompatActivity() {

    private lateinit var homeFragment: Home
    private lateinit var meFragment: Me
    private lateinit var toDoFragment: ToDo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        // Retrieve user data
        val sharedPref = getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val name = sharedPref.getString("USER_NAME", "N/A")

        // Initialize fragments
        homeFragment = Home()
        meFragment = Me().apply { arguments = Bundle().apply { putString("USER_NAME", name) } }
        toDoFragment = ToDo()

        // Default fragment
        replaceFragment(homeFragment)

        // Bottom Navigation
        val bottomNav: BottomNavigationView = findViewById(R.id.bottomNavigation)
        bottomNav.setOnNavigationItemSelectedListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> replaceFragment(homeFragment)
                R.id.nav_todo -> replaceFragment(toDoFragment)
                R.id.nav_me -> replaceFragment(meFragment)
            }
            true
        }

        // Exit Button
        val btnExit: ImageButton = findViewById(R.id.btnSettings)
        btnExit.setOnClickListener { showExitConfirmationDialog() }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    private fun showExitConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Exit App")
            .setMessage("Are you sure you want to exit?")
            .setPositiveButton("Yes") { _, _ -> finishAffinity() }
            .setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
            .show()
    }
}
