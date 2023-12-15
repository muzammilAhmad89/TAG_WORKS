package com.devZ.tagworks.Models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.devZ.tagworks.Data.Repo
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.launch

class ProductViewModel(context: Application) : AndroidViewModel(context) {

    private val repo = Repo(context)

    suspend fun saveProductToFirebase(product: ProductModel) {
        repo.saveProduct(product)
    }

    suspend fun getProduct(): Task<QuerySnapshot>{
        return repo.getproduct()
    }

    suspend fun updateProduct(product: ProductModel){
        return repo.updateProduct(product)
    }
}
