package com.example.cryptoapp.Response

data class PagingParameterResponseModel(
    val currentPage: Int,
    val nextPage: String,
    val pageSize: Int,
    val previousPage: String,
    val totalCount: Int,
    val totalPages: Int
)