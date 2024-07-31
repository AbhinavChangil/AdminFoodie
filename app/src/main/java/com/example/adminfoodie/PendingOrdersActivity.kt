package com.example.adminfoodie

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminfoodie.adapter.PendingOrderAdapter
import com.example.adminfoodie.databinding.ActivityPendingOrdersBinding
import com.example.adminfoodie.model.OrderDetails
import com.google.firebase.database.*

class PendingOrdersActivity : AppCompatActivity(), PendingOrderAdapter.OnItemClicked {
    private val binding: ActivityPendingOrdersBinding by lazy {
        ActivityPendingOrdersBinding.inflate(layoutInflater)
    }

    private var listOfOrderItems: MutableList<OrderDetails> = mutableListOf()
    private lateinit var adapter: PendingOrderAdapter

    private lateinit var database: FirebaseDatabase
    private lateinit var databaseOrderDetails: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance()
        databaseOrderDetails = database.reference.child("OrderDetails")

        binding.btnBackPendingOrders.setOnClickListener {
            finish()
        }

        setAdapter()
        getOrdersDetails()
    }

    private fun getOrdersDetails() {
        databaseOrderDetails.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listOfOrderItems.clear() // Clear the list before adding new items
                for (orderSnapshot in snapshot.children) {
                    val orderDetails = orderSnapshot.getValue(OrderDetails::class.java)
                    orderDetails?.let {
                        listOfOrderItems.add(it)
                    }
                }
                adapter.notifyDataSetChanged() // Notify adapter after updating the list
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    private fun setAdapter() {
        binding.rvPendingOrders.layoutManager = LinearLayoutManager(this)
        adapter = PendingOrderAdapter(
            this,
            listOfOrderItems,
            this
        )
        binding.rvPendingOrders.adapter = adapter
    }

    override fun OnItemClickListener(position: Int) {
        val intent = Intent(this, OrderDetailsActivity::class.java)
        val userOrderDetails = listOfOrderItems[position]
        intent.putExtra("UserOrderDetails", userOrderDetails)
        startActivity(intent)
    }

    override fun OnItemAcceptClickListener(position: Int) {
        val orderDetails = listOfOrderItems[position]
        val childItemPushKey = orderDetails.itemPushKey
        val clickItemOrderReference = childItemPushKey?.let {
            database.reference.child("OrderDetails").child(it)
        }

        clickItemOrderReference?.child("orderAccepted")?.setValue(true)
        orderDetails.orderAccepted = true // Update the order status locally
        adapter.notifyItemChanged(position) // Notify adapter to refresh the item
        updateOrderAcceptStatus(position)
    }

    override fun OnItemDispatchClickListener(position: Int) {
        val orderDetails = listOfOrderItems[position]
        val dispatchItemPushKey = orderDetails.itemPushKey
        val dispatchItemOrderReference = database.reference.child("CompletedOrders").child(dispatchItemPushKey!!)
        dispatchItemOrderReference.setValue(orderDetails)
            .addOnSuccessListener {
                deleteThisItemFromOrderDetails(dispatchItemPushKey)
                // Remove item from the local list and notify adapter
                listOfOrderItems.removeAt(position)
                adapter.notifyItemRemoved(position)
            }
            .addOnFailureListener {
                showToast("Failed to dispatch order")
            }
    }

    private fun deleteThisItemFromOrderDetails(dispatchItemPushKey: String) {
        val orderDetailsItemsReference = database.reference.child("OrderDetails").child(dispatchItemPushKey)
        orderDetailsItemsReference.removeValue()
            .addOnSuccessListener {
                showToast("Order is Dispatched!")
            }
            .addOnFailureListener {
                showToast("Order Not Dispatched!")
            }
    }

    private fun updateOrderAcceptStatus(position: Int) {
        val userIdOfClickedItem = listOfOrderItems[position].userUid
        val pushKeyOfClickedItem = listOfOrderItems[position].itemPushKey
        val orderHistoryReference = database.reference.child("user").child(userIdOfClickedItem!!).child("OrderHistory").child(pushKeyOfClickedItem!!)
        orderHistoryReference.child("orderAccepted").setValue(true)

        databaseOrderDetails.child(pushKeyOfClickedItem).child("orderAccepted").setValue(true)
        showToast("Order is Accepted!")
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
