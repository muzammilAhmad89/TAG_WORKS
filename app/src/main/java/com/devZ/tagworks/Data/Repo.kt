package com.devz.tagworks.Data


import android.content.Context
import android.widget.Toast
import com.devZ.tagworks.Constants
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Repo(val context: Context) {

    private val db = Firebase.firestore
    private val constans = Constants()
    private val PRODUCT_COLLECTION= db.collection(constans.Product_COLLECTION)
    private val FIELD_ID= db.collection(constans.FIELD_ID)

    data class ProductModel(
        val pId: String? = null, // Unique ID for each product
        val name: String? = null,
        var type: String? = null, // Type of product (aluminum or glass)
        // Other fields as needed
    )

    fun saveProduct(product: com.devZ.tagworks.Models.ProductModel) {
        requireNotNull(context) { "Context cannot be null" }

        val collectionReference = PRODUCT_COLLECTION

        // Add a new document to the collection with an auto-generated ID
        collectionReference.add(product)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val documentReference = task.result
                    if (documentReference != null) {
                        val documentID = documentReference.id

                        // Update the document with the unique ID (pId)
                        collectionReference.document(documentID).update(constans.FIELD_ID,documentID)
                            .addOnCompleteListener { updateTask ->
                                if (updateTask.isSuccessful) {
                                    // Document updated successfully
                                    Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()
                                } else {
                                    // Handle update failure
                                    Toast.makeText(context, "Update failed: ${updateTask.exception?.message}", Toast.LENGTH_SHORT).show()
                                }
                            }
                    } else {
                        // Handle null document reference
                        Toast.makeText(context, "Document reference is null", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Handle add failure
                    Toast.makeText(context, "Add failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }



//    fun saveProduct(product: ProductModel) {
//        requireNotNull(context) { "Context cannot be null" }
//
//        // Toast.makeText(context, "debug 1" + product, Toast.LENGTH_SHORT).show()
//
//        val collectionReference = PRODUCT_COLLECTION
//
//        collectionReference.add(product)
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    val documentReference = task.result
//                    if (documentReference != null) {
//                        val documentID = documentReference.id
//                        product.type = documentID
//
//                        // Update the document with the product
//                        collectionReference.document(documentID).set(product)
//                            .addOnCompleteListener { updateTask ->
//                                if (updateTask.isSuccessful) {
//                                    // Document updated successfully
//                                    Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()
//                                } else {
//                                    // Handle update failure
//                                    Toast.makeText(context, "Update failed: ${updateTask.exception?.message}", Toast.LENGTH_SHORT).show()
//                                }
//                            }
//                    } else {
//                        // Handle null document reference
//                        Toast.makeText(context, "Document reference is null", Toast.LENGTH_SHORT).show()
//                    }
//                } else {
//                    // Handle add failure
//                    Toast.makeText(context, "Add failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
//                }
//            }
//    }
    suspend fun getproduct(): Task<QuerySnapshot> {
        return PRODUCT_COLLECTION.get()
    }

}
//    fun saveProduct(product: ProductModel) {
//        requireNotNull(context) { "Context cannot be null" }
//        db.collection(PRODUCT_COLLECTION.toString()).add(product)
//            .addOnSuccessListener { documentReference ->
//                val documentID = documentReference.id
//                product.pID = documentID
//                Toast.makeText(context, "debug", Toast.LENGTH_SHORT).show()
//                // Use the documentID to update the document with set
//                db.collection(PRODUCT_COLLECTION.toString()).document(documentID)
//                    .set(product)
//                    .addOnSuccessListener {
//                        Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()
//                    }
//                    .addOnFailureListener { e ->
//                        Toast.makeText(context, "Failed to update: ${e.message}", Toast.LENGTH_SHORT).show()
//                    }
//            }
//            .addOnFailureListener { e ->
//                Toast.makeText(context, "Failed to save: ${e.message}", Toast.LENGTH_SHORT).show()
//            }
//    }
//}
