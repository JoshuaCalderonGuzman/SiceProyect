package com.example.siceproject.data.local.dao

import androidx.room.*
import com.example.siceproject.data.local.entities.CargaEntity

@Dao
interface CargaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(carga: CargaEntity)

    @Query("SELECT * FROM carga WHERE control = :control")
    suspend fun getByControl(control: String): CargaEntity?
}