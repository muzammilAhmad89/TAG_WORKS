package com.devZ.tagworks.Ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.devZ.tagworks.Models.ProductModel
import com.devZ.tagworks.Models.ProductViewModel
import com.devZ.tagworks.R
import com.devZ.tagworks.SharedPrefManager
import com.devZ.tagworks.Utils
import com.devZ.tagworks.databinding.FragmentAdminBinding
import kotlinx.coroutines.launch

class AdminFragment : Fragment() {
    private lateinit var binding: FragmentAdminBinding
    private lateinit var utils: Utils
    private val productList = mutableListOf<ProductModel>()
    private val productVieModel : ProductViewModel by viewModels()
    private lateinit var sharedPrefManager: SharedPrefManager
    private lateinit var mContext: Context
    private val splashDelayMillis = 2000 // 2 seconds

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdminBinding.inflate(inflater, container, false)
        val view = binding.root
        mContext = requireContext()
        utils = Utils(mContext)
        sharedPrefManager = SharedPrefManager(requireContext())

        binding.fabAdd.setOnClickListener {
            showSelectngDialog()
        }
        return view
    }

    private fun showSelectngDialog() {
        val builder = AlertDialog.Builder(requireContext()) // or context
        val dialogView = layoutInflater.inflate(R.layout.selectingmodule, null)
        builder.setView(dialogView)

        val aluminium = dialogView.findViewById<TextView>(R.id.Admin)
        val glass = dialogView.findViewById<TextView>(R.id.customer)

        val dialog = builder.create()

        aluminium.setOnClickListener {
            showAddingDialog()
            dialog.dismiss()
        }

        glass.setOnClickListener {
            showAddingDialog()
            dialog.dismiss()
        }

        dialog.show()
    }
    private fun showAddingDialog() {
        val builder = AlertDialog.Builder(requireContext())
        val dialogView = layoutInflater.inflate(R.layout.adding_dialogbox, null)
        builder.setView(dialogView)
        val saveBtn = dialogView.findViewById<TextView>(R.id.savebtn)

        saveBtn.setOnClickListener {
            val section = dialogView.findViewById<EditText>(R.id.Sections).text.toString()
            val colors = dialogView.findViewById<EditText>(R.id.Colors).text.toString()
            val rate = dialogView.findViewById<EditText>(R.id.Price).text.toString()
            val maxDiscount = dialogView.findViewById<EditText>(R.id.maxDiscount).text.toString()
            val series = dialogView.findViewById<EditText>(R.id.series).text.toString()

            // Create a ProductModel using the retrieved data
            val product = ProductModel(
                pid = "",
                section = section,
                color = colors,
                rate = rate,
                maxDiscount = maxDiscount,
                "",
                series =series

                )

            productList.add(product)
            saveProduct() // Moved saveProduct() inside the setOnClickListener block
        }

        builder.setNegativeButton("", null)
        val dialog = builder.create()
        dialog.show()
    }

    private fun saveProduct() {
        utils.startLoadingAnimation()

        lifecycleScope.launch {
            try {
              //  Toast.makeText(context, "debug", Toast.LENGTH_SHORT).show()
                for (product in productList) {
                    productVieModel.saveProductToFirebase(product)
                }
                Toast.makeText(mContext, ""+productList.size, Toast.LENGTH_SHORT).show()

            } catch (e: Exception) {
                Toast.makeText(context, "Failed to save products: $e", Toast.LENGTH_SHORT).show()
            } finally {
                utils.endLoadingAnimation()
            }
        }
    }

}
//    private fun dismissDialog() {
//        // Dismiss the dialog when called
//        // Make sure to replace "dialog" with your actual dialog instance
//        val dialog = builder.create()
//        dialog.dismiss()
//    }

//    private fun storeProductInSharedPreferences(product: ProductModel) {
//        // Retrieve the existing list from SharedPreferences
//        val productList = sharedPrefManager.getProductList()
//        // Add the new product to the list
//        productList.add(product)
//        // Save the updated list back to SharedPreferences
//        sharedPrefManager.putProductList(productList)
//    }
