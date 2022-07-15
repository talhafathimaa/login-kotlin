package com.example.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.login.databinding.ActivityMainBinding
import com.example.login.models.LogInBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
            retrofitInstance.logIn(logInBody).enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(
                        this@MainActivity,
                        t.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.code() == 200) {
                        Toast.makeText(
                            this@MainActivity,
                            response.body()?.string(),
                            Toast.LENGTH_SHORT
                        ).show()
                        val homeIntent = Intent(this@MainActivity, HomeActivity::class.java)
                        homeIntent.putExtra("userName", userName)
                        startActivity(homeIntent)
                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            response.errorBody()?.string(),
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