package com.devZ.tagworks

import android.content.Context
import android.content.SharedPreferences
import android.provider.SyncStateContract.Constants
import com.devZ.tagworks.Models.ProductModel
import com.google.gson.Gson

class SharedPrefManager (context: Context) {
    private var constants = Constants()

    private val sharedPref: SharedPreferences = context.getSharedPreferences("myAppPrefs", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPref.edit()

    fun clearSharedPref(){
        sharedPref.edit().clear().apply()
    }
    fun putALsectiontList(list:List<ProductModel>): Boolean {
        editor.putString("ListSection", Gson().toJson(list))
        editor.commit()
        return true
    }  fun getALsectiontList(list:List<ProductModel>): Boolean {
        editor.putString("ListStudents", Gson().toJson(list))
        editor.commit()
        return true
    }
    fun putGlassList(list:List<ProductModel>): Boolean {
        editor.putString("GlassList", Gson().toJson(list))
        editor.commit()
        return true
    }
    fun getGlassList(list:List<ProductModel>): Boolean {
        editor.putString("GlassList", Gson().toJson(list))
        editor.commit()
        return true
    }
}