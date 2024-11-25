package com.unison.appproductos.room



import androidx.room.Database
import androidx.room.RoomDatabase
import  com.unison.appproductos.Models.Nota

@Database(entities = [Nota::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun notaDao(): NotaDao
}
