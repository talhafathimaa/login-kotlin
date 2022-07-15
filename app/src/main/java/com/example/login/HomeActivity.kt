package com.example.login

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.login.databinding.ActivityHomeBinding
import com.example.login.models.UserDetails
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val userName = intent.getStringExtra("userName")
        if (userName != null) {
            getProfile(userName)
        }
    }

    private fun getProfile(userName: String) {
        val retrofitInstance = RetrofitInstance.getRetrofitInstance().create(API::class.java)
        retrofitInstance.getProfile(userName).enqueue(object : Callback<UserDetails> {
            override fun onFailure(call: Call<UserDetails>, t: Throwable) {
                Toast.makeText(
                    this@HomeActivity,
                    t.message,
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onResponse(
                call: Call<UserDetails>,
                response: Response<UserDetails>
            ) {
                val userDetails: UserDetails? = response.body()
                binding.userid.text = getString(R.string.user_id, userDetails?.id.toString())
                binding.username.text = getString(R.string.username, userDetails?.userName ?: "")
                binding.firstname.text = getString(R.string.firstname, userDetails?.firstName ?: "")
                binding.lastname.text = getString(R.string.lastname, userDetails?.lastName ?: "")
            }
        })
    }
}