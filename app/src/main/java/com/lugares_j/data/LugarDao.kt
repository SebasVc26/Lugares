package com.lugares_j.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.lugares_j.model.Lugar

@Dao
interface LugarDao {
    //Las funciones de bajo nivel para hacer un CRUD
    // Create Read Update Delete

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addLugar(lugar: Lugar)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updateLugar(lugar: Lugar)

    @Delete
    suspend fun deleteLugar(lugar: Lugar)

    @Query("SELECT * FROM LUGAR")
    fun getLugares() : LiveData<List<Lugar>>
}