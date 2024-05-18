package com.example.petdaycarekotandfire

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class AltaMascota : AppCompatActivity() {
    lateinit var editTextNombreAlta : EditText
    lateinit var spinnerEspecieAlta : Spinner
    lateinit var editTextRazaAlta : EditText
    lateinit var editTextNumberPesoAlta : EditText
    lateinit var editTextDuennoAlta : EditText
    lateinit var spinnerSexoAlta : Spinner
    val db = Firebase.firestore

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alta_mascota)
        editTextNombreAlta = findViewById(R.id.editTextNombreAlta)
        spinnerEspecieAlta = findViewById(R.id.spinnerEspecieAlta)
        editTextRazaAlta = findViewById(R.id.editTextRazaAlta)
        editTextNumberPesoAlta = findViewById(R.id.editTextNumberPesoAlta)
        editTextDuennoAlta = findViewById(R.id.editTextDuennoAlta)
        spinnerSexoAlta = findViewById(R.id.spinnerSexoAlta)
        var imageButtonAnnadir = findViewById<ImageButton>(R.id.buttonAnnadir)


        var arraySexo = ArrayList<String>()
        arraySexo.add("- Seleccionar -")
        arraySexo.add("Macho")
        arraySexo.add("Hembra")
        val adapter = ArrayAdapter(this,android.R.layout.simple_spinner_item,arraySexo)
        spinnerSexoAlta.adapter = adapter
        spinnerSexoAlta.textAlignment = View.TEXT_ALIGNMENT_CENTER

        var arrayEspecie = ArrayList<String>()
        arrayEspecie.add("- Seleccionar -")
        arrayEspecie.add("Perro")
        arrayEspecie.add("Gato")
        arrayEspecie.add("Pajaro")
        val adapter2 = ArrayAdapter(this,android.R.layout.simple_spinner_item,arrayEspecie)
        spinnerEspecieAlta.adapter = adapter2
        spinnerEspecieAlta.textAlignment = View.TEXT_ALIGNMENT_CENTER


        imageButtonAnnadir.setOnClickListener {
            if(camposCompletos()){
                annadirMascota()
            }else{
                alert("Hay campos sin completar")
            }
        }

    }

    fun annadirMascota(){
        val mascota = crearMascota()
        db.collection("Mascotas")
            .add(mascota)
            .addOnSuccessListener { documentReference ->
                var toast = Toast.makeText(applicationContext, "Se ha añadido la mascota correctamente",Toast.LENGTH_SHORT)
                toast.show()
                var i = Intent(applicationContext,ListadoDeMascotas::class.java)
                startActivity(i)
            }
            .addOnFailureListener { e ->
                alert("Error al añadir el documento:\n $e")
            }
    }

    fun camposCompletos() : Boolean{
       return editTextCompletado(editTextNombreAlta) && spinnerSeleccionado(spinnerEspecieAlta)
           && editTextCompletado(editTextRazaAlta) && editNumberCompletado(editTextNumberPesoAlta)
           && editTextCompletado(editTextDuennoAlta)  && spinnerSeleccionado(spinnerSexoAlta)
    }

    fun editTextCompletado(e : EditText) : Boolean{
        return !(e.text.toString().trim().isNullOrEmpty() || e.text.toString().trim().isBlank())
    }

    fun editNumberCompletado(e : EditText) : Boolean{
        return !(e.text.toString().trim().isNullOrEmpty() || e.text.toString().trim().isBlank()
                || e.text.toString().trim().endsWith(".",false)
                || e.text.toString().trim().startsWith(".",false)
                || e.text.toString().trim().contentEquals("..",false)
                || e.text.toString().trim().toDouble() == 0.0)
    }

    fun spinnerSeleccionado(s : Spinner) : Boolean{
        return s.selectedItemPosition != -1 && s.selectedItemPosition != 0
    }

    fun crearMascota() : Mascota{
        val sexoAlta = spinnerSexoAlta.selectedItem.toString()
        val nombre = editTextNombreAlta.text.toString().trim()
        val especie = spinnerEspecieAlta.selectedItem.toString()
        val raza = editTextRazaAlta.text.toString().trim()
        val peso = editTextNumberPesoAlta.text.toString().trim().toDouble()
        val duenno = editTextDuennoAlta.text.toString().trim()
        val mascota = Mascota(nombre,especie,raza,peso,sexoAlta,duenno)
        return mascota
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