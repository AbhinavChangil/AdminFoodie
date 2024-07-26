package com.example.adminfoodie

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.adminfoodie.databinding.ActivityLoginBinding
import com.example.adminfoodie.databinding.ActivitySigninBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class LoginActivity : AppCompatActivity() {

    ///1. craete varibles to be used in authentication
    private lateinit var email:String
    private lateinit var password:String
    private lateinit var auth : FirebaseAuth
    private lateinit var database : DatabaseReference

    private val binding : ActivityLoginBinding by lazy{
        ActivityLoginBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        //2. initialize authentication and database
        auth = Firebase.auth
        database = Firebase.database.reference

        //3. button k click par kya hoga
        binding.btnLogin.setOnClickListener{

            //get values from edit text intoo variables we have creted at first
            email = binding.edtEmail.text.toString().trim()
            password = binding.edtPassword.text.toString().trim()

            //check if edit text is empty and if empty then show toast message
            if(email.isBlank() || password.isBlank()){
                showToast("Please fill all details!")
            }
            else{
                login(email, password)
            }

        }

        binding.tvSignupLink.setOnClickListener{
            val intent = Intent(this, SigninActivity::class.java)
            startActivity(intent)
            binding.tvSignupLink.setTextColor(Color.parseColor("#000000"))
            finish()
            binding.tvSignupLink.setTextColor(Color.parseColor("#1B14ED"))
        }
    }

    private fun login(email : String,password : String) {
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
            task ->
                    if(task.isSuccessful){
                        val user:FirebaseUser? = auth.currentUser
                        showToast("Login Successful!")
                        updateUI(user)
                    }
                    else{
                        updateUIFailure()
                        showToast("Wrong Credentials... Authentication Failed!")
                        Log.d("Authentication", "loginAccount: Authentication Failed", task.exception)
                    }
        }
    }

    private fun updateUIFailure() {
        binding.edtEmail.text.clear()
        binding.edtPassword.text.clear()
    }

    private fun updateUI(user: FirebaseUser?) {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun showToast(message:String){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show()
    }
}