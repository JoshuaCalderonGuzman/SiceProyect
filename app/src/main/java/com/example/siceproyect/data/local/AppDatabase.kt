package com.example.siceproyect.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.siceproject.data.local.dao.CalifFinalDao
import com.example.siceproject.data.local.dao.CalifUnidadesDao
import com.example.siceproject.data.local.dao.CargaDao
import com.example.siceproject.data.local.dao.KardexDao
import com.example.siceproject.data.local.entities.CalifFinalEntity
import com.example.siceproject.data.local.entities.CalifUnidadesEntity
import com.example.siceproject.data.local.entities.CargaEntity
import com.example.siceproject.data.local.entities.KardexEntity
import com.example.siceproyect.data.local.dao.AlumnoDao
import com.example.siceproyect.data.local.entity.AlumnoEntity

@Database(
    entities = [
        AlumnoEntity::class,
        CargaEntity::class,
        KardexEntity::class,
        CalifFinalEntity::class,
        CalifUnidadesEntity::class
    ],
    version = 2
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun alumnoDao(): AlumnoDao
    abstract fun cargaDao(): CargaDao
    abstract fun kardexDao(): KardexDao
    abstract fun califFinalDao(): CalifFinalDao
    abstract fun califUnidadesDao(): CalifUnidadesDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "sice_database"
                )
                    .fallbackToDestructiveMigration() // IMPORTANTE
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}
