package com.example.adminfoodie

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.adminfoodie.databinding.ActivityCreateNewUserBinding

class CreateNewUserActivity : AppCompatActivity() {
    private val binding : ActivityCreateNewUserBinding by lazy{
        ActivityCreateNewUserBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        //Back Button
        binding.btnBackCreateNewUser.setOnClickListener {
            finish()
        }


    }
}