package com.lugares_j.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.ktx.Firebase
import com.lugares_j.model.Lugar

class LugarDao {

    private val coleccion1 = "lugaresApp"
    private val usuario = Firebase.auth.currentUser?.email.toString()
    private val coleccion2 = "misLugares"

    //Contiene la coleccion a la base de datos...
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    init {
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
    }
    //Las funciones de bajo nivel para hacer un CRUD
    // Create Read Update Delete

    fun saveLugar(lugar: Lugar) {
        //Para definir un documento en la nube...
        val documento : DocumentReference

        if (lugar.id.isEmpty()){ //Si esta vacio ... es un nuevo documento
            documento = firestore
                .collection(coleccion1)
                .document(usuario)
                .collection(coleccion2)
                .document()
            lugar.id = documento.id
        } else { //Si el id tiene algo... entonces se va a modificar ese documento (lugar)
            documento = firestore
                .collection(coleccion1)
                .document(usuario)
                .collection(coleccion2)
                .document(lugar.id)
        }

        //Ahora se modifica o crea el documento
        documento.set(lugar)
            .addOnSuccessListener {
                Log.d("saveLugar", "lugar creado/actualizado")
            }
            .addOnCanceledListener {
                Log.e("saveLugar", "Lugar NO creado/actualizado")
            }
    }

    fun deleteLugar(lugar: Lugar) {
        //Se valida si el lugar tiene id para poder borrarlo
        if (lugar.id.isNotEmpty()){ //Si esta vacio ... es un nuevo documento
            firestore
                .collection(coleccion1)
                .document(usuario)
                .collection(coleccion2)
                .document(lugar.id)
                .delete()
                .addOnSuccessListener {
                    Log.d("deleteLugar", "lugar eliminado/actualizado")
                }
                .addOnCanceledListener {
                    Log.e("saveLugar", "Lugar NO eliminado/actualizado")
                }
        }
    }

    fun getLugares() : MutableLiveData<List<Lugar>> {
        val listaLugares = MutableLiveData<List<Lugar>>()
        firestore
            .collection(coleccion1)
            .document(usuario)
            .collection(coleccion2)
            .addSnapshotListener{instantanea, e ->
                if (e != null){ //Se dio un error capturando la imagen de la info
                    return@addSnapshotListener
                }
                //Si estamos aca, no hubo error
                if (instantanea != null){ //Si se pudo recuperar la info
                    val lista = ArrayList<Lugar>()
                    //Se recorre la instantanea documento por documento, convirtiendolo en Lugar y agregandolo a la lista
                    instantanea.documents.forEach{
                        val lugar = it.toObject(Lugar::class.java)
                        if (lugar != null){ //Si se pudo conventir el documento en un lugar
                            lista.add(lugar) //Se agrega el lugar a la lista
                        }
                    }

                    listaLugares.value = lista
                }
            }

        return listaLugares
    }
}