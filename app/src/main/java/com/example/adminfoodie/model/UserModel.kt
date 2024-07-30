package com.example.adminfoodie.model

//yha hume vo variables create karne hn jisme hmare user ka data save hoga
data class UserModel(
    val name:String? = null,
    val restaurantName:String? = null,
    val email:String? = null,
    val password:String? = null,
    val address:String? =null,
    val phone:String? = null
)
