package com.devZ.tagworks.Adapter

import android.graphics.Color
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.devZ.tagworks.Ui.AdminFragment
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
                val spannable = SpannableString("Customers")
                spannable.setSpan(ForegroundColorSpan(Color.WHITE), 0, spannable.length, 0)

                return spannable            }
            1 -> {

                // Customize the title and text color for the second page (position 1)
                val spannable = SpannableString("Admin")
                spannable.setSpan(ForegroundColorSpan(Color.WHITE), 0, spannable.length, 0)

                return spannable
            }

        }
        return super.getPageTitle(position)
    }

}
