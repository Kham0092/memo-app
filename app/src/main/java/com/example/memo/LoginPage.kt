package com.example.memo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.memo.databinding.ActivityLoginPageBinding
import com.google.firebase.auth.FirebaseAuth

class LoginPage : AppCompatActivity() {
    private lateinit var binding: ActivityLoginPageBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginPageBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            startActivity(Intent(this@LoginPage, MainActivity::class.java))
        }
        setContentView(binding.root)
        binding.btnGoToSignUp.setOnClickListener {
            startActivity(Intent(this@LoginPage, SignUpPage::class.java))
        }
        binding.btnLogIn.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val pass = binding.etPass.text.toString()
            auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                if (it.isSuccessful) {
                    startActivity(Intent(this@LoginPage, MainActivity::class.java))
                    Toast.makeText(this@LoginPage, "Successfull login", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@LoginPage, "Unsuccessfull login", Toast.LENGTH_SHORT).show()
                }

            }

        }
    }
}