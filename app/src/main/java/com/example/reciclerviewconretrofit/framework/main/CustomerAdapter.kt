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

class CustomerAdapter(
    val context: Context,
    val actions: CustomerActions,
    private val cambiarPantalla: (Int) -> Unit

) : ListAdapter<Customer, CustomerAdapter.ItemViewholder>(DiffCallback()) {

    interface CustomerActions {
        fun onDelete(customer: Customer)
        fun onStartSelectMode(customer: Customer)
        fun itemHasClicked(customer: Customer)



    }

    private var selectedPersonas = mutableSetOf<Customer>()

    fun startSelectMode() {
        selectedMode = true
        notifyDataSetChanged()
    }


    fun resetSelectMode() {
        selectedMode = false
        selectedPersonas.clear()
        notifyDataSetChanged()
    }

    fun setSelectedItems(personasSeleccionadas: List<Customer>){
        selectedPersonas.clear()
        selectedPersonas.addAll(personasSeleccionadas)
    }

    private var selectedMode: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewholder {

        return ItemViewholder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.view_customer, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ItemViewholder, position: Int) = with(holder) {
        val item = getItem(position)
        bind(item, cambiarPantalla)
    }


    inner class ItemViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = ViewCustomerBinding.bind(itemView)

        fun bind(customer: Customer ,cambiarPantalla:(Int)->Unit) {
               itemView.setOnClickListener{

                   cambiarPantalla(customer.id)
               }
            itemView.setOnLongClickListener {


                if (!selectedMode) {
//                    selectedMode = true
                    actions.onStartSelectMode(customer)
//                    item.isSelected = true
//                    binding.selected.isChecked = true
                    //selectedPersonas.add(item)
//                    notifyDataSetChanged()
                    //notifyItemChanged(adapterPosition)
                }
                true
            }
            with(binding) {

                selected.setOnClickListener {
                    if (selectedMode) {

                        if (binding.selected.isChecked ) {
                            customer.isSelected = true
                            itemView.setBackgroundColor(Color.GREEN)
                            //binding.selected.isChecked = true
                            //notifyItemChanged(adapterPosition)
                            selectedPersonas.add(customer)
                        } else {
                            customer.isSelected = false
                            itemView.setBackgroundColor(Color.WHITE)
                            selectedPersonas.remove(customer)
                            //binding.selected.isChecked = false
                            //notifyItemChanged(adapterPosition)

                        }
                        actions.itemHasClicked(customer)
                    }
                }

                tvNombre.text = customer.first_name
                tvId.text = customer.id.toString()
                if (selectedMode)
                    selected.visibility = View.VISIBLE
                else{
                    customer.isSelected = false
                    selected.visibility = View.GONE
                }

                if (selectedPersonas.contains(customer)) {
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

    val swipeGesture = object : SwipeGesture(context) {
//        override fun onMove(
//            recyclerView: RecyclerView,
//            viewHolder: RecyclerView.ViewHolder,
//            target: RecyclerView.ViewHolder
//        ): Boolean {
//            var initPos = viewHolder.adapterPosition
//            var targetPos = target.adapterPosition
//
//            val mutable = currentList.toMutableList()
//            Collections.swap(mutable,initPos,targetPos)
//
//           // this@PersonaAdapter.submitList(mutable)
//
//            return false
//
//        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            //if (!selectedMode) {
            when (direction) {
                ItemTouchHelper.LEFT -> {
                    selectedPersonas.remove(currentList[viewHolder.adapterPosition])
                    actions.onDelete(currentList[viewHolder.adapterPosition])
                    if (selectedMode)
                        actions.itemHasClicked(currentList[viewHolder.adapterPosition])
                }
            }
            //}
        }
    }
}