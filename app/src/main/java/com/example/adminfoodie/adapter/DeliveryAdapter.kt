package com.example.adminfoodie.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.adminfoodie.databinding.DeliveryItemBinding

class DeliveryAdapter(private val customerName : ArrayList<String>, private val deliveryStatus : ArrayList<String>) : RecyclerView.Adapter<DeliveryAdapter.DeliveryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeliveryViewHolder {

        val binding = DeliveryItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return DeliveryViewHolder(binding)
    }


    override fun onBindViewHolder(holder: DeliveryViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = customerName.size

    inner class DeliveryViewHolder(private val binding: DeliveryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                tvCustomerName.text = customerName[position]
                tvPaymentStatus.text = deliveryStatus[position]
                val colorMap = mapOf(
                    "Recieved" to Color.GREEN, "Not Recieved" to Color.RED,"Pending" to Color.parseColor("#FFEFC02E")
                )
                tvPaymentStatus.setTextColor(colorMap[deliveryStatus[position]]?:Color.BLACK)
                iconDeliveryStatus.backgroundTintList = ColorStateList.valueOf(colorMap[deliveryStatus[position]]?:Color.BLACK)
            }
        }
    }

}