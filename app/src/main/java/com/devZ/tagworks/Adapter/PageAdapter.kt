package com.devZ.tagworks.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.devZ.tagworks.fragments.AdminFragment
import com.devZ.tagworks.fragments.FragmentUser

class PageAdapter(fm:FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return 2;
    }

    override fun getItem(position: Int): Fragment {
         when(position) {
            0 -> {
                return FragmentUser()
            }

            else -> {
                return AdminFragment()
            }

        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when(position) {
            0 -> {
                return "Customers"
            }
            1 -> {
                return "Admin"
            }

        }
        return super.getPageTitle(position)
    }

}
