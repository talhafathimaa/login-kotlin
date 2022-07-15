package com.example.login;

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.login.databinding.ActivityRegisterBinding
import com.example.login.models.RegisterBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.registerButton.setOnClickListener {
            register(
                binding.username.text.toString(),
                binding.firstname.text.toString(),
                binding.lastname.text.toString(),
                binding.password.text.toString()
            )
        }

    }

    private fun register(
        userName: String,
        firstName: String,
        lastName: String,
        password: String
    ) {
        if (userName.isNotBlank() && password.isNotBlank() && firstName.isNotBlank() && lastName.isNotBlank()) {
            val retrofitInstance =
                RetrofitInstance.getRetrofitInstance().create(API::class.java)
            val registerBody = RegisterBody(userName, firstName, lastName, password)
            retrofitInstance.register(registerBody).enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(
                        this@RegisterActivity,
                        t.message,
                        Toast.LENGTH_SHORT
                    ).show()

                }

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.code() == 201) {
                        Toast.makeText(
                            this@RegisterActivity,
                            response.body()?.string(), Toast.LENGTH_SHORT
                        )
                            .show()
                        val mainIntent = Intent(this@RegisterActivity, MainActivity()::class.java)
                        startActivity(mainIntent)

                    } else {
                        Toast.makeText(
                            this@RegisterActivity,
                            response.errorBody()?.string(),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            })
        } else {
            Toast.makeText(
                this@RegisterActivity,
                "Enter all fields",
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }
}