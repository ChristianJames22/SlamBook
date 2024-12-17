package com.example.slambook

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

class AboutMe : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_me)

        val etName: EditText = findViewById(R.id.etName)
        val etNickname: EditText = findViewById(R.id.etNickname)
        val etBirthdate: EditText = findViewById(R.id.etBirthdate)
        val etAge: EditText = findViewById(R.id.etAge)
        val etHobbies: EditText = findViewById(R.id.etHobbies)
        val etSports: EditText = findViewById(R.id.etSports)
        val spinnerSex: Spinner = findViewById(R.id.spinnerSex)
        val btnContinue: Button = findViewById(R.id.btnContinue)
        val btnSkip: Button = findViewById(R.id.btnSkip)

        // Set up Spinner for Sex input
        val sexOptions = arrayListOf("Sex", "Male", "Female")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, sexOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerSex.adapter = adapter

        // Set up DatePickerDialog for Birthdate input
        etBirthdate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    val formattedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                    etBirthdate.setText(formattedDate)

                    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
                    val age = currentYear - selectedYear
                    etAge.setText(age.toString())
                },
                year,
                month,
                day
            )

            datePickerDialog.datePicker.maxDate = Calendar.getInstance().apply {
                set(2018, Calendar.DECEMBER, 31)
            }.timeInMillis

            datePickerDialog.show()
        }

        // Handle "Continue" button click
        btnContinue.setOnClickListener {
            val fields = listOf(etName, etNickname, etBirthdate, etHobbies, etSports)
            var allFieldsValid = true

            // Reset all fields to default hint color
            fields.forEach {
                it.setHintTextColor(Color.GRAY)
            }

            // Check if all fields are filled
            fields.forEach { field ->
                if (field.text.toString().isEmpty()) {
                    field.setHintTextColor(Color.RED)
                    allFieldsValid = false
                }
            }

            // Check if a valid option is selected in the Spinner
            if (spinnerSex.selectedItemPosition == 0) {
                spinnerSex.setBackgroundColor(Color.RED)
                allFieldsValid = false
            } else {
                spinnerSex.setBackgroundColor(Color.TRANSPARENT)
            }

            if (allFieldsValid) {
                val sharedPref = getSharedPreferences("user_data", Context.MODE_PRIVATE)
                with(sharedPref.edit()) {
                    putString("USER_NAME", etName.text.toString())
                    putString("USER_NICKNAME", etNickname.text.toString())
                    putString("USER_BIRTHDATE", etBirthdate.text.toString())
                    putString("USER_AGE", etAge.text.toString())
                    putString("USER_SEX", spinnerSex.selectedItem.toString())
                    putString("USER_HOBBIES", etHobbies.text.toString())
                    putString("USER_SPORTS", etSports.text.toString())
                    apply()
                }

                val statePref = getSharedPreferences("app_state", Context.MODE_PRIVATE)
                with(statePref.edit()) {
                    putBoolean("SKIPPED_ABOUT_ME", false)
                    apply()
                }

                val intent = Intent(this, UserActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show()
            }
        }

        // Handle "Skip" button click with confirmation dialog
        btnSkip.setOnClickListener {
            showSkipConfirmationDialog()
        }
    }

    // Show confirmation dialog for skipping
    private fun showSkipConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Skip For Now?")
        builder.setPositiveButton("Yes") { _, _ ->
            val statePref = getSharedPreferences("app_state", Context.MODE_PRIVATE)
            with(statePref.edit()) {
                putBoolean("SKIPPED_ABOUT_ME", true)
                apply()
            }

            val intent = Intent(this, UserActivity::class.java)
            startActivity(intent)
            finish()
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }

    override fun onStart() {
        super.onStart()

        val statePref = getSharedPreferences("app_state", Context.MODE_PRIVATE)
        val skippedAboutMe = statePref.getBoolean("SKIPPED_ABOUT_ME", false)

        if (skippedAboutMe) {
            val intent = Intent(this, UserActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
