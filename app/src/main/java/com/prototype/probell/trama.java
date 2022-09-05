package com.prototype.probell;

import android.util.Log;

public class trama {

    //Calcula el crc para las tramas
    public String calculoCRC (String msg){
        String mensaje,polinomio,divisor,trama,auxString,nuevaTramaString = null,crcString;
        char[] comparacionChar,nuevaTramaChar = null;
        char[] tramaChar={0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        char aux;
        mensaje = msg;
        polinomio = "1000000000000101"; //Polinomio x16+x15+x2+1
        divisor = "0000000000000000"; //15 ceros
        trama = mensaje.concat(divisor); //Segun internet n-1 y el resultado son los 0s que se agregan
        //n es el grado del exponente más grande del polinomio
        int iniv, finv; //Inicio de la ventana y fin
        iniv = 0; finv = 0;

        for(int i = 0;i<mensaje.length();i++){ //Recorre los valores del mensaje sin concatenar
            if(finv != trama.length() ){
                iniv = i;
                finv = i+16;
                auxString = trama.substring(iniv,finv); //Subcadena
                //System.out.println("Corrimiento de la trama concatenada "+auxString);

                //Compara las subcadenas
                if(i == 0 && auxString.startsWith("0")){
                    nuevaTramaChar = comparacionDivisor(auxString,divisor);
                    /*for(int d=0;d<nuevaTramaChar.length;d++){System.out.println("nuevaTramaChar "+nuevaTramaChar[d]);}*/
                    nuevaTramaString = String.copyValueOf(nuevaTramaChar,1, 15);
                    aux = trama.charAt(finv);
                    nuevaTramaString = nuevaTramaString + aux;
                    //System.out.println("nuevaTramaString "+nuevaTramaString);
                }else if(i == 0 && auxString.startsWith("1")){
                    nuevaTramaChar = comparacionPolinomio(auxString,polinomio);
                    /*for(int d=0;d<nuevaTramaChar.length;d++){System.out.println("nuevaTramaChar "+nuevaTramaChar[d]);}*/
                    nuevaTramaString = String.copyValueOf(nuevaTramaChar,1, 15);
                    aux = trama.charAt(finv);
                    nuevaTramaString = nuevaTramaString + aux;
                    //System.out.println("nuevaTramaString "+nuevaTramaString);
                }else if(i != 0 && nuevaTramaString.startsWith("0")){
                    nuevaTramaChar = comparacionDivisor(nuevaTramaString,divisor);
                    /*for(int d=0;d<nuevaTramaChar.length;d++){System.out.println("nuevaTramaChar "+nuevaTramaChar[d]);}*/
                    if(finv+1 == trama.length()){
                        nuevaTramaString = String.copyValueOf(nuevaTramaChar,0, 16);
                    }else{
                        nuevaTramaString = String.copyValueOf(nuevaTramaChar,1, 15);
                        aux = trama.charAt(finv);
                        nuevaTramaString = nuevaTramaString +String.valueOf(aux);
                    }
                    //System.out.println("nuevaTramaString "+nuevaTramaString);
                }else if(i !=0 && nuevaTramaString.startsWith("1")){
                    comparacionChar = comparacionPolinomio(nuevaTramaString,polinomio);
                    /*for(int c=0;c<comparacionChar.length;c++){System.out.println("comparacion "+comparacionChar[c]);}*/
                    nuevaTramaChar = comparacionChar;
                    /*for(int d=0;d<nuevaTramaChar.length;d++){System.out.println("nuevaTramaChar "+nuevaTramaChar[d]);}*/
                    if(finv+1 == trama.length()){
                        nuevaTramaString = String.copyValueOf(nuevaTramaChar,0, 16);
                    }else{
                        nuevaTramaString = String.copyValueOf(nuevaTramaChar,1, 15);
                        aux = trama.charAt(finv);
                        nuevaTramaString = nuevaTramaString +String.valueOf(aux);
                    }
                    //System.out.println("nuevaTramaString "+nuevaTramaString);
                }else {
                    //System.out.println("No existe el caso");
                    Log.i("No existe el caso","No existe el caso");
                }
            }
        }
        crcString = nuevaTramaString.substring(1,16);
        return crcString;
    }

    //Obtiene el resultado de la comparacion binaria entre una cadena de ceros y la cadena del mensaje
    public char[] comparacionDivisor(String auxString,String divisor){
        char[] tramaChar,divisorChar,comparacionChar;
        tramaChar = auxString.toCharArray();
        /*for(int a=0;a<tramaChar.length;a++){System.out.println("tramaChar "+tramaChar[a]); }*/
        divisorChar = divisor.toCharArray();
        /*for(int b=0;b<divisorChar.length;b++){System.out.println("divisorChar "+divisorChar[b]);}*/
        comparacionChar = comparacionBinaria(tramaChar,divisorChar);
        /*for(int c=0;c<comparacionChar.length;c++){System.out.println("comparacion "+comparacionChar[c]);}*/
        return comparacionChar;
    }

    //Obtiene el resultado de la comparacion binaria entre la cadena del polinomio y la cadena del mensaje
    public char[] comparacionPolinomio(String auxString,String polinomio){
        char[] tramaChar,polinomioChar,comparacionChar;
        tramaChar = auxString.toCharArray();
        /*for(int a=0;a<tramaChar.length;a++){System.out.println("tramaChar "+tramaChar[a]);}*/
        polinomioChar = polinomio.toCharArray();
        /*for(int b=0;b<polinomioChar.length;b++){System.out.println("polinomioChar "+polinomioChar[b]);}*/
        comparacionChar = comparacionBinaria(tramaChar,polinomioChar);
        /*for(int c=0;c<comparacionChar.length;c++){System.out.println("comparacion "+comparacionChar[c]);}*/
        return comparacionChar;
    }

    //Realiza la comparación binaria entre dos cadenas
    public char[] comparacionBinaria(char[] subTra, char[] subCom){
        boolean[] auxbool = {false,false,false,false,false,false,false,false,
                false,false,false,false,false,false,false,false};
        char[] auxChar = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};

        boolean[] subtrama = conversionCharBool(subTra);
        boolean[] arrayComparar = conversionCharBool(subCom);

        if(subtrama.length == arrayComparar.length){ //Primero ver que sean del mismo tamaño
            //Hace la operación XOR
            for(int i=0;i<subtrama.length;i++){
                auxbool[i] = subtrama[i] ^ arrayComparar[i];
            }
            auxChar = conversionBoolChar(auxbool);
        }
        return auxChar;
    }

    //Realiza la conversión de booleano a char
    public char[] conversionBoolChar(boolean[]subtramabool){
        boolean[] auxbool = {false,false,false,false,false,false,false,false,
                false,false,false,false,false,false,false,false};
        char[] auxChar={0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};

        //Convierte valores booleanos en caracteres
        for(int i=0;i<subtramabool.length;i++){
            if(subtramabool[i] == false){
                auxChar[i] = '0';
            }else{
                auxChar[i] = '1';
            }
        }
        return auxChar;
    }

    //Realiza la conversión de char a booleano
    public boolean[] conversionCharBool(char[]subtramaChar){
        boolean[] subtramabool={false,false,false,false,false,false,false,false,
                false,false,false,false,false,false,false,false};

        //Convierte la subtrama en valores booleanos
        for(int i=0;i<subtramaChar.length;i++){
            if(subtramaChar[i] == '0'){
                subtramabool[i] = false;
            }else{
                subtramabool[i] = true;
            }
        }
        return subtramabool;
    }
}
