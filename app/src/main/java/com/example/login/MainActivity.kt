package com.example.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.login.databinding.ActivityMainBinding
import com.example.login.models.LogInBody
import com.example.login.models.LogInResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this)
        binding.loginButton.setOnClickListener {
            logIn(
                binding.username.text.toString(),
                binding.password.text.toString()
            )
        }
        binding.regButton.setOnClickListener {
            val registerIntent = Intent(this, RegisterActivity()::class.java)
            startActivity(registerIntent)
        }
    }


    private fun logIn(userName: String, password: String) {
        if (userName.isNotBlank() && password.isNotBlank()) {
            val retrofitInstance = RetrofitInstance.getRetrofitInstance().create(API::class.java)
            val logInBody = LogInBody(userName, password)
            retrofitInstance.logIn(logInBody).enqueue(object : Callback<LogInResponse> {
                override fun onFailure(call: Call<LogInResponse>, t: Throwable) {
                    Toast.makeText(
                        this@MainActivity,
                        t.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onResponse(
                    call: Call<LogInResponse>,
                    response: Response<LogInResponse>
                ) {
                    if (response.code() == 200) {
                        val logInResponse=response.body()
                        Toast.makeText(
                            this@MainActivity,
                            "Login Successful",
                            Toast.LENGTH_SHORT
                        ).show()
                        if (logInResponse != null) {
                            sessionManager.saveToken(logInResponse.token)
                        }
                        val homeIntent = Intent(this@MainActivity, HomeActivity::class.java)
                        startActivity(homeIntent)
                    } else {
                        val gson = Gson()
                        val type = object : TypeToken<LogInResponse>() {}.type
                        val logInResponse: LogInResponse? = gson.fromJson(response.errorBody()!!.charStream(), type)
                        Toast.makeText(
                            this@MainActivity,
                            logInResponse?.msg ?: "Login failed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })
        } else {
            Toast.makeText(this@MainActivity, "Enter all fields", Toast.LENGTH_SHORT).show()
        }
    }
}