package com.devZ.tagworks.Models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.devZ.tagworks.Constants
import com.devz.tagworks.Data.Repo

class ProductViewModel(context: Application) : AndroidViewModel(context) {

    private val repo = Repo(context)
    private val constants = Constants()

    fun saveProductToFirebase(product: ProductModel) {
         repo.saveProduct(product)
    }
}
