package com.devZ.tagworks.ui
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import com.devZ.tagworks.R
import com.devZ.tagworks.databinding.ActivityMainBinding
import com.devZ.tagworks.Adapter.PageAdapter
import com.google.android.material.tabs.TabLayout
import com.google.firebase.FirebaseApp

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewPager: ViewPager
    private lateinit var mContext: Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        mContext=this@MainActivity
        FirebaseApp.initializeApp(this)

        // Initialize your ViewPager
        viewPager = findViewById(R.id.viewPager)
        binding.cvCustomer.setBackgroundColor(resources.getColor(R.color.white, null))


        // Set up a PageChangeListener
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                // This method is called when the pager is scrolling
            }

            override fun onPageSelected(position: Int) {
                // This method will be invoked when a new page becomes selected

                // Now, you can determine which fragment is opened by checking the position
                when (position) {
                    0 -> {
                        // Fragment at position 0 is currently visible/opened
                        with(binding) {
                            cvCustomer.setBackgroundColor(resources.getColor(R.color.white, null))
                            cvAdmin.setBackgroundColor(resources.getColor(R.color.skyBlueShade, null))
                        }
                    }
                    1 -> {

                        // Fragment at position 1 is currently visible/opened
                        binding.run {
                            cvAdmin.setBackgroundColor(resources.getColor(R.color.white, null))
                            cvCustomer.setBackgroundColor(resources.getColor(R.color.skyBlueShade, null))
                        }
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
                // This method is called when the scroll state changes
            }
        })


        viewPager.adapter = PageAdapter(supportFragmentManager)

        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        tabLayout.setupWithViewPager(viewPager)

    }

}