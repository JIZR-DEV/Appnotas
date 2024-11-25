// NotaViewModel.kt
package com.unison.appproductos.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.unison.appproductos.Models.Nota
import com.unison.appproductos.room.NotaRepositorio
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

// Clase para el estado de la UI de notas
data class EstadoUiNota(
    val notas: List<Nota> = emptyList(),
    val fondoSeleccionado: String = "#FFFFFF", // Color por defecto
    val mensaje: String? = null // Mensaje para Snackbar
)

class NotaViewModel(private val repositorio: NotaRepositorio) : ViewModel() {

    private val _estadoUi = MutableStateFlow(EstadoUiNota())
    val estadoUi: StateFlow<EstadoUiNota> = _estadoUi.asStateFlow()

    init {
        // Observa las notas desde el repositorio
        viewModelScope.launch {
            repositorio.todasLasNotas.collect { notas ->
                _estadoUi.update { it.copy(notas = notas) }
            }
        }
    }

    // Agrega una nueva nota al repositorio
    fun agregarNota(nota: Nota) {
        viewModelScope.launch {
            repositorio.insertar(nota)
            setMensaje("Nota agregada")
        }
    }

    // Actualiza una nota existente en el repositorio
    fun actualizarNota(nota: Nota) {
        viewModelScope.launch {
            repositorio.actualizar(nota)
            setMensaje("Nota actualizada")
        }
    }

    // Elimina una nota del repositorio
    fun eliminarNota(nota: Nota) {
        viewModelScope.launch {
            repositorio.eliminar(nota)
            setMensaje("Nota eliminada")
        }
    }

    // Cambia el fondo de la aplicación
    fun cambiarFondoApp(color: String) {
        _estadoUi.update { it.copy(fondoSeleccionado = color) }
    }

    // Establece un mensaje para la UI
    fun setMensaje(mensaje: String) {
        _estadoUi.update { it.copy(mensaje = mensaje) }
    }

    // Limpia el mensaje actual de la UI
    fun clearMensaje() {
        _estadoUi.update { it.copy(mensaje = null) }
    }
}

// Factory para crear instancias de NotaViewModel
class NotaViewModelFactory(private val repositorio: NotaRepositorio) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NotaViewModel(repositorio) as T
        }
        throw IllegalArgumentException("Clase ViewModel desconocida")
    }
}
