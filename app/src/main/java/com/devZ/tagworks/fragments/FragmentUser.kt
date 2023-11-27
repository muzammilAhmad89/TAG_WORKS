package com.devZ.tagworks.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devZ.tagworks.Adapter.CustomerAdapter
import com.devZ.tagworks.Models.ProductModel
import com.devZ.tagworks.Models.ProductViewModel
import com.devZ.tagworks.SharedPrefManager
import com.devZ.tagworks.Utils
import com.devZ.tagworks.databinding.FragmentUserBinding


class FragmentUser : Fragment(),CustomerAdapter.ProductListener {
    private lateinit var binding: FragmentUserBinding
    private lateinit var utils: Utils
    private val productList = mutableListOf<ProductModel>()
    private val productVieModel : ProductViewModel by viewModels()
    private lateinit var sharedPrefManager: SharedPrefManager
    private lateinit var mContext: Context
    private lateinit var recyclerView: RecyclerView
    private lateinit var customerAdapter: CustomerAdapter
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
    fun getAllProducts(){

      //     utils.startLoadingAnimation()
            productList.addAll(sharedPrefManager.getProductList())
        // End loading animation
        utils.endLoadingAnimation()
            // Update RecyclerView
            recyclerView = binding.recyclerViewAminData
            recyclerView.layoutManager = LinearLayoutManager(mContext)
            customerAdapter = CustomerAdapter(mContext, productList,this@FragmentUser)
            recyclerView.adapter = customerAdapter


        }

    override fun onProductClicked(pID: String, productList: List<ProductModel>) {

    }


}