// FragmentUser.kt
package com.devZ.tagworks.fragments

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devZ.tagworks.Adapter.CustomerAdapter
import com.devZ.tagworks.Models.ProductModel
import com.devZ.tagworks.R
import com.devZ.tagworks.SharedPrefManager
import com.devZ.tagworks.Utils
import com.devZ.tagworks.databinding.FragmentUserBinding

class FragmentUser : Fragment(), CustomerAdapter.ProductListener {
    private lateinit var binding: FragmentUserBinding
    private lateinit var utils: Utils

    private lateinit var productList: List<ProductModel>
    private lateinit var serisName: LinkedHashSet<String> // Declare serisName as LinkedHashSet
    private lateinit var mContext: Context
    private lateinit var recyclerView: RecyclerView
    private lateinit var customerAdapter: CustomerAdapter
    private lateinit var sharedPrefManager: SharedPrefManager
    private lateinit var Rate: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserBinding.inflate(inflater, container, false)
        val view = binding.root
        mContext = requireContext()
        utils = Utils(mContext)
        sharedPrefManager = SharedPrefManager(requireContext())
        utils.startLoadingAnimation()

        // Retrieve data and prepare it for the adapter
        productList = sharedPrefManager.getProductList()
        serisName = sharedPrefManager.getSeriesNames().toSet().toCollection(LinkedHashSet())
       // Toast.makeText(mContext, "gettinglistseries"+serisName.size, Toast.LENGTH_SHORT).show()

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
        customerAdapter = CustomerAdapter(mContext, seriesAndProducts, this@FragmentUser)
        recyclerView.adapter = customerAdapter

        utils.endLoadingAnimation()

        return view
    }

    override fun onProductClicked(productModel: ProductModel) {
        showRateDialog(productModel)
    }

    private fun showRateDialog(productModel: ProductModel) {
        val builder = AlertDialog.Builder(requireContext())
        val dialogView = layoutInflater.inflate(R.layout.rate_dialog, null)
        builder.setView(dialogView)

        val sizeEditText = dialogView.findViewById<EditText>(R.id.size)
        val discountEditText = dialogView.findViewById<EditText>(R.id.discount)

        val sections = dialogView.findViewById<TextView>(R.id.Sectionss)
        val colors = dialogView.findViewById<TextView>(R.id.Colorss)
        Rate = dialogView.findViewById<TextView>(R.id.getRate)
        val ratee = dialogView.findViewById<TextView>(R.id.ratee)
        val productSeries = dialogView.findViewById<TextView>(R.id.productSeries)

        //sections.text = productModel.section
        colors.text = productModel.color
        productSeries.text=productModel.series

        sections.text=productModel.itemCode

        val adminRate = productModel.rate.toIntOrNull() ?: 0

        val dialog = builder.create()

        ratee.setOnClickListener {
            // Get the values from the EditText fields when the button is clicked
            val sizeText = sizeEditText.text.toString()
            val discountText = discountEditText.text.toString()

            val sizeInt = sizeText.toIntOrNull() ?: 0
            val discountInt = discountText.toIntOrNull() ?: 0

            calculatePrice(adminRate, sizeInt, discountInt)
        }

        dialog.show()
    }

    private fun calculatePrice(adminRate: Int, size: Int, discount: Int) {
        val ratePerInch = adminRate.toDouble() / 12.0
        val initialPrice = ratePerInch * size
        val discountPrice = initialPrice * (discount.toDouble() / 100.0)
        val finalPrice = initialPrice - discountPrice
        Rate.text = finalPrice.toString()
    }
}
