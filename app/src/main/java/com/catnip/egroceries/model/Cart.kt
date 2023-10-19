package com.catnip.egroceries.model

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

/**
Written with love by Muhammad Hermas Yuda Pamungkas
Github : https://github.com/hermasyp
 **/
data class Cart(
    var id: Int? = null,
    var productId : Int? = null,
    var productName :String,
    var productPrice :Double,
    var productImgUrl :String,
    var itemQuantity: Int = 0,
    var itemNotes: String? = null,
)
