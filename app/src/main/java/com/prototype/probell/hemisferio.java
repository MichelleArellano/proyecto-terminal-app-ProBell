package com.prototype.probell;

//Esta clase permite hacer uso de los datos extraídos del modulo de rutinas
public class hemisferio {
    //Variables con los 2 hemisferios que hay
    String izquierdo;
    String derecho;

    //Constructor
    public hemisferio() {
        this.izquierdo = "";
        this.derecho = "";
    }

    /*Los siguientes métodos colocan o retornan el valor de los atributos */

    public String getIzquierdo() {
        return izquierdo;
    }

    public void setIzquierdo(String izquierdo) {
        this.izquierdo = izquierdo;
    }

    public String getDerecho() {
        return derecho;
    }

    public void setDerecho(String derecho) {
        this.derecho = derecho;
    }
}
