package com.example.app_readbook

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import com.example.app_readbook.`interface`.Users
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class login_activity : AppCompatActivity() {
    lateinit var usernameEditText: EditText
    lateinit var passwordEditText: EditText
    lateinit var loginButton: Button
    lateinit var savepass: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

         usernameEditText = findViewById<EditText>(R.id.editTextUsername)
         passwordEditText = findViewById<EditText>(R.id.editTextPassword)
         loginButton = findViewById<Button>(R.id.buttonLogin)
         savepass  = findViewById<CheckBox>(R.id.savePasswordCheckBox)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                loginUser(username, password)
            } else {
                Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show()
            }
        }
        savepass.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                // Save password
                val password = passwordEditText.text.toString()
                val username = usernameEditText.text.toString()
                if (!username.isEmpty() && !password.isEmpty()) {
                    val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("password", password)
                    editor.putString("username", username)
                    editor.putBoolean("savePassword", true)
                    editor.apply()
                } else {
                    Toast.makeText(baseContext, "Hãy nhập Username và Password trước khi lưu!!!", Toast.LENGTH_SHORT).show()
                    savepass.isChecked = false // Do not save if password is not entered
                }
            } else {
                // Remove saved password
                val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.remove("password")
                editor.remove("username")
                editor.putBoolean("savePassword", false)
                editor.apply()
            }
        }

        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val isPasswordSaved = sharedPreferences.getBoolean("savePassword", false)
        if (isPasswordSaved) {
            val savedUsername = sharedPreferences.getString("username", "")
            val savedPassword = sharedPreferences.getString("password", "")

            if (!savedPassword.isNullOrEmpty()) {
                passwordEditText.setText(savedPassword)
                usernameEditText.setText(savedUsername)
                savepass.isChecked = true
            } else {
                Toast.makeText(this, "Nhập Username, Password trước khi lưu!!!", Toast.LENGTH_SHORT).show()
                savepass.isChecked = false // Do not check if password is not saved
            }
        } else {
            passwordEditText.text = null
            usernameEditText.text = null
            savepass.isChecked = false
        }

        usernameEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                // Do nothing
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                // Do nothing
            }

            override fun afterTextChanged(editable: Editable) {
                // Save credentials whenever the username changes
                saveCredentials()
            }
        })

        passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                // Do nothing
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                // Do nothing
            }

            override fun afterTextChanged(editable: Editable) {
                // Save credentials whenever the password changes
                saveCredentials()
            }
        })


    }
    private fun saveCredentials() {
        val password = passwordEditText.text.toString()
        val username = usernameEditText.text.toString()
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("password", password)
        editor.putString("username", username)
        editor.putBoolean("savePassword", true)
        editor.apply()
    }
    private fun loginUser(username: String, password: String) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val usersService = Users.retrofit.create(Users::class.java)
                val response = usersService.loginUser(Users.LoginRequest(username, password))
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    val message = loginResponse?.message
                    val token = loginResponse?.token
                    val iduser = loginResponse?._id

                    // Perform actions based on the response
                    // For example, update UI, save token to shared preferences, navigate to the next screen, etc.

                    // Update LiveData to notify observers
                    val intent = Intent(baseContext,MainActivity::class.java)
                    val  sharedPreferences = getSharedPreferences("UserLogin", Context.MODE_PRIVATE)
                    val editText = sharedPreferences.edit()
                    editText.putString("username",username)
                    editText.putString("password",password)
                    editText.putString("iduser",iduser)
                    editText.apply()
                    Toast.makeText(baseContext, "Login successful", Toast.LENGTH_SHORT).show()

                    startActivity(intent)
                } else {
                    // Handle login failure
                    Toast.makeText(baseContext, "Login failed", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                // Handle exception
                Toast.makeText(baseContext, "An error occurred", Toast.LENGTH_SHORT).show()
            }
        }
    }
}