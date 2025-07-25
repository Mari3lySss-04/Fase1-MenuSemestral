package com.example.menusemestral

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.menusemestral.models.CarritoItem
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoScreen(
    carritoItems: List<CarritoItem>,
    onClearCart: () -> Unit,
    onNavChange: (String) -> Unit
) {
    val context = LocalContext.current
    var total by remember { mutableStateOf(0.0) }
    total = carritoItems.sumOf { it.producto.precio * it.cantidad }

    // 🎨 Paleta de colores
    val fondoPastel = Color(0xFFFFF5BA)
    val navBarChocolate = Color(0xFF5D4037)
    val textoOscuro = Color(0xFF333333)
    val verdeMenta = Color(0xFFA0E7E5)

    Scaffold(
        containerColor = Color.Transparent,
        bottomBar = {
            NavigationBar(containerColor = navBarChocolate) {
                NavigationBarItem(
                    selected = false,
                    onClick = { onNavChange("home") },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Inicio", tint = Color.White) },
                    label = { Text("Inicio", color = Color.White) }
                )
                NavigationBarItem(
                    selected = true,
                    onClick = { onNavChange("carrito") },
                    icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito", tint = Color.White) },
                    label = { Text("Carrito", color = Color.White) }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { onNavChange("historial") },
                    icon = { Icon(Icons.Default.History, contentDescription = "Historial", tint = Color.White) },
                    label = { Text("Historial", color = Color.White) }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { onNavChange("perfil") },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Perfil", tint = Color.White) },
                    label = { Text("Perfil", color = Color.White) }
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(fondoPastel)
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // 🔹 Título de la pantalla
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Carrito",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = textoOscuro
                )
            }


            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(carritoItems) { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = item.producto.nombre,
                                color = textoOscuro,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Divider(color = Color(0xFFEEEEEE), thickness = 1.dp)
                            Text(
                                text = "Cantidad: ${item.cantidad}",
                                color = textoOscuro,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "Subtotal: $${"%.2f".format(item.producto.precio * item.cantidad)}",
                                color = textoOscuro,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Total: $${"%.2f".format(total)}",
                style = MaterialTheme.typography.titleMedium,
                color = textoOscuro
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
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
                            override fun onResponse(
                                call: Call<Map<String, Any>>,
                                response: Response<Map<String, Any>>
                            ) {
                                val raw = response.errorBody()?.string() ?: response.body().toString()
                                Log.d("DEBUG_COMPRAR", "Response: $raw")
                                if (response.isSuccessful && response.body()?.get("success") == true) {
                                    onClearCart()
                                    Toast.makeText(context, "Compra realizada con éxito", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, "Error al realizar la compra", Toast.LENGTH_SHORT).show()
                                }
                            }

                            override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                            }
                        })
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = verdeMenta,
                    contentColor = textoOscuro
                )
            ) {
                Text("Comprar")
            }
        }
    }
}
