package com.example.adminfoodie.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adminfoodie.databinding.AllitemItemBinding
import com.example.adminfoodie.model.AllMenu
import com.google.firebase.database.DatabaseReference

//replace varibles in AllItemAdapter(private val menuItemName : ArrayList<String>, private val menuItemPrice : ArrayList<String>, private val menuItemImage : ArrayList<Int>)
class AllItemAdapter(
    private val context: Context,
    private val menuList: ArrayList<AllMenu>,
    databaseReference: DatabaseReference,
    private val onDeleteClickedListener: (position: Int) -> Unit
) : RecyclerView.Adapter<AllItemAdapter.AllItemViewHolder>(){

    private val itemQuantities = IntArray(menuList.size){1}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllItemViewHolder {
        val binding = AllitemItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return AllItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AllItemViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = menuList.size

    inner class AllItemViewHolder (private val binding : AllitemItemBinding) : RecyclerView.ViewHolder(binding.root) {

            fun bind(position: Int){
                binding.apply{
                    val quantity = itemQuantities[position]
                    //create varibales for loading data from firebase .... quantity is already there
                    val menuItem : AllMenu = menuList[position]
                    val uriString = menuItem.foodImage
                    val uri = Uri.parse(uriString)


                    tvFoodNameAllitemItem.text = menuItem.foodName
                    tvFoodPriceAllitemItem.text = menuItem.foodPrice
                    //we will use glide to load image
                    Glide.with(context)
                        .load(uri)
                        .into(imgAllitemItem)

                    tvAllitemItemCount.text = quantity.toString()


                    btnMinusAllitemItem.setOnClickListener{
                        decrease(position)
                    }

                    btnPlusAllitemItem.setOnClickListener {
                        increase(position)
                    }

                    btnDeleteAllitemItem.setOnClickListener {
//                        val itemPosition = adapterPosition
//                        // phle check krega recycler view me iski position hai ya nahi
//                        if(itemPosition != RecyclerView.NO_POSITION) {
//                            delete(itemPosition)
//                        }
                        onDeleteClickedListener(position)
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
            menuList.removeAt(position)
            menuList.removeAt(position)
            menuList.removeAt(position)
            itemQuantities[position] = itemQuantities[position+1]
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, menuList.size)
        }

    }
}