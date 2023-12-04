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
    private var productList: List<ProductModel>,
    private var productListener: ProductListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_SERIES = 1
    private val TYPE_PRODUCT = 2

    // Assuming productList is sorted by series
    private val seriesPositions: MutableMap<String, Int> = mutableMapOf()

    inner class SeriesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val seriesName: TextView = itemView.findViewById(R.id.seriesName)
    }

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val section: TextView = itemView.findViewById(R.id.Psections)
        val colors: TextView = itemView.findViewById(R.id.Pcolors)
        val discount: TextView = itemView.findViewById(R.id.Pdiscount)
        val card: CardView = itemView.findViewById(R.id.rateCard)
        val series: TextView = itemView.findViewById(R.id.seriesName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_SERIES -> SeriesViewHolder(inflater.inflate(R.layout.series_item, parent, false))
            TYPE_PRODUCT -> ProductViewHolder(
                inflater.inflate(
                    R.layout.customer_item,
                    parent,
                    false
                )
            )

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (seriesPositions.containsKey(productList[position].series)) {
            TYPE_SERIES
        } else {
            TYPE_PRODUCT
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val product = productList[position]

        when (holder) {
            is SeriesViewHolder -> {
                holder.seriesName.text = product.series
            }

            is ProductViewHolder -> {
                holder.section.text = product.section
                holder.colors.text = product.color
                holder.discount.text = product.maxDiscount
                holder.series.text = product.series

                holder.card.setOnClickListener {
                    productListener.onProductClicked(product)
                }
            }
        }
    }

    fun updateSeriesPositions() {
        seriesPositions.clear()
        for (i in productList.indices) {
            if (!seriesPositions.containsKey(productList[i].series)) {
                seriesPositions[productList[i].series] = i
            }
        }
    }

    interface ProductListener {
        fun onProductClicked(productModel: ProductModel)
    }
}
//
//    Function to set
//    data
//    fun setData(products: List<ProductModel>) {
//        productList = products
//        notifyDataSetChanged()
//        // Debug toast messages
//        if (productList.isEmpty()) {
//            Toast.makeText(context, "Adapter received an empty list", Toast.LENGTH_SHORT).show()
//        } else {
//            Toast.makeText(
//                context,
//                "Adapter received a list with size: ${productList.size}",
//                Toast.LENGTH_SHORT
//            ).show()
//        }
//    }
//
//}