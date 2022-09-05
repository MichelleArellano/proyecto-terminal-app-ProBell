package com.prototype.probell;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class menuPrincipal extends AppCompatActivity {

    CardView rutinas, rutinaSeleccionada, manual;
    private int lapso;
    private long tiempoPrimerClick;

    //Constructor
    public menuPrincipal() {
        setLapso(2000); //2 segundos para salir
    }

    //Método para crear la vista
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //No permite que la aplicación se gire

        buscarIDs(); //Relaciona los elementos del XML en JAVA

        //Botones para moverse en el menú
        //Cambio a vista --> rutinas disponibles
        rutinas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), rutinasDisponibles.class);
                startActivity(intent);
            }
        });

        //Cambio a vista --> seleccionar rutina
        rutinaSeleccionada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), seleccionDeRutina.class);
                startActivity(intent);
            }
        });

        //Cambio a vista --> manual de usuario
        manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), manualDeUsuario.class);
                startActivity(intent);
            }
        });

    }

    //Salir de la app
    @Override
    public void onBackPressed(){
        if (getTiempoPrimerClick() + getLapso() > System.currentTimeMillis()){
            super.onBackPressed();
            return;
        }else {
            Toast.makeText(this, R.string.presionaParaSalir, Toast.LENGTH_SHORT).show();
        }
        setTiempoPrimerClick(System.currentTimeMillis());
    }

    /*Los siguientes métodos colocan o retornan el valor de los atributos */

    private int getLapso() {
        return lapso;
    }

    private void setLapso(int lapso) {
        this.lapso = lapso;
    }

    private long getTiempoPrimerClick() {
        return tiempoPrimerClick;
    }

    private void setTiempoPrimerClick(long tiempoPrimerClick) {
        this.tiempoPrimerClick = tiempoPrimerClick;
    }

    //Relacionar XML con JAVA
    private void buscarIDs(){
        rutinas = findViewById(R.id.cardRutinas);
        rutinaSeleccionada = findViewById(R.id.cardRutinaSeleccionada);
        manual = findViewById(R.id.cardManual);
    }
}