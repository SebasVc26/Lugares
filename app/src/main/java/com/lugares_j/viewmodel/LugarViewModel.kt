package com.lugares_j.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.lugares_j.data.LugarDataBase
import com.lugares_j.model.Lugar
import com.lugares_j.repository.LugarRepositry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LugarViewModel(application: Application) : AndroidViewModel(application) {

    private val lugarRepositry: LugarRepositry
    val getLugares: LiveData<List<Lugar>>

    init{
        val lugarDao = LugarDataBase.getDataBase(application).lugarDao()
        lugarRepositry = LugarRepositry(lugarDao)
        getLugares = lugarRepositry.getLugares
    }

    fun saveLugar(lugar: Lugar){
        viewModelScope.launch(Dispatchers.IO) {
            lugarRepositry.saveLugar(lugar)
        }
    }

    fun deleteLugar(lugar: Lugar){
        viewModelScope.launch(Dispatchers.IO) {
            lugarRepositry.deleteLugar(lugar)
        }
    }
}