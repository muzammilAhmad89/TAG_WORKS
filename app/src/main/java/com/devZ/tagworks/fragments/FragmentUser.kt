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
import com.devZ.tagworks.Constants
import com.devZ.tagworks.Models.ProductModel
import com.devZ.tagworks.R
import com.devZ.tagworks.SharedPrefManager
import com.devZ.tagworks.Utils
import com.devZ.tagworks.databinding.FragmentUserBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class FragmentUser : Fragment(), CustomerAdapter.ProductListener {
    private lateinit var binding: FragmentUserBinding
    private lateinit var utils: Utils

    private lateinit var productList: List<ProductModel>
    private lateinit var serisName: LinkedHashSet<String>
    private lateinit var mContext: Context
    private lateinit var recyclerView: RecyclerView
    private lateinit var customerAdapter: CustomerAdapter
    private lateinit var sharedPrefManager: SharedPrefManager
    private lateinit var Rate: TextView

    private val db = Firebase.firestore
    private lateinit var constants: Constants

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserBinding.inflate(inflater, container, false)
        val view = binding.root
        mContext = requireContext()
        utils = Utils(mContext)
        constants=Constants()
        sharedPrefManager = SharedPrefManager(requireContext())

        setupRecyclerView()
        updateList()

        return view
    }

    private fun setupRecyclerView() {
        utils.startLoadingAnimation()
        productList = sharedPrefManager.getProductList()
        serisName = sharedPrefManager.getSeriesNames().toSet().toCollection(LinkedHashSet())
        recyclerView = binding.recyclerViewAminData
        recyclerView.layoutManager = LinearLayoutManager(mContext)
        val seriesAndProducts = prepareSeriesAndProducts()

        customerAdapter = CustomerAdapter(mContext, seriesAndProducts, this@FragmentUser)
        recyclerView.adapter = customerAdapter

        utils.endLoadingAnimation()
    }

    private fun prepareSeriesAndProducts(): List<Any> {
        val seriesAndProducts = mutableListOf<Any>()

        for (seriesName in serisName) {
            Log.d("SeriesComparison", "SeriesName: $seriesName")
            val filteredProducts = productList.filter { it.series == seriesName }

            seriesAndProducts.add(seriesName)
            seriesAndProducts.addAll(filteredProducts)
        }

        return seriesAndProducts
    }

    override fun onProductClicked(productModel: ProductModel) {

        when (productModel.type) {
            constants.ALUMINIUM -> showRateDialog(productModel)
            constants.GLASS -> Toast.makeText(mContext, "Soon", Toast.LENGTH_SHORT).show()
            else -> Toast.makeText(mContext, "Some thing went wrong", Toast.LENGTH_SHORT).show()
        }
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

        colors.text = productModel.color
        productSeries.text = productModel.series
        sections.text = productModel.itemCode

        val adminRate = productModel.rate.toIntOrNull() ?: 0
        val dialog = builder.create()

        ratee.setOnClickListener {
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

    private fun updateList(){
        val updatedList= ArrayList<ProductModel>()
        val query = db.collection(constants.Product_COLLECTION)

        query.addSnapshotListener { snapshot, firebaseFirestoreException ->
            if (firebaseFirestoreException != null) {
                // Handle any errors that occurred while listening to the snapshot
                Toast.makeText(mContext, firebaseFirestoreException.message.toString(), Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }

            snapshot?.let { querySnapshot ->
                updatedList.clear()

                for (document in querySnapshot) {
                    val product = document.toObject(ProductModel::class.java)
                    updatedList.add(product)
                }

                sharedPrefManager.putProductList(updatedList)
                // Extract series names from each product and store them in SharedPreferences
                val seriesNames = updatedList.mapNotNull { it.series }
                sharedPrefManager.putSeriesNames(seriesNames)

                setupRecyclerView()
            }
        }
    }
}

