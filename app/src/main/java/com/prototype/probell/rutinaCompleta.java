package com.prototype.probell;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class rutinaCompleta extends AppCompatActivity {

    //Botones para el manejo de la vista
    Button cerrar;

    //Método para crear la vista
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rutina_completa);
        buscarIDs(); //Relaciona los elementos del XML en JAVA

        //Cambio a vista --> rutinas disponibles
        cerrar.setOnClickListener((View v) -> {
            finish();
        });

    }

    //Relacionar XML con JAVA
    private void buscarIDs(){
        cerrar = findViewById(R.id.cerrar);
    }
}