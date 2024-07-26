package com.example.adminfoodie.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.adminfoodie.databinding.ActivityPendingOrdersBinding
import com.example.adminfoodie.databinding.PendingOrdersItemBinding

class PendingOrderAdapter(private val foodImage : ArrayList<Int>, private val customerName : ArrayList<String>, private val quantity : ArrayList<String>, private val context : Context) : RecyclerView.Adapter<PendingOrderAdapter.PendingOrderViewHolder>() {

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
                tvQuantityNumberPendingOrdersItem.text = quantity[position]
                imgPendingOrdersItem.setImageResource(foodImage[position])

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
                        }else{
                            customerName.removeAt(adapterPosition)
                            notifyItemRemoved(adapterPosition)
                            showToast("Order Disatched!")

                        }



                }


                }
            }

        }

         private fun showToast(message:String){
            Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
        }

    }

}