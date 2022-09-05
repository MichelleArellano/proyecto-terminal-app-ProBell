package com.prototype.probell;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

public class dialogosManual extends AppCompatActivity {

    //Elementos para interactuar
    Button siguiente,cerrar;
    ImageSwitcher imageSwitcher;
    TextSwitcher txt;
    String numDialogo;
    private int mPosicion;
    private int mPosicion1;

    //Constructor
    public dialogosManual(){
        setNumDialogo("");
        setmPosicion(0);
        setmPosicion1(0);
    }

    //Método para crear la vista
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialogos_manual);
        setNumDialogo(getIntent().getStringExtra("numDialogo"));
        buscarIDs(); //Relaciona los elementos del XML en JAVA

        //Textos
        //----------------------------------------------------------------------Menu
        String DesRutDisponibles,DesSeleccionarRutina;
        DesRutDisponibles="En esta sección se visualiza una breve definición de la parálisis de Bell y se muestran los tipos de rutina que se pueden manipular con ProBell.";
        DesSeleccionarRutina="En esta sección se muestra la interfaz de usuario para seleccionar una rutina completa o una rutina por zonas.";
        String menu[] ={DesRutDisponibles,DesSeleccionarRutina};

        //----------------------------------------------------------------------Botones
        String DesPruebaDeMotores,DesPruebaDeMotores1;
        DesPruebaDeMotores="Este es un botón ubicado en la vista de rutina activa, permite activar los motores de la máscara por un lapso previamente establecido.";
        DesPruebaDeMotores1="Para activarse dicho botón es necesario realizar una conexión previa con un dispositivo que cuente con tecnología Bluetooth.";
        String motores[] ={DesPruebaDeMotores,DesPruebaDeMotores1};

        String DesConexionBlue,DesConexionBlue1;
        DesConexionBlue="Este es un botón ubicado en la vista de rutina activa, permite realizar una conexión con un dispositivo nuevo o vinculado.";
        DesConexionBlue1="Cuenta con un indicador que permite conocer el estado de la conexión (sin conexión, conectado o conexión perdida).";
        String conBluetooth[] ={DesConexionBlue,DesConexionBlue1};

        String DesControles,DesControles1,DesControles2,DesControles3;
        DesControles="Este botón se encuentra en la vista de rutina activa, permite dar inicio a la rutina por medio de un mensaje que se envía por Bluetooth a la máscara.";
        DesControles1="Este botón se encuentra en la vista de rutina activa, permite interrumpir momentáneamente la rutina por medio de un mensaje que se envía por Bluetooth a la máscara.";
        DesControles2="Este botón se encuentra en la vista de rutina activa, permite empezar nuevamente la rutina por medio de un mensaje que se envía por Bluetooth a la máscara.";
        DesControles3="Este botón se encuentra en la vista de rutina activa, permite finalizar la rutina por medio de un mensaje que se envía por Bluetooth a la máscara.";
        String controles[] ={DesControles,DesControles1,DesControles2,DesControles3};

        //----------------------------------------------------------------------¿Para qué sirve ProBell
        String DesParaQueSirvePB,DesParaQueSirvePB1,DesParaQueSirvePB2;
        DesParaQueSirvePB="ProBell es una aplicación móvil que forma parte de una herramienta auxiliar al tratamiento de fisioterapia.";
        DesParaQueSirvePB1="Manda indicaciones vía Bluetooth hacia una máscara para estimular zonas clave en el rostro.";
        DesParaQueSirvePB2="También puede visualizarse como un control inalámbrico que brinda la información necesaria para la descripción, configuración y manipulación de rutinas.";
        String pqspbell[] ={DesParaQueSirvePB,DesParaQueSirvePB1,DesParaQueSirvePB2,};

        //----------------------------------------------------------------------¿Cómo ver rutinas disponibles?
        String DesComoVerRutDisp,DesComoVerRutDisp1;
        DesComoVerRutDisp="En el menú principal selecciona el apartado: Rutinas disponibles.";
        DesComoVerRutDisp1="A continuación, se muestra la vista que permite visualizar las rutinas. Selecciona un botón para obtener información.";
        String cvrdisp[] ={DesComoVerRutDisp,DesComoVerRutDisp1};

        //----------------------------------------------------------------------¿Cómo seleccionar una rutina completa?
        String DesComoSelRutinaC,DesComoSelRutinaC1,DesComoSelRutinaC2,DesComoSelRutinaC3,DesComoSelRutinaC4;
        DesComoSelRutinaC="En el menú principal selecciona el apartado: Selección de rutina.";
        DesComoSelRutinaC1="A continuación, se muestra un submenú en la parte superior y selecciona el apartado que dice: Completa.";
        DesComoSelRutinaC2="Se pueden observar los elementos necesarios para configurar la rutina.";
        DesComoSelRutinaC3="Primero selecciona el hemisferio sano del rostro, un tiempo y una velocidad dentro de los valores disponibles. Después de hacerlo ya se tiene configurada la rutina completa.";
        DesComoSelRutinaC4="Finalmente presiona iniciar donde se desplegará la información correspondiente a la rutina configurada.";
        String csurcompleta[] ={DesComoSelRutinaC,DesComoSelRutinaC1,DesComoSelRutinaC2,DesComoSelRutinaC3,DesComoSelRutinaC4};

        //----------------------------------------------------------------------¿Cómo seleccionar una rutina por zonas?
        String DesComoSelRutinaZ,DesComoSelRutinaZ1,DesComoSelRutinaZ2,DesComoSelRutinaZ3,DesComoSelRutinaZ4;
        DesComoSelRutinaZ="En el menú principal selecciona el apartado: Selección de rutina.";
        DesComoSelRutinaZ1="A continuación, se muestra un submenú en la parte superior y selecciona el apartado que dice: Zonas.";
        DesComoSelRutinaZ2="Se pueden observar los elementos necesarios para configurar la rutina.";
        DesComoSelRutinaZ3="Primero selecciona el hemisferio sano del rostro, después la o las zonas que se requieran estimular, también un tiempo y finalmente una velocidad dentro de los valores disponibles. Después de hacerlo ya se tiene configurada la rutina por zonas.";
        DesComoSelRutinaZ4="Finalmente presiona iniciar donde se desplegará la información correspondiente a la rutina configurada.";
        String csurzona[] ={DesComoSelRutinaZ,DesComoSelRutinaZ1,DesComoSelRutinaZ2,DesComoSelRutinaZ3,DesComoSelRutinaZ4};

        //----------------------------------------------------------------------¿Cómo conectar un dispositivo Bluetooth?
        String DesSeleccionDispBlue,DesSeleccionDispBlue1,DesSeleccionDispBlue2;
        DesSeleccionDispBlue="Presiona el botón de conexión Bluetooth.";
        DesSeleccionDispBlue1="A continuación, se muestran las listas de dispositivos vinculados y dispositivos disponibles, si el dispositivo vinculado no aparece, presiona escanear y vincula, después selecciona un dispositivo.";
        DesSeleccionDispBlue2="Finalmente se visualiza el estado de conexión.";
        String ccudispblue[] ={DesSeleccionDispBlue,DesSeleccionDispBlue1,DesSeleccionDispBlue2};

        //----------------------------------------------------------------------Valores de rutina
        String DesVerificarValRutSel,DesVerificarValRutSel1,DesVerificarValRutSel2;
        DesVerificarValRutSel="Existen dos lugares dentro de la aplicación donde se pueden verificar los datos correspondientes a las rutinas.";
        DesVerificarValRutSel1="El primero es en el momento de confirmar la rutina antes de iniciar. Muestra un diálogo con los valores correspondientes.";
        DesVerificarValRutSel2="El segundo es cuando la rutina esta activa, aparece una sección de detalles de la rutina, en este a diferencia del primero se incluye el número de rutina por zona que se encuentra almacenado en ProBell.";
        String valoresRutina[] ={DesVerificarValRutSel,DesVerificarValRutSel1,DesVerificarValRutSel2};

        //----------------------------------------------------------------------Mensajes recibidos
        String DesMensajesRecibidos,DesMensajesRecibidos1,DesMensajesRecibidos2,DesMensajesRecibidos3;
        DesMensajesRecibidos="Existen mensajes que pueden recibirse dentro de ProBell durante la ejecución de las rutinas, el significado de cada uno se muestra a continuación.";
        DesMensajesRecibidos1="Este es un mensaje que recibe el móvil cuando la rutina de prueba ha finalizado; es decir, se ha terminado la rutina que corresponde al presionar el  botón de funcionamiento.";
        DesMensajesRecibidos2="Este es un mensaje que recibe el móvil cuando la rutina ha comenzado en la máscara, corresponde al presionar el  botón de inicio y prueba de funcionamiento.";
        DesMensajesRecibidos3="Este es un mensaje indica que la rutina ha terminado y redirige a selección de rutina.";
        String mensajes[] ={DesMensajesRecibidos,DesMensajesRecibidos1,DesMensajesRecibidos2,DesMensajesRecibidos3};

        //Imagenes
        final int[] imagenes5={R.drawable.dmbluetooth,R.drawable.dmconexionblue};//Conexión bluetooth
        final int[] imagenes11={R.drawable.dmvrutdisp,R.drawable.dmrutinasdisponibles};//Ver rutinas disponibles
        final int[] imagenes12={R.drawable.dmvselrut,R.drawable.dmrutcompleta,R.drawable.dmrutinacompleta,R.drawable.dmdrutcompleta,R.drawable.dminiciarrut};//Ver rutina completa
        final int[] imagenes13={R.drawable.dmvselrut,R.drawable.dmrutzonas,R.drawable.dmrutinazonas,R.drawable.dmdrutzonas,R.drawable.dminiciarrut};//Ver rutina por zonas
        final int[] imagenes14={R.drawable.dmbluetooth,R.drawable.dmdispblue,R.drawable.dmconexionblue1};//Conexión a un dispositivo bluetooth
        final int[] imagenes15={R.drawable.dmv1,R.drawable.dmv1,R.drawable.dmv2};//Ver valores de rutina
        final int[] imagenes16={R.drawable.msg1,R.drawable.msg1,R.drawable.msg2,R.drawable.msg6};//Mensajes recibidos

        //Contenedores
        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView =new ImageView(getApplicationContext());
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imageView.setLayoutParams(new ImageSwitcher.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT,ActionBar.LayoutParams.WRAP_CONTENT));
                return imageView;
            }
        });
        txt.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView textView = new TextView(getApplicationContext());
                textView.setTextColor(getResources().getColor(R.color.negro));
                textView.setTextSize(18);
                return textView;
            }
        });

        //Selecciona que diálogo se requiere visualizar
        switch(getNumDialogo()){
            case "1":
                this.setTitle(R.string.menuSeccionRutinasDisponibles);
                imageSwitcher.setBackgroundResource(R.drawable.dmrutdisp);
                txt.setText(menu[0]);
                siguiente.setVisibility(View.GONE);
                break;
            case "2":
                this.setTitle(R.string.menuSeccionSeleccionRutina);
                imageSwitcher.setBackgroundResource(R.drawable.dmselrut);
                txt.setText(menu[1]);
                siguiente.setVisibility(View.GONE);
                break;
            case "4":
                this.setTitle(R.string.cPruebaMotores);
                imageSwitcher.setBackgroundResource(R.drawable.dmfuncionamiento);
                txt.setText(motores[mPosicion]);
                cerrar.setVisibility(View.GONE);
                break;
            case "5":
                this.setTitle(R.string.conBlue);
                imageSwitcher.setBackgroundResource(imagenes5[getmPosicion()]);
                txt.setText(conBluetooth[mPosicion]);
                cerrar.setVisibility(View.GONE);
                break;
            case "6":
                this.setTitle(R.string.controles);
                imageSwitcher.setBackgroundResource(R.drawable.dminiciar);
                txt.setText(controles[0]);
                siguiente.setVisibility(View.GONE);
                break;
            case "7":
                this.setTitle(R.string.controles);
                imageSwitcher.setBackgroundResource(R.drawable.dmpausar);
                txt.setText(controles[1]);
                siguiente.setVisibility(View.GONE);
                break;
            case "8":
                this.setTitle(R.string.controles);
                imageSwitcher.setBackgroundResource(R.drawable.dmreanudar);
                txt.setText(controles[2]);
                siguiente.setVisibility(View.GONE);
                break;
            case "9":
                this.setTitle(R.string.controles);
                imageSwitcher.setBackgroundResource(R.drawable.dmdetener);
                txt.setText(controles[3]);
                siguiente.setVisibility(View.GONE);
                break;
            case "10":
                this.setTitle(R.string.pqspb);
                imageSwitcher.setBackgroundResource(R.drawable.dmpqspb);
                txt.setText(pqspbell[mPosicion]);
                cerrar.setVisibility(View.GONE);
                break;
            case "11":
                this.setTitle(R.string.menuSeccionRutinasDisponibles);
                imageSwitcher.setBackgroundResource(imagenes11[getmPosicion()]);
                txt.setText(cvrdisp[mPosicion]);
                cerrar.setVisibility(View.GONE);
                break;
            case "12":
                this.setTitle(R.string.rutC);
                imageSwitcher.setBackgroundResource(imagenes12[getmPosicion()]);
                txt.setText(csurcompleta[mPosicion]);
                cerrar.setVisibility(View.GONE);
                break;
            case "13":
                this.setTitle(R.string.rutZ);
                imageSwitcher.setBackgroundResource(imagenes13[getmPosicion()]);
                txt.setText(csurzona[mPosicion]);
                cerrar.setVisibility(View.GONE);
                break;
            case "14":
                this.setTitle(R.string.dispBlue);
                imageSwitcher.setBackgroundResource(imagenes14[getmPosicion()]);
                txt.setText(ccudispblue[mPosicion]);
                cerrar.setVisibility(View.GONE);
                break;
            case "15":
                this.setTitle(R.string.valRut);
                txt.setText(valoresRutina[mPosicion]);
                imageSwitcher.setVisibility(View.GONE);
                cerrar.setVisibility(View.GONE);
                break;
            case "16":
                this.setTitle(R.string.msgR);
                txt.setText(mensajes[mPosicion]);
                imageSwitcher.setVisibility(View.GONE);
                cerrar.setVisibility(View.GONE);
                break;
            default:
                Toast.makeText(getApplicationContext(),"No hay opción",Toast.LENGTH_SHORT).show();
        }


        //Botón para cambiar en el switch de imágenes y textos
        siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (getNumDialogo()){
                    case "4":
                        if(mPosicion1<motores.length-1){
                            mPosicion1 = mPosicion1+1;
                            txt.setText(motores[mPosicion1]);
                            if(mPosicion1<motores.length && getmPosicion1() == 1){
                                cerrar.setVisibility(View.VISIBLE);
                                siguiente.setVisibility(View.GONE);
                            }
                        }
                        break;
                    case "5":
                        if(mPosicion<imagenes5.length-1){
                            mPosicion = mPosicion+1;
                            imageSwitcher.setBackgroundResource(imagenes5[mPosicion]);
                        }

                        if(mPosicion1<conBluetooth.length-1){
                            mPosicion1 = mPosicion1+1;
                            txt.setText(conBluetooth[mPosicion1]);
                            if(mPosicion1<conBluetooth.length && getmPosicion1() == 1){
                                cerrar.setVisibility(View.VISIBLE);
                                siguiente.setVisibility(View.GONE);
                            }
                        }
                        break;
                    case "10":
                        if(mPosicion1<pqspbell.length-1){
                            mPosicion1 = mPosicion1+1;
                            txt.setText(pqspbell[mPosicion1]);
                            if(mPosicion1<pqspbell.length && getmPosicion1() == 2){
                                cerrar.setVisibility(View.VISIBLE);
                                siguiente.setVisibility(View.GONE);
                            }
                        }
                        break;
                    case "11":
                        if(mPosicion<imagenes11.length-1){
                            mPosicion = mPosicion+1;
                            imageSwitcher.setBackgroundResource(imagenes11[mPosicion]);
                        }

                        if(mPosicion1<cvrdisp.length-1){
                            mPosicion1 = mPosicion1+1;
                            txt.setText(cvrdisp[mPosicion1]);
                            if(mPosicion1<cvrdisp.length && getmPosicion1() == 1){
                                cerrar.setVisibility(View.VISIBLE);
                                siguiente.setVisibility(View.GONE);
                            }
                        }
                        break;
                    case "12":
                        if(mPosicion<imagenes12.length-1){
                            mPosicion = mPosicion+1;
                            imageSwitcher.setBackgroundResource(imagenes12[mPosicion]);
                        }

                        if(mPosicion1<csurcompleta.length-1){
                            mPosicion1 = mPosicion1+1;
                            txt.setText(csurcompleta[mPosicion1]);
                            if(mPosicion1<csurcompleta.length && getmPosicion1() == 4){
                                cerrar.setVisibility(View.VISIBLE);
                                siguiente.setVisibility(View.GONE);
                            }
                        }
                        break;
                    case "13":
                        if(mPosicion<imagenes13.length-1){
                            mPosicion = mPosicion+1;
                            imageSwitcher.setBackgroundResource(imagenes13[mPosicion]);
                        }

                        if(mPosicion1<csurzona.length-1){
                            mPosicion1 = mPosicion1+1;
                            txt.setText(csurzona[mPosicion1]);
                            if(mPosicion1<csurzona.length && getmPosicion1() == 4){
                                cerrar.setVisibility(View.VISIBLE);
                                siguiente.setVisibility(View.GONE);
                            }
                        }
                        break;
                    case "14":
                        if(mPosicion<imagenes14.length-1){
                            mPosicion = mPosicion+1;
                            imageSwitcher.setBackgroundResource(imagenes14[mPosicion]);
                        }

                        if(mPosicion1<ccudispblue.length-1){
                            mPosicion1 = mPosicion1+1;
                            txt.setText(ccudispblue[mPosicion1]);
                            if(mPosicion1<ccudispblue.length && getmPosicion1() == 2){
                                cerrar.setVisibility(View.VISIBLE);
                                siguiente.setVisibility(View.GONE);
                            }
                        }
                        break;
                    case "15":
                        imageSwitcher.setVisibility(View.VISIBLE);
                        if(mPosicion<imagenes15.length-1){
                            mPosicion = mPosicion+1;
                            imageSwitcher.setBackgroundResource(imagenes15[mPosicion]);
                        }

                        if(mPosicion1<valoresRutina.length-1){
                            mPosicion1 = mPosicion1+1;
                            txt.setText(valoresRutina[mPosicion1]);
                            if(mPosicion1<valoresRutina.length && getmPosicion1() == 2){
                                cerrar.setVisibility(View.VISIBLE);
                                siguiente.setVisibility(View.GONE);
                            }
                        }
                        break;
                    case "16":
                        imageSwitcher.setVisibility(View.VISIBLE);
                        if(mPosicion<imagenes16.length-1){
                            mPosicion = mPosicion+1;
                            imageSwitcher.setBackgroundResource(imagenes16[mPosicion]);
                        }

                        if(mPosicion1<mensajes.length-1){
                            mPosicion1 = mPosicion1+1;
                            txt.setText(mensajes[mPosicion1]);
                            if(mPosicion1<mensajes.length && getmPosicion1() == 3){
                                cerrar.setVisibility(View.VISIBLE);
                                siguiente.setVisibility(View.GONE);
                            }
                        }
                        break;
                }
            }
        });

        //Cierra el diálogo
        cerrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setmPosicion(0); setmPosicion1(0);
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });

    }

    /*Los siguientes métodos colocan o retornan el valor de los atributos */

    public int getmPosicion1() {
        return mPosicion1;
    }

    public void setmPosicion1(int mPosicion1) {
        this.mPosicion1 = mPosicion1;
    }

    public int getmPosicion() {
        return mPosicion;
    }

    public void setmPosicion(int mPosicion) {
        this.mPosicion = mPosicion;
    }

    public String getNumDialogo() {
        return numDialogo;
    }

    public void setNumDialogo(String numDialogo) {
        this.numDialogo = numDialogo;
    }

    //Relacionar XML con JAVA
    private void buscarIDs(){
        siguiente = findViewById(R.id.siguiente);
        cerrar = findViewById(R.id.cerrar);
        txt = findViewById(R.id.txtTXTm1);
        imageSwitcher = findViewById(R.id.imgSwitcher);
    }
}