// CustomerAdapter.kt
package com.devZ.tagworks.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.devZ.tagworks.Models.ProductModel
import com.devZ.tagworks.R

class CustomerAdapter(
    private val context: Context,
    private var seriesAndProducts: List<Any>,
    private var productListener: ProductListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    inner class SeriesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val seriesTextView: TextView = itemView.findViewById(R.id.seriesName)
    }

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val section: TextView = itemView.findViewById(R.id.Psections)
        val colors: TextView = itemView.findViewById(R.id.Pcolors)
        val discount: TextView = itemView.findViewById(R.id.Pdiscount)
        val card: CardView = itemView.findViewById(R.id.rateCard)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.series_item -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.series_item, parent, false)
                SeriesViewHolder(view)
            }
            R.layout.customer_item -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.customer_item, parent, false)
                ProductViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid viewType: $viewType")
        }
    }

    override fun getItemCount(): Int {
        return seriesAndProducts.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = seriesAndProducts[position]
        Toast.makeText(context, ""+seriesAndProducts.size, Toast.LENGTH_SHORT).show()
        when (holder) {
            is SeriesViewHolder -> {
                val seriesName = item as String
                holder.seriesTextView.text = seriesName
            }
            is ProductViewHolder -> {
                val product = item as ProductModel
                holder.section.text = product.itemCode
                holder.colors.text = product.color
                holder.discount.text = product.maxDiscount

                // Set click listener for product item
                holder.card.setOnClickListener { productListener.onProductClicked(product) }
            }
            else -> throw IllegalArgumentException("Invalid ViewHolder type")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (seriesAndProducts[position]) {
            is String -> R.layout.series_item
            is ProductModel -> R.layout.customer_item
            else -> throw IllegalArgumentException("Invalid item type")
        }
    }

    interface ProductListener {
        fun onProductClicked(productModel: ProductModel)
    }
}
