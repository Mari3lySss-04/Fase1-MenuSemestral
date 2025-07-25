package com.example.menusemestral.models


data class Producto(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val imagen: String
)

/**

Modelo de datos que representa un producto individual.

Contiene ID, nombre, descripción, precio e imagen.

Usado en la visualización de productos y al agregarlos al carrito.
 */
/**

Data class que representa un producto ofrecido por la aplicación.

Es utilizado tanto para mostrar en la UI como para enviar información en compras.
 */