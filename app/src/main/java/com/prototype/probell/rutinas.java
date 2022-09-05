package com.prototype.probell;

//Esta clase permite hacer uso de los datos extraídos del modulo de rutinas
public class rutinas {
    //Variables para almacenar el ID de cada zona
    String zona1S;
    String zona2S;
    String zona3S;

    //Constructor
    public rutinas() {
        this.zona1S = "0";
        this.zona2S = "0";
        this.zona3S = "0";
    }

    /*Los siguientes métodos colocan o retornan el valor de los atributos */

    public String getZona1S() {
        return zona1S;
    }

    public void setZona1S(String zona1S) {
        this.zona1S = zona1S;
    }

    public String getZona2S() {
        return zona2S;
    }

    public void setZona2S(String zona2S) {
        this.zona2S = zona2S;
    }

    public String getZona3S() {
        return zona3S;
    }

    public void setZona3S(String zona3S) {
        this.zona3S = zona3S;
    }
}


