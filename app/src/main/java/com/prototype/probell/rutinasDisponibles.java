package com.prototype.probell;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class rutinasDisponibles extends AppCompatActivity {

    Button regresar,rutcompleta,rutzonas;

    //Método para crear la vista
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rutinas_disponibles);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //No permite que la aplicación se gire

        buscarIDs(); //Relaciona los elementos del XML en JAVA

        //Cambio a vista --> menu principal
        regresar.setOnClickListener((View v) -> {
            finish();
        });


        //Cambio a vista --> rutina completa
        rutcompleta.setOnClickListener((View v) -> {
            Intent intent = new Intent(getApplicationContext(), rutinaCompleta.class);
            startActivity(intent);
        });

        //Cambio a vista --> rutina por zonas
        rutzonas.setOnClickListener((View v) -> {
            Intent intent = new Intent(getApplicationContext(), rutinaPorZonas.class);
            startActivity(intent);
        });
    }

    //Relacionar XML con JAVA
    private void buscarIDs(){
        regresar = findViewById(R.id.btRegresar);
        rutcompleta = findViewById(R.id.btnRC);
        rutzonas = findViewById(R.id.btnRZ);
    }
}