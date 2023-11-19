package com.example.reciclerviewconretrofit.framework.detail

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.reciclerviewconretrofit.domain.Order

import com.example.reciclerviewconretrofit.framework.main.SwipeGesture
import com.example.recyclerviewenhanced.R
import com.example.recyclerviewenhanced.databinding.ViewOrderBinding

class DetalleAdapter(
    val context: Context,
    val actions: OrderActions,

): ListAdapter<Order, DetalleAdapter.ItemViewholder>(DiffCallback()) {
interface OrderActions {
    fun onDelete(order:Order )
    fun onStartSelectMode(order: Order)
    fun itemHasClicked(order: Order)



}

private var selectedPersonas = mutableSetOf<Order>()

@SuppressLint("NotifyDataSetChanged")
fun startSelectMode() {
    selectedMode = true
    notifyDataSetChanged()
}


@SuppressLint("NotifyDataSetChanged")
fun resetSelectMode() {
    selectedMode = false
    selectedPersonas.clear()
    notifyDataSetChanged()
}

fun setSelectedItems(personasSeleccionadas: List<Order>){
    selectedPersonas.clear()
    selectedPersonas.addAll(personasSeleccionadas)
}

private var selectedMode: Boolean = false

override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewholder {

    return ItemViewholder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.view_order, parent, false)
    )
}



    override fun onBindViewHolder(holder: ItemViewholder, position: Int) = with(holder) {
    val item = getItem(position)
    bind(item)
}


inner class ItemViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val binding = ViewOrderBinding.bind(itemView)

    fun bind(order: Order) {

        itemView.setOnLongClickListener {


            if (!selectedMode) {

                actions.onStartSelectMode(order)

            }
            true
        }
        with(binding) {

            selected.setOnClickListener {
                if (selectedMode) {

                    if (binding.selected.isChecked ) {
                        order.isSelected = true
                        itemView.setBackgroundColor(Color.GREEN)

                        selectedPersonas.add(order)
                    } else {
                        order.isSelected = false
                        itemView.setBackgroundColor(Color.WHITE)
                        selectedPersonas.remove(order)


                    }
                    actions.itemHasClicked(order)
                }
            }

            tvdateTable.text = order.order_date.toString()
            tvId.text = order.order_id.toString()
            if (selectedMode)
                selected.visibility = View.VISIBLE
            else{
                order.isSelected = false
                selected.visibility = View.GONE
            }

            if (selectedPersonas.contains(order)) {
                itemView.setBackgroundColor(Color.GREEN)
                binding.selected.isChecked = true

            } else {
                itemView.setBackgroundColor(Color.WHITE)
                binding.selected.isChecked = false
            }
        }
    }
}

class DiffCallback : DiffUtil.ItemCallback<Order>() {
    override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
        return oldItem.order_id == newItem.order_id
    }

    override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
        return oldItem == newItem
    }

}

val swipeGesture = object : SwipeGesture(context) {


    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        when (direction) {
            ItemTouchHelper.LEFT -> {
                selectedPersonas.remove(currentList[viewHolder.adapterPosition])
                actions.onDelete(currentList[viewHolder.adapterPosition])
                if (selectedMode)
                    actions.itemHasClicked(currentList[viewHolder.adapterPosition])
            }
        }

    }
}
}