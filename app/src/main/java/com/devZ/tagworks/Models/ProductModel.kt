package com.devZ.tagworks.Models

data class ProductModel(
    var pid :String,
    val section:String,
    val color:String,
    val rate:String,
    val maxDiscount:String,
    var type:String,
    val series:String,
    val itemCode:String
) {
    // Required no-argument constructor for Firestore deserialization
    constructor() : this("", "", "", "", "", "", "","")
}

