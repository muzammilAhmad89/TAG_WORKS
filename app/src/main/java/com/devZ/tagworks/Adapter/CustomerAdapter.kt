package com.devZ.tagworks.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.devZ.tagworks.Models.ProductModel
import com.devZ.tagworks.R

class CustomerAdapter(
    private val context: Context,
    private var productList: List<ProductModel>
) : RecyclerView.Adapter<CustomerAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val Section: TextView = itemView.findViewById(R.id.Psections)
        val Colors: TextView = itemView.findViewById(R.id.Pcolors)
        val Rate: TextView = itemView.findViewById(R.id.Rate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.customer_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {

            return productList.size

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val products=productList[position]

        holder.Section.text=products.section
        holder.Colors.text=products.color
    }
}