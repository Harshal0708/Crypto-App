package com.example.cryptoapp.Response

data class OtpResendResponse(
    val code : String,
    val message : String,
    val data : OtpResendUserDataResponse,
)

