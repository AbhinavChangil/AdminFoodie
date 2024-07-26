package com.example.adminfoodie

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminfoodie.adapter.DeliveryAdapter
import com.example.adminfoodie.adapter.PendingOrderAdapter
import com.example.adminfoodie.databinding.ActivityPendingOrdersBinding

class PendingOrdersActivity : AppCompatActivity() {
    //enable binding
    private val binding : ActivityPendingOrdersBinding by lazy{
        ActivityPendingOrdersBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        //back button
        binding.btnBackPendingOrders.setOnClickListener {
            finish()
        }

        //dummy data
        val foodImage = arrayListOf(R.drawable.jalebi, R.drawable.rasmalai, R.drawable.menu1, R.drawable.menu2, R.drawable.menu3, R.drawable.menu4, R.drawable.menu5)
        val customerName = arrayListOf("Abhinav Changil", "Vipin Goyat", "Shubham Jangra", "Mayank Ahuja", "Mohammad Ali", "Aryan Bansal", "Rahul")
        val quantity = arrayListOf("2","4", "9","6", "1", "2", "5")

        //adapter
        val adapter = PendingOrderAdapter(foodImage, customerName, quantity, this)
        binding.rvPendingOrders.adapter = adapter
        binding.rvPendingOrders.layoutManager = LinearLayoutManager(this)

    }
}