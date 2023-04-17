package com.example.bargainhunter.models/*
Copyright (c) 2023 Kotlin Data Classes Generated from JSON powered by http://www.json2kotlin.com

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

For support, please feel free to contact me at https://www.linkedin.com/in/syedabsar */

import com.google.gson.annotations.SerializedName
data class SteamAppData (

    @SerializedName("type") val type : String,
    @SerializedName("name") val name : String,
    @SerializedName("steam_appid") val steam_appid : Int,
    @SerializedName("url") val url : String,
    @SerializedName("dlc") val dlc : List<Int>,
    @SerializedName("header_image") val header_image : String,
    @SerializedName("pc_requirements") val pc_requirements : Pc_requirements,
    @SerializedName("developers") val developers : List<String>,
    @SerializedName("publishers") val publishers : List<String>,
    @SerializedName("price_overview") val price_overview : Price_overview,
    @SerializedName("metacritic") val metacritic : Metacritic,
    @SerializedName("platforms") val platforms : Platforms,
    @SerializedName("categories") val categories : List<Categories>,
    @SerializedName("genres") val genres : List<Genres>,
    @SerializedName("screenshots") val screenshots : List<Screenshots>,
    @SerializedName("movies") val movies : List<Movies>,
    @SerializedName("achievements") val achievements : Achievements,
    @SerializedName("release_date") val release_date : Release_date,
    @SerializedName("app_review") val app_review : AppReview
)