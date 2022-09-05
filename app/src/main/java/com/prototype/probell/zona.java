package com.prototype.probell;

//Esta clase permite hacer uso de los datos extraídos del modulo de rutinas
public class zona {

    Integer idZona;
    String nombreZona;
    String detallesZona;

    //Constructor
    public zona() {
        this.idZona=0;
        this.nombreZona = "";
        this.detallesZona = "";
    }

    /*Los siguientes métodos colocan o retornan el valor de los atributos */
    public String getNombreZona() {
        return nombreZona;
    }

    public void setNombreZona(String nombreZona) {
        this.nombreZona = nombreZona;
    }

    public String getDetallesZona() {
        return detallesZona;
    }

    public void setDetallesZona(String detallesZona) {
        this.detallesZona = detallesZona;
    }

    public Integer getIdZona() {
        return idZona;
    }

    public void setIdZona(Integer idZona) {
        this.idZona = idZona;
    }
}
