package com.lugares_j

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lugares_j.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    //Definicion del objeto para hacer la autenticacion
    private lateinit var auth : FirebaseAuth
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Se inicializa el objeto para manejar las vistas
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Se inicializa Firebase para usarse en el App
        //Se asigna el objeto auth para hacer autenticación
        FirebaseApp.initializeApp(this)
        auth = Firebase.auth

        binding.btRegister.setOnClickListener { haceRegistro() }
        binding.btLogin.setOnClickListener { haceLogin() }
    }

    private fun haceRegistro() {
        //Recuperamos la informacion que ingreso el usuario
        val email = binding.etEmail.text.toString()
        val clave = binding.etClave.text.toString()

        //Se llama ala funcion para crear un usuario en Firebase (correo/contraseña)
        auth.createUserWithEmailAndPassword(email,clave)
            .addOnCompleteListener(this) { task ->
                var user:FirebaseUser? = null
                if (task.isSuccessful){//Si pudo crear el usuario
                    Log.d("Autenticando", "usuario creado")
                     user = auth.currentUser //Recupero la info del usuario creado
                } else{
                    Log.d("Autenticando", "Error creando usuario")
                }
                Actualiza(user)
            }
    }



    private fun haceLogin() {
        //Recuperamos la informacion que ingreso el usuario
        val email = binding.etEmail.text.toString()
        val clave = binding.etClave.text.toString()

        //Se llama ala funcion para crear un usuario en Firebase (correo/contraseña)
        auth.signInWithEmailAndPassword(email,clave)
            .addOnCompleteListener(this) { task ->
                var user:FirebaseUser? = null
                if (task.isSuccessful){//Si pudo crear el usuario
                    Log.d("Autenticando", "usuario autenticado")
                    user = auth.currentUser //Recupero la info del usuario creado
                } else{
                    Log.d("Autenticando", "Error autenticando usuario")
                }
                Actualiza(user)
            }
    }

    private fun Actualiza(user: FirebaseUser?) {
        //Si hay un usuario definido, se pasa a la pantalla principal (Activity)
        if (user!=null){
            //Se pasa a la otra pantalla
            val intent = Intent(this, Principal::class.java)
            startActivity(intent)
        }
    }

    //Se ejecuta cuando el app aparezca en la pantalla...
    public override fun onStart(){
        super.onStart()
        val usuario = auth.currentUser
        Actualiza(usuario)
    }
}