package com.devZ.tagworks.Ui

import android.content.Context
import android.os.Bundle
import android.util.Log
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devZ.tagworks.Adapter.AdminAdapter
import com.devZ.tagworks.Adapter.CustomerAdapter
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
    private var productList = mutableListOf<ProductModel>()
    private val productVieModel : ProductViewModel by viewModels()
    private lateinit var sharedPrefManager: SharedPrefManager
    private lateinit var mContext: Context
    private lateinit var recyclerView: RecyclerView
    private lateinit var adminAdapter: AdminAdapter
    private val splashDelayMillis = 2000 // 2 seconds
    private lateinit var serisName: LinkedHashSet<String>
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
        productList = sharedPrefManager.getProductList()
        serisName = sharedPrefManager.getSeriesNames().toSet().toCollection(LinkedHashSet())
        Toast.makeText(mContext, "gettinglistseries"+serisName.size, Toast.LENGTH_SHORT).show()

        recyclerView = binding.recyclerViewAminData
        recyclerView.layoutManager = LinearLayoutManager(mContext)
        Toast.makeText(mContext, "product list"+productList.size, Toast.LENGTH_SHORT).show()

        // Initialize an empty list to store series names and corresponding products
        val seriesAndProducts = mutableListOf<Any>()

        // Loop through each series name
        for (seriesName in serisName) {
            // Log the values for debugging
            Log.d("SeriesComparison", "SeriesName: $seriesName")
            val filteredProducts = productList.filter { it.series == seriesName }
            Log.d("SeriesComparison", "FilteredProductsSize: ${filteredProducts.size}")

            // Add series name followed by filtered products to the list
            seriesAndProducts.add(seriesName)
            seriesAndProducts.addAll(filteredProducts)
        }

        // Create and set the adapter with the updated data structure
        adminAdapter = AdminAdapter(mContext, seriesAndProducts)
        recyclerView.adapter = adminAdapter

        utils.endLoadingAnimation()

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
