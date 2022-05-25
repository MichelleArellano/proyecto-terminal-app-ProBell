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
        setCrc(calculoCRC16(getOpcion()));
        setTramaFinal(tipo + getOpcion() + getCrc());
    }

    //Identifica las acciones que se requieren realizar
    public String identificarOpcion(int opc){

        switch (opc){
            case 1:
                opcion = "0000" ; //Detener rutina
                break;
            case 2:
                opcion = "0001" ; //Pausar rutina
                break;
            case 3:
                opcion = "0010" ; //Reanudar rutina
                break;
            case 4:
                opcion = "0011" ; //Reanudación de rutina a la app
                break;
            case 5:
                opcion = "0100" ; //Solicitar rutina de prueba
            case 6:
                opcion = "0101" ; //Componentes en buen estado
                break;
            case 7:
                opcion = "0110" ; //Expiración del temporizador de la operación Pausa
                break;
            case 8:
                opcion = "0111" ; //Fin de rutina
                break;
            case 9:
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

            if (getTramaRecibida().length() == 21) {
                getTramaRecibida();
                Log.i("Interpretación", "Trama recibida longitud " + getTramaRecibida().length());
                subSecuencia = getTramaRecibida().subSequence(0, 20);
                Log.i("Interpretación", "Subsecuencia " + subSecuencia);
                caso = interpretacionTrama(subSecuencia);
                Log.i("Interpretación", "Caso " + caso);
            } else if (getTramaRecibida().length() > 21) {
                getTramaRecibida();
                residuo = longitudTrama % 21;
                if (residuo == 0) {
                    Log.i("Interpretación", getTramaRecibida() + " es multiplo de 21 " + getTramaRecibida().length());
                    subSecuencia = getTramaRecibida().subSequence(getTramaRecibida().length() - 21, getTramaRecibida().length());
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

        if(tramaR.startsWith("00011") || tramaR.endsWith("0001111")){ //Solicitar reanudación de rutina 00011000000000001111
            caso =1;
        }else if(tramaR.startsWith("00101") || tramaR.endsWith("0010001")){//Componentes en buen estado 00101000000000010001
            caso =2;
        }else if(tramaR.startsWith("00110") || tramaR.endsWith("0011110")){//Notificar fin de pausa 00110000000000011110
            caso =3;
        }else if(tramaR.startsWith("00111") || tramaR.endsWith("0011011")){//Fin de rutina 00111000000000011011
            caso =4;
        }else if(tramaR.startsWith("01000") || tramaR.endsWith("0101000")){//Retransmisión 01000000000000101000
            caso =5;
        }else if(tramaR.startsWith("01001") || tramaR.endsWith("0101101")) { // Rutina iniciada 01001000000000101101
            caso=6;
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
