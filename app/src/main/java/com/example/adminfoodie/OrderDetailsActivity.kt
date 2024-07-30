package com.example.adminfoodie

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminfoodie.adapter.OrderDetailsAdapter
import com.example.adminfoodie.databinding.ActivityOrderDetailsBinding
import com.example.adminfoodie.model.OrderDetails
import com.example.adminfoodie.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class OrderDetailsActivity : AppCompatActivity() {
    //enable binding
    private val binding: ActivityOrderDetailsBinding by lazy {
        ActivityOrderDetailsBinding.inflate(layoutInflater)
    }

//    //variables to set profile data from database
//    private lateinit var auth: FirebaseAuth
//    private lateinit var database: FirebaseDatabase

    private var username: String? = null
    private var userAddress: String? = null
    private var phone: String? = null
    private var totalPrice: String? = null

    private var foodNamesList: ArrayList<String> = arrayListOf()
    private var foodImagesList: ArrayList<String> = arrayListOf()
    private var foodPricesList: ArrayList<String> = arrayListOf()
    private var foodQuantitiesList: ArrayList<Int> = arrayListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.btnBackOrderDetails.setOnClickListener {
            finish()
        }

//        // Initialize auth and database
//        auth = FirebaseAuth.getInstance()
//        database = FirebaseDatabase.getInstance()


        getDataFromIntent()

    }

    private fun getDataFromIntent() {
        val receivedOrderDetails = intent.getSerializableExtra("UserOrderDetails") as OrderDetails
        receivedOrderDetails?.let { orderDetails ->
                username = receivedOrderDetails.userName
                foodNamesList = receivedOrderDetails.foodNamesOrderDetails as ArrayList<String>
                foodImagesList = receivedOrderDetails.foodImagesOrderDetails as ArrayList<String>
                foodPricesList = receivedOrderDetails.foodPricesOrderDetails as ArrayList<String>
                foodQuantitiesList = receivedOrderDetails.foodQuantitiesOrderDetails as ArrayList<Int>
                phone = receivedOrderDetails.phoneNumber
                userAddress = receivedOrderDetails.addess
                totalPrice = receivedOrderDetails.totalPrice

                // set user details
                setUserDetails()

                //set adapter
                setAdapter()

        }

    }

    private fun setAdapter() {
        binding.rvOrderDetails.layoutManager = LinearLayoutManager(this)
        val adapter = OrderDetailsAdapter(this, foodNamesList,foodPricesList,foodImagesList,foodQuantitiesList)
        binding.rvOrderDetails.adapter = adapter
    }

    private fun setUserDetails() {
        binding.edtNameOrderDetails.text = username
        binding.edtAddressOrderDetails.text = userAddress
        binding.edtPhoneOrderDetails.text = phone
        binding.edtTotalAmountOrderDetails.text = totalPrice
    }


    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}