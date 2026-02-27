package com.example.siceproject.data.local.dao

import androidx.room.*
import com.example.siceproject.data.local.entities.CalifFinalEntity

@Dao
interface CalifFinalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(calif: CalifFinalEntity)

    @Query("SELECT * FROM calificaciones_finales WHERE control = :control")
    suspend fun getByControl(control: String): CalifFinalEntity?
}