package com.example.reciclerviewconretrofit.framework.main

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.reciclerviewconretrofit.domain.Customer
import com.example.recyclerviewenhanced.R
import com.example.recyclerviewenhanced.databinding.ViewCustomerBinding

class UserAdapter(
    val context: Context,
    val actions: CustomerActions

) : ListAdapter<Customer, UserAdapter.ItemViewholder>(DiffCallback()) {

    interface CustomerActions {
        fun onDelete(customer: Customer)
        fun onStartSelectMode()
        fun itemHasClicked(customer: Customer)
        fun isItemSelected(customer: Customer): Boolean


    }

    private var selectedMode: Boolean = false
    private var selectedItem = mutableListOf<Customer>()
    fun resetSelectMode() {
        selectedMode = false

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewholder {
        return ItemViewholder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.view_customer, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ItemViewholder, position: Int) = with(holder) {
        val item = getItem(position)
        bind(item)
    }


    inner class ItemViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ViewCustomerBinding.bind(itemView)

        fun bind(item: Customer) {

            itemView.setOnLongClickListener {
                if (!selectedMode) {
                    selectedMode = true
                    actions.onStartSelectMode()
                    item.isSelected = true
                    binding.selected.isChecked = true
                    selectedItem.add(item)
                    //notifyDataSetChanged()
                    notifyItemChanged(adapterPosition)
                }
                true
            }
            with(binding) {
                selected.setOnClickListener {
                    if (selectedMode) {

                        if (binding.selected.isChecked) {
                            item.isSelected = true
                            itemView.setBackgroundColor(Color.GREEN)
                            //binding.selected.isChecked = true
                            //notifyItemChanged(adapterPosition)
                            selectedItem.add(item)
                        } else {
                            item.isSelected = false
                            itemView.setBackgroundColor(Color.WHITE)
                            selectedItem.remove(item)
                            //binding.selected.isChecked = false
                            //notifyItemChanged(adapterPosition)

                        }
                        actions.itemHasClicked(item)
                    }
                }

                tvNombre.text = item.name
                tvId.text = item.id.toString()
                if (selectedMode)
                    selected.visibility = View.VISIBLE
                else {
                    item.isSelected = false
                    selected.visibility = View.GONE
                }

                if (actions.isItemSelected(item)) {
                    itemView.setBackgroundColor(Color.GREEN)
                    binding.selected.isChecked = true
                    //selected.visibility = View.VISIBLE
                } else {
                    itemView.setBackgroundColor(Color.WHITE)
                    binding.selected.isChecked = false
                }
            }
        }

    }

    class DiffCallback : DiffUtil.ItemCallback<Customer>() {
        override fun areItemsTheSame(oldItem: Customer, newItem: Customer): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Customer, newItem: Customer): Boolean {
            return oldItem == newItem
        }
    }
    val swipeGesture = object : SwipeGesture(context){
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            //if (!selectedMode) {
            when (direction) {
                ItemTouchHelper.LEFT -> {
                    selectedItem.remove(currentList[viewHolder.adapterPosition])
                    actions.onDelete(currentList[viewHolder.adapterPosition])
                    if (selectedMode)
                        actions.itemHasClicked(currentList[viewHolder.adapterPosition])
                }
            }
            //}
        }
    }
}