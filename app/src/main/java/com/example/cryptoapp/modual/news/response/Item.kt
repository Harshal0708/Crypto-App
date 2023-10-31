package com.example.cryptoapp.modual.news.response

data class Item(
    val authors: List<Any>,
    val content_html: String,
    val content_text: String,
    val date_published: String,
    val id: String,
    val image: String,
    val title: String,
    val url: String
)