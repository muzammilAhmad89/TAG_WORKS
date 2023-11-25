package com.devZ.tagworks

import android.content.Context
import android.content.SharedPreferences
import android.provider.SyncStateContract.Constants
import com.devZ.tagworks.Models.ProductModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SharedPrefManager (context: Context) {
    private var constants = Constants()

    private val sharedPref: SharedPreferences = context.getSharedPreferences("myAppPrefs", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPref.edit()

    fun clearSharedPref(){
        sharedPref.edit().clear().apply()
    }
    fun putProductList(list: List<ProductModel>): Boolean {
        editor.putString("ProductList", Gson().toJson(list))
        editor.apply()
        return true
    }
    fun getProductList(): MutableList<ProductModel> {
        val json = sharedPref.getString("ProductList", "")
        return if (json.isNullOrBlank()) {
            mutableListOf()
        } else {
            Gson().fromJson(json, object : TypeToken<MutableList<ProductModel>>() {}.type)
        }
    }
}