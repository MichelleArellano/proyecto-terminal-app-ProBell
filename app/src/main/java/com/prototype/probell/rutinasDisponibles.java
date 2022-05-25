package com.prototype.probell;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

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
        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //Cambio a vista --> rutina completa
        rutcompleta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), rutinaCompleta.class);
                startActivity(intent);
            }
        });

        //Cambio a vista --> rutina por zonas
        rutzonas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), rutinaPorZonas.class);
                startActivity(intent);
            }
        });
    }

    //Relacionar XML con JAVA
    private void buscarIDs(){
        regresar = (Button) findViewById(R.id.btRegresar);
        rutcompleta = (Button) findViewById(R.id.btnRC);
        rutzonas = (Button) findViewById(R.id.btnRZ);
    }
}