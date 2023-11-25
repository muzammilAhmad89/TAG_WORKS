package com.devZ.tagworks.Data

import android.content.Context
import com.devZ.tagworks.Constants
import com.devZ.tagworks.SharedPrefManager
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Repo(val context: Context) {
        private var constants = Constants()
        private var sharedPrefManager = SharedPrefManager(context)
        private val db = Firebase.firestore



    private var ALUMINIUM_COLLECTION = db.collection(constants.Product_COLLECTION)
//    private var GLASS_COLLECTION = db.collection(constants.GLASS_COLLECTION)










         //FUNCTIONS.............
         fun getAlumininum(): Task<QuerySnapshot> {
             return ALUMINIUM_COLLECTION.get()
         }
//        fun getGlass(): Task<QuerySnapshot> {
//             return GLASS_COLLECTION.get()
//         }

    }