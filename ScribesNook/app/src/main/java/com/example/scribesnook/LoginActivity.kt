package com.example.scribesnook

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.scribesnook.api.BackendApi
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var usernameField: EditText
    private lateinit var passwordField: EditText
    private lateinit var loginButton: Button
    private lateinit var registerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 🔐 Auto-login if token exists
        Prefs.getToken(this)?.let {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.login_activity)

        usernameField = findViewById(R.id.usernameField)
        passwordField = findViewById(R.id.passwordField)
        loginButton = findViewById(R.id.loginButton)
        registerButton = findViewById(R.id.registerButton)

        loginButton.setOnClickListener { performLogin() }
        registerButton.setOnClickListener { performRegister() }
    }

    private fun performLogin() {
        val username = usernameField.text.toString().trim()
        val password = passwordField.text.toString().trim()

        if (username.isEmpty() || password.isEmpty()) {
            toast("Username and password required")
            return
        }

        lifecycleScope.launch {
            try {
                val response = BackendApi.service.login(
                    LoginRequest(username, password)
                )

                if (response.isSuccessful) {
                    val token = response.body()?.token

                    if (token.isNullOrEmpty()) {
                        toast("Login failed: empty token")
                        return@launch
                    }

                    Prefs.saveToken(this@LoginActivity, token)
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()

                } else {
                    Log.e("LoginActivity", response.errorBody()?.string() ?: "")
                    toast("Invalid username or password")
                }

            } catch (e: Exception) {
                Log.e("LoginActivity", "Login error", e)
                toast("Network error")
            }
        }
    }

    private fun performRegister() {
        val username = usernameField.text.toString().trim()
        val password = passwordField.text.toString().trim()

        if (username.isEmpty() || password.length < 6) {
            toast("Password must be at least 6 characters")
            return
        }

        lifecycleScope.launch {
            try {
                val response = BackendApi.service.register(
                    LoginRequest(username, password)
                )

                if (response.isSuccessful) {
                    toast("Registered successfully! You can now log in.")
                } else {
                    Log.e("Register", response.errorBody()?.string() ?: "")
                    toast("Username already exists")
                }

            } catch (e: Exception) {
                Log.e("Register", "Register error", e)
                toast("Network error")
            }
        }
    }

    private fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}
