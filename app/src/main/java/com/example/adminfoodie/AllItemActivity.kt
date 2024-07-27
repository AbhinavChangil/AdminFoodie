package com.example.adminfoodie

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminfoodie.adapter.AllItemAdapter
import com.example.adminfoodie.databinding.ActivityAllItemBinding
import com.example.adminfoodie.model.AllMenu
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AllItemActivity : AppCompatActivity() {

    //varibales for loading data from firebase databse
    private lateinit var databaseReference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private var menuItems: ArrayList<AllMenu> = ArrayList()


    private val binding: ActivityAllItemBinding by lazy {
        ActivityAllItemBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        //initialize database reference and database
        databaseReference = FirebaseDatabase.getInstance().reference
        retrieveMenuItem()

        //Back button
        binding.btnBackAllItem.setOnClickListener {
            finish()
        }


    }

    private fun retrieveMenuItem() {
        database = FirebaseDatabase.getInstance()
        val foodRef: DatabaseReference = database.reference.child("menu")

        //fetch data from database
        foodRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //clear existing data before populating
                menuItems.clear()

                //loop for traverse each food item
                for (foodSnapshot: DataSnapshot in snapshot.children) {
                    val menuItem: AllMenu? = foodSnapshot.getValue(AllMenu::class.java)
                    menuItem?.let {
                        menuItems.add(it)
                    }
                }
                setAdapter()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Database", "Error: ${error.message}")
            }
        })

    }

    private fun setAdapter(){
        val adapter = AllItemAdapter(this,menuItems,databaseReference)
        binding.rvAllitem.layoutManager = LinearLayoutManager(this)
        binding.rvAllitem.adapter = adapter


    }
}