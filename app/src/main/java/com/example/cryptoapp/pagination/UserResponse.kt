package com.example.cryptoapp.pagination

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UserResponse(

    @SerializedName("data")
    val listUsers : ArrayList<User>

) : Serializable