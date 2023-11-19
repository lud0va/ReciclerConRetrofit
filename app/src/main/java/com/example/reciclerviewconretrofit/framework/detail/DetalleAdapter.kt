package com.example.reciclerviewconretrofit.framework.detail

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.reciclerviewconretrofit.domain.Customer
import com.example.reciclerviewconretrofit.domain.Order

import com.example.reciclerviewconretrofit.framework.main.SwipeGesture
import com.example.recyclerviewenhanced.R
import com.example.recyclerviewenhanced.databinding.ViewOrderBinding

class DetalleAdapter(
    val context: Context,
    val actions: DetalleAdapter.OrderActions,

): ListAdapter<Order, DetalleAdapter.ItemViewholder>(DetalleAdapter.DiffCallback()) {
interface OrderActions {
    fun onDelete(order:Order )
    fun onStartSelectMode(order: Order)
    fun itemHasClicked(order: Order)



}

private var selectedPersonas = mutableSetOf<Order>()

fun startSelectMode() {
    selectedMode = true
    notifyDataSetChanged()
}


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
//                    selectedMode = true
                actions.onStartSelectMode(order)
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
                        order.isSelected = true
                        itemView.setBackgroundColor(Color.GREEN)
                        //binding.selected.isChecked = true
                        //notifyItemChanged(adapterPosition)
                        selectedPersonas.add(order)
                    } else {
                        order.isSelected = false
                        itemView.setBackgroundColor(Color.WHITE)
                        selectedPersonas.remove(order)
                        //binding.selected.isChecked = false
                        //notifyItemChanged(adapterPosition)

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
                //selected.visibility = View.VISIBLE
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