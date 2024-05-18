package com.example.petdaycarekotandfire

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class EditarMascota : AppCompatActivity() {
    lateinit var editTextNombre : EditText
    lateinit var spinnerEspecie: Spinner
    lateinit var editTextRaza : EditText
    lateinit var editTextDuenno : EditText
    lateinit var editTextNumberPeso : EditText
    lateinit var spinnerSexo : Spinner
    lateinit var imageViewMascota : ImageView
    var arrayEspecie = ArrayList<String>()
    var arraySexo = ArrayList<String>()
    val db = Firebase.firestore
    
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_mascota)

        editTextNombre = findViewById(R.id.editTextNombreAlta)
        spinnerEspecie = findViewById(R.id.spinnerEspecieAlta)
        editTextRaza = findViewById(R.id.editTextRazaAlta)
        editTextDuenno = findViewById(R.id.editTextDuennoAlta)
        editTextNumberPeso = findViewById(R.id.editTextNumberPesoAlta)
        spinnerSexo = findViewById(R.id.spinnerSexoAlta)
        imageViewMascota = findViewById(R.id.imageViewMascota)

        val intent = intent
        val mascotaRecibida = intent.getSerializableExtra("mascota") as Mascota
        val idMascota = intent.getStringExtra("id") as String
        editarMascota(mascotaRecibida)

        var imageButtonModificar = findViewById<ImageButton>(R.id.ImageButtonModificar)
        var imageButtonEliminar = findViewById<ImageButton>(R.id.ImageButtonEliminar)

        imageButtonModificar.setOnClickListener {
            if(camposCompletos()){
                if(hayCambios(mascotaRecibida)){
                    modificarMascota(idMascota)
                }else{
                    alert("No se puede modificar.\nNo se ha modificado ningún dato de la mascota")
                }
            }else{
                alert("No se puede modificar. Hay campos vacíos o erróneos")
            }
        }

        imageButtonEliminar.setOnClickListener {
            eliminarMascota(idMascota)
        }
    }

    fun editarMascota(mascotaRecibida : Mascota){
        if(mascotaRecibida.sexo == "Macho"){
            arraySexo.add("Macho")
            arraySexo.add("Hembra")
        }else{
            arraySexo.add("Hembra")
            arraySexo.add("Macho")
        }

        when(mascotaRecibida.especie){
            "Perro" -> {
                arrayEspecie.add("Perro")
                arrayEspecie.add("Gato")
                arrayEspecie.add("Pajaro")
                imageViewMascota.setImageResource(R.drawable.perrocara)
            }
            "Gato" ->{
                arrayEspecie.add("Gato")
                arrayEspecie.add("Perro")
                arrayEspecie.add("Pajaro")
                imageViewMascota.setImageResource(R.drawable.gatocara)
            }
            "Pajaro"->{
                arrayEspecie.add("Pajaro")
                arrayEspecie.add("Perro")
                arrayEspecie.add("Gato")
                imageViewMascota.setImageResource(R.drawable.pajarocara)
            }
        }
        val adapter = ArrayAdapter(this,android.R.layout.simple_spinner_item,arraySexo)
        spinnerSexo.adapter = adapter
        val adapter2 = ArrayAdapter(this,android.R.layout.simple_spinner_item,arrayEspecie)
        spinnerEspecie.adapter = adapter2
        spinnerSexo.textAlignment = View.TEXT_ALIGNMENT_CENTER
        spinnerEspecie.textAlignment = View.TEXT_ALIGNMENT_CENTER
        editTextNombre.text.append(mascotaRecibida.nombre)
        editTextRaza.text.append(mascotaRecibida.raza)
        editTextDuenno.text.append(mascotaRecibida.duenno)
        editTextNumberPeso.text.append(mascotaRecibida.peso.toString())

    }

    fun alert (mensaje : String){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage(mensaje)
        builder.setPositiveButton("Aceptar",null)
        val alerta = builder.create()
        alerta.show()
    }

    fun eliminarMascota (id : String){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Eliminar")
        builder.setMessage("¿Estás seguro de eliminar la mascota?")
        builder.setPositiveButton("Aceptar", DialogInterface.OnClickListener { dialog, which ->
            eliminar(id)
        })
        builder.setNegativeButton("Cancelar",null)
        val alerta = builder.create()
        alerta.show()
    }

    fun modificarMascota (id : String){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Modificar")
        builder.setMessage("¿Estás seguro de modificar los datos de la mascota?")
        builder.setPositiveButton("Aceptar", DialogInterface.OnClickListener { dialog, which ->
            modificar(id)
        })
        builder.setNegativeButton("Cancelar",null)
        val alerta = builder.create()
        alerta.show()
    }

    fun eliminar(id : String){
        db.collection("Mascotas").document(id)
            .delete().addOnCompleteListener {
                if(it.isSuccessful){
                    Toast.makeText(applicationContext,"Se ha eliminado la mascota correctamente", Toast.LENGTH_SHORT).show()
                    var i = Intent(applicationContext,ListadoDeMascotas::class.java)
                    startActivity(i)
                }else{
                    alert("No ha sido posible eliminar la mascota.")
                }
            }
    }

    fun modificar(id : String){
        db.collection("Mascotas").document(id)
            .update("nombre",editTextNombre.text.toString(),"especie", contenidoSpinner(spinnerEspecie,arrayEspecie),
                "raza", editTextRaza.text.toString(),"sexo", contenidoSpinner(spinnerSexo,arraySexo),
                "peso", editTextNumberPeso.text.toString().toDouble(), "duenno", editTextDuenno.text.toString())
            .addOnCompleteListener {
                if(it.isSuccessful){
                    Toast.makeText(applicationContext,"Se ha modificado correctamente", Toast.LENGTH_SHORT).show()
                    var i = Intent(applicationContext,ListadoDeMascotas::class.java)
                    startActivity(i)
                }else{
                    alert("No ha sido posible modificar la mascota.")
                }
            }
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

    fun camposCompletos() : Boolean{
        return (editTextCompletado(editTextNombre) && editTextCompletado(editTextRaza) && editNumberCompletado(editTextNumberPeso)
                && editTextCompletado(editTextDuenno))
    }

    fun hayCambios(mascota : Mascota) : Boolean{
        return (spinnerSexo.selectedItemPosition !=-1 && spinnerSexo.selectedItemPosition != 0)
            || mascota.nombre != editTextNombre.text.toString().trim()
            || (spinnerEspecie.selectedItemPosition !=-1 && spinnerEspecie.selectedItemPosition != 0)
            || mascota.duenno != editTextDuenno.text.toString().trim()
            || mascota.peso != editTextNumberPeso.text.toString().trim().toDouble()
            || mascota.raza != editTextRaza.text.toString().trim()

    }

    fun contenidoSpinner(e : Spinner, a : ArrayList<String>) : String {
        if(e.selectedItemPosition == -1){
            return a[0];
        }else {
            return a[e.selectedItemPosition];
        }
    }
}