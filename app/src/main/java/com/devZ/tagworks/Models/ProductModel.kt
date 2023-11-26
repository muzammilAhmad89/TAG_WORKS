package com.devZ.tagworks.Models

data class ProductModel(
    var pID :String,
    val section:String,
    val color:String,
    val rate:String,
    val maxDiscount:String,
    val type:String,
) {
    // Required no-argument constructor for Firestore deserialization
    constructor() : this("", "", "", "", "", "")
}

