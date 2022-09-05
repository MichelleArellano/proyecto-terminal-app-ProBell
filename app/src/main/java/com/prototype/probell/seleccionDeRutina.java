package com.prototype.probell;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class seleccionDeRutina extends AppCompatActivity {

    //Botones para el manejo de la vista
    Button regresar, iniRut;
    TextView z1T,z2T,z3T,z1DT,z2DT,z3DT;
    TabHost tabs;

    //Modulo de rutinas
    public moduloDeRutinas mR;
    private int estatusMR;
    protected String moduloRPath;  //Ubicación de la base de datos

    //Para el llenado de datos en los spinners
    ArrayList<String> listaTiempos; //Información que se va a representar
    ArrayList<tiempo> listaT; //Se va a mapear la información
    ArrayList<String> listaVelocidades; //Informacion que se va a representar
    ArrayList<velocidad> listaV; //Se va a mapear la información

    //Rutina completa
    RadioGroup grupoHemisferiosC;
    RadioButton hemisferioIzqC, hemisferioDerC;
    Spinner tiempoZonasC,velocidadZonasC;
    String tiemRC; //Tiempo en binario
    int velRC;//Velocidad

    //Rutina por zonas
    RadioGroup grupoHemisferiosZ;
    RadioButton hemisferioIzqZ, hemisferioDerZ;
    Spinner tiempoZonasZ, velocidadZonasZ;
    boolean boolZona1; //Zona activa
    boolean boolZona2; //Zona activa
    boolean boolZona3; //Zona activa
    String tiemRZ; //Tiempo en binario
    int velRZ;//Velocidad
    CheckBox zona1, zona2, zona3; //

    //Trama
    String HemisferioC;
    String HemisferioZ;
    hemisferio hemisferio = new hemisferio();
    String tipo; //Tipo de rutina
    int auxIni,auxInter,auxFin; //Orden de ejecución
    rutinas rutina = new rutinas();
    zona Zona = new zona();

    String imagen; //Imagenes
    String tiempo; //Tiempo para enviar

    //Constructor
    public seleccionDeRutina() {
        setHemisferioC("3");
        setHemisferioZ("3");
        setTipo("1");
        estatusMR = 0;
        moduloRPath = "/data/data/com.prototype.probell/databases/mR.db";
        boolZona1 = false; boolZona2 = false; boolZona3 = false;
        auxIni = 0; auxInter = 0; auxFin = 0;
        tiemRC = "0"; tiemRZ = "0";
        setVelRC(0); setVelRZ(0);
        setImagen("0");
        setTiempo("0");
    }

    //Método para crear la vista
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccion_de_rutina);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        buscarIDs(); //Relaciona los elementos del XML en JAVA
        revisarModuloRutinas(moduloRPath); //Revisa si el módulo de rutinas ya existe o en su defecto lo crea
        mR = new moduloDeRutinas(getApplicationContext(), "moduloR.db",null,1); //Conexion a la base de datos
        layoutInicializar(); //Inicializa los botones con la informacion del módulo de rutinas y el tab

        //Botones para cambiar de vista
        //Cambio a vista --> iniciar rutina
        iniRut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getTipo()=="1"){
                    setAuxIni(1); setAuxInter(2); setAuxFin(3);
                    mostrarMensajeRutina();
                }else if(getTipo()=="0"){
                    mostrarAlerta();
                }
            }
        });

        //Cambio a vista --> main activity
        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), menuPrincipal.class);
                startActivity(intent);
            }
        });

        //Identifica el tipo de rutina
        tabs.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                switch(s){
                    case "tag1": //Completa
                        setTipo("1");
                        setHemisferioZ("3");
                        setBoolZona1(false);
                        setBoolZona2(false);
                        setBoolZona3(false);
                        setAuxIni(0);
                        setAuxInter(0);
                        setAuxFin(0);
                        setVelRZ(0);
                        setTiemRZ("0");
                        setImagen("0");
                        setTiempo("0");
                        tiempoZonasZ.setSelection(0);
                        velocidadZonasZ.setSelection(0);
                        hemisferioDerZ.setChecked(false);
                        hemisferioIzqZ.setChecked(false);
                        break;
                    case "tag2": //Zonas
                        setTipo("0");
                        setHemisferioC("3");
                        setBoolZona1(false);
                        setBoolZona2(false);
                        setBoolZona3(false);
                        setAuxIni(0);
                        setAuxInter(0);
                        setAuxFin(0);
                        setTiemRC("0");
                        setVelRC(0);
                        setImagen("0");
                        setTiempo("0");
                        tiempoZonasC.setSelection(0);
                        velocidadZonasC.setSelection(0);
                        zona1.setChecked(false); zona2.setChecked(false); zona3.setChecked(false);
                        hemisferioDerC.setChecked(false);
                        hemisferioIzqC.setChecked(false);
                        break;
                    default:
                        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
                }
            }
        });

        //---------------------------RUTINA COMPLETA
        //Hemisferio sano
        grupoHemisferiosC.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int iD) {
                switch(iD){
                    case R.id.rgIzqC:
                        setHemisferioC("1");
                        break;
                    case R.id.rgDerC:
                        setHemisferioC("0");
                        break;
                }
            }
        });

        //Tiempo
        tiempoZonasC.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                //Toast.makeText(getApplicationContext(),"Posicion:"+position,Toast.LENGTH_SHORT).show();
                setTiemRC(obtenerTiempoSeleccionado(position));
                //Toast.makeText(getApplicationContext(),"Tiempo:"+getTiempo(),Toast.LENGTH_SHORT).show();
                //Toast.makeText(getApplicationContext(),"TiempoC:"+getTiemRC(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Velocidad
        velocidadZonasC.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                setVelRC(obtenerVelocidadSeleccionada(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //---------------------------RUTINA POR ZONAS
        //Hemisferio sano
        grupoHemisferiosZ.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int iD) {
                switch(iD){
                    case R.id.rgIzqZ:
                        setHemisferioZ("1");
                        break;
                    case R.id.rgDerZ:
                        setHemisferioZ("0");
                        break;
                }
            }
        });

        //Tiempo
        tiempoZonasZ.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                setTiemRZ(obtenerTiempoSeleccionado(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Velocidad
        velocidadZonasZ.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                setVelRZ(obtenerVelocidadSeleccionada(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Zonas activas
        zona1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(zona1.isChecked() == true){
                    setBoolZona1(true);
                }else{
                    setBoolZona1(false);
                }
            }
        });

        zona2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(zona2.isChecked() == true){
                    setBoolZona2(true);
                }else{
                    setBoolZona2(false);
                }
            }
        });

        zona3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(zona3.isChecked() == true){
                    setBoolZona3(true);
                }else{
                    setBoolZona3(false);
                }
            }
        });
    }

    //Mostrar datos finales antes de iniciar
    private void mostrarMensajeRutina(){
        String auxtipo,auxhemisferio,auxtiempo,auxzona1=null,auxzona2=null,auxzona3=null;
        int auxvelocidad;

        if("1".contentEquals(getTipo())){
            auxtipo = "completa";
            auxtiempo = getTiemRC();
            auxvelocidad = getVelRC();
            setImagen("Todas");
            consultarRutinas("1","2","3",String.valueOf(auxvelocidad));
            setBoolZona1(true); setBoolZona2(true); setBoolZona3(true);
            //Toast.makeText(getApplicationContext(), "auxtiempo"+auxtiempo, Toast.LENGTH_SHORT).show();

            if(getHemisferioC() == "3"){
                Toast.makeText(getApplicationContext(), "Selecciona un hemisferio", Toast.LENGTH_SHORT).show();
            }else if(auxtiempo == "0") {
                Toast.makeText(getApplicationContext(),"Ingresa un tiempo",Toast.LENGTH_SHORT).show();
            }else if(auxvelocidad == 0) {
                Toast.makeText(getApplicationContext(), "Ingresa una velocidad", Toast.LENGTH_SHORT).show();
            }else{

                if(getHemisferioC() == "0"){
                    auxhemisferio = "Derecho";
                }else{
                    auxhemisferio = "Izquierdo";
                }

                new AlertDialog.Builder(this)
                        .setTitle("Rutina "+auxtipo)
                        .setMessage("Hemisferio sano: " + auxhemisferio + "\n" +
                                "Inicio: Zona " + getAuxIni() + "\n" +
                                "Intermedio: Zona " + getAuxInter() + "\n" +
                                "Final: Zona " + getAuxFin() + "\n" +
                                "Tiempo: " + auxtiempo +" mins" + "\n" +
                                "Velocidad: "+ auxvelocidad)

                        .setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                //Toast.makeText(getApplicationContext(),"Continuando",Toast.LENGTH_SHORT).show();
                                tramaRutina trama = new tramaRutina();
                                trama.crearTramaRutina(getTipo(),getHemisferioC(),getAuxIni(),getAuxInter(), getAuxFin(),getTiemRC(),getVelRC());
                                //Toast.makeText(getApplicationContext(),"Trama final "+ trama.getTramaFinal() ,Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(), rutinaActiva.class);
                                intent.putExtra("tramaEnviada",trama.getTramaFinal());
                                intent.putExtra("zonaIni",String.valueOf(getAuxIni()));
                                intent.putExtra("zonaInter",String.valueOf(getAuxInter()));
                                intent.putExtra("zonaFin",String.valueOf(getAuxFin()));
                                intent.putExtra("tiempo",auxtiempo);
                                intent.putExtra("velocidad",String.valueOf(auxvelocidad));
                                intent.putExtra("imagen",getImagen());
                                intent.putExtra("nRZ1",rutina.getZona1S());
                                intent.putExtra("nRZ2",rutina.getZona2S());
                                intent.putExtra("nRZ3",rutina.getZona3S());
                                intent.putExtra("BoolZ1",isBoolZona1());
                                intent.putExtra("BoolZ2",isBoolZona2());
                                intent.putExtra("BoolZ3",isBoolZona3());
                                startActivity(intent);
                            }
                        })
                        .setNeutralButton("Regresar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .show();
            }
        }else if("0".contentEquals(getTipo())){
            auxtipo = "por zonas";
            auxtiempo = getTiemRZ();
            auxvelocidad = getVelRZ();
            consultarRutinas("1","2","3",String.valueOf(auxvelocidad));
            //Toast.makeText(getApplicationContext(), "auxtiempo"+auxtiempo, Toast.LENGTH_SHORT).show();

            if(getHemisferioZ() == "3"){
                Toast.makeText(getApplicationContext(), "Selecciona un hemisferio", Toast.LENGTH_SHORT).show();
            }else if(auxtiempo == "0") {
                Toast.makeText(getApplicationContext(),"Ingresa un tiempo",Toast.LENGTH_SHORT).show();
            }else if(auxvelocidad == 0) {
                Toast.makeText(getApplicationContext(), "Ingresa una velocidad", Toast.LENGTH_SHORT).show();
            }else{

                if(getHemisferioZ() == "0"){
                    auxhemisferio = "Derecho";
                }else{
                    auxhemisferio = "Izquierdo";
                }

                if(isBoolZona1() == false && isBoolZona2()==false && isBoolZona3()==false){//000
                    auxzona1 = "no activa"; auxzona2 = "no activa"; auxzona3 = "no activa"; setImagen("Ninguna");
                }else if(isBoolZona1() == false && isBoolZona2()==false && isBoolZona3()==true){//001
                    auxzona1 = "no activa"; auxzona2 = "no activa"; auxzona3 = "activa"; setImagen("3");
                }else if(isBoolZona1() == false && isBoolZona2()==true && isBoolZona3()==false){//010
                    auxzona1 = "no activa"; auxzona2 = "activa"; auxzona3 = "no activa"; setImagen("2");
                }else if(isBoolZona1() == false && isBoolZona2()==true && isBoolZona3()==true){//011
                    auxzona1 = "no activa"; auxzona2 = "activa"; auxzona3 = "activa"; setImagen("23");
                }else if(isBoolZona1() == true && isBoolZona2()==false && isBoolZona3()==false){//100
                    auxzona1 = "activa"; auxzona2 = "no activa"; auxzona3 = "no activa"; setImagen("1");
                }else if(isBoolZona1() == true && isBoolZona2()==false && isBoolZona3()==true){//101
                    auxzona1 = "activa"; auxzona2 = "no activa"; auxzona3 = "activa"; setImagen("13");
                }else if(isBoolZona1() == true && isBoolZona2()==true && isBoolZona3()==false){//110
                    auxzona1 = "activa"; auxzona2 = "activa"; auxzona3 = "no activa"; setImagen("12");
                }else if(isBoolZona1() == true && isBoolZona2()==true && isBoolZona3()==true){//111
                    auxzona1 = "activa"; auxzona2 = "activa"; auxzona3 = "activa"; setImagen("Todas");
                }

                new AlertDialog.Builder(this)
                        .setTitle("Rutina "+auxtipo)
                        .setMessage("Hemisferio sano: " + auxhemisferio + "\n" +
                                "Zona 1 " + auxzona1 + "\n" +
                                "Zona 2 " + auxzona2 + "\n" +
                                "Zona 3 " + auxzona3 + "\n" +
                                "Inicio: Zona " + getAuxIni() + "\n" +
                                "Intermedio: Zona " + getAuxInter() + "\n" +
                                "Final: Zona " + getAuxFin() + "\n" +
                                "Tiempo: " + auxtiempo+" mins" + "\n" +
                                "Velocidad: "+ auxvelocidad)

                        .setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                //Toast.makeText(getApplicationContext(),"Continuando",Toast.LENGTH_SHORT).show();
                                tramaRutina trama = new tramaRutina();
                                trama.crearTramaRutina(getTipo(),getHemisferioZ(),getAuxIni(),getAuxInter(), getAuxFin(),getTiemRZ(),getVelRZ());
                                //Toast.makeText(getApplicationContext(),"Trama final "+ trama.getTramaFinal() ,Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(), rutinaActiva.class);
                                intent.putExtra("tramaEnviada",trama.getTramaFinal());
                                intent.putExtra("zonaIni",String.valueOf(getAuxIni()));
                                intent.putExtra("zonaInter",String.valueOf(getAuxInter()));
                                intent.putExtra("zonaFin",String.valueOf(getAuxFin()));
                                intent.putExtra("tiempo",auxtiempo);
                                intent.putExtra("velocidad",String.valueOf(auxvelocidad));
                                intent.putExtra("imagen",getImagen());
                                intent.putExtra("nRZ1",rutina.getZona1S());
                                intent.putExtra("nRZ2",rutina.getZona2S());
                                intent.putExtra("nRZ3",rutina.getZona3S());
                                intent.putExtra("BoolZ1",isBoolZona1());
                                intent.putExtra("BoolZ2",isBoolZona2());
                                intent.putExtra("BoolZ3",isBoolZona3());
                                startActivity(intent);
                            }
                        })
                        .setNeutralButton("Regresar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .show();

            }
        }
    }

    //Alerta para cuando son solo 2 zonas activas
    private void mostrarAlerta2z(String[] items,String aux1,String aux2) {
        new AlertDialog.Builder(this)
                .setTitle("Selecciona la zona que iniciará")
                .setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case 0:
                                    if("Zona 1".contentEquals(aux1) && "Zona 2".contentEquals(aux2)){
                                        setAuxIni(1); setAuxInter(0); setAuxFin(2);
                                    }else if("Zona 1".contentEquals(aux1) && "Zona 3".contentEquals(aux2)){
                                        setAuxIni(1); setAuxInter(0); setAuxFin(3);
                                    }else if("Zona 2".contentEquals(aux1) && "Zona 3".contentEquals(aux2)){
                                        setAuxIni(2); setAuxInter(0); setAuxFin(3);
                                    }
                                break;
                            case 1:
                                if("Zona 1".contentEquals(aux1) && "Zona 2".contentEquals(aux2)){
                                    setAuxIni(2); setAuxInter(0); setAuxFin(1);
                                }else if("Zona 1".contentEquals(aux1) && "Zona 3".contentEquals(aux2)){
                                    setAuxIni(3); setAuxInter(0); setAuxFin(1);
                                }else if("Zona 2".contentEquals(aux1) && "Zona 3".contentEquals(aux2)){
                                    setAuxIni(3); setAuxInter(0); setAuxFin(2);
                                }
                                break;
                        }
                    }
                })
                .setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        mostrarMensajeRutina();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        setAuxIni(0); setAuxInter(0); setAuxFin(0);
                        zona1.setChecked(false); zona2.setChecked(false); zona3.setChecked(false);
                        setBoolZona1(false); setBoolZona2(false); setBoolZona3(false);
                    }
                })
                .show();
    }

    //Alerta para cuando son 3 zonas activas
    private void mostrarAlerta3z(String[] items,String aux1,String aux2,String aux3) {
        new AlertDialog.Builder(this)
                .setTitle("Selecciona la zona que iniciará")
                .setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String[] itm = null;
                        switch (i){
                            case 0:
                                setAuxIni(1);
                                itm = new String[]{"Zona 2", "Zona 3"};
                                mostrarAlerta3zInter(itm,"Zona 2", "Zona 3");
                                dialogInterface.dismiss();
                                break;
                            case 1:
                                setAuxIni(2);
                                itm = new String[]{"Zona 1", "Zona 3"};
                                mostrarAlerta3zInter(itm,"Zona 1", "Zona 3");
                                dialogInterface.dismiss();
                                break;
                            case 2:
                                setAuxIni(3);
                                itm = new String[]{"Zona 1", "Zona 2"};
                                mostrarAlerta3zInter(itm,"Zona 1", "Zona 2");
                                dialogInterface.dismiss();
                                break;
                        }
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        setAuxIni(0); setAuxInter(0); setAuxFin(0);
                        zona1.setChecked(false); zona2.setChecked(false); zona3.setChecked(false);
                        setBoolZona1(false); setBoolZona2(false); setBoolZona3(false);
                    }
                })
                .show();
    }

    //Segunda alerta para definir el orden cuando son 3 zonas activas
    private void mostrarAlerta3zInter(String[] items,String aux1,String aux2) {
        new AlertDialog.Builder(this)
                .setTitle("Selecciona la zona que continuará")
                .setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case 0:
                                if("Zona 1".contentEquals(aux1) && "Zona 2".contentEquals(aux2)){
                                    setAuxInter(1); setAuxFin(2);
                                }else if("Zona 1".contentEquals(aux1) && "Zona 3".contentEquals(aux2)){
                                    setAuxInter(1); setAuxFin(3);
                                }else if("Zona 2".contentEquals(aux1) && "Zona 3".contentEquals(aux2)){
                                    setAuxInter(2); setAuxFin(3);
                                }
                                break;
                            case 1:
                                if("Zona 1".contentEquals(aux1) && "Zona 2".contentEquals(aux2)){
                                    setAuxInter(2); setAuxFin(1);
                                }else if("Zona 1".contentEquals(aux1) && "Zona 3".contentEquals(aux2)){
                                    setAuxInter(3); setAuxFin(1);
                                }else if("Zona 2".contentEquals(aux1) && "Zona 3".contentEquals(aux2)){
                                    setAuxInter(3); setAuxFin(2);
                                }
                                break;
                        }
                    }
                })
                .setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        mostrarMensajeRutina();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        setAuxIni(0); setAuxInter(0); setAuxFin(0);
                        zona1.setChecked(false); zona2.setChecked(false); zona3.setChecked(false);
                        setBoolZona1(false); setBoolZona2(false); setBoolZona3(false);
                    }
                })
                .show();
    }

    //Muestra las alertas personalizadas dependiendo de los checkbox activos
    private void mostrarAlerta(){
        String[] items = null;

        if(isBoolZona1()==false && isBoolZona2()==false && isBoolZona3()==false){ //000
            Toast.makeText(getApplicationContext(),"Selecciona zonas a estimular", Toast.LENGTH_LONG).show();
        }else if(isBoolZona1()==false && isBoolZona2()==false && isBoolZona3()==true){//001
            //Toast.makeText(getApplicationContext(),"Iniciando rutina en Zona 3", Toast.LENGTH_LONG).show();
            setAuxIni(3); setAuxInter(0); setAuxFin(0);
            mostrarMensajeRutina();
        }else if(isBoolZona1()==false && isBoolZona2()==true && isBoolZona3()==false){//010
            //Toast.makeText(getApplicationContext(),"Iniciando rutina en Zona 2", Toast.LENGTH_LONG).show();
            setAuxIni(2); setAuxInter(0); setAuxFin(0);
            mostrarMensajeRutina();
        }else if(isBoolZona1()==false && isBoolZona2()==true && isBoolZona3()==true){//011
            items = new String[]{"Zona 2", "Zona 3"};
            mostrarAlerta2z(items,"Zona 2","Zona 3");
        }else if(isBoolZona1()==true && isBoolZona2()==false && isBoolZona3()==false){//100
            //Toast.makeText(getApplicationContext(),"Iniciando rutina en Zona 1", Toast.LENGTH_LONG).show();
            setAuxIni(1); setAuxInter(0); setAuxFin(0);
            mostrarMensajeRutina();
        }else if(isBoolZona1()==true && isBoolZona2()==false && isBoolZona3()==true){//101
            items = new String[]{"Zona 1", "Zona 3"};
            mostrarAlerta2z(items,"Zona 1","Zona 3");
        }else if(isBoolZona1()==true && isBoolZona2()==true && isBoolZona3()==false){//110
            items = new String[]{"Zona 1", "Zona 2"};
            mostrarAlerta2z(items,"Zona 1","Zona 2");
        }else if(isBoolZona1()==true && isBoolZona2()==true && isBoolZona3()==true){//111
            items = new String[]{"Zona 1", "Zona 2", "Zona 3"};
            mostrarAlerta3z(items,"Zona 1","Zona 2"," Zona 3");
        }
    }

    //Se recupera el valor de la velocidad que ha sido seleccionada
    private int obtenerVelocidadSeleccionada(int pos){
        String velocidadList=null,auxSubcadena=null;
        int nuevaVel;
        velocidadList = listaVelocidades.get(pos);
        auxSubcadena = velocidadList.substring(0,1);

        switch(auxSubcadena){
            case "1":
                nuevaVel = 1;
                break;
            case "2":
                nuevaVel = 2;
                break;
            case "3":
                nuevaVel = 3;
                break;
            case "4":
                nuevaVel = 4;
                break;
            case "5":
                nuevaVel = 5;
                break;
            case "6":
                nuevaVel = 6;
                break;
            case "7":
                nuevaVel = 7;
                break;
            case "8":
                nuevaVel = 8;
                break;
            default:
                //Toast.makeText(getApplicationContext(),"La velocidad ingresada no existe "+ auxSubcadena ,Toast.LENGTH_SHORT).show();
                nuevaVel=0;
        }
        return nuevaVel;
    }

    //Se recupera el valor del tiempo que ha sido seleccionado
    private String obtenerTiempoSeleccionado(int pos){
        String tiempoList=null,auxSubcadena=null,tiem="";
        tiempoList = listaTiempos.get(pos);
        auxSubcadena = tiempoList.substring(0,2);
        //Toast.makeText(getApplicationContext(),"auxSubcadena"+auxSubcadena,Toast.LENGTH_SHORT).show();

        switch (auxSubcadena){
            case "15":
                setTiempo("1");
                tiem = auxSubcadena;
                break;
            case "20":
                setTiempo("2");
                tiem = auxSubcadena;
                break;
            case "25":
                setTiempo("3");
                tiem = auxSubcadena;
                break;
            case "30":
                setTiempo("4");
                tiem = auxSubcadena;
                break;
            case "35":
                setTiempo("5");
                tiem = auxSubcadena;
                break;
            case "40":
                setTiempo("6");
                tiem = auxSubcadena;
                break;
            case "45":
                setTiempo("7");
                tiem = auxSubcadena;
                break;
            default:
                setTiempo("0");
                tiem="0";
        }
        //Toast.makeText(getApplicationContext(),"tiem"+tiem,Toast.LENGTH_SHORT).show();
        //Toast.makeText(getApplicationContext(),"gettiempo"+getTiempo(),Toast.LENGTH_SHORT).show();
        return tiem;
    }

    //Obteniendo datos a mostrar en el spinner (tiempos)
    private void obtenerListaMRTiem(){
        listaTiempos = new ArrayList<String>();
        listaTiempos.add("Tiempo");

        for(int i=0;i<listaT.size();i++){
            listaTiempos.add(listaT.get(i).getValorMinutos());
        }
    }

    //Obteniendo datos a mostrar en el spinner (velocidades)
    private void obtenerListaMRVel(){
        listaVelocidades = new ArrayList<String>();
        listaVelocidades.add("Velocidad");

        for(int i=0;i<listaV.size();i++){
            listaVelocidades.add(listaV.get(i).getValorVString());
        }
    }

    //Consultar tiempos en el módulo de rutinas
    private void consultarMRTiem(){
        tiempo timeClass;
        listaT = new ArrayList<tiempo>();
        //Select * from tiempo
        Cursor cursor = mR.obtenerTablaCompleta(moduloDeRutinas.tiempo.TABLA_tiempo);

        while (cursor.moveToNext()){
            timeClass = new tiempo();
            timeClass.setID_tiempo(cursor.getInt(0));
            timeClass.setValorSegundos(cursor.getInt(1));
            timeClass.setValorMinutos(cursor.getString(2));

            Log.i("ID tiempo",timeClass.getID_tiempo().toString());
            Log.i("Tiempo segundos",timeClass.getValorSegundos().toString());
            Log.i("Tiempo minutos",timeClass.getValorSegundos().toString());

            listaT.add(timeClass);
        }

        obtenerListaMRTiem();
    }

    //Consultar velocidades en el módulo de rutinas
    private void consultarMRVel(){
        velocidad velClass;
        listaV = new ArrayList<velocidad>();

        Cursor cursor = mR.obtenerTablaCompleta(moduloDeRutinas.velocidad.TABLA_velocidad);

        while(cursor.moveToNext()){
            velClass = new velocidad();
            velClass.setID_velocidad(cursor.getInt(0));
            velClass.setValorV(cursor.getInt(1));
            velClass.setValorVString(cursor.getString(2));

            listaV.add(velClass);
        }

        obtenerListaMRVel();
    }

    //Consultar zonas en el módulo de rutinas
    private void consultarMRZonas(){
       Cursor czona1, czona2, czona3;
        czona1 = mR.obtenerZonas(moduloDeRutinas.zona.TABLA_zona, moduloDeRutinas.zona.COL_ID_ZONA,String.valueOf(1));
        czona2 = mR.obtenerZonas(moduloDeRutinas.zona.TABLA_zona, moduloDeRutinas.zona.COL_ID_ZONA,String.valueOf(2));
        czona3 = mR.obtenerZonas(moduloDeRutinas.zona.TABLA_zona, moduloDeRutinas.zona.COL_ID_ZONA,String.valueOf(3));

        if(czona1.moveToFirst()){
            Zona.setNombreZona(czona1.getString(0));
            z1T.setText(Zona.getNombreZona());
        }

        if(czona2.moveToFirst()){
            Zona.setNombreZona(czona2.getString(0));
            z2T.setText(Zona.getNombreZona());
        }

        if(czona3.moveToFirst()){
            Zona.setNombreZona(czona3.getString(0));
            z3T.setText(Zona.getNombreZona());
        }

        czona1 = mR.obtenerDetallesZona(moduloDeRutinas.zona.TABLA_zona, moduloDeRutinas.zona.COL_ID_ZONA,String.valueOf(1));
        czona2 = mR.obtenerDetallesZona(moduloDeRutinas.zona.TABLA_zona, moduloDeRutinas.zona.COL_ID_ZONA,String.valueOf(2));
        czona3 = mR.obtenerDetallesZona(moduloDeRutinas.zona.TABLA_zona, moduloDeRutinas.zona.COL_ID_ZONA,String.valueOf(3));

        if(czona1.moveToFirst()){
            Zona.setDetallesZona(czona1.getString(0));
            z1DT.setText(Zona.getDetallesZona());
        }

        if(czona2.moveToFirst()){
            Zona.setDetallesZona(czona2.getString(0));
            z2DT.setText(Zona.getDetallesZona());
        }

        if(czona3.moveToFirst()){
            Zona.setDetallesZona(czona3.getString(0));
            z3DT.setText(Zona.getDetallesZona());
        }
    }

    //Consultar hemisferios en el módulo de rutinas
    private  void consultarHemisferiosMR(){
        Cursor cursorIzquierdo, cursorDerecho;
        cursorDerecho = mR.obtenerHemisferios(moduloDeRutinas.hemisferio.TABLA_hemisferio, moduloDeRutinas.hemisferio.COL_ID_HEMISFERIO,String.valueOf(1)); //Derecho 1
        cursorIzquierdo = mR.obtenerHemisferios(moduloDeRutinas.hemisferio.TABLA_hemisferio, moduloDeRutinas.hemisferio.COL_ID_HEMISFERIO,String.valueOf(2)); //Izquierdo 2
        String datosIzq = null, datosDer = null;

        if(cursorDerecho.moveToFirst()){
            datosDer = cursorDerecho.getString(1);
            hemisferio.setDerecho(datosDer);
        }

        if(cursorIzquierdo.moveToFirst()){
            datosIzq = cursorIzquierdo.getString(1);
            hemisferio.setIzquierdo(datosIzq);
        }
    }

    //Consulta los ID de la zonas en el módulo de rutinas
    private void consultarRutinas(String Z1, String Z2, String Z3,String iDVelocidad){
        Cursor zona1,zona2,zona3;
        String Zona1S="0",Zona2S="0",Zona3S="0";
        zona1 = mR.obtenerNumeroRutina(Z1,getTiempo(),iDVelocidad);
        zona2 = mR.obtenerNumeroRutina(Z2,getTiempo(),iDVelocidad);
        zona3 = mR.obtenerNumeroRutina(Z3,getTiempo(),iDVelocidad);

        if(zona1.moveToFirst()){
            Zona1S = zona1.getString(0);
        }
        if(zona2.moveToFirst()){
            Zona2S = zona2.getString(0);
        }
        if(zona3.moveToFirst()){
            Zona3S= zona3.getString(0);
        }

        rutina.setZona1S(Zona1S); rutina.setZona2S(Zona2S); rutina.setZona3S(Zona3S);
        Log.i("ConsultaRutina","NumRutinas "+ rutina.getZona1S()+","+ rutina.getZona2S() +","+ rutina.getZona3S());
        zona1.close(); zona2.close(); zona3.close();
    }

    //Verifica si el modulo de rutinas ya ha sido creado o en su defecto lo crea
    private boolean revisarModuloRutinas(String Database_path) {
        SQLiteDatabase checkDB = null;
        try {
            checkDB = SQLiteDatabase.openDatabase(Database_path, null, SQLiteDatabase.OPEN_READONLY);
            checkDB.close();
            setEstatusMR(1);
            //Toast.makeText(getApplicationContext(),"Existe la base de datos, estatus "+ getEstatusMR(),Toast.LENGTH_LONG).show();
        } catch (SQLiteException e) {
            setEstatusMR(0);
            //Toast.makeText(getApplicationContext(),"No existe la base de datos, estatus "+ getEstatusMR(),Toast.LENGTH_LONG).show();
            moduloRutinas();
            //Toast.makeText(getApplicationContext(),"Ahora existe la base de datos, estatus "+ getEstatusMR(),Toast.LENGTH_LONG).show();
        }
        return checkDB != null;
    }

    //Crea y llena la base de datos
    private void moduloRutinas() {
        int estatusBDMR;
        moduloDeRutinas mR = new moduloDeRutinas(getApplicationContext(), "mR.db",null,1);
        estatusBDMR=mR.insertarDatos();
        setEstatusMR(estatusBDMR);
    }

    //Inicializar layout con valores del módulo de rutinas
    private void layoutInicializar(){
        consultarHemisferiosMR(); //Consulta el módulo de rutinas para inicializar los radio Buttons
        consultarMRTiem(); //Consulta los tiempos en el módulo de rutinas
        consultarMRVel(); //Consulta las velocidades en el módulo de rutinas
        consultarMRZonas(); //Consulta las zonas y su descripción
        hemisferioDerC.setText(hemisferio.getDerecho());
        hemisferioDerZ.setText(hemisferio.getDerecho());
        hemisferioIzqC.setText(hemisferio.getIzquierdo());
        hemisferioIzqZ.setText(hemisferio.getIzquierdo());

        //Iniciar el submenú
        tabs.setup();
        TabHost.TabSpec spec = tabs.newTabSpec("tag1");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Completa");
        tabs.addTab(spec);
        spec = tabs.newTabSpec("tag2");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Zonas");
        tabs.addTab(spec);

        //Tiempos
        //Rutina completa
        ArrayAdapter<CharSequence> adaptadorTC = new ArrayAdapter(this,R.layout.estilo_spinnertexto,listaTiempos);
        tiempoZonasC.setAdapter(adaptadorTC);

        //Rutina por zonas
        ArrayAdapter<CharSequence> adaptadorTZ = new ArrayAdapter(this,R.layout.estilo_spinnertexto,listaTiempos);
        tiempoZonasZ.setAdapter(adaptadorTZ);

        //Velocidades
        //Rutina completa
        ArrayAdapter<CharSequence> adaptadorVC = new ArrayAdapter(this,R.layout.estilo_spinnertexto,listaVelocidades);
        velocidadZonasC.setAdapter(adaptadorVC);

        //Rutina por zonas
        ArrayAdapter<CharSequence> adaptadorVZ = new ArrayAdapter(this,R.layout.estilo_spinnertexto,listaVelocidades);
        velocidadZonasZ.setAdapter(adaptadorVZ);

        //Inicializar Layout
    }

    //Relacionar XML con JAVA
    private void buscarIDs(){
        tabs = findViewById(R.id.tabhost);
        iniRut = findViewById(R.id.btIniRut);
        regresar = findViewById(R.id.btRegresar);
        grupoHemisferiosC = findViewById(R.id.radioGroupHC);
        hemisferioIzqC = findViewById(R.id.rgIzqC);
        hemisferioDerC = findViewById(R.id.rgDerC);
        tiempoZonasC = findViewById(R.id.spTiemRC);
        velocidadZonasC = findViewById(R.id.spVelRC);
        grupoHemisferiosZ = findViewById(R.id.radioGroupHZ);
        hemisferioIzqZ = findViewById(R.id.rgIzqZ);
        hemisferioDerZ = findViewById(R.id.rgDerZ);
        tiempoZonasZ = findViewById(R.id.spTiemRZ);
        velocidadZonasZ = findViewById(R.id.spVelRZ);
        zona1 = findViewById(R.id.cbzona1);
        zona2 = findViewById(R.id.cbzona2);
        zona3 = findViewById(R.id.cbzona3);
        z1T = findViewById(R.id.txtTXTzona1);
        z2T = findViewById(R.id.txtTXTzona2);
        z3T = findViewById(R.id.txtTXTzona3);
        z1DT = findViewById(R.id.txtdescripcion1);
        z2DT = findViewById(R.id.txtdescripcion2);
        z3DT = findViewById(R.id.txtdescripcion3);
    }

    /*Los siguientes métodos colocan o retornan el valor de los atributos */

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getTiempo() {
        return tiempo;
    }

    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }

    public String getTiemRC() {
        return tiemRC;
    }

    public void setTiemRC(String tiemRC) {
        this.tiemRC = tiemRC;
    }

    public int getVelRC() {
        return velRC;
    }

    public void setVelRC(int velRC) {
        this.velRC = velRC;
    }

    public String getTiemRZ() {
        return tiemRZ;
    }

    public void setTiemRZ(String tiemRZ) {
        this.tiemRZ = tiemRZ;
    }

    public int getVelRZ() {
        return velRZ;
    }

    public void setVelRZ(int velRZ) {
        this.velRZ = velRZ;
    }

    public boolean isBoolZona1() {
        return boolZona1;
    }

    public void setBoolZona1(boolean boolZona1) {
        this.boolZona1 = boolZona1;
    }

    public boolean isBoolZona2() {
        return boolZona2;
    }

    public void setBoolZona2(boolean boolZona2) {
        this.boolZona2 = boolZona2;
    }

    public boolean isBoolZona3() {
        return boolZona3;
    }

    public void setBoolZona3(boolean boolZona3) {
        this.boolZona3 = boolZona3;
    }

    public int getAuxIni() {
        return auxIni;
    }

    public void setAuxIni(int auxIni) {
        this.auxIni = auxIni;
    }

    public int getAuxInter() {
        return auxInter;
    }

    public void setAuxInter(int auxInter) {
        this.auxInter = auxInter;
    }

    public int getAuxFin() {
        return auxFin;
    }

    public void setAuxFin(int auxFin) {
        this.auxFin = auxFin;
    }

    public int getEstatusMR() {
        return estatusMR;
    }

    public void setEstatusMR(int estatusMR) {
        this.estatusMR = estatusMR;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getHemisferioC() {
        return HemisferioC;
    }

    public void setHemisferioC(String hemisferioC) {
        HemisferioC = hemisferioC;
    }

    public String getHemisferioZ() {
        return HemisferioZ;
    }

    public void setHemisferioZ(String hemisferioZ) {
        HemisferioZ = hemisferioZ;
    }
}

