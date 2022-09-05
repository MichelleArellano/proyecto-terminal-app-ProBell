package com.prototype.probell;

//Esta clase permite hacer uso de los datos extraídos del modulo de rutinas
public class tiempo {

    //Variables para almacenar los datos de los tiempos disponibles
    private Integer ID_tiempo;
    private Integer valorSegundos;
    private String valorMinutos;

    //Constructor
    public tiempo() {
        this.ID_tiempo = ID_tiempo;
        this.valorSegundos = valorSegundos;
        this.valorMinutos = valorMinutos;
    }

    /*Los siguientes métodos colocan o retornan el valor de los atributos */

    public Integer getID_tiempo() {
        return ID_tiempo;
    }

    public void setID_tiempo(Integer ID_tiempo) {
        this.ID_tiempo = ID_tiempo;
    }

    public Integer getValorSegundos() {
        return valorSegundos;
    }

    public void setValorSegundos(Integer valorSegundos) {
        this.valorSegundos = valorSegundos;
    }

    public String getValorMinutos() {
        return valorMinutos;
    }

    public void setValorMinutos(String valorMinutos) {
        this.valorMinutos = valorMinutos;
    }

}
