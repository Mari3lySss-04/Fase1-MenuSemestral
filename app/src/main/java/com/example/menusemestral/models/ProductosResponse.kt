package com.example.menusemestral.models


data class ProductosResponse(
    val success: Boolean,
    val message: String,
    val productos: List<Producto>
)
/**

Modelo de respuesta usado para mapear el JSON devuelto por la API al obtener productos.

Contiene una lista de Producto, un mensaje y un booleano de éxito.
 */