package com.devz.tagworks.Data


import android.content.Context
import android.widget.Toast
import com.devZ.tagworks.Constants
import com.devZ.tagworks.Models.ProductModel
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Repo(val context: Context) {

    private val db = Firebase.firestore
    private val constans = Constants()
    private val PRODUCT_COLLECTION= db.collection(constans.Product_COLLECTION)




    fun saveProduct(product: ProductModel){
        requireNotNull(context) { "Context cannot be null" }
        db.collection(PRODUCT_COLLECTION.toString()).add(product).addOnSuccessListener { documentRefrence ->
            val documentID = documentRefrence.id
            product.pID = documentID
            db.collection(PRODUCT_COLLECTION.toString()).document(documentID).set(product)
            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()

        }
            .addOnFailureListener {e->
                Toast.makeText(context, ""+e.message, Toast.LENGTH_SHORT).show()
            }

    }
    suspend fun getproduct(): Task<QuerySnapshot> {
        return PRODUCT_COLLECTION.get()
    }

}
