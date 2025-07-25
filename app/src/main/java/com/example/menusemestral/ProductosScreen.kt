package com.example.menusemestral

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.menusemestral.models.Producto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import androidx.compose.material.icons.filled.History
import com.example.menusemestral.models.CarritoItem
import com.example.menusemestral.models.ProductosResponse

/**

Pantalla principal que muestra una cuadrícula de productos traídos desde la API usando Retrofit.

El usuario puede agregar productos al carrito, ver su nombre y navegar a otras secciones como el historial o el perfil.

Utiliza SessionManager para validar el token y proteger el acceso a productos.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductosScreen(
    nombreUsuario: String,
    onLogout: () -> Unit,
    onGoToCarrito: () -> Unit,
    onNavChange: (String) -> Unit,
    carritoItems: MutableList<CarritoItem>
) {
    val context = LocalContext.current
    val token = SessionManager.fetchToken(context).let { if (it.isNotEmpty()) "Bearer $it" else "" }

    // Log para ver si se está obteniendo correctamente el token
    Log.d("TOKEN_ENVIADO desde screenproductos", token ?: "No hay token")


    var productos by remember { mutableStateOf(listOf<Producto>()) }

    LaunchedEffect(Unit) {
        RetrofitInstance.api.getProductos(token).enqueue(object : Callback<ProductosResponse> {
            override fun onResponse(call: Call<ProductosResponse>, response: Response<ProductosResponse>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    Log.d("API_RESPONSE", "Respuesta completa: ${body.toString()}")
                    if (body != null && body.success) {
                        //Toast.makeText(context, "Token VALIDO desde ProductoScreen", Toast.LENGTH_SHORT).show()
                        productos = body.productos
                    } else {
                        Toast.makeText(context, "Error token es $token: ${body?.message ?: "Respuesta vacía"}", Toast.LENGTH_SHORT).show()
                        Toast.makeText(context, "Error: ${body?.message ?: "Respuesta vacía"}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Token inválido desde ProductoScreen", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ProductosResponse>, t: Throwable) {
                Toast.makeText(context, "Error token es $token}", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "Error en Screen: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Menu") },
                navigationIcon = {
                    TextButton(onClick = {}) {
                        Text(nombreUsuario, color = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = onGoToCarrito) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito")
                    }
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Logout")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = true,
                    onClick = { onNavChange("home") },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
                    label = { Text("Inicio") }
                )
                NavigationBarItem(
                    selected = false,
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
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .padding(paddingValues)
                .padding(8.dp)
        ) {
            items(productos) { producto ->
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {

                        Image(
                            painter = rememberAsyncImagePainter(producto.imagen),
                            contentDescription = producto.nombre,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(producto.nombre, style = MaterialTheme.typography.titleMedium)
                        Text(
                            "$${producto.precio}",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Start
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                val existente = carritoItems.find { it.producto.id == producto.id }
                                if (existente != null) {
                                    existente.cantidad += 1
                                } else {
                                    carritoItems.add(CarritoItem(producto, 1))
                                }

                                Toast.makeText(
                                    context,
                                    "${producto.nombre} agregado al carrito",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Agregar")
                        }

                    }
                }
            }
        }
    }
}

/**


Pantalla que lista productos usando LazyVerticalGrid. Se conecta a la API para obtener los productos mediante un token JWT.

Los productos se muestran con nombre, precio e imagen. El usuario puede agregarlos al carrito compartido.

Incluye navegación inferior y permite cerrar sesión o ir al carrito.
 */