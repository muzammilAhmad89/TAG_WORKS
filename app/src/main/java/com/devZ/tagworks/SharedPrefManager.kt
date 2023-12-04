package com.devZ.tagworks

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import com.devZ.tagworks.Models.ProductModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class SharedPrefManager(context: Context) {
    private val sharedPref: SharedPreferences = context.getSharedPreferences("myAppPrefs", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPref.edit()

    fun clearSharedPref() {
        sharedPref.edit().clear().apply()
    }

    fun putProductList(seriesName: String, productList: List<ProductModel>) {
        clearSharedPref()
        val gson = Gson()
        val json = gson.toJson(productList)
        sharedPref.edit().putString("productList", json).apply()
    }

    fun putSeriesName(seriesName: String) {
        val editor = sharedPref.edit()
        editor.putString("series", seriesName)  // Corrected key to "series"
        editor.apply()
    }

    fun getSeriesNames(): List<String> {
        val json = sharedPref.getString("series", "")
        return if (json.isNullOrBlank()) {
            mutableListOf()
        } else {
            Gson().fromJson(json, object : TypeToken<List<String>>() {}.type)
        }
    }

    fun getProductList(): MutableList<ProductModel> {
        val json = sharedPref.getString("productList", "")
        return if (json.isNullOrBlank()) {
            mutableListOf()
        } else {
            Gson().fromJson(json, object : TypeToken<MutableList<ProductModel>>() {}.type)
        }
    }
}
