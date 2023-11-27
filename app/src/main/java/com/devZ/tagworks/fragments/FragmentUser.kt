package com.devZ.tagworks.fragments

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devZ.tagworks.Adapter.CustomerAdapter
import com.devZ.tagworks.Models.ProductModel
import com.devZ.tagworks.Models.ProductViewModel
import com.devZ.tagworks.R
import com.devZ.tagworks.SharedPrefManager
import com.devZ.tagworks.Utils
import com.devZ.tagworks.databinding.FragmentUserBinding

class FragmentUser : Fragment(), CustomerAdapter.ProductListener {
    private lateinit var binding: FragmentUserBinding
    private lateinit var utils: Utils


    private val productList = mutableListOf<ProductModel>()
    private val productVieModel: ProductViewModel by viewModels()
    private lateinit var sharedPrefManager: SharedPrefManager
    private lateinit var mContext: Context
    private lateinit var recyclerView: RecyclerView
    private lateinit var customerAdapter: CustomerAdapter
    private var AdminRate: Int = 0
    private lateinit var Rate: TextView
    private val splashDelayMillis = 2000 // 2 seconds

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
        getAllProducts()

        return view
    }

    fun getAllProducts() {
        productList.addAll(sharedPrefManager.getProductList())
        utils.endLoadingAnimation()
        recyclerView = binding.recyclerViewAminData
        recyclerView.layoutManager = LinearLayoutManager(mContext)
        customerAdapter = CustomerAdapter(mContext, productList, this@FragmentUser)
        recyclerView.adapter = customerAdapter
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

        sections.text = productModel.section
        colors.text = productModel.color
        AdminRate = productModel.rate.toInt()

        val dialog = builder.create()

        ratee.setOnClickListener {
            // Get the values from the EditText fields when the button is clicked
            val sizeText = sizeEditText.text.toString()
            val discountText = discountEditText.text.toString()

            val sizeInt = if (sizeText.isNotEmpty()) {
                try {
                    sizeText.toInt()
                } catch (e: NumberFormatException) {
                    e.printStackTrace()
                    0
                }
            } else {
                0
            }

            val discountInt = if (discountText.isNotEmpty()) {
                try {
                    discountText.toInt()
                } catch (e: NumberFormatException) {
                    e.printStackTrace()
                    0
                }
            } else {
                0
            }

            calculatePrice(sizeInt, discountInt)
        }

        dialog.show()
    }

    fun calculatePrice(size: Int, discount: Int) {

        val adminRate = AdminRate.toDouble()
        val ratePerInch = adminRate / 12.0
        val initialPrice = ratePerInch * size
        val discountPrice = initialPrice * (discount.toDouble() / 100.0)
        val finalPrice = initialPrice - discountPrice
        Rate.text = finalPrice.toString()


}

}
