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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistorialScreen(
    historial: List<CompraConDetalles>,
    onNavChange: (String) -> Unit
) {
    val pastelTop = Color(0xFFFFD6A5)
    val pastelBottom = Color(0xFFFFF5BA)
    val textDark = Color(0xFF333333)
    val navBarChocolate = Color(0xFF5D4037)

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
                    selected = false,
                    onClick = { onNavChange("carrito") },
                    icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito", tint = Color.White) },
                    label = { Text("Carrito", color = Color.White) }
                )
                NavigationBarItem(
                    selected = true,
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
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(listOf(pastelTop, pastelBottom))
                )
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                "Historial de Compras",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = textDark
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(historial) { compra ->
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "Compra #${compra.id}",
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 16.sp,
                                        color = textDark
                                    )
                                    Text(
                                        text = compra.fecha,
                                        fontSize = 14.sp,
                                        color = Color.Gray
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .background(Color(0xFF81C784), shape = RoundedCornerShape(8.dp))
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "Orden hecha",
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                "Total: $${"%.2f".format(compra.total)}",
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp,
                                color = textDark
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                "Productos comprados:",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = textDark
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Column {
                                compra.productos.forEach {
                                    Text(
                                        text = "• ${it.nombre} x${it.cantidad}",
                                        fontSize = 14.sp,
                                        color = textDark
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
