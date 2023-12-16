package com.devZ.tagworks.Data


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
    private val FIELD_ID= db.collection(constans.FIELD_ID)
    fun saveProduct(product: ProductModel) {
        PRODUCT_COLLECTION.add(product)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val documentReference = task.result
                    if (documentReference != null) {
                        val documentID = documentReference.id
                        // Update the document with the unique ID (pId)
                        PRODUCT_COLLECTION.document(documentID).update(constans.FIELD_ID,documentID)
                            .addOnCompleteListener { updateTask ->
                                if (updateTask.isSuccessful) {
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

    fun updateProduct(updatedProduct: ProductModel) {
        PRODUCT_COLLECTION.document(updatedProduct.pid).get()
            .addOnCompleteListener { documentSnapshotTask ->
                if (documentSnapshotTask.isSuccessful) {
                    if (documentSnapshotTask.result?.exists() == true) {
                        PRODUCT_COLLECTION.document(updatedProduct.pid).set(updatedProduct)
                            .addOnCompleteListener { updateTask ->
                                if (updateTask.isSuccessful) {
                                    Toast.makeText(
                                        context,
                                        "Product updated successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Update failed: ${updateTask.exception?.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    } else {
                        Toast.makeText(context, "Document does not exist", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Toast.makeText(
                        context,
                        "Error checking document existence: ${documentSnapshotTask.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
    }

        suspend fun getproduct(): Task<QuerySnapshot> {
        return PRODUCT_COLLECTION.get()
    }

}
