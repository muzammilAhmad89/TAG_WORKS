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





        Timer().schedule(10) {


            lifecycleScope.launch {

                productViewModel.getProduct().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        sharedPrefManager.putProductList(task.result.map { it.toObject(ProductModel::class.java) })
                        startActivity(Intent(this@Splash,MainActivity::class.java))
                    }
                }
                    .addOnFailureListener {
                        Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}