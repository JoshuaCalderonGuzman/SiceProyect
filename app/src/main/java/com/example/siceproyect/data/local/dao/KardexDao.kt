package com.example.siceproject.data.local.dao

import androidx.room.*
import com.example.siceproject.data.local.entities.KardexEntity

@Dao
interface KardexDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(kardex: KardexEntity)

    @Query("SELECT * FROM kardex WHERE control = :control")
    suspend fun getByControl(control: String): KardexEntity?
}