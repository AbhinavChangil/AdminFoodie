package com.example.adminfoodie

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.adminfoodie.databinding.ActivityAddItemBinding
import com.example.adminfoodie.model.AllMenu
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class AddItemActivity : AppCompatActivity() {

    //Variables for food item details
    private lateinit var foodName : String
    private lateinit var foodPrice : String
    private  var foodImageUri : Uri? = null
    private lateinit var foodDescription : String
    private lateinit var foodIngredients : String

    //variables for firebase and database
    private lateinit var auth : FirebaseAuth
    private lateinit var database : FirebaseDatabase

    private val binding : ActivityAddItemBinding by lazy {
        ActivityAddItemBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        //initialize firebase and database
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()


        binding.btnAddItem.setOnClickListener {

            //get values from edit texts
            foodName = binding.edtAddItemName.text.toString().trim()
            foodPrice = binding.edtAddItemPrice.text.toString().trim()
            foodDescription = binding.edtShortDes.text.toString().trim()
            foodIngredients = binding.edtIngredientsAddItem.text.toString().trim()

            if(!(foodName.isBlank() || foodPrice.isBlank() || foodDescription.isBlank() || foodIngredients.isBlank())){
                uploadData()
            }
            else{
                showToast("Please fill all details!")
            }
        }


        //Adding image from gallery
        binding.tvAddItemImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 101)
        }

        //back buttom intent
        binding.btnBackAddItem.setOnClickListener {
            finish()
        }

    }

    private fun uploadData() {

        //get reference(node in database) to menu from model data class
        val menuRef : DatabaseReference = database.getReference("menu")
        //Generate unique id for new menu
        val newItemKey : String? = menuRef.push().key


        if(foodImageUri != null){
            binding.btnAddItem.isEnabled = false
            var storageRef = FirebaseStorage.getInstance().reference
            val imageRef = storageRef.child("menu_images/${newItemKey}.jpg")
            val uploadTask = imageRef.putFile(foodImageUri!!)

            uploadTask.addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener {
                    downloadUrl->
                    //check downlaod hua ya nahi
                    //create new menu item
                    val newItem = AllMenu(
                        foodName = foodName,
                        foodPrice = foodPrice,
                        foodImage = downloadUrl.toString(),
                        foodDes = foodDescription,
                        foodIngredients = foodIngredients
                        )
                    // for all make new unique key
                    newItemKey?.let {
                        key->
                        menuRef.child(key).setValue(newItem).addOnSuccessListener {
                            showToast("Item Added Successfully!")
                            finish()
                        }
                            .addOnFailureListener {
                                showToast("Item Adding Failed!")
                                finish()
                            }
                    }
                }

            }
                .addOnFailureListener {
                    //agar image upload na ho to
                    showToast("Image Upload Failed")
                }

        }else{
                //agar image selected na ho to
                showToast("Please select an image!")
        }
    }

    //to add image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101 && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                binding.imgAddItem.setImageURI(uri)
                foodImageUri = uri
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}











/* To Add Image from Gallery

//Add this in onCreate

    binding.tvAddItemImage.setOnClickListener {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 101)
}

//Add this after onCreate (override)

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (requestCode == 101 && resultCode == Activity.RESULT_OK) {
        data?.data?.let { uri ->
            binding.imgAddItem.setImageURI(uri)
        }
    }
}


 */
 */



/* To Add Image from google drive and phone

//Add this in onCreate

    binding.tvAddItemImage.setOnClickListener {
            pickImage.launch("image/*")     // Adding the Selected Image
        }

//Add this after onCreate (override)

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()){
            uri-> if(uri !=null){
        binding.imgAddItem.setImageURI(uri)
    }
    }


 */
 */
