package com.prototype.probell;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class manualDeUsuario extends AppCompatActivity {

    //Botones
    Button regresar,inf1,inf2,inf4,inf5,inf6,inf7,inf8,inf9,inf10,inf11,inf12,inf13,inf14,inf15,inf16;

    //Método para crear la vista
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_de_usuario);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        buscarIDs(); //Relaciona los elementos del XML en JAVA

        //Cambio a vista --> menu principal
        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //Llama al diálogo correspondiente y para mostrar el logo y significado de rutinas disponibles
        inf1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), dialogosManual.class);
                intent.putExtra("numDialogo", "1");
                startActivityForResult(intent,1);
            }
        });

        //Llama al diálogo correspondiente y para mostrar el logo y significado de selección de rutinas
        inf2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), dialogosManual.class);
                intent.putExtra("numDialogo", "2");
                startActivityForResult(intent,2);
            }
        });

        //Llama al diálogo correspondiente y para mostrar el logo y significado de prueba de motores
        inf4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), dialogosManual.class);
                intent.putExtra("numDialogo", "4");
                startActivityForResult(intent,4);
            }
        });

        //Llama al diálogo correspondiente y para mostrar el logo y significado de conexión Bluetooth
        inf5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), dialogosManual.class);
                intent.putExtra("numDialogo", "5");
                startActivityForResult(intent,5);
            }
        });

        //Llama al diálogo correspondiente y para mostrar el logo y significado de iniciar
        inf6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), dialogosManual.class);
                intent.putExtra("numDialogo", "6");
                startActivityForResult(intent,6);
            }
        });

        //Llama al diálogo correspondiente y para mostrar el logo y significado de detener
        inf9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), dialogosManual.class);
                intent.putExtra("numDialogo", "9");
                startActivityForResult(intent,9);
            }
        });

        //Llama al diálogo correspondiente --> ¿Para qué sirve ProBell?
        inf10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), dialogosManual.class);
                intent.putExtra("numDialogo", "10");
                startActivityForResult(intent,10);
            }
        });

        //Llama al diálogo correspondiente --> ¿Cómo ver rutinas disponibles?
        inf11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), dialogosManual.class);
                intent.putExtra("numDialogo", "11");
                startActivityForResult(intent,11);
            }
        });

        //Llama al diálogo correspondiente --> ¿Cómo seleccionar una rutina completa?
        inf12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), dialogosManual.class);
                intent.putExtra("numDialogo", "12");
                startActivityForResult(intent,12);
            }
        });

        //Llama al diálogo correspondiente --> ¿Cómo seleccionar una rutina por zonas?
        inf13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), dialogosManual.class);
                intent.putExtra("numDialogo", "13");
                startActivityForResult(intent,13);
            }
        });

        //Llama al diálogo correspondiente --> ¿Cómo seleccionar un dispositivo Bluetooth?
        inf14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), dialogosManual.class);
                intent.putExtra("numDialogo", "14");
                startActivityForResult(intent,14);
            }
        });

        //Llama al diálogo correspondiente --> ¿Cómo ver la rutina configurada?
        inf15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), dialogosManual.class);
                intent.putExtra("numDialogo", "15");
                startActivityForResult(intent,15);
            }
        });

        //Llama al diálogo correspondiente --> Mensajes recibidos
        inf16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), dialogosManual.class);
                intent.putExtra("numDialogo", "16");
                startActivityForResult(intent,16);
            }
        });
    }

    //Continuación después de cerrar los diálogos del manual de usuario
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            Toast.makeText(getApplicationContext(),"Selecciona una opción",Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //Relacionar XML con JAVA
    private void buscarIDs(){
        regresar = findViewById(R.id.btRegresar);
        inf1 = findViewById(R.id.desplegarm1);
        inf2 = findViewById(R.id.desplegarm2);
        inf4 = findViewById(R.id.desplegarm4);
        inf5 = findViewById(R.id.desplegarm5);
        inf6 = findViewById(R.id.desplegarm6);
        inf9 = findViewById(R.id.desplegarm9);
        inf10 = findViewById(R.id.desplegarm10);
        inf11 = findViewById(R.id.desplegarm11);
        inf12 = findViewById(R.id.desplegarm12);
        inf13 = findViewById(R.id.desplegarm13);
        inf14 = findViewById(R.id.desplegarm14);
        inf15 = findViewById(R.id.desplegarm15);
        inf16 = findViewById(R.id.desplegarm16);
    }
}