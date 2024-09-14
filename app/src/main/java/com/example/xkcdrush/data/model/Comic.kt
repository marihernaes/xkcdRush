package com.example.xkcdrush.data.model

data class Comic(
    val num: Int = 0,
    val title: String = "",
    val img: String = "",
    val alt: String = "",
    val month: String = "",
    val year: String = "",
    val day: String = ""
) {
    fun isValid(): Boolean = num != 0 &&
                title.isNotEmpty() &&
                img.isNotEmpty() &&
                alt.isNotEmpty() &&
                month.isNotEmpty() &&
                year.isNotEmpty() &&
                day.isNotEmpty()
}