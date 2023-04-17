package com.example.bargainhunter.models

import com.google.gson.annotations.SerializedName

/*
Copyright (c) 2023 Kotlin Data Classes Generated from JSON powered by http://www.json2kotlin.com

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

For support, please feel free to contact me at https://www.linkedin.com/in/syedabsar */


data class Review (

    @SerializedName("recommendationid") val recommendationid : String,
    @SerializedName("author") val author : Author,
    @SerializedName("language") val language : String,
    @SerializedName("review") val review : String,
    @SerializedName("timestamp_created") val timestamp_created : Int,
    @SerializedName("timestamp_updated") val timestamp_updated : Int,
    @SerializedName("voted_up") val voted_up : Boolean,
    @SerializedName("votes_up") val votes_up : Int,
    @SerializedName("votes_funny") val votes_funny : Int,
    @SerializedName("weighted_vote_score") val weighted_vote_score : String,
    @SerializedName("comment_count") val comment_count : Int,
    @SerializedName("steam_purchase") val steam_purchase : Boolean,
    @SerializedName("received_for_free") val received_for_free : Boolean,
    @SerializedName("written_during_early_access") val written_during_early_access : Boolean,
    @SerializedName("hidden_in_steam_china") val hidden_in_steam_china : Boolean,
    @SerializedName("steam_china_location") val steam_china_location : String
)