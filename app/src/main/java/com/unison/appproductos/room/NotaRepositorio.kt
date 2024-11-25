package com.unison.appproductos.room



import  com.unison.appproductos.Models.Nota
import  com.unison.appproductos.room.NotaDao
import kotlinx.coroutines.flow.Flow

class NotaRepositorio(private val notaDao: NotaDao) {
    val todasLasNotas: Flow<List<Nota>> = notaDao.obtenerTodasLasNotas()

    suspend fun insertar(nota: Nota) = notaDao.insertarNota(nota)

    suspend fun actualizar(nota: Nota) = notaDao.actualizarNota(nota)

    suspend fun eliminar(nota: Nota) = notaDao.eliminarNota(nota)
}
