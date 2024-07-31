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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileActivity : AppCompatActivity() {
    private val binding : ActivityProfileBinding by lazy{
        ActivityProfileBinding.inflate(layoutInflater)
    }

    //variables to set profile data from database
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var adminReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        //Back button
        binding.btnBackProfile.setOnClickListener {
            finish()
        }

//        we donot allow email to edit as login authentication data do not change
        binding.edtProfileEmail.isEnabled = false


        //Enable or Disable editing
        fun enableDisable(enableDisbale : Boolean){
            binding.edtProfileName.isEnabled = enableDisbale
            binding.edtProfileAddress.isEnabled = enableDisbale
//            binding.edtProfileEmail.isEnabled = enableDisbale
            binding.edtProfilePhone.isEnabled = enableDisbale
            binding.edtProfilePassword.isEnabled = enableDisbale
            binding.btnProfileSaveInfo.isEnabled = enableDisbale
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


        // Initialize auth and database and adminReference
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        adminReference = database.reference.child("vendor")


        retrieveUserData()


        binding.btnProfileSaveInfo.setOnClickListener {
            val updateName = binding.edtProfileName.text.toString()
            val updateAddress = binding.edtProfileAddress.text.toString()
            val updateEmail = binding.edtProfileEmail.text.toString()
            val updatePhone = binding.edtProfilePhone.text.toString()
            val updatePassword = binding.edtProfilePassword.text.toString()

            updateUserData(updateName, updateAddress, updateEmail, updatePhone, updatePassword)
            enableDisable(false)
        }


    }

    private fun retrieveUserData() {
        val adminId = auth.currentUser?.uid
        if(adminId != null){
            val adminProfileReference = adminReference.child(adminId).child("profile")

            adminProfileReference.addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        var ownerName = snapshot.child("name").getValue()
                        var ownerAddress = snapshot.child("address").getValue()
                        var ownerEmail = snapshot.child("email").getValue()
                        var ownerPhone = snapshot.child("phone").getValue()
                        var ownerPassword = snapshot.child("password").getValue()

                        //set Data to edit texts
                        setProfileData(ownerName, ownerAddress, ownerEmail, ownerPhone, ownerPassword)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    showToast("Admin Data Not Retrieved")
                }

            })
        }

    }

    private fun setProfileData(ownerName: Any?, ownerAddress: Any?, ownerEmail: Any?, ownerPhone: Any?, ownerPassword: Any?) {
        binding.edtProfileName.setText(ownerName.toString())
        binding.edtProfileAddress.setText(ownerAddress.toString())
        binding.edtProfileEmail.setText(ownerEmail.toString())
        binding.edtProfilePhone.setText(ownerPhone.toString())
        binding.edtProfilePassword.setText(ownerPassword.toString())
    }


    private fun updateUserData(updateName: String, updateAddress: String, updateEmail: String, updatePhone: String, updatePassword: String) {

        val adminId = auth.currentUser?.uid?:""
        if (adminId != null) {
            val userReference = database.getReference("vendor").child(adminId).child("profile")

            val userData = hashMapOf(
                "name" to updateName,
                "address" to updateAddress,
                "email" to updateEmail,
                "phone" to updatePhone,
                "password" to updatePassword
            )
            userReference.setValue(userData).addOnSuccessListener {
                showToast("Profile Updated Successfully!")
                auth.currentUser?.updateEmail(updateEmail)
                auth.currentUser?.updatePassword(updatePassword)
            }
                .addOnFailureListener {
                    showToast("Profile Update Failed!")
                }
        }
    }


    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

