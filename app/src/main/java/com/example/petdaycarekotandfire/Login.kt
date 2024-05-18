package com.example.petdaycarekotandfire

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {
    lateinit var mail : EditText
    lateinit var passwd : EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mail = findViewById(R.id.editTextTextEmailAddress)
        passwd = findViewById(R.id.editTextTextPassword)
        var regist = findViewById<Button>(R.id.buttonRegist)
        var login = findViewById<Button>(R.id.buttonAccess)

        regist.setOnClickListener {
            registrar()
        }

        login.setOnClickListener {
            acceder()
        }
    }

    fun registrar(){
        var mailext = mail.text.toString()
        var psswd = passwd.text.toString()
        if(mailext.isEmpty()== false && mailext.isBlank() == false && psswd.isEmpty() == false && psswd.isBlank() ==false){
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(mailext.toString(),psswd.toString()).addOnCompleteListener {
                if(it.isSuccessful){
                    Toast.makeText(applicationContext,"Se ha registrado correctamente.",Toast.LENGTH_SHORT).show()
                    val i = Intent(applicationContext,ListadoDeMascotas::class.java)
                    startActivity(i)
                }else{
                    alert("No ha sido posible el registro")
                }
            }
        }
    }

    fun acceder(){
        var mailext = mail.text.toString().trim()
        var psswd = passwd.text.toString().trim()
        // mail y paswd
        if(mailext.isEmpty()== false && mailext.isBlank() == false && psswd.isEmpty() == false && psswd.isBlank() ==false){
            FirebaseAuth.getInstance().signInWithEmailAndPassword(mailext.toString(),psswd.toString()).addOnCompleteListener {
                if(it.isSuccessful){
                    val i = Intent(applicationContext,ListadoDeMascotas::class.java)
                    startActivity(i)
                }else{
                    alert("No ha sido posible el acceso")
                }

            }
        }
    }

    fun alert (mensaje : String){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage(mensaje)
        builder.setPositiveButton("Aceptar",null)
        val alerta = builder.create()
        alerta.show()
    }
}