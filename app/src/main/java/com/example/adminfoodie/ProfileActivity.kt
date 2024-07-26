package com.example.adminfoodie

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.adminfoodie.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {
    private val binding : ActivityProfileBinding by lazy{
        ActivityProfileBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        //Back button
        binding.btnBackProfile.setOnClickListener {
            finish()
        }


        //Enable or Disable editing
        fun enableDisable(enableDisbale : Boolean){
            binding.edtProfileName.isEnabled = enableDisbale
            binding.edtProfileAddress.isEnabled = enableDisbale
            binding.edtProfileEmail.isEnabled = enableDisbale
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


    }
}