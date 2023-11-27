package com.devZ.tagworks.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.devZ.tagworks.Models.ProductModel
import com.devZ.tagworks.R

class CustomerAdapter(
    private val context: Context,
    private var productList: List<ProductModel>,
    private var productListener:ProductListener
) : RecyclerView.Adapter<CustomerAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val Section: TextView = itemView.findViewById(R.id.Psections)
        val Colors: TextView = itemView.findViewById(R.id.Pcolors)
        val discount: TextView = itemView.findViewById(R.id.Pdiscount)
        val card: CardView = itemView.findViewById(R.id.rateCard)
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
        holder.discount.text=products.maxDiscount

        holder.card.setOnClickListener {
            productListener.onProductClicked(products)
        }
    }
    interface ProductListener {
        fun onProductClicked(productModel: ProductModel)

    }
}