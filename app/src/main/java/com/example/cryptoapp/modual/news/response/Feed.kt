package com.example.cryptoapp.modual.news.response

data class Feed(
    val description: String,
    val feed_url: String,
    val home_page_url: String,
    val icon: String,
    val items: List<Item>,
    val language: String,
    val title: String,
    val version: String
)