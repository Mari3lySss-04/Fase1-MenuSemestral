package com.example.menusemestral


import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.menusemestral.models.CarritoItem
import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.platform.LocalContext
import com.google.gson.Gson

/**

 muestra el historial de compras del usuario.

Requiere una lista historial con la información ya cargada  desde una llamada API en otra parte).

Muestra tarjetas con el número de compra, fecha, total y productos asociados.
 */


@Composable
fun HistorialScreen(
    historial: List<CompraConDetalles>,
    onNavChange: (String) -> Unit
) {



    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(selected = false, onClick = { onNavChange("home") }, icon = {
                    Icon(Icons.Default.Home, contentDescription = "Inicio")
                }, label = { Text("Inicio") })
                NavigationBarItem(selected = false, onClick = { onNavChange("carrito") }, icon = {
                    Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito")
                }, label = { Text("Carrito") })
                NavigationBarItem(selected = true, onClick = { onNavChange("historial") }, icon = {
                    Icon(Icons.Default.History, contentDescription = "Historial")
                }, label = { Text("Historial") })
                NavigationBarItem(selected = false, onClick = { onNavChange("perfil") }, icon = {
                    Icon(Icons.Default.Person, contentDescription = "Perfil")
                }, label = { Text("Perfil") })
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("Historial", style = MaterialTheme.typography.titleMedium)

            LazyColumn {
                items(historial) { compra ->
                    Card(modifier = Modifier.padding(vertical = 8.dp)) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Compra #${compra.id} - ${compra.fecha}", style = MaterialTheme.typography.titleSmall)
                            Text("Total: $${"%.2f".format(compra.total)}")

                            compra.productos.forEach {
                                Text("• ${it.nombre} x${it.cantidad}")
                            }
                        }
                    }
                }
            }
        }
    }
}
data class CompraConDetalles(
    val id: Int,
    val fecha: String,
    val total: Double,
    val productos: List<ProductoDetalle>
)

data class ProductoDetalle(
    val nombre: String,
    val cantidad: Int
)

data class HistorialResponse(
    val success: Boolean,
    val historial: List<CompraConDetalles>
)

/**


Pantalla de solo lectura que lista compras anteriores en tarjetas informativas.

Se alimenta con una lista de CompraConDetalles que contiene ID, fecha, total y productos comprados.

No realiza llamadas a la API directamente, pero es ideal para mostrar datos obtenidos externamente.
 */