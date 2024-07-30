package com.example.adminfoodie

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.adminfoodie.databinding.ActivityProfileBinding
import com.example.adminfoodie.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileActivity : AppCompatActivity() {
    private val binding : ActivityProfileBinding by lazy{
        ActivityProfileBinding.inflate(layoutInflater)
    }

    //variables to set profile data from database
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        //Back button
        binding.btnBackProfile.setOnClickListener {
            finish()
        }

        //we donot allow email to edit as login authentication data do not change
        binding.edtProfileEmail.isEnabled = false


        //Enable or Disable editing
        fun enableDisable(enableDisbale : Boolean){
            binding.edtProfileName.isEnabled = enableDisbale
            binding.edtProfileAddress.isEnabled = enableDisbale
//            binding.edtProfileEmail.isEnabled = enableDisbale
            binding.edtProfilePhone.isEnabled = enableDisbale
            binding.edtProfilePassword.isEnabled = enableDisbale
        }

        // by deafault disable editing
        enableDisable(false)

        //Enable editing on edit profile clicked
        binding.editProfileLink.setOnClickListener {
            enableDisable(true)
            //when edit profile clicked focus on name edit text
            binding.edtProfileName.requestFocus()
        }


        //Disable editing on save info button clicked
        binding.btnProfileSaveInfo.setOnClickListener {
            enableDisable(false)
        }


        // Initialize auth and database
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()


        setUserData()

        binding.btnProfileSaveInfo.setOnClickListener {
            val name = binding.edtProfileName.text.toString()
            val address = binding.edtProfileAddress.text.toString()
            val email = binding.edtProfileEmail.text.toString()
            val phone = binding.edtProfilePhone.text.toString()
            val password = binding.edtProfilePassword.text.toString()

            updateUserData(name, address, email, phone, password)
            enableDisable(false)
        }


    }


    private fun updateUserData(name: String, address: String, email: String, phone: String, password: String) {
        val userId = auth.currentUser?.uid?:""
        if (userId != null) {
            val userReference = database.getReference("vendor").child(userId).child("profile")

            val userData = hashMapOf(
                "name" to name,
                "address" to address,
                "email" to email,
                "phone" to phone,
                "password" to password
            )
            userReference.setValue(userData).addOnSuccessListener {
                showToast("Profile Updated Successfully!")
            }
                .addOnFailureListener {
                    showToast("Profile Update Failed!")
                }
        }
    }

    private fun setUserData() {
        val user = auth.currentUser
        if (user != null) {
            val userId = auth.currentUser?.uid?:""
            val userReference = database.getReference("vendor").child(userId).child("profile")

            // Set all values
            userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val userProfile = snapshot.getValue(UserModel::class.java)
                        if (userProfile != null) {
                            binding.apply {
                                edtProfileName.setText(userProfile.name)
                                edtProfileAddress.setText(userProfile.address)
                                edtProfileEmail.setText(userProfile.email)
                                edtProfilePhone.setText(userProfile.phone)
                                edtProfilePassword.setText(userProfile.password)
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    showToast("Failed to load user data")
                }
            })
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}