package com.example.cryptoapp.Response

data class Data(
    val orderHistories: List<OrderHistory>,
    val pagingParameterResponseModel: PagingParameterResponseModel
)