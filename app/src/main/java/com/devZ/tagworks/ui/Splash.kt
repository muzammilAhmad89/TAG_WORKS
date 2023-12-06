package com.devZ.tagworks.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.devZ.tagworks.Constants
import com.devZ.tagworks.Models.ProductModel
import com.devZ.tagworks.Models.ProductViewModel
import com.devZ.tagworks.R
import com.devZ.tagworks.SharedPrefManager
import kotlinx.coroutines.launch
import java.util.Timer
import kotlin.concurrent.schedule

class Splash : AppCompatActivity() {
    private val productViewModel: ProductViewModel by viewModels()
    private lateinit var mContext: Context
    private lateinit var constants: Constants
    private lateinit var sharedPrefManager: SharedPrefManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        supportActionBar?.hide()
        mContext = this@Splash;
        constants = Constants()
        sharedPrefManager = SharedPrefManager(mContext)
        lifecycleScope.launch {
            try {
                // Get a list of all products from the ViewModel
                productViewModel.getProduct()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val productList = task.result?.toObjects(ProductModel::class.java) ?: emptyList()

                            // Store the product list in SharedPreferences
                            sharedPrefManager.putProductList(productList)

                            // Extract series names from each product and store them in SharedPreferences
                            val seriesNames = productList.mapNotNull { it.series }
                            sharedPrefManager.putSeriesNames(seriesNames)
                            startActivity(Intent(this@Splash, MainActivity::class.java))
                            finish() // Finish the current activity to prevent going back
                        } else {
                            Toast.makeText(this@Splash, "Task not successful", Toast.LENGTH_SHORT).show()
                            // Additional logging
                            println("Task Exception: ${task.exception}")
                        }
                    }
            } catch (e: Exception) {
                // Handle exceptions if any
                Toast.makeText(this@Splash, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                // Additional logging
                println("Exception: ${e.message}")
            }
        }
    }
}
