package com.devZ.tagworks.Data

import android.content.Context
import android.widget.Toast
import com.devZ.tagworks.Constants
import com.devZ.tagworks.Models.ProductModel
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class Repo(val context: Context) {

    private val db = Firebase.firestore
    private var constants = Constants()



    private var PRODUCT_COLLECTION = db.collection(constants.Product_COLLECTION)
    private var PRODUCT_SUBCOLLECTION = db.collection(constants.Product_SUB_COLLECTION)
//    fun saveProducts(product:ProductModel){
//        PRODUCT_COLLECTION.add(product)
//            .addOnCompleteListener {task->
//                if (task.isSuccessful){
//                    val
//                    Toast.makeText(context, "Prodect saved", Toast.LENGTH_SHORT).show()
//                }
//
//            }
//            .addOnFailureListener { e->
//                Toast.makeText(context, ""+e.message, Toast.LENGTH_SHORT).show()
//            }
//    }

    suspend fun saveProduct(product: ProductModel) {
        val seriesName = product.series
        if (seriesName.isNullOrBlank()) {
            Toast.makeText(context, "Error: Invalid seriesName", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            // Add the product directly to the collection
            PRODUCT_COLLECTION
                .document(seriesName)
            //    .set(product)
                .collection("products")
//               .collection("product")
                .add(product)
                .await()

        } catch (e: Exception) {
            // Handle exceptions
            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    suspend fun getAllSeriesNames(): List<String> {
        val seriesNames = mutableListOf<String>()

        try {
            val querySnapshot =PRODUCT_COLLECTION.get().await()
            for (document in querySnapshot.documents) {
                seriesNames.add(document.id)
            }
        } catch (e: Exception) {
            // Handle exceptions
        }
        return seriesNames
    }
    suspend fun getProductsForSeries(seriesName: String): List<ProductModel> {
        val productList = mutableListOf<ProductModel>()

        try {
            val querySnapshot =
                PRODUCT_COLLECTION
                .document(seriesName)
                .collection("products")
                .get()
                .await()

            for (document in querySnapshot.documents) {
                val product = document.toObject(ProductModel::class.java)
                if (product != null) {
                    productList.add(product)
                }
            }
        } catch (e: Exception) {
            // Handle exceptions
        }
        return productList
    }
}
