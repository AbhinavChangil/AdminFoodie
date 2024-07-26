package com.example.adminfoodie

import android.R
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.adminfoodie.databinding.ActivitySigninBinding
import com.example.adminfoodie.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class SigninActivity : AppCompatActivity() {

    // variables to be used for email password authentication
    private lateinit var auth: FirebaseAuth
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var userName: String
    private lateinit var nameOfRestaurant: String
    private lateinit var database: DatabaseReference

    private val binding: ActivitySigninBinding by lazy {
        ActivitySigninBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        //initialize karenge apne authentication or database ko
        auth = Firebase.auth
        database = Firebase.database.reference

        //dummy data
        val locationList: Array<String> =
            arrayOf("Rohtak", "Bhiwani", "Hisar", "Ambala", "Panchkula")
        val adapter = ArrayAdapter(this, R.layout.simple_list_item_1, locationList)
        val autoCompleteTextView = binding.listOfLocations
        autoCompleteTextView.setAdapter(adapter)

        binding.btnSignin.setOnClickListener {

            //find all ids from layout and map to corresponding variables
            // get values from edit texts
            email = binding.edtEmail2.text.toString().trim()
            password = binding.edtPassword2.text.toString().trim()
            userName = binding.edtName.text.toString().trim()
            nameOfRestaurant = binding.edtRestaurantName.text.toString().trim()

            //if any edit text is empty then ask user to enter data
            if(userName.isBlank() || nameOfRestaurant.isBlank() || email.isBlank() || password.isBlank()){
                showToast("Please fill all details")
            }else{
                createAccount(email, password)
            }
        }

        binding.tvLoginLink.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            binding.tvLoginLink.setTextColor(Color.parseColor("#000000"))
            finish()
            binding.tvLoginLink.setTextColor(Color.parseColor("#1B14ED"))
        }

    }

    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
            //ye complete hone pr hum ek lambda function bnaenge (task k naam se) jo check krega ki kaam hua h ya nhi
            task ->
            if(task.isSuccessful){
                //show toast message
                showToast("Account Created Successfully!")

                //save user data in relatime database
                saveUserData()

                //if account is created then move to main activity
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            else{
                showToast("Account Creation Failed!")
                Log.d("Account","createAccount: Failure", task.exception)
            }
        }
    }


    //save data into database
    private fun saveUserData() {
        // get values from edit texts
        email = binding.edtEmail2.text.toString().trim()
        password = binding.edtPassword2.text.toString().trim()
        userName = binding.edtName.text.toString().trim()
        nameOfRestaurant = binding.edtRestaurantName.text.toString().trim()

        //ekk varibale bnaenge user node k liye or ek bnaenge model using data class (jha par variable honge jisme data save karenge)
        val user = UserModel(userName,nameOfRestaurant,email,password)
        //ek user k liye unique id generate ho jaegi
        val userId : String = FirebaseAuth.getInstance().currentUser!!.uid
        //node jo bnaenge vo phle child me dalenge fir userId or node data wala variable
        //save user data firebase database
        database.child("user").child(userId).setValue(user)
    }

    private fun showToast(message:String){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }
}