package com.example.adminfoodie

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminfoodie.adapter.AllItemAdapter
import com.example.adminfoodie.databinding.ActivityAllItemBinding

class AllItemActivity : AppCompatActivity() {
    private val binding : ActivityAllItemBinding by lazy{
        ActivityAllItemBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        //Back button
        binding.btnBackAllItem.setOnClickListener {
            finish()
        }

        //ab hum bnaenge dummy data
        val allitemFoodNames = listOf("Ras Malai", "Jalebi", "Laddu", "Gulab Jamun", "Fruit Salad", "Burger", "Herbal Pancake", "Green Noodles")
        val allitemFoodPrices = listOf("Rs. 50", "Rs. 70", "Rs. 250", "Rs. 30", "Rs. 100", "Rs. 70", "Rs. 110", "Rs. 150")
        val allitemFoodImages = listOf(
            R.drawable.rasmalai,
            R.drawable.jalebi,
            R.drawable.laddu,
            R.drawable.gulabjamun,
            R.drawable.menu5,
            R.drawable.menu4,
            R.drawable.menu1,
            R.drawable.menu2
        )

        val adapter = AllItemAdapter(
            ArrayList(allitemFoodNames),
            ArrayList(allitemFoodPrices),
            ArrayList(allitemFoodImages)
        )
        binding.rvAllitem.layoutManager = LinearLayoutManager(this)
        binding.rvAllitem.adapter = adapter


    }
}