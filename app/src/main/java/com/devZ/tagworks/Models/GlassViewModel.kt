//package com.devz.tagworks.Models
//
//import android.app.Application
//import androidx.lifecycle.AndroidViewModel
//import com.devz.tagworks.Constants
//import com.devz.tagworks.Data.Repo
//import com.google.android.gms.tasks.Task
//import com.google.firebase.firestore.QuerySnapshot
//
//class GlassViewModel   (context: Application) : AndroidViewModel(context) {
//    private val constants = Constants()
//    private val repo =  Repo(context)
//    private val context = Constants()
//
//
//
//    fun getGlassModel(): Task<QuerySnapshot> {
//        return repo.getGlass()
//    }
//
//}