package com.example.cryptoapp.Response

data class UserSubscriptionsDataResponse(
    val pagingParameterResponseModel: PagingParameterResponseModelX,
    val userSubscriptions: List<UserSubscription>
)