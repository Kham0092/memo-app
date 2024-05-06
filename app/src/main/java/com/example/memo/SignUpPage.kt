package com.example.memo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.memo.databinding.ActivitySignUpPageBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpPage : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpPageBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        binding.btnSignIn.setOnClickListener {
            createUser()
        }
        binding.btnGoToLogin.setOnClickListener {
            startActivity(Intent(this@SignUpPage,LoginPage::class.java))
        }
    }
    private fun createUser(){
        val email = binding.signEtEmail.text.toString()
        val pass = binding.signEtPass.text.toString()
        auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener {task->
            Toast.makeText(this@SignUpPage,"Sign Up Successfull",Toast.LENGTH_SHORT).show()
            if(task.isSuccessful){
                auth.signInWithEmailAndPassword(email,pass)
                startActivity(Intent(this@SignUpPage,MainActivity::class.java))
            }
        }
    }
}