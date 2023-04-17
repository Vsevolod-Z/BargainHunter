package com.example.bargainhunter
import com.google.gson.annotations.SerializedName
data class PaginationState( var isLoading: Boolean = false,
                            var currentPage: Int = 0,
                            var totalPages: Int = 0) {
}