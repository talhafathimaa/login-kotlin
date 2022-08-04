package com.example.login

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
            if (validateFields(
                    binding.username.text.toString(),
                    binding.firstname.text.toString(),
                    binding.lastname.text.toString(),
                    binding.password.text.toString()
                )
            ) {
                register(
                    binding.username.text.toString(),
                    binding.firstname.text.toString(),
                    binding.lastname.text.toString(),
                    binding.password.text.toString()
                )
            }
        }
    }

    private fun validateFields(
        userName: String,
        firstName: String,
        lastName: String,
        password: String
    ): Boolean {
        var valid=true
        val userNameRegex = Regex("^[a-zA-Z0-9_.-]*$")
        val nameRegex = Regex("^[a-zA-Z ]*$")
        val passwordRegex = Regex("^[?!\\S]*$")
            if((userName.isBlank() || password.isEmpty() || firstName.isBlank() || lastName.isBlank())) {
                Toast.makeText(
                    this@RegisterActivity,
                    "Enter all fields",
                    Toast.LENGTH_SHORT
                ).show()
                valid=false
            }
            if(!(userNameRegex.containsMatchIn(userName))) {
                binding.username.error = "Username should only contain alphabets, numbers and characters(_,.,-)"
                valid=false
            }
            if(!(nameRegex.containsMatchIn(firstName))) {
                binding.firstname.error = "First name can only have alphabets"
                valid=false
            }
            if(!(nameRegex.containsMatchIn(lastName))) {
                binding.lastname.error = "Last name can only have alphabets"
                valid=false
            }
            if(!(passwordRegex.containsMatchIn(password))) {
                binding.password.error = "password cannot include spaces"
                valid=false
            }
            if(password.length<6) {
                binding.password.error = "Password should have at-least 6 characters"
                valid=false
            }
        return valid
        }

    private fun register(
        userName: String,
        firstName: String,
        lastName: String,
        password: String
    ) {
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
    }
}