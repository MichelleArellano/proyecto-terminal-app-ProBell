package com.prototype.probell;

import android.util.Log;
import android.widget.Toast;

public class tramaRutina extends trama {

    int hemisferioAfectado;
    String tipoCZ;
    String velocidad;
    String tiempo;
    String zonaT1,zonaT2,zonaT3;
    String crc;
    String tramaFinal;

    //Constructor
    public tramaRutina() {
        tipoCZ = "1";
        hemisferioAfectado = 0;
        zonaT1 = "";
        zonaT2 = "";
        zonaT3 = "";
        tiempo = "";
        velocidad = "";
        crc = null;
        tramaFinal=null;
    }

    //Crea la trama de rutina
    public void crearTramaRutina(String tipoRutina,String hemisferio,int zona1,int zona2,int zona3,String tiemZonas,int velZonas){
        String tramaMensaje;

        if(tipoRutina == "1"){
            if(zona1 == 1 && zona2 == 2 && zona3== 3){
                setZonaT1("01"); setZonaT2("10"); setZonaT3("11");
            }
            opcionVelocidad(velZonas);
            setTiempo(decimalBinario(tiemZonas));
            tramaMensaje=getTipoCZ()+tipoRutina+hemisferio+getZonaT1()+getZonaT2()+getZonaT3()+getTiempo()+getVelocidad();
            setCrc(calculoCRC(tramaMensaje));
            setTramaFinal(tramaMensaje+getCrc());
            Log.i("Trama final",getTramaFinal());
        }else if(tipoRutina == "0"){
            zonasSeleccionadas(zona1,zona2,zona3);
            opcionVelocidad(velZonas);
            setTiempo(decimalBinario(tiemZonas));
            tramaMensaje=getTipoCZ()+tipoRutina+hemisferio+getZonaT1()+getZonaT2()+getZonaT3()+getTiempo()+getVelocidad();
            setCrc(calculoCRC(tramaMensaje));
            setTramaFinal(tramaMensaje+getCrc());
            Log.i("Trama final",getTramaFinal());
        }
    }

    //Identifica la velocidad y asigna su valor correspondiente
    private void opcionVelocidad(int velZonas) {
        Log.i("Log entra velocidad","vel"+ velZonas);
        switch (velZonas){
            case 1:
                setVelocidad("001"); //Velocidad 1
                break;
            case 2:
                setVelocidad("010"); //Velocidad 2
                break;
            case 3:
                setVelocidad("011"); //Velocidad 3
                break;
            case 4:
                setVelocidad("100"); //Velocidad 4
                break;
            case 5:
                setVelocidad("101"); //Velocidad 5
                break;
            case 6:
                setVelocidad("110"); //Velocidad 6
                break;
            case 7:
                setVelocidad("111"); //Velocidad 7
                break;
            default:
                setVelocidad("0"); //Velocidad no disponible
        }
    }

    //Transforma un numero que ingresar en decimal para convertirlo a binario
    private String decimalBinario(String velocidad){
        String ceros="", binario ="", binario7bits="";
        int longitud = 0; // longitud del string binario
        int largo = 7;
        int cantidad=0;
        binario = Integer.toBinaryString(Integer.parseInt(velocidad));
        Log.i("Binario","binario inicio "+ binario);
        //Toast.makeText(getApplicationContext(),"Tiempo binario "+binario,Toast.LENGTH_SHORT).show(); Integer.toBinaryString(Integer.parseInt(auxSubcadena));

        cantidad = largo - binario.length();

        if (cantidad >= 1)
        {
            for(int i=0;i<cantidad;i++)
            {
                ceros += "0";
            }
            binario7bits = ceros + binario;
            Log.i("Binario","binario final "+ binario7bits);
            return binario7bits;
        }
        else
        {
            return "0";
        }

    }

    //Identifica y asigna los valores correspondientes dependiendo de las zonas
    private void zonasSeleccionadas(int zona1, int zona2, int zona3){
        if(zona1 == 0 && zona2 == 0 && zona3 == 0){
            setZonaT1("00"); setZonaT2("00"); setZonaT3("00");
        }else if(zona1 == 1 && zona2 == 0 && zona3 == 0){
            setZonaT1("01"); setZonaT2("00"); setZonaT3("00");
        }else if(zona1 == 2 && zona2 == 0 && zona3 == 0){
            setZonaT1("00"); setZonaT2("01"); setZonaT3("00");
        }else if(zona1 == 3 && zona2 == 0 && zona3 == 0){
            setZonaT1("00"); setZonaT2("00"); setZonaT3("01");
        }else if(zona1 == 1 && zona2 == 0 && zona3 == 2){
            setZonaT1("01"); setZonaT2("10"); setZonaT3("00");
        }else if(zona1 == 2 && zona2 == 0 && zona3 == 1){
            setZonaT1("10"); setZonaT2("01"); setZonaT3("00");
        }else if(zona1 == 1 && zona2 == 0 && zona3 == 3){
            setZonaT1("01"); setZonaT2("00"); setZonaT3("10");
        }else if(zona1 == 3 && zona2 == 0 && zona3 == 1){
            setZonaT1("10"); setZonaT2("00"); setZonaT3("01");
        }else if(zona1 == 2 && zona2 == 0 && zona3 == 3){
            setZonaT1("00"); setZonaT2("01"); setZonaT3("10");
        }else if(zona1 == 3 && zona2 == 0 && zona3 == 2){
            setZonaT1("00"); setZonaT2("10"); setZonaT3("01");
        }else if(zona1 == 1 && zona2 == 2 && zona3 == 3){
            setZonaT1("01"); setZonaT2("10"); setZonaT3("11");
        }else if(zona1 == 1 && zona2 == 3 && zona3 == 2){
            setZonaT1("01"); setZonaT2("11"); setZonaT3("10");
        }else if(zona1 == 2 && zona2 == 1 && zona3 == 3){
            setZonaT1("10"); setZonaT2("01"); setZonaT3("11");
        }else if(zona1 == 2 && zona2 == 3 && zona3 == 1){
            setZonaT1("11"); setZonaT2("01"); setZonaT3("10");
        }else if(zona1 == 3 && zona2 == 1 && zona3 == 2){
            setZonaT1("10"); setZonaT2("11"); setZonaT3("01");
        }else if(zona1 == 3 && zona2 == 2 && zona3 == 1){
            setZonaT1("11"); setZonaT2("10"); setZonaT3("01");
        }
    }

    /*Los siguientes métodos colocan o retornan el valor de los atributos */

    public String getTipoCZ() {
        return tipoCZ;
    }

    public void setTipoCZ(String tipoCZ) {
        this.tipoCZ = tipoCZ;
    }

    public String getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(String velocidad) {
        this.velocidad = velocidad;
    }

    public String getTiempo() {
        return tiempo;
    }

    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }

    public String getZonaT1() {
        return zonaT1;
    }

    public void setZonaT1(String zonaT1) {
        this.zonaT1 = zonaT1;
    }

    public String getZonaT2() {
        return zonaT2;
    }

    public void setZonaT2(String zonaT2) {
        this.zonaT2 = zonaT2;
    }

    public String getZonaT3() {
        return zonaT3;
    }

    public void setZonaT3(String zonaT3) {
        this.zonaT3 = zonaT3;
    }

    public String getCrc() {
        return crc;
    }

    public void setCrc(String crc) {
        this.crc = crc;
    }

    public String getTramaFinal() {
        return tramaFinal;
    }

    public void setTramaFinal(String tramaFinal) {
        this.tramaFinal = tramaFinal;
    }

}
