package com.example.adminfoodie.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.example.adminfoodie.databinding.AllitemItemBinding

class AllItemAdapter(private val menuItemName : ArrayList<String>, private val menuItemPrice : ArrayList<String>, private val menuItemImage : ArrayList<Int>) : RecyclerView.Adapter<AllItemAdapter.AllItemViewHolder>(){

    private val itemQuantities = IntArray(menuItemName.size){1}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllItemViewHolder {
        val binding = AllitemItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return AllItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AllItemViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = menuItemName.size

    inner class AllItemViewHolder (private val binding : AllitemItemBinding) : RecyclerView.ViewHolder(binding.root) {

            fun bind(position: Int){
                binding.apply{
                    val quantity = itemQuantities[position]
                    tvFoodNameAllitemItem.text = menuItemName[position]
                    tvFoodPriceAllitemItem.text = menuItemPrice[position]
                    imgAllitemItem.setImageResource(menuItemImage[position])

                    tvAllitemItemCount.text = quantity.toString()


                    btnMinusAllitemItem.setOnClickListener{
                        decrease(position)
                    }

                    btnPlusAllitemItem.setOnClickListener {
                        increase(position)
                    }

                    btnDeleteAllitemItem.setOnClickListener {
                        val itemPosition = adapterPosition
                        // phle check krega recycler view me iski position hai ya nahi
                        if(itemPosition != RecyclerView.NO_POSITION) {
                            delete(itemPosition)
                        }
                    }



                }

        }


        private fun decrease(position : Int){
            if(itemQuantities[position]>1){
                itemQuantities[position]--
                binding.tvAllitemItemCount.text = itemQuantities[position].toString()
            }
        }

        private fun increase(position : Int){
            if(itemQuantities[position]<10){
                itemQuantities[position]++
                binding.tvAllitemItemCount.text = itemQuantities[position].toString()
            }
        }

        private fun delete(position : Int){
            menuItemName.removeAt(position)
            menuItemPrice.removeAt(position)
            menuItemImage.removeAt(position)
            itemQuantities[position] = itemQuantities[position+1]
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, menuItemName.size)
        }

    }
}