package com.example.adminfoodie.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.adminfoodie.databinding.DeliveryItemBinding

class DeliveryAdapter(private val customerName : MutableList<String>, private val moneyStatus : MutableList<Boolean>) : RecyclerView.Adapter<DeliveryAdapter.DeliveryViewHolder>() {

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
                if(moneyStatus[position] == true){
                    tvPaymentStatus.text = "Recieved"
                }else{
                    tvPaymentStatus.text = "Not Recieved"                }

                val colorMap = mapOf(
                    true to Color.GREEN, false to Color.RED,"Pending" to Color.parseColor("#FFEFC02E")
                )
                tvPaymentStatus.setTextColor(colorMap[moneyStatus[position]]?:Color.BLACK)
                iconDeliveryStatus.backgroundTintList = ColorStateList.valueOf(colorMap[moneyStatus[position]]?:Color.BLACK)
            }
        }
    }

}