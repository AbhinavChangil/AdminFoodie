package com.example.adminfoodie

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.adminfoodie.databinding.ActivityLoginBinding
import com.example.adminfoodie.databinding.ActivitySigninBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import com.google.android.gms.*
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.GooglePlayServicesUtil.isGooglePlayServicesAvailable
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {

    ///1. craete varibles to be used in authentication
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var googleSignInClient: GoogleSignInClient

    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        //set up Google Signin options to Request email
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        //initialize google sign in
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

        //on click listener on google sign in button
        binding.btnGoogle.setOnClickListener {
            val signinIntent: Intent = googleSignInClient.signInIntent
            launcher.launch(signinIntent)
        }

        //2. initialize authentication and database
        auth = Firebase.auth
        database = Firebase.database.reference

        //3. button k click par kya hoga
        binding.btnLogin.setOnClickListener {

            //get values from edit text intoo variables we have creted at first
            email = binding.edtEmail.text.toString().trim()
            password = binding.edtPassword.text.toString().trim()

            //check if edit text is empty and if empty then show toast message
            if (email.isBlank() || password.isBlank()) {
                showToast("Please fill all details!")
            } else {
                login(email, password)
            }

        }

        binding.tvSignupLink.setOnClickListener {
            val intent = Intent(this, SigninActivity::class.java)
            startActivity(intent)
            binding.tvSignupLink.setTextColor(Color.parseColor("#000000"))
            finish()
            binding.tvSignupLink.setTextColor(Color.parseColor("#1B14ED"))
        }
    }

    private fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user: FirebaseUser? = auth.currentUser
                showToast("Login Successful!")
                updateUI(user)
            } else {
                updateUIFailure()
                showToast("Wrong Credentials... Authentication Failed!")
                Log.d("Authentication", "loginAccount: Authentication Failed", task.exception)
            }
        }
    }


    //Launcherfor google signin
    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                if (task.isSuccessful) {
                    val account: GoogleSignInAccount? = task.result
                    val credential: AuthCredential =
                        GoogleAuthProvider.getCredential(account?.idToken, null)
                    auth.signInWithCredential(credential).addOnCompleteListener { authTask ->
                        if (authTask.isSuccessful) {
                            //successfully sigin with google
                            val user: FirebaseUser? = auth.currentUser
                            showToast("Google Sign-in Successful!")
                            updateUI(user)
                        } else {
                            showToast("Google Sign-in failed!")
                        }
                    }
                } else {
                    showToast("Google Sign-in failed!")
                }
            }
        }


    private fun updateUIFailure() {
        binding.edtEmail.text.clear()
        binding.edtPassword.text.clear()
    }

    //check if user is already logged in
    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            updateUI(currentUser)
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}