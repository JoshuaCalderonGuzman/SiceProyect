package com.example.siceproyect.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.siceproyect.data.local.dao.AlumnoDao
import com.example.siceproyect.data.local.entity.AlumnoEntity

@Database(
    entities = [AlumnoEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun alumnoDao(): AlumnoDao
}
