package com.example.adminfoodie.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adminfoodie.databinding.PendingOrdersItemBinding
import com.example.adminfoodie.model.OrderDetails

class PendingOrderAdapter(
    private val context: Context,
    private val orders: MutableList<OrderDetails>,
    private val itemClicked: OnItemClicked
) : RecyclerView.Adapter<PendingOrderAdapter.PendingOrderViewHolder>() {

    interface OnItemClicked {
        fun OnItemClickListener(position: Int)
        fun OnItemAcceptClickListener(position: Int)
        fun OnItemDispatchClickListener(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PendingOrderViewHolder {
        val binding = PendingOrdersItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PendingOrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PendingOrderViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = orders.size

    inner class PendingOrderViewHolder(private val binding: PendingOrdersItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            val order = orders[position]
            binding.apply {
                tvCustomerNamePendingOrdersItem.text = order.userName
                tvTotalAmountPendingOrdersItem.text = order.totalPrice
                val uri = Uri.parse(order.foodImagesOrderDetails?.firstOrNull())
                Glide.with(context)
                    .load(uri)
                    .into(imgPendingOrdersItem)

                btnPendingOrdersItem.apply {
                    text = if (order.orderAccepted == true) "Dispatch" else "Accept"

                    setOnClickListener {
                        if (order.orderAccepted == true) {
                            itemClicked.OnItemDispatchClickListener(position)
                        } else {
                            order.orderAccepted = true
                            itemClicked.OnItemAcceptClickListener(position)
                            notifyItemChanged(position) // Update the button text
                        }
                    }
                }

                itemView.setOnClickListener {
                    itemClicked.OnItemClickListener(position)
                }
            }
        }
    }
}
