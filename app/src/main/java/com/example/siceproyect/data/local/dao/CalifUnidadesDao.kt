package com.example.siceproject.data.local.dao

import androidx.room.*
import com.example.siceproject.data.local.entities.CalifUnidadesEntity

@Dao
interface CalifUnidadesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(calif: CalifUnidadesEntity)

    @Query("SELECT * FROM calificaciones_unidades WHERE control = :control")
    suspend fun getByControl(control: String): CalifUnidadesEntity?
}