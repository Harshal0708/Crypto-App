package com.example.cryptoapp.modual.subscription

data class SubscriptionHistoryModel
    (
    var userId:Int,
    var subId:Int,
    var startDate : String,
    var endDate :String,
    var name :String,
    var price :Int,
    var status :String,
    )
{

}