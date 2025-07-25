package com.example.menusemestral.models



/**
 * Un producto con cantidad en el carrito.
 */
data class CarritoItem(
    val producto: Producto,
    var cantidad: Int = 1
)
