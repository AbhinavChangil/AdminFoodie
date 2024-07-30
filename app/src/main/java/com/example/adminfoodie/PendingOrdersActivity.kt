package com.example.adminfoodie

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminfoodie.adapter.PendingOrderAdapter
import com.example.adminfoodie.databinding.ActivityPendingOrdersBinding
import com.example.adminfoodie.model.OrderDetails
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PendingOrdersActivity : AppCompatActivity(), PendingOrderAdapter.OnItemClicked {
    //enable binding
    private val binding : ActivityPendingOrdersBinding by lazy{
        ActivityPendingOrdersBinding.inflate(layoutInflater)
    }

    //sare order details list me aayenge kyuki multiple orders honge
    private var listOfUserNames: MutableList<String> = mutableListOf()
    private var listOfTotalPrices: MutableList<String> = mutableListOf()
    private var listOfImageFirstFoodOrders: MutableList<String> = mutableListOf()
    private var listOfOrderItems: ArrayList<OrderDetails> = arrayListOf()

    //database
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseOrderDetails: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        //initialize database
        database = FirebaseDatabase.getInstance()
        //initialize databaseReference
        databaseOrderDetails = database.reference.child("OrderDetails")

        //back button
        binding.btnBackPendingOrders.setOnClickListener {
            finish()
        }

        getOrdersDetails()



//        //adapter
//        val adapter = PendingOrderAdapter(foodImage, customerName, quantity, this)
//        binding.rvPendingOrders.adapter = adapter
//        binding.rvPendingOrders.layoutManager = LinearLayoutManager(this)

    }

    private fun getOrdersDetails() {
        //retrieve orderDetails from firebase database
        databaseOrderDetails.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(orderSnapshot in snapshot.children){
                    //ek variable bnaenge jisme sab store kra lemge
                    var orderDetails = orderSnapshot.getValue(OrderDetails::class.java)

                    orderDetails?.let {
                        listOfOrderItems.add(it)
                    }
                }
                addDataToListForRecyclerView()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun addDataToListForRecyclerView() {
        for(orderItem in listOfOrderItems){

            //add data to respective list for populating the recycler view
            orderItem.userName?.let { listOfUserNames.add(it) }
            orderItem.totalPrice?.let { listOfTotalPrices.add(it) }
            orderItem.foodImagesOrderDetails?.filterNot { it.isEmpty() }?.forEach{
                listOfImageFirstFoodOrders.add(it)
            }
        }

        //now after getting all details set data in adpater
        setAdapter()
    }

    private fun setAdapter() {
        binding.rvPendingOrders.layoutManager = LinearLayoutManager(this)
        val adapter = PendingOrderAdapter(this,listOfUserNames,listOfImageFirstFoodOrders,listOfTotalPrices,this)
        binding.rvPendingOrders.adapter = adapter
    }

    override fun OnItemClickListener(position: Int) {
        val intent = Intent(this, OrderDetailsActivity::class.java)
        val userOrderDetails = listOfOrderItems[position]
        intent.putExtra("UserOrderDetails", userOrderDetails)
        startActivity(intent)
    }

    override fun OnItemAcceptClickListener(position: Int) {
        TODO("Not yet implemented")
    }

    override fun OnItemDispatchClickListener(position: Int) {
        TODO("Not yet implemented")
    }

}




////dummy data
//val foodImage = arrayListOf(R.drawable.jalebi, R.drawable.rasmalai, R.drawable.menu1, R.drawable.menu2, R.drawable.menu3, R.drawable.menu4, R.drawable.menu5)
//val customerName = arrayListOf("Abhinav Changil", "Vipin Goyat", "Shubham Jangra", "Mayank Ahuja", "Mohammad Ali", "Aryan Bansal", "Rahul")
//val quantity = arrayListOf("2","4", "9","6", "1", "2", "5")