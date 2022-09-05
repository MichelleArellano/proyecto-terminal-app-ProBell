package com.prototype.probell;

import android.util.Log;

public class tramaControl extends trama{

    String opcion;
    String crc;
    String tramaFinal;
    CharSequence tramaRecibida;
    int caso;

    //Constructor
    public tramaControl() {
        opcion = null;
        crc = null;
        tramaFinal=null;
        tramaRecibida = null;
        caso = 0;
    }

    //Crea la trama de Control
    public void crearTramaControl(String tipo, int identificador){
        setOpcion(identificarOpcion(identificador));
        setCrc(calculoCRC(getOpcion()));
        setTramaFinal(tipo + getOpcion() + getCrc());
        Log.i("Crear trama de control", "CRC " + getCrc());
        Log.i("Crear trama de control", "Trama Final " + getTramaFinal());
    }

    //Identifica las acciones que se requieren realizar
    public String identificarOpcion(int opc){

        switch (opc){
            case 1:
                setOpcion("0100");//Solicitar rutina de prueba --> envío
                break;
            case 2:
                setOpcion("0101");//Componentes en buen estado --> recibo
            case 3:
                setOpcion("0001");//Iniciando rutina --> recibo
                break;
            case 4:
                setOpcion("0000");//Detener rutina --> envío
                break;
            case 5:
                setOpcion("0111");//Fin de rutina --> recibo
                break;
            case 6:
                setOpcion("1000"); //Solicitar retransmisión, temporizadores hayan expirado
                break;
            default:
                Log.i("Valor opcion ", String.valueOf(getOpcion()));
        }
        return opcion;
    }

    //Interpretar la trama que envía el modulo de control
    public int recepcionTrama(CharSequence rtATramaRecibida){
        int residuo,caso = 0;
        setTramaRecibida(rtATramaRecibida);

        if(getTramaRecibida() != null) {
            int longitudTrama = getTramaRecibida().length();
            CharSequence subSecuencia;
            Log.i("Interpretación", "Trama recibida longitud" + longitudTrama);
            Log.i("Interpretación", "Trama recibida " + getTramaRecibida());

            if (getTramaRecibida().length() == 20) {
                getTramaRecibida();
                //Log.i("Interpretación", "Trama recibida longitud " + getTramaRecibida().length());
                subSecuencia = getTramaRecibida().subSequence(0, 20);
                Log.i("Interpretación", "Subsecuencia " + subSecuencia);
                caso = interpretacionTrama(subSecuencia);
                Log.i("Interpretación", "Caso " + caso);
            } else if (getTramaRecibida().length() > 20) {
                getTramaRecibida();
                residuo = longitudTrama % 20;
                if (residuo == 0) {
                    //Log.i("Interpretación", getTramaRecibida() + " es multiplo de 21 " + getTramaRecibida().length());
                    subSecuencia = getTramaRecibida().subSequence(getTramaRecibida().length() - 20, getTramaRecibida().length());
                    Log.i("Interpretación", "Subsecuencia " + subSecuencia);
                    caso = interpretacionTrama(subSecuencia);
                    Log.i("Interpretación", "Caso " + caso);
                }
            }
        }
        return caso;
    }

    //Identifica la acción que se requere realizar
    public int interpretacionTrama(CharSequence trama){
        int caso;
        String tramaR = trama.toString();

        if(tramaR.startsWith("00101") || tramaR.endsWith("0010001")){//Componentes en buen estado 00101000000000010001
            caso =1;
        }else if(tramaR.startsWith("00111") || tramaR.endsWith("0011011")){//Fin de rutina 00111000000000011011
            caso =2;
        }else if(tramaR.startsWith("000001") || tramaR.endsWith("0000101")) { // Rutina iniciada 00001000000000000101
            caso =3;
        }else if(tramaR.startsWith("01000") || tramaR.endsWith("0101000")){//Retransmisión 01000000000000101000
            caso=4;
        }else{
            caso=0;
            Log.i("Interpretación","Caso "+caso);
        }
        Log.i("Interpretación","Caso "+caso);
        return caso;
    }

    /*Los siguientes métodos colocan o retornan el valor de los atributos */

    public CharSequence getTramaRecibida() {
        return tramaRecibida;
    }

    public void setTramaRecibida(CharSequence tramaRecibida) {
        this.tramaRecibida = tramaRecibida;
    }

    public String getCrc() {
        return crc;
    }

    public void setCrc(String crc) {
        this.crc = crc;
    }

    public String getOpcion() {
        return opcion;
    }

    public void setOpcion(String opcion) {
        this.opcion = opcion;
    }

    public String getTramaFinal() {
        return tramaFinal;
    }

    public void setTramaFinal(String tramaFinal) {
        this.tramaFinal = tramaFinal;
    }
}
