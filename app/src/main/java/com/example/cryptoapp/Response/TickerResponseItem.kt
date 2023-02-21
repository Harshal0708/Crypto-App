package com.example.cryptoapp.Response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TickerResponseItem(


    @Json(name = "A") val A: String,
    @Json(name = "B") val B: String,
    @Json(name = "C") val C: Long,
    @Json(name = "E") val E: Long,
    @Json(name = "F") val F: Long,
    @Json(name = "L") val L: Long,
    @Json(name = "O") val O: Long,
    @Json(name = "P") val pp: String,
    @Json(name = "Q") val qq: String,
    @Json(name = "a") val aa: String,
    @Json(name = "b") val bb: String,
    @Json(name = "c")  val c: String,
    @Json(name = "e")  val e: String,
    @Json(name = "h") val h: String,
    @Json(name = "l") val l: String,
    @Json(name = "n") val n: Int,
    @Json(name = "o") val o: String,
    @Json(name = "p") val p: String,
    @Json(name = "q") val q: String,
    @Json(name = "s") val s: String,
    @Json(name = "v") val v: String,
    @Json(name = "w") val w: String,
    @Json(name = "x") val x: String,


)

//
//@SerializedName("A")val A: String,
//@SerializedName("B")val B: String,
//@SerializedName("C")val C: Long,
//@SerializedName("E")val E: Long,
//@SerializedName("F")val F: Long,
//@SerializedName("L")val L: Long,
//@SerializedName("O")val O: Long,
//@SerializedName("P")val P: String,
//@SerializedName("Q")val Q: String,
//@SerializedName("a")val aa: String,
//@SerializedName("b")val bb: String,
//@SerializedName("c")val cc: String,
//@SerializedName("e")val ee: String,
//@SerializedName("h")val hh: String,
//@SerializedName("l")val l: String,
//@SerializedName("n")val n: Int,
//@SerializedName("o")val o: String,
//@SerializedName("p")val pp: String,
//@SerializedName("qq")val qq: String,
//@SerializedName("s")val s: String,
//@SerializedName("v")val v: String,
//@SerializedName("w")val w: String,
//@SerializedName("x")val x: String,