package com.unison.appproductos.room



import androidx.room.*
import kotlinx.coroutines.flow.Flow
import  com.unison.appproductos.Models.Nota

@Dao
interface NotaDao {
    @Query("SELECT * FROM notas ORDER BY id DESC")
    fun obtenerTodasLasNotas(): Flow<List<Nota>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarNota(nota: Nota): Long

    @Update
    suspend fun actualizarNota(nota: Nota)

    @Delete
    suspend fun eliminarNota(nota: Nota)
}
