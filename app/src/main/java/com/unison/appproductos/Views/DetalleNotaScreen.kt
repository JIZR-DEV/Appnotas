// DetalleNotaScreen.kt
package com.unison.appproductos.Views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.unison.appproductos.ViewModel.NotaViewModel
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleNotaScreen(navController: NavController, viewModel: NotaViewModel, notaId: Int) {
    val estadoUi by viewModel.estadoUi.collectAsState()
    val nota = estadoUi.notas.find { it.id == notaId }

    // Estado para controlar la visibilidad del diálogo de confirmación
    var showDeleteDialog by remember { mutableStateOf(false) }

    // Obtener el SnackbarHostState
    val snackbarHostState = remember { SnackbarHostState() }

    // Observar mensajes de la ViewModel
    LaunchedEffect(key1 = estadoUi.mensaje) {
        estadoUi.mensaje?.let { mensaje ->
            snackbarHostState.showSnackbar(mensaje)
            viewModel.clearMensaje() // Limpiar el mensaje después de mostrar el Snackbar
            if (mensaje == "Nota eliminada" || mensaje == "Nota actualizada") {
                navController.navigate("listaNotas") {
                    popUpTo("listaNotas") { inclusive = true }
                    launchSingleTop = true
                }
            }
        }
    }

    // Redirigir automáticamente a ListaNotasScreen si la nota no se encuentra
    if (nota == null) {
        LaunchedEffect(Unit) {
            navController.navigate("listaNotas") {
                popUpTo("listaNotas") { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    // Solo mostrar el contenido si la nota existe
    if (nota != null) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Información de la Nota") },
                    navigationIcon = {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Volver")
                        }
                    }
                )
            },
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) }, // Agregar SnackbarHost
            content = { padding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    // Imagen de fondo si existe
                    if (nota.uriImagen != null) {
                        Image(
                            painter = rememberAsyncImagePainter(model = File(nota.uriImagen)),
                            contentDescription = "Imagen de fondo",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        // Color de fondo si no hay imagen
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color(android.graphics.Color.parseColor(nota.colorFondo ?: "#FFFFFF")))
                        )
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        OutlinedTextField(
                            value = nota.titulo,
                            onValueChange = {},
                            label = { Text("Título") },
                            readOnly = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp)
                        )

                        OutlinedTextField(
                            value = nota.contenido,
                            onValueChange = {},
                            label = { Text("Descripción") },
                            readOnly = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                                .padding(bottom = 16.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Button(
                                onClick = {
                                    navController.navigate("formularioNota?notaId=$notaId")
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF81D4FA)),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Modificar")
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Button(
                                onClick = { showDeleteDialog = true },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE57373)),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Eliminar")
                            }
                        }
                    }

                    // Diálogo de confirmación para eliminar
                    if (showDeleteDialog) {
                        AlertDialog(
                            onDismissRequest = { showDeleteDialog = false },
                            title = { Text(text = "Confirmar Eliminación") },
                            text = { Text("¿Estás seguro de que deseas eliminar esta nota? Esta acción no se puede deshacer.") },
                            confirmButton = {
                                TextButton(onClick = {
                                    viewModel.eliminarNota(nota)
                                    showDeleteDialog = false
                                }) {
                                    Text("Eliminar")
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = { showDeleteDialog = false }) {
                                    Text("Cancelar")
                                }
                            }
                        )
                    }
                }
            }
        )
    }
}
