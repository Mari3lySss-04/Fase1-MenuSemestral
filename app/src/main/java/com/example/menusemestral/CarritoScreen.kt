package com.example.menusemestral

// CarritoScreen.kt

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

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**

se ven  los productos agregados, calcular el total y confirmar la compra.

Al hacer clic en "Comprar", se genera un JSON con los productos, precios y cantidades, y se envía a la API.

En caso de éxito, se vacía el carrito y se muestra un mensaje.
 */

@Composable
fun CarritoScreen(
    carritoItems: List<CarritoItem>,
    onClearCart: () -> Unit,
    onNavChange: (String) -> Unit
) {
    val context = LocalContext.current
    var total by remember { mutableStateOf(0.0) }
    total = carritoItems.sumOf { it.producto.precio * it.cantidad }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = false,
                    onClick = { onNavChange("home") },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
                    label = { Text("Inicio") }
                )
                NavigationBarItem(
                    selected = true,
                    onClick = { onNavChange("carrito") },
                    icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito") },
                    label = { Text("Carrito") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { onNavChange("historial") },
                    icon = { Icon(Icons.Default.History, contentDescription = "Historial") },
                    label = { Text("Historial") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { onNavChange("perfil") },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
                    label = { Text("Perfil") }
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Column(Modifier.fillMaxSize().padding(16.dp)) {
                LazyColumn {
                    items(carritoItems) { item ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(item.producto.nombre)
                            Text("x${item.cantidad}")
                            Text("$${item.producto.precio * item.cantidad}")
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))
                Text("Total: $${"%.2f".format(total)}", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                Button(onClick = {
                    val token = SessionManager.fetchToken(context).let { if (it.isNotEmpty()) "Bearer $it" else "" }
                    val totalCompra = carritoItems.sumOf { it.producto.precio * it.cantidad }


                    val compraData = mapOf(
                        "total" to totalCompra,
                        "productos" to carritoItems.map {
                            mapOf(
                                "id" to it.producto.id,
                                "precio" to it.producto.precio,
                                "cantidad" to it.cantidad
                            )
                        }
                    )

                    val json = Gson().toJson(compraData)
                    Log.d("DEBUG_COMPRAR", "JSON que se envía: $json")
                    RetrofitInstance.api.realizarCompra(token, compraData)
                        .enqueue(object : Callback<Map<String, Any>> {

                        override fun onResponse(call: Call<Map<String, Any>>, response: Response<Map<String, Any>>) {
                            val raw = response.errorBody()?.string() ?: response.body().toString()
                            Log.d("DEBUG_COMPRAR", "Response: $raw")
                            Log.d("DEBUG_COMPRAR", "Compra data: $compraData")


                            if (response.isSuccessful && response.body()?.get("success") == true) {
                                onClearCart()
                                Toast.makeText(context, "Compra realizada con éxito", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "Error al realizar la compra", Toast.LENGTH_SHORT).show()
                            }
                        }


                        override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {

                            Toast.makeText(context, "Error de carrito: ${t.message}", Toast.LENGTH_SHORT).show()

                        }
                    })
                }, Modifier.fillMaxWidth()) {
                    Text("Comprar")
                }

            }
        }
    }






}
/**



Muestra los productos agregados al carrito en una lista vertical. Calcula el total de la compra y realiza la operación de compra enviando los datos a través de Retrofit.

Usa el token del usuario para autorizar la operación.

También permite navegar entre otras secciones mediante la barra de navegación inferior.
 */