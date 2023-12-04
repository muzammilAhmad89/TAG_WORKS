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

    suspend fun getAllSeriesNames(): List<String> {
        return repo.getAllSeriesNames()
    }

    suspend fun getProductsForSeries(seriesName: String): List<ProductModel> {
        return repo.getProductsForSeries(seriesName)
    }

    // Uncomment this if you need to fetch all products (if this is your use case)
    // suspend fun getAllProducts(): Task<QuerySnapshot> {
    //     return repo.getAllProducts()
    // }
}
