package com.example.adminfoodie.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adminfoodie.databinding.OrderDetailsItemBinding

class OrderDetailsAdapter(
    private val context: Context,
    private var foodNames: ArrayList<String>,
    private var foodPrices: ArrayList<String>,
    private var foodImages: ArrayList<String>,
    private var foodQuantities: ArrayList<Int>
): RecyclerView.Adapter<OrderDetailsAdapter.OrderDetilsViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDetilsViewHolder {
        val binding = OrderDetailsItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return OrderDetilsViewHolder(binding)
    }



    override fun onBindViewHolder(holder: OrderDetilsViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = foodNames.size


    inner class OrderDetilsViewHolder(private val binding: OrderDetailsItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                tvFoodNameOrderDetailsItem.text = foodNames[position]
                tvQuantityNumberOrderDetailsItem.text = foodQuantities[position].toString()
                tvPriceOrderDetailsItem.text = foodPrices[position]
                val uriString = foodImages[position]
                val uri = Uri.parse(uriString)
                Glide.with(context)
                    .load(uri)
                    .into(imgOrderDetailsItem)
            }
        }

    }

}