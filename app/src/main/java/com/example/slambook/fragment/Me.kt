package com.example.slambook

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.NumberPicker
import android.widget.RadioGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import java.util.Calendar

class Me : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_me, container, false)

        val sharedPref = requireContext().getSharedPreferences("user_data", Context.MODE_PRIVATE)

        // Initialize views
        val nameTextView = view.findViewById<TextView>(R.id.textName)
        val nicknameTextView = view.findViewById<TextView>(R.id.textNickname)
        val birthdateTextView = view.findViewById<TextView>(R.id.textBirthdate)
        val ageTextView = view.findViewById<TextView>(R.id.textAge)
        val sexTextView = view.findViewById<TextView>(R.id.textSex)
        val hobbiesTextView = view.findViewById<TextView>(R.id.textHobbies)
        val sportsTextView = view.findViewById<TextView>(R.id.textSports)

        // Load SharedPreferences data
        nameTextView.text = "Name: ${sharedPref.getString("USER_NAME", "(add name)")}"
        nicknameTextView.text = "Nickname: ${sharedPref.getString("USER_NICKNAME", "(add nickname)")}"
        birthdateTextView.text = "Birthdate: ${sharedPref.getString("USER_BIRTHDATE", "(add birthdate)")}"
        ageTextView.text = "Age: ${sharedPref.getString("USER_AGE", "(add age)")}"
        sexTextView.text = "Sex: ${sharedPref.getString("USER_SEX", "(add gender)")}"
        hobbiesTextView.text = "Hobbies: ${sharedPref.getString("USER_HOBBIES", "(add hobbies)")}"
        sportsTextView.text = "Sports: ${sharedPref.getString("USER_SPORTS", "(add sports)")}"

        // Set listeners for edit icons
        view.findViewById<ImageView>(R.id.iconEditName).setOnClickListener {
            showEditDialog("Edit Name", "USER_NAME", nameTextView, sharedPref)
        }

        view.findViewById<ImageView>(R.id.iconEditNickname).setOnClickListener {
            showEditDialog("Edit Nickname", "USER_NICKNAME", nicknameTextView, sharedPref)
        }

        view.findViewById<ImageView>(R.id.iconEditBirthdate).setOnClickListener {
            showDatePickerDialog(sharedPref, birthdateTextView, ageTextView)
        }

        view.findViewById<ImageView>(R.id.iconEditAge).setOnClickListener {
            showAgePickerDialog(sharedPref, ageTextView, birthdateTextView)
        }

        view.findViewById<ImageView>(R.id.iconEditSex).setOnClickListener {
            showSexPickerDialog(sharedPref, sexTextView)
        }

        view.findViewById<ImageView>(R.id.iconEditHobbies).setOnClickListener {
            showEditDialog("Edit Hobbies", "USER_HOBBIES", hobbiesTextView, sharedPref)
        }

        view.findViewById<ImageView>(R.id.iconEditSports).setOnClickListener {
            showEditDialog("Edit Sports", "USER_SPORTS", sportsTextView, sharedPref)
        }

        return view
    }

    private fun showEditDialog(
        title: String,
        key: String,
        textView: TextView,
        sharedPref: SharedPreferences
    ) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_field, null)
        val editText = dialogView.findViewById<EditText>(R.id.editTextField)

        // Pre-fill the current value
        editText.setText(textView.text.toString().split(": ")[1])

        AlertDialog.Builder(context)
            .setTitle(title)
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val newValue = editText.text.toString()
                sharedPref.edit().putString(key, newValue).apply()
                textView.text = "${title.split(" ")[1]}: $newValue"
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showDatePickerDialog(
        sharedPref: SharedPreferences,
        birthdateTextView: TextView,
        ageTextView: TextView
    ) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val selectedDate = "$year-${month + 1}-$dayOfMonth"
                val currentYear = Calendar.getInstance().get(Calendar.YEAR)
                val age = currentYear - year
                sharedPref.edit().putString("USER_BIRTHDATE", selectedDate).apply()
                sharedPref.edit().putString("USER_AGE", age.toString()).apply()
                birthdateTextView.text = "Birthdate: $selectedDate"
                ageTextView.text = "Age: $age"
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun showAgePickerDialog(
        sharedPref: SharedPreferences,
        ageTextView: TextView,
        birthdateTextView: TextView
    ) {
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_number_picker, null)
        val numberPicker = dialogView.findViewById<NumberPicker>(R.id.numberPickerAge)
        numberPicker.minValue = 1
        numberPicker.maxValue = 100

        AlertDialog.Builder(requireContext())
            .setTitle("Select Age")
            .setView(dialogView)
            .setPositiveButton("Set") { _, _ ->
                val selectedAge = numberPicker.value
                val birthYear = currentYear - selectedAge
                val birthdate = "$birthYear-01-01"
                sharedPref.edit().putString("USER_AGE", selectedAge.toString()).apply()
                sharedPref.edit().putString("USER_BIRTHDATE", birthdate).apply()
                ageTextView.text = "Age: $selectedAge"
                birthdateTextView.text = "Birthdate: $birthdate"
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showSexPickerDialog(sharedPref: SharedPreferences, sexTextView: TextView) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_select_sex, null)
        val radioGroup = dialogView.findViewById<RadioGroup>(R.id.radioGroupSex)

        AlertDialog.Builder(requireContext())
            .setTitle("Select Gender")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val selectedSex = when (radioGroup.checkedRadioButtonId) {
                    R.id.radioMale -> "Male"
                    R.id.radioFemale -> "Female"
                    else -> "N/A"
                }
                sharedPref.edit().putString("USER_SEX", selectedSex).apply()
                sexTextView.text = "Sex: $selectedSex"
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
