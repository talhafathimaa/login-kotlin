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
        println(password)
        var validationMessage = ""
        val userNameRegex = Regex("^[a-zA-Z0-9_.-]*$")
        val nameRegex = Regex("^[a-zA-Z ]*$")
        val passwordRegex = Regex("^[?!\\S]*$")
        println(passwordRegex.containsMatchIn(password))
        println(password.length)
        when {
            (userName.isBlank() || password.isEmpty() || firstName.isBlank() || lastName.isBlank()) -> validationMessage =
                "Enter all fields"
            !(userNameRegex.containsMatchIn(userName)) -> validationMessage =
                "Username should only contain alphabets, numbers and characters(_,.,-)"
            !(nameRegex.containsMatchIn(firstName))-> validationMessage =
                "First name can only have alphabets"
            !(nameRegex.containsMatchIn(lastName)) -> validationMessage =
                "Last name can only have alphabets"
            !(passwordRegex.containsMatchIn(password))-> validationMessage=
                "password cannot include spaces"
            (password.length<6) -> validationMessage =
                "Password should have at-least 6 characters"
        }
        if(validationMessage.isNotEmpty()){
        Toast.makeText(
            this@RegisterActivity,
            validationMessage, Toast.LENGTH_SHORT
        ).show()
            return false
        }
        return true
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