package com.example.petdaycarekotandfire

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class MascotaArrayAdapter(context: Context, vista: Int, val listaMascotas : ArrayList<Mascota>) :
    ArrayAdapter<Mascota>(context , vista, listaMascotas ){

    @SuppressLint("MissingInflatedId")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var inflater = LayoutInflater.from(context)
        var currentMascota = inflater.inflate(R.layout.mascota, null)

        var nombre = currentMascota.findViewById<TextView>(R.id.textViewNombre)
        var sexo = currentMascota.findViewById<TextView>(R.id.textViewGenero)
        var duenno = currentMascota.findViewById<TextView>(R.id.textViewDueño)
        var especie = currentMascota.findViewById<ImageView>(R.id.imageViewMascota)

        nombre.text = listaMascotas.get(position).nombre
        sexo.text = listaMascotas.get(position).sexo
        duenno.text = listaMascotas.get(position).duenno

        when (listaMascotas.get(position).especie){
            "Perro"-> especie.setImageResource(R.drawable.perrocarapueq)
            "Gato" -> especie.setImageResource(R.drawable.gatocarapeq)
            "Pajaro" -> especie.setImageResource(R.drawable.pajarocarapeq)
        }

        return currentMascota
    }
}