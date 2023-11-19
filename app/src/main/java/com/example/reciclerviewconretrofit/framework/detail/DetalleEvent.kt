package com.example.reciclerviewconretrofit.framework.detail

import com.example.reciclerviewconretrofit.domain.Order

sealed class DetalleEvent {
    class DeleteOrdersSeleccionadas() : DetalleEvent()
    class DeleteOrder(val order: Order) :  DetalleEvent()
    class SeleccionaOrder(val order: Order) :  DetalleEvent()
    class InsertOrder(val order: Order) :  DetalleEvent()
    class GetOrderPorId(val id: Int) :  DetalleEvent()

    class GetOrdersFiltrados(val filtro: String) :  DetalleEvent()
    object GetOrders :  DetalleEvent()
    object ErrorVisto :  DetalleEvent()

    object StartSelectMode:  DetalleEvent()
    object ResetSelectMode:  DetalleEvent()
}