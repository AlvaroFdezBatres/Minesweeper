package com.example.buscaminas;

import android.widget.ImageView;

public class Mina {
    private String nombre;
    private int id;

    @Override
    public String toString() {
        return "Mina{" +
                "nombre='" + nombre + '\'' +
                ", id=" + id +
                '}';
    }

    public Mina(){}

    public Mina(String nombre, int id) {
        this.nombre = nombre;
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
