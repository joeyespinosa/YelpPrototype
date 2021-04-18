package com.axelia.yelpprototype.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.axelia.yelpprototype.model.Business.Companion.TABLE_NAME
import com.squareup.moshi.Json

@Entity(tableName = TABLE_NAME)
data class Business(

    @PrimaryKey
    val id: String,

    val alias: String? = "",

    val categories: List<Category>,

    @Json(name = "display_phone")
    val displayPhone: String? = "",

    val distance: Double? = 0.00,

    @Json(name = "image_url")
    val imageUrl: String? = "",

    val is_closed: Boolean? = true,
    val location: Location,

    val name: String? = "",

    val phone: String? = "",
    val price: String? = "",
    val rating: Double? = 0.00,
    val review_count: Int? = 0,

    val url: String? = "",

    var isFavorite: Boolean? = false
) {
    companion object {
        const val TABLE_NAME = "business"
    }
}
