package com.example.siceproyect.data.local.dao

import androidx.room.*
import com.example.siceproyect.data.local.entity.AlumnoEntity

@Dao
interface AlumnoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(alumno: AlumnoEntity)

    @Query("SELECT * FROM alumno WHERE control = :control LIMIT 1")
    suspend fun get(control: String): AlumnoEntity?
}
