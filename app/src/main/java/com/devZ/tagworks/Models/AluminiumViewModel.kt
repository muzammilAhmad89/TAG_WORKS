package com.devZ.tagworks.Models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.devZ.tagworks.Constants
import com.devZ.tagworks.Data.Repo
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot

class AluminiumViewModel  (context: Application) : AndroidViewModel(context) {
    private val constants = Constants()
    private val repo =  Repo(context)
    private val context = Constants()



    fun getAluminiumModel():Task<QuerySnapshot>{
        return repo.getAlumininum()
    }

}