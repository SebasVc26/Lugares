package com.lugares_j.repository

import androidx.lifecycle.LiveData
import com.lugares_j.data.LugarDao
import com.lugares_j.model.Lugar


class LugarRepositry(private val lugarDao: LugarDao) {
    suspend fun saveLugar(lugar: Lugar){
        if(lugar.id==0){ //Es un lugar nuevo
            lugarDao.addLugar(lugar)
        }else{ //Es un lugar registrado
            lugarDao.updateLugar(lugar)
        }
    }

    suspend fun deleteLugar(lugar: Lugar){
        if(lugar.id!=0){ //Si el id tiene un valor, lo intento eliminar
            lugarDao.deleteLugar(lugar)
        }
    }

    val getLugares  : LiveData<List<Lugar>> = lugarDao.getLugares()
}