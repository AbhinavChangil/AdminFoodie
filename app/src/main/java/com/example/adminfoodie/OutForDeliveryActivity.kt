package com.example.adminfoodie

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminfoodie.adapter.DeliveryAdapter
import com.example.adminfoodie.databinding.ActivityOutForDeliveryBinding
import com.example.adminfoodie.model.OrderDetails
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class OutForDeliveryActivity : AppCompatActivity() {
    //enable binding
    private val binding : ActivityOutForDeliveryBinding by lazy {
        ActivityOutForDeliveryBinding.inflate(layoutInflater)
    }

    private lateinit var database: FirebaseDatabase
    private var listOfCompletedOrder: ArrayList<OrderDetails> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        //Back Button
        binding.btnBackOutForDelivery.setOnClickListener {
            finish()
        }

        //retrieve and display completed orders
        retrieveCompletedOrdersDetails()

//        //dummy data
//        val customerName = arrayListOf("Abhinav Changil", "Vipin Goyat", "Shubham Jangra", "Mayank Ahuja", "Mohammad Ali", "Aryan Bansal", "Rahul")
//        val paymentStatus = arrayListOf("Recieved","Not Recieved", "Recieved","Pending", "Not Recieved", "Recieved", "Pending")
//
//        //adapter


    }

    private fun retrieveCompletedOrdersDetails() {
        //Initialize Firebase Database
        database = FirebaseDatabase.getInstance()
        val completedOrrderReference = database.reference.child("CompletedOrders")
            .orderByChild("currentTime")
        completedOrrderReference.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                //clear the list before populating it withnew data
                listOfCompletedOrder.clear()

                for(completedOrderSnapshot in snapshot.children){

                    val completedOrder = completedOrderSnapshot.getValue(OrderDetails::class.java)
                    completedOrder?.let{
                        listOfCompletedOrder.add(it)
                    }
                }
                //reverse the list to display latest order first
                listOfCompletedOrder.reverse()

                //set data in recycler view
                setDataIntoRecyclerView()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private fun setDataIntoRecyclerView() {
        //initialization of list to hold customers name and payment status
        val customerNamesList = mutableListOf<String>()
        val moneyStatusList = mutableListOf<Boolean>()

        //ek loop lgakr recycler view ko set krwa lenge
        for(order in listOfCompletedOrder){
            order.userName?.let {
                customerNamesList.add(it)
            }

            moneyStatusList.add(order.paymentRecieved)
        }

        val adapter = DeliveryAdapter(customerNamesList, moneyStatusList)
        binding.rvOutForDelivery.adapter = adapter
        binding.rvOutForDelivery.layoutManager = LinearLayoutManager(this)
    }
}