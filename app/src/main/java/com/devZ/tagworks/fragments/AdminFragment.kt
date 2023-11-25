package com.devZ.tagworks.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.devZ.tagworks.R
import com.devZ.tagworks.databinding.FragmentAdminBinding

class AdminFragment : Fragment() {
    private lateinit var binding: FragmentAdminBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdminBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.fabAdd.setOnClickListener {
            showSelectngDialog()
        }
        return view
    }

    private fun showSelectngDialog() {
        val builder = AlertDialog.Builder(requireContext()) // or context
        val dialogView = layoutInflater.inflate(R.layout.selectingmodule, null)
        builder.setView(dialogView)
        val aluminium = dialogView.findViewById<TextView>(R.id.Admin)
        val glass = dialogView.findViewById<TextView>(R.id.customer)
    }
}