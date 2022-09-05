package com.prototype.probell;

//Esta clase permite hacer uso de los datos extraídos del modulo de rutinas
public class velocidad {

    //Variables para almacenar los datos de las velocidades disponibles
    private Integer ID_velocidad;
    private Integer valorV;
    private String valorVString;

    //Constructor
    public velocidad() {
        this.ID_velocidad = ID_velocidad;
        this.valorV = valorV;
        this.valorVString = valorVString;
    }

    /*Los siguientes métodos colocan o retornan el valor de los atributos */

    public Integer getID_velocidad() {
        return ID_velocidad;
    }

    public void setID_velocidad(Integer ID_velocidad) {
        this.ID_velocidad = ID_velocidad;
    }

    public Integer getValorV() {
        return valorV;
    }

    public void setValorV(Integer valorV) {
        this.valorV = valorV;
    }

    public String getValorVString() {
        return valorVString;
    }

    public void setValorVString(String valorVString) {
        this.valorVString = valorVString;
    }

}
