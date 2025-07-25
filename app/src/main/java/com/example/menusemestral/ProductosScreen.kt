package com.example.menusemestral

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.menusemestral.models.CarritoItem
import com.example.menusemestral.models.Producto
import com.example.menusemestral.models.ProductosResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

    Log.d("TOKEN_ENVIADO desde screenproductos", token)

    var productos by remember { mutableStateOf(listOf<Producto>()) }

    LaunchedEffect(Unit) {
        RetrofitInstance.api.getProductos(token).enqueue(object : Callback<ProductosResponse> {
            override fun onResponse(call: Call<ProductosResponse>, response: Response<ProductosResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        if (it.success) productos = it.productos
                        else Toast.makeText(context, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Token inválido desde ProductoScreen", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ProductosResponse>, t: Throwable) {
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // 🎨 Paleta de colores
    val pastelTop = Color(0xFFFFD6A5)
    val pastelBottom = Color(0xFFFFF5BA)
    val mintGreen = Color(0xFFA0E7E5)
    val textDark = Color(0xFF333333)
    val navBarChocolate = Color(0xFF5D4037)

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                        Text("Menú", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = textDark)
                    }
                },
                navigationIcon = {
                    Text(
                        text = nombreUsuario,
                        modifier = Modifier.padding(start = 16.dp),
                        color = textDark
                    )
                },
                actions = {
                    IconButton(onClick = onGoToCarrito) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito", tint = textDark)
                    }
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Logout", tint = textDark)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = pastelTop
                )
            )
        },
        bottomBar = {
            NavigationBar(containerColor = navBarChocolate) {
                NavigationBarItem(
                    selected = true,
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        listOf(pastelTop, pastelBottom)
                    )
                )
                .padding(paddingValues)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(productos) { producto ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(260.dp),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(6.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Image(
                                    painter = rememberAsyncImagePainter(producto.imagen),
                                    contentDescription = producto.nombre,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(100.dp),
                                    contentScale = ContentScale.Crop
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = producto.nombre,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = textDark
                                )
                                Text(
                                    text = "$${producto.precio}",
                                    fontSize = 14.sp,
                                    color = textDark
                                )
                            }

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
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp),
                                shape = RoundedCornerShape(50),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = mintGreen,
                                    contentColor = textDark
                                )
                            ) {
                                Text("Agregar")
                            }
                        }
                    }
                }
            }
        }
    }
}
