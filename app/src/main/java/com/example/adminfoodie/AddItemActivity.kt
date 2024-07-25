package com.example.adminfoodie

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.adminfoodie.databinding.ActivityAddItemBinding

class AddItemActivity : AppCompatActivity() {
    private val binding : ActivityAddItemBinding by lazy {
        ActivityAddItemBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

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

    //to add image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101 && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                binding.imgAddItem.setImageURI(uri)
            }
        }
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
