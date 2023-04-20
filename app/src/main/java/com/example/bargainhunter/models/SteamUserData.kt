package com.example.bargainhunter.models

import com.google.gson.annotations.SerializedName

data class SteamUserData(

    @SerializedName("personaname") val personaname : String,
    @SerializedName("avatarfull") val avatarfull : String,
    @SerializedName("player_level") val player_level : Int,
    @SerializedName("years_of_service") val years_of_service : Int,
    @SerializedName("wishlist") val wishlist : List<Int>,
    var apps:MutableList<App>
)
