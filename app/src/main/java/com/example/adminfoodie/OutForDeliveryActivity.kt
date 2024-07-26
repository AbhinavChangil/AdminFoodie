package com.example.adminfoodie

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminfoodie.adapter.DeliveryAdapter
import com.example.adminfoodie.databinding.ActivityOutForDeliveryBinding

class OutForDeliveryActivity : AppCompatActivity() {
    //enable binding
    private val binding : ActivityOutForDeliveryBinding by lazy {
        ActivityOutForDeliveryBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        //Back Button
        binding.btnBackOutForDelivery.setOnClickListener {
            finish()
        }

        //dummy data
        val customerName = arrayListOf("Abhinav Changil", "Vipin Goyat", "Shubham Jangra", "Mayank Ahuja", "Mohammad Ali", "Aryan Bansal", "Rahul")
        val paymentStatus = arrayListOf("Recieved","Not Recieved", "Recieved","Pending", "Not Recieved", "Recieved", "Pending")

        //adapter
        val adapter = DeliveryAdapter(customerName, paymentStatus)
        binding.rvOutForDelivery.adapter = adapter
        binding.rvOutForDelivery.layoutManager = LinearLayoutManager(this)

    }
}