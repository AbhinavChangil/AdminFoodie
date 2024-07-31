package com.example.adminfoodie

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.adminfoodie.databinding.ActivityMainBinding
import com.example.adminfoodie.model.OrderDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    private val binding : ActivityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var completedOrderReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.cardViewAddMenu.setOnClickListener {
            val intent = Intent(this, AddItemActivity::class.java)
            startActivity(intent)
        }

        binding.cardViewAllItemMenu.setOnClickListener {
            val intent = Intent(this, AllItemActivity::class.java)
            startActivity(intent)
        }

        binding.cardViewOrderDispatch.setOnClickListener {
            val intent = Intent(this, OutForDeliveryActivity::class.java)
            startActivity(intent)
        }

        binding.cardViewProfile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        binding.cardViewCreateNewUser.setOnClickListener {
            val intent = Intent(this, SigninActivity::class.java)
            startActivity(intent)
        }
        auth = FirebaseAuth.getInstance()

        binding.cardViewLogOut.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }

        binding.tvPendingOrder.setOnClickListener {
            val intent = Intent(this, PendingOrdersActivity::class.java)
            startActivity(intent)
        }



        //update count of pending orders
        updatePendingOrdersCount()

        //update count of completed order
        updateCompletedOrdersCount()

        //update all time earning
        updateAllTimeEarning()

    }

    private fun updateAllTimeEarning() {
        var listOfTotalAmount = mutableListOf<Int>()
        database = FirebaseDatabase.getInstance()
        completedOrderReference = database.reference.child("CompletedOrders")
        completedOrderReference.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(orderSnapshot in snapshot.children){
                    var completedOrder = orderSnapshot.getValue(OrderDetails::class.java)
                    completedOrder?.totalPrice?.replace("₹","")?.toInt()
                        ?.let {
                            i ->
                            listOfTotalAmount.add(i)
                        }
                }
                binding.tvEarningAmount.text = "₹" + listOfTotalAmount.sum().toString()
            }

            override fun onCancelled(error: DatabaseError) {
                showToast("Earnings Not Updated")
            }

        })
    }

    private fun updateCompletedOrdersCount() {
            database = FirebaseDatabase.getInstance()
            completedOrderReference = database.reference.child("CompletedOrders")
            var completedOrderItemCount = 0
            completedOrderReference.addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    completedOrderItemCount = snapshot.childrenCount.toInt()
                    binding.tvCompletedOrderCount.text = completedOrderItemCount.toString()
                }

                override fun onCancelled(error: DatabaseError) {
                    showToast("Completed Orders Count Not Updated")
                }

            })
    }

    private fun updatePendingOrdersCount() {
        database = FirebaseDatabase.getInstance()
        val pendingOrderReference = database.reference.child("OrderDetails")
        var pendingOrderItemCount = 0
        pendingOrderReference.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                pendingOrderItemCount = snapshot.childrenCount.toInt()
                binding.tvPendingOrderCount.text = pendingOrderItemCount.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                showToast("Pending Orders Count Not Updated")
            }

        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


}