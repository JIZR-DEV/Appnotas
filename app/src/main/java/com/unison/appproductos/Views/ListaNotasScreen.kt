// ListaNotasScreen.kt
package com.unison.appproductos.Views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.unison.appproductos.Models.Nota
import com.unison.appproductos.ViewModel.NotaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaNotasScreen(navController: NavController, viewModel: NotaViewModel) {
    val estadoUi by viewModel.estadoUi.collectAsState()

    // Obtener el SnackbarHostState
    val snackbarHostState = remember { SnackbarHostState() }

    // Observar mensajes de la ViewModel
    LaunchedEffect(key1 = estadoUi.mensaje) {
        estadoUi.mensaje?.let { mensaje ->
            snackbarHostState.showSnackbar(mensaje)
            viewModel.clearMensaje() // Limpiar el mensaje después de mostrar el Snackbar
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("formularioNota") },
                containerColor = Color(0xFFA5D6A7)
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Agregar Nota")
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }, // Agregar SnackbarHost
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Color(0xFFF5F5F5)) // Fondo gris claro para un mejor contraste
            ) {
                // Recuadro Amarillo para Encabezado y Divisor sin Márgenes Externos
                Surface(
                    modifier = Modifier
                        .fillMaxWidth(),
                    color = Color.Yellow,
                    shape = MaterialTheme.shapes.medium,
                    tonalElevation = 4.dp
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp), // Padding interno para el contenido
                        contentAlignment = Alignment.Center
                    ) {
                        // Flecha de Retroceso alineada a la izquierda
                        IconButton(
                            onClick = {
                                navController.navigate("inicio") {
                                    // Opcional: Evitar múltiples instancias de la pantalla de inicio
                                    popUpTo("inicio") { inclusive = true }
                                }
                            },
                            modifier = Modifier.align(Alignment.CenterStart)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Volver",
                                tint = Color.Black
                            )
                        }

                        // Título "Mis Notas" centrado
                        Text(
                            text = "Mis Notas",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp)) // Espacio entre el recuadro y el contenido

                // Verificar si hay notas
                if (estadoUi.notas.isEmpty()) {
                    // Mostrar mensaje para agregar una nota en Negritas
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No hay notas. ¡Agrega una nueva nota!",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold // Texto en Negritas
                            ),
                            color = Color.Black
                        )
                    }
                } else {
                    // Lista de notas
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        items(estadoUi.notas) { nota ->
                            ItemNota(
                                nota = nota,
                                onClick = {
                                    navController.navigate("detalleNota/${nota.id}")
                                }
                            )
                        }
                    }
                }
            }
        })
}


@Composable
fun ItemNota(nota: Nota, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .height(100.dp) // Altura ajustada para acomodar título y contenido
            .clickable { onClick() },
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp) // Añadir sombra
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(android.graphics.Color.parseColor(nota.colorFondo)))
                .border( // Añadir borde para resaltar la nota
                    width = 1.dp,
                    color = if (obtenerColorContraste(nota.colorFondo) == Color.White) Color.Black else Color.White,
                    shape = MaterialTheme.shapes.medium
                )
        ) {
            // Imagen de fondo
            if (nota.uriImagen != null) {
                Image(
                    painter = rememberAsyncImagePainter(model = nota.uriImagen),
                    contentDescription = "Imagen de fondo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                // Título
                Text(
                    text = nota.titulo,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = obtenerColorContraste(nota.colorFondo),
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(4.dp))
                // Vista previa del contenido de la nota
                Text(
                    text = nota.contenido,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    color = obtenerColorContraste(nota.colorFondo)
                )
            }
        }
    }
}

/**
 * Función para determinar el color de contraste basado en el color de fondo.
 */
fun obtenerColorContraste(colorFondo: String): Color {
    val color = android.graphics.Color.parseColor(colorFondo)
    val r = android.graphics.Color.red(color)
    val g = android.graphics.Color.green(color)
    val b = android.graphics.Color.blue(color)
    val luminancia = (0.299 * r + 0.587 * g + 0.114 * b) / 255
    return if (luminancia > 0.5) Color.Black else Color.White
}
