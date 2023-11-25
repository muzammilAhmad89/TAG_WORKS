package com.devz.tagworks.Data

import android.content.Context
import android.widget.Toast
import com.devZ.tagworks.Constants
import com.devZ.tagworks.Models.ProductModel
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Repo(val context: Context) {

    private val db = Firebase.firestore
    private val constans = Constants()
    private val Product_COLLECTION = constans.Product_COLLECTION

    fun saveProduct(product: ProductModel){
        requireNotNull(context) { "Context cannot be null" }
        db.collection(Product_COLLECTION).add(product).addOnSuccessListener { documentRefrence ->
            val documentID = documentRefrence.id
            product.pID = documentID
            db.collection(Product_COLLECTION).document(documentID).set(product)
            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()

        }
            .addOnFailureListener {e->
                Toast.makeText(context, ""+e.message, Toast.LENGTH_SHORT).show()
            }

    }
}
