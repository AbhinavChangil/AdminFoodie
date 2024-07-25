package com.example.adminfoodie

import android.R
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.adminfoodie.databinding.ActivitySigninBinding

class SigninActivity : AppCompatActivity() {
    private val binding : ActivitySigninBinding by lazy{
        ActivitySigninBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        val locationList:Array<String> = arrayOf("Rohtak", "Bhiwani", "Hisar", "Ambala", "Panchkula")
        val adapter = ArrayAdapter(this, R.layout.simple_list_item_1, locationList)
        val autoCompleteTextView = binding.listOfLocations
        autoCompleteTextView.setAdapter(adapter)

        binding.btnSignin.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.tvLoginLink.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            binding.tvLoginLink.setTextColor(Color.parseColor("#000000"))
            finish()
            binding.tvLoginLink.setTextColor(Color.parseColor("#1B14ED"))
        }

    }
}