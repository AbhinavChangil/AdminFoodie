package com.example.adminfoodie.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adminfoodie.databinding.PendingOrdersItemBinding

class PendingOrderAdapter(
    private val context: Context,
    private val customerName: MutableList<String>,
    private val foodImage: MutableList<String>,
    private val quantity: MutableList<String>,
    private val itemClicked: OnItemClicked
) : RecyclerView.Adapter<PendingOrderAdapter.PendingOrderViewHolder>() {

    interface OnItemClicked{
        fun OnItemClickListener(position: Int)
        fun OnItemAcceptClickListener(position: Int)
        fun OnItemDispatchClickListener(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PendingOrderViewHolder {
        val binding = PendingOrdersItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PendingOrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PendingOrderViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = customerName.size


    inner class PendingOrderViewHolder(private val binding: PendingOrdersItemBinding) : RecyclerView.ViewHolder(binding.root) {

        private var isAccepted = false

        fun bind(position: Int) {
            binding.apply {
                tvCustomerNamePendingOrdersItem.text = customerName[position]
                tvTotalAmountPendingOrdersItem.text = quantity[position]
//                imgPendingOrdersItem.setImageResource(foodImage[position])
                //we will now set image with glide
                val uri = Uri.parse(foodImage[position])
                Glide.with(context)
                    .load(uri)
                    .into(imgPendingOrdersItem)

                btnPendingOrdersItem.apply {

                    if (!isAccepted) {
                        text = "Accept"
                    }else{
                        text = "Dispatch"
                    }

                    setOnClickListener {
                        if (!isAccepted) {
                            text = "Dispatch"
                            isAccepted = true
                            showToast("Order Accepted!")
//                            itemClicked.OnItemAcceptClickListener(position)
                        }else{
                            customerName.removeAt(adapterPosition)
                            notifyItemRemoved(adapterPosition)
                            showToast("Order Disatched!")
//                            itemClicked.OnItemDispatchClickListener(position)

                        }
                    }
                }

                itemView.setOnClickListener {
                    itemClicked.OnItemClickListener(position)
                }
            }

        }

         private fun showToast(message:String){
            Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
        }

    }

}