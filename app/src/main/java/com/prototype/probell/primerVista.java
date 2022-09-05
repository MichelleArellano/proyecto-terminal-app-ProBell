package com.prototype.probell;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

public class primerVista extends AppCompatActivity {

    private int tiempoVista;

    //Constructor
    public primerVista(){
        setTiempoVista(2000);
    }

    //Método para crear la vista
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //No permite que la applicación se gire
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_primer_vista);

        //Muestra la primera vista por 2 segundos y cambia a --> menuPrincipal
        new Handler().postDelayed(new Runnable(){
            public void run(){
                Intent intent = new Intent(getApplicationContext(), menuPrincipal.class);
                startActivity(intent);
                finish();
            }
        }, getTiempoVista());
    }

    /*Los siguientes métodos colocan o retornan el valor de los atributos */

    public int getTiempoVista() {
        return tiempoVista;
    }

    public void setTiempoVista(int tiempoVista) {
        this.tiempoVista = tiempoVista;
    }
}