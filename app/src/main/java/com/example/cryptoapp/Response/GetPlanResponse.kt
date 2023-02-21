package com.example.cryptoapp.Response

data class GetPlanResponse(
    val code: Int,
    val `data`: List<GetPlanDataResponse>,
    val isSuccess: Boolean,
    val message: Any
)