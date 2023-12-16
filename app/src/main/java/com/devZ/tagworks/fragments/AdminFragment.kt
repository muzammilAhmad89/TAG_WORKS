package com.devZ.tagworks.Ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
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
import com.devZ.tagworks.Constants
import com.devZ.tagworks.Models.ProductModel
import com.devZ.tagworks.Models.ProductViewModel
import com.devZ.tagworks.R
import com.devZ.tagworks.SharedPrefManager
import com.devZ.tagworks.Utils
import com.devZ.tagworks.databinding.FragmentAdminBinding
import kotlinx.coroutines.launch

class AdminFragment : Fragment(), AdminAdapter.ProductEditListener {
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
    private lateinit var constants: Constants
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdminBinding.inflate(inflater, container, false)
        val view = binding.root
        mContext = requireContext()
        utils = Utils(mContext)
        sharedPrefManager = SharedPrefManager(requireContext())
        constants= Constants()

        binding.fabAdd.setOnClickListener {
            showSelectngDialog()
        }
        productList = sharedPrefManager.getProductList()
        serisName = sharedPrefManager.getSeriesNames().toSet().toCollection(LinkedHashSet())
        recyclerView = binding.recyclerViewAminData
        recyclerView.layoutManager = LinearLayoutManager(mContext)
        val seriesAndProducts = mutableListOf<Any>()

        // Loop through each series name
        for (seriesName in serisName) {
            val filteredProducts = productList.filter { it.series == seriesName }

            // Add series name followed by filtered products to the list
            seriesAndProducts.add(seriesName)
            seriesAndProducts.addAll(filteredProducts)
        }

        // Create and set the adapter with the updated data structure
        adminAdapter = AdminAdapter(mContext, seriesAndProducts, this@AdminFragment)
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
            showAddingDialog(constants.ALUMINIUM)
            dialog.dismiss()
        }

        glass.setOnClickListener {
            showAddingDialog(constants.GLASS)
            dialog.dismiss()
        }

        dialog.show()
    }
    private fun showAddingDialog(type:String) {
        val builder = AlertDialog.Builder(requireContext())
        val dialogView = layoutInflater.inflate(R.layout.adding_dialogbox, null)
        builder.setView(dialogView)
        val dialog = builder.create()
        val saveBtn = dialogView.findViewById<TextView>(R.id.savebtn)

        val section = dialogView.findViewById<EditText>(R.id.Sections)
        val rate = dialogView.findViewById<EditText>(R.id.Price)
        val maxDiscount = dialogView.findViewById<EditText>(R.id.maxDiscount)
        val itemCode = dialogView.findViewById<EditText>(R.id.itemCode)
       // val itemSeries = dialogView.findViewById<EditText>(R.id.itemSeries)

        val items = listOf(constants.DULL,
            constants.CH_RALL,
            constants.SH_BR,
            constants.MULTI,
            constants.WOOD
            )
        var color=constants.DULL

        val spinner = dialogView.findViewById<Spinner>(R.id.spinner)
        val colorAdapter = ArrayAdapter(mContext, android.R.layout.simple_spinner_item, items)
        colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = colorAdapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                color = items[position]

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        saveBtn.setOnClickListener {

            val product = ProductModel(
                "",
                section.text.toString(),
                color,
                rate.text.toString(),
                maxDiscount.text.toString(),
                type,
                section.text.toString(),
                itemCode.text.toString()
            )
            dialog.dismiss()

            saveProduct(product)

        }

        dialog.show()
    }

    private fun saveProduct(productModel: ProductModel) {
        utils.startLoadingAnimation()

        lifecycleScope.launch {
            try {
                productVieModel.saveProductToFirebase(productModel)
            }
            catch (e: Exception) {
                Toast.makeText(context, "Failed to save products: $e", Toast.LENGTH_SHORT).show()
            }
            finally {
                utils.endLoadingAnimation()
            }
        }
    }

    override fun selectedItemToEdit(productModel: ProductModel) {
        dialogBoxToEdit(productModel)
    }

    private fun dialogBoxToEdit(productModel: ProductModel){
        val builder = AlertDialog.Builder(requireContext())
        val dialogView = layoutInflater.inflate(R.layout.edit_dialog_box, null)
        builder.setView(dialogView)
        val dialog = builder.create()
        val updateBtn = dialogView.findViewById<TextView>(R.id.updateBtn)

        val eSection = dialogView.findViewById<EditText>(R.id.eSection)
        val eRate = dialogView.findViewById<EditText>(R.id.eRate)
        val eDiscount = dialogView.findViewById<EditText>(R.id.eDiscount)
        val eItemCode = dialogView.findViewById<EditText>(R.id.eItemCode)
        val eColor = dialogView.findViewById<EditText>(R.id.eColor)

        eSection.setText(productModel.section)
        eRate.setText(productModel.rate)
        eDiscount.setText(productModel.maxDiscount)
        eItemCode.setText(productModel.itemCode)
        eColor.setText(productModel.color)

        updateBtn.setOnClickListener {

            val product = ProductModel(
                pid = productModel.pid,
                eSection.text.toString(),
                eColor.text.toString(),
                eRate.text.toString(),
                eDiscount.text.toString(),
                productModel.type,
                eSection.text.toString(),
                eItemCode.text.toString()
            )
            dialog.dismiss()

            updateProduct(product)

        }

        dialog.show()
    }

    private fun updateProduct(productModel: ProductModel) {
        utils.startLoadingAnimation()

        lifecycleScope.launch {
            try {
                productVieModel.updateProduct(productModel)
            }
            catch (e: Exception) {
                Toast.makeText(context, "$e", Toast.LENGTH_SHORT).show()
            }
            finally {
                utils.endLoadingAnimation()
            }
        }
    }

}
