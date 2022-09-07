package com.c22ps104.heypetanimalwelfare.view.signup

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.c22ps104.heypetanimalwelfare.data.PreferencesHelper
import com.c22ps104.heypetanimalwelfare.databinding.ActivitySignupBinding
import com.c22ps104.heypetanimalwelfare.view.onboarding.OnBoardingActivity

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private val signupViewModel: SignupViewModel by viewModels()
    private lateinit var preferencesHelper: PreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferencesHelper = PreferencesHelper(this)

        setupView()
    }

    private fun setupView() {
        supportActionBar?.hide()

        binding.btnSignup.setOnClickListener {
            val name = binding.etUsername.text.toString()
            val email = binding.etEmail.text.toString()
            val pass = binding.etPassword.text.toString()

            signupViewModel.register(
                email = email,
                password = pass,
                name = name,
                bio = "",
                phoneNumber = ""
            )
        }

        signupViewModel.register.observe(this) {
            if (it != null) {
                saveSession(
                    it.data.token.accessToken,
                    it.data.user.id.toString(),
                    it.data.user.name
                )

                Toast.makeText(this, "Registration Success", Toast.LENGTH_SHORT).show()

                val intentToOnBoarding = Intent(this@SignupActivity, OnBoardingActivity::class.java)
                intentToOnBoarding.putExtra(EXTRA_TOKEN, it.data.token.accessToken)
                intentToOnBoarding.putExtra(EXTRA_USERNAME, it.data.user.name)
                startActivity(intentToOnBoarding)
                finish()
                RESULT_OK
            }
        }
    }

    private fun saveSession(accessToken: String, userId: String, userName: String) {
        preferencesHelper.putString(PreferencesHelper.PREF_TOKEN, accessToken)
        preferencesHelper.putString(PreferencesHelper.PREF_ID, userId)
        preferencesHelper.putBoolean(PreferencesHelper.PREF_IS_LOGIN, true)
        preferencesHelper.putString(PreferencesHelper.PREF_USER_NAME, userName)
    }

    companion object {
        const val EXTRA_TOKEN = "EXTRA_TOKEN"
        const val EXTRA_USERNAME = "EXTRA_USERNAME"
    }
}