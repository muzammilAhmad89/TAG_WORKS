package com.devZ.tagworks.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.devZ.tagworks.Constants
import com.devZ.tagworks.Models.ProductModel
import com.devZ.tagworks.Models.ProductViewModel
import com.devZ.tagworks.R
import com.devZ.tagworks.SharedPrefManager
import kotlinx.coroutines.launch

class Splash : AppCompatActivity() {

    private val productViewModel: ProductViewModel by viewModels()
    private lateinit var sharedPrefManager: SharedPrefManager

    companion object {
        const val TIMER_DELAY_MS = 10L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        supportActionBar?.hide()
        sharedPrefManager = SharedPrefManager(this)

        lifecycleScope.launch {
            try {
                // Get a list of all series names from the ViewModel
                val seriesNames = productViewModel.getAllSeriesNames()


                // Process each series name
                for (seriesName in seriesNames) {
                    // Get products for the current series from the ViewModel
                    val productList = productViewModel.getProductsForSeries(seriesName)

                    // Store the series name and product list in SharedPreferences
                    sharedPrefManager.putSeriesName(seriesName)
                    sharedPrefManager.putProductList(seriesName, productList)
                }

                // Start the MainActivity after processing all series names
                startActivity(Intent(this@Splash, MainActivity::class.java))
                finish() // Finish the current activity to prevent going back

            } catch (e: Exception) {
                // Handle exceptions if any
                Toast.makeText(this@Splash, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

