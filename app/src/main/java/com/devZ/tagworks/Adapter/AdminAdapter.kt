package com.devZ.tagworks.Adapter
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.devZ.tagworks.Models.ProductModel
import com.devZ.tagworks.R

class AdminAdapter (
    private val context: Context,
    private var seriesAndProducts: List<Any>,
    private var productEditListener: ProductEditListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    inner class SeriesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val seriesTextView: TextView = itemView.findViewById(R.id.seriesName)
    }

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val section: TextView = itemView.findViewById(R.id.Asections)
        val colors: TextView = itemView.findViewById(R.id.Acolors)
        val discount: TextView = itemView.findViewById(R.id.Adiscount)
        val rate: TextView = itemView.findViewById(R.id.Arate)
        val card:CardView=itemView.findViewById(R.id.rateCard)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.series_item -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.series_item, parent, false)
                SeriesViewHolder(view)
            }
            R.layout.customer_item -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.admin_fragment, parent, false)
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
                holder.rate.text = product.rate
                holder.card.setOnClickListener { productEditListener.selectedItemToEdit(product) }

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

    interface ProductEditListener {
        fun selectedItemToEdit(productModel:ProductModel)
    }


}