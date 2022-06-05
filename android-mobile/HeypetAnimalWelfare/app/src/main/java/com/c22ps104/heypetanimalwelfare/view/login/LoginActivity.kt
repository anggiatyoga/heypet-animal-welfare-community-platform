package com.c22ps104.heypetanimalwelfare.view.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.c22ps104.heypetanimalwelfare.data.PreferencesHelper
import com.c22ps104.heypetanimalwelfare.data.PreferencesHelper.Companion.PREF_ID
import com.c22ps104.heypetanimalwelfare.data.PreferencesHelper.Companion.PREF_IS_LOGIN
import com.c22ps104.heypetanimalwelfare.data.PreferencesHelper.Companion.PREF_TOKEN
import com.c22ps104.heypetanimalwelfare.databinding.ActivityLoginBinding
import com.c22ps104.heypetanimalwelfare.view.bottomnavigation.BottomNavigationActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel:LoginViewModel by viewModels()
    private lateinit var preferencesHelper: PreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferencesHelper = PreferencesHelper(this)

        setupView()
    }

    private fun setupView() {
        supportActionBar?.hide()

        binding.btnLogin.setOnClickListener {

            val email = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()

            Log.d("LoginFrom","$email $password")
            viewModel.login(email,password)

            viewModel.login.observe(this) {
                if (it != null) {
                    saveSession(it.data.token.accessToken, it.data.user.id.toString())

                    val intentToMain = Intent(this@LoginActivity, BottomNavigationActivity::class.java)
                    intentToMain.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intentToMain)
                    finish()
                }
            }
        }
    }

    private fun saveSession(accessToken: String, userId: String) {
        preferencesHelper.putString(PREF_TOKEN, accessToken)
        preferencesHelper.putString(PREF_ID, userId)
        preferencesHelper.putBoolean(PREF_IS_LOGIN, true)
    }
}