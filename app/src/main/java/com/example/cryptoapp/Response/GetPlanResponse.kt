package com.example.cryptoapp.Response

data class GetPlanResponse(
    val code: Int,
    val `data`: List<DataXXXXXX>,
    val isSuccess: Boolean,
    val message: Any
)