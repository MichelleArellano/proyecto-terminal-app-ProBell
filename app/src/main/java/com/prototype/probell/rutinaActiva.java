package com.prototype.probell;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class rutinaActiva extends AppCompatActivity {

    TextView txt, estado, txtrutinaVZ1, txtrutinaVZ2, txtrutinaVZ3, txtTiempo, txtVelocidad, txtZona1, txtZona2, txtZona3;
    CardView pruebaFun, iniciar, pausar, reanudar, detener;
    FloatingActionButton fabluetooth;
    Button regresar;
    ImageView imagen;

    //Variables
    private int lapso;
    private long tiempoPrimerClick;
    String tramaEnviar, numeroRutina, tiempo, velocidad, inicio, intermedio, fin;
    String img, zona1, zona2, zona3;
    Boolean Z1, Z2, Z3;
    int estatusBluetooth; //Determina el estado del bluetooth en para habilitar los botones
    int estatusTramaEnviada; //Determina el estado de los botones
    ThreadConnectBTdevice myThreadConnectBTdevice;
    ThreadConnected myThreadConnected;
    BluetoothAdapter bluetoothAdapter;
    tramaControl tramac;
    CharSequence tramaRecibida;

    //Valores para indicar que hacer en el Handler
    static final int STATE_LISTENING = 1;
    static final int STATE_CONNECTING=2;
    static final int STATE_CONNECTED=3;
    static final int STATE_CONNECTION_FAILED=4;
    static final int STATE_MESSAGE_RECEIVED=5;

    //Valores que tienen relación al Bluetooth
    private final int SELECT_DEVICE = 102;
    private Context context;
    private final int LOCATION_PERMISSION_REQUEST = 101;
    private static final UUID MY_UUID= UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    //Permite manejar un hilo en segundo plano este permite maupular el estado durante el uso de Bluetooth
    Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            switch (msg.what)
            {
                case STATE_LISTENING:
                    setEstadoConexion("Sin conexión");
                    detenerConexion();
                    break;
                case STATE_CONNECTING:
                    setEstadoConexion("Conectando...");
                    break;
                case STATE_CONNECTED:
                    setEstadoConexion("Conectado");
                    break;
                case STATE_CONNECTION_FAILED:
                    setEstadoConexion("Falló la conexión");
                    detenerConexion();
                case STATE_MESSAGE_RECEIVED:
                    int caso;
                    setEstadoConexion("Conectado");
                    caso = tramac.recepcionTrama(getTramaRecibida());
                    accionesTramaRecibida(caso);
                    break;
            }
            return true;
        }
    });

    //Constructor
    public rutinaActiva(){
        setEstatusBluetooth(0);
        setLapso(2000); //2 segundos para salir
        setEstatusTramaEnviada(0);
    }

    //Método para crear la vista
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rutina_activa);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //No permite que la applicación se gire
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); //No permite que el móvil de ponga en suspención
        buscarIDs(); //Relaciona los elementos del XML en JAVA
        context = this;
        tramac = new tramaControl();
        Toast.makeText(this, "Conecta un dispositivo antes de iniciar", Toast.LENGTH_LONG).show();
        initBluetooth(); //Verifica si el dispositivo cuenta con adaptador Bluetooth y lo habilita
        obtenerIntents(); //Obtiene los datos que han sido pasados desde la vista anterior
        iniciarlizarTextosImagen(); //Inicializa la imagen de detalles

        //Cambio a vista --> selección de rutina
        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context)
                        .setTitle("Aviso")
                        .setMessage("Esta por salir de la rutina configurada. ¿Continuar? ")
                        .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Message message=Message.obtain();
                                message.what=STATE_LISTENING;
                                handler.sendMessage(message);
                                dialogInterface.dismiss();
                            }
                        })
                        .setNegativeButton("Esperar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .show();
            }
        });

        //Conexión Bluetooth
        fabluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, conexionBluetooth.class);
                startActivityForResult(intent, SELECT_DEVICE);

            }
        });

        //Botón de prueba de funconamiento crea una trama y la envía
        pruebaFun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getEstatusBluetooth() == 1){
                    if(getEstatusTramaEnviada() == 0){
                        Toast.makeText(getApplicationContext(), "Comprobando funcionamiento de motores" , Toast.LENGTH_SHORT).show();
                        tramac.crearTramaControl("0",1);
                        if (!tramac.getTramaFinal().isEmpty()) {
                            myThreadConnected.write(tramac.getTramaFinal().getBytes());
                            //Toast.makeText(getApplicationContext(), "Trama de control " + tramac.getTramaFinal() , Toast.LENGTH_LONG).show();
                        }
                        setEstatusTramaEnviada(1);
                    }else if(getEstatusTramaEnviada() == 1){
                        Toast.makeText(getApplicationContext(), "Hay una trama en ejecución" , Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(), "No hay caso status trama" , Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Conecta un dispositivo" , Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Botón de inicio de rutina toma la trama creada en selección de rutina y la envía
        iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getEstatusBluetooth() == 1){
                    if(getEstatusTramaEnviada() == 0){
                        Toast.makeText(getApplicationContext(), "Iniciando rutina" , Toast.LENGTH_SHORT).show();
                        if (!getTramaEnviar().isEmpty()) {
                            myThreadConnected.write(getTramaEnviar().getBytes());
                            //Toast.makeText(getApplicationContext(), "Trama control inicio " + getTramaEnviar() , Toast.LENGTH_LONG).show();
                        }
                        setEstatusTramaEnviada(1);
                    }else if(getEstatusTramaEnviada() == 1){
                        Toast.makeText(getApplicationContext(), "Hay una trama en ejecución" , Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(), "No hay caso status trama" , Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Conecta un dispositivo" , Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Botón de detener crea una trama que interrumpe totalmente la rutina y finalmente la envía
        detener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getEstatusBluetooth() == 1){
                    if(getEstatusTramaEnviada() == 0){
                        Toast.makeText(getApplicationContext(), "No hay una trama en ejecución" , Toast.LENGTH_SHORT).show();
                    }else if(getEstatusTramaEnviada() == 1){
                        Toast.makeText(getApplicationContext(), "Detener rutina" , Toast.LENGTH_SHORT).show();
                        tramac.crearTramaControl("0",4);
                        if (!tramac.getTramaFinal().isEmpty()) {
                            myThreadConnected.write(tramac.getTramaFinal().getBytes());
                            //Toast.makeText(getApplicationContext(), "Trama control detener " + tramac.getTramaFinal() , Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "No hay caso estatus trama" , Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Conecta un dispositivo" , Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    //Método que permite ejecutar conmandos después de crearse la vista, revisa el adaptador de Bluetooth y lo habilita
    @Override
    protected void onStart() {
        super.onStart();
        checkPermissions();
        enableBluetooth();
    }

    //Regresar a selección de rutina
    @Override
    public void onBackPressed(){
        if (getTiempoPrimerClick() + getLapso() > System.currentTimeMillis()){
            Message message=Message.obtain();
            message.what=STATE_LISTENING;
            handler.sendMessage(message);
        }else {
            Toast.makeText(this, R.string.presionaParaRegresar, Toast.LENGTH_SHORT).show();
        }
        setTiempoPrimerClick(System.currentTimeMillis());
    }

    //Detiene los hilos sincronizados al módulo de control, cambia valores y termina la vista
    public synchronized void detenerConexion() {
        if(myThreadConnected != null){
            myThreadConnected.interrupt();
            myThreadConnectBTdevice.cancel();
        }
        setZona1("0"); setZona2("0"); setZona3("0");
        txtrutinaVZ1.setText("000"); txtrutinaVZ2.setText("000"); txtrutinaVZ3.setText("000");
        bluetoothAdapter.disable();
        finish();
    }

    //Después de que recibe una trama e identifica que hacer, aqui muestra el mensaje de esa trama
    public void accionesTramaRecibida(int caso){
        Log.i("accionesTramaRecibida","Caso "+caso);

        if(caso!=0){
            Log.i("accionesTramaRecibida","Caso diferente de cero "+caso);
            switch (caso){
                case 1:
                    Toast.makeText(getApplicationContext(),"Máscara en buen estado",Toast.LENGTH_SHORT).show();
                    setEstatusTramaEnviada(0);
                    break;
                case 2:
                    Toast.makeText(getApplicationContext(),"Fin de rutina",Toast.LENGTH_SHORT).show();
                    Message message=Message.obtain();
                    message.what=STATE_LISTENING;
                    handler.sendMessage(message);
                    break;
                case 3:
                    Toast.makeText(getApplicationContext(),"La rutina inició",Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    Toast.makeText(getApplicationContext(), "Reenviando rutina" , Toast.LENGTH_SHORT).show();
                    if (!getTramaEnviar().isEmpty()) {
                        myThreadConnected.write(getTramaEnviar().getBytes());
                        Toast.makeText(getApplicationContext(), "Trama control inicio " + getTramaEnviar() , Toast.LENGTH_LONG).show();
                    }
                    break;
                default:
                    Toast.makeText(getApplicationContext(),"No hay opción",Toast.LENGTH_SHORT).show();
            }
        }


    }

    //Obtiene los datos que han sido pasados desde la vista anterior
    private void obtenerIntents(){
        setTramaEnviar(getIntent().getStringExtra("tramaEnviada"));
        setNumeroRutina(getIntent().getStringExtra("tramaEnviada"));
        setInicio(String.valueOf(getIntent().getStringExtra("zonaIni")));
        setIntermedio(getIntent().getStringExtra("zonaInter"));
        setFin(getIntent().getStringExtra("zonaFin"));
        setTiempo(getIntent().getStringExtra("tiempo"));
        setVelocidad(getIntent().getStringExtra("velocidad"));
        setImg(getIntent().getStringExtra("imagen"));
        setZona1(getIntent().getStringExtra("nRZ1"));
        setZona2(getIntent().getStringExtra("nRZ2"));
        setZona3(getIntent().getStringExtra("nRZ3"));
        setZ1(getIntent().getBooleanExtra("BoolZ1",true));
        setZ2(getIntent().getBooleanExtra("BoolZ2",true));
        setZ3(getIntent().getBooleanExtra("BoolZ3",true));
    }

    //Inicializa los detalles de la vista
    private void iniciarlizarTextosImagen(){
        txt.setText("");
        txtTiempo.setText(getTiempo()+" mins");
        txtVelocidad.setText(getVelocidad());
        txtZona1.setText("Zona "+getInicio());
        txtZona2.setText("Zona "+getIntermedio());
        txtZona3.setText("Zona "+getFin());

        if("Zona 0".contentEquals(txtZona2.getText())){
            txtZona2.setText("No activo");
        }

        if("Zona 0".contentEquals(txtZona3.getText())){
            txtZona3.setText("No activo");
        }


        if(getZ1() == false && getZ2() == false && getZ3() == false){
            setZona1("0"); setZona2("0"); setZona3("0");
            txtrutinaVZ1.setText(getZona1()); txtrutinaVZ2.setText(getZona2()); txtrutinaVZ3.setText(getZona3());
        }else if(getZ1() == false && getZ2() == false && getZ3() == true){
            setZona1("0"); setZona2("0");
            txtrutinaVZ1.setText(getZona1()); txtrutinaVZ2.setText(getZona2()); txtrutinaVZ3.setText(getZona3());
        }else if(getZ1() == false && getZ2() == true && getZ3() == false){
            setZona1("0"); setZona3("0");
            txtrutinaVZ1.setText(getZona1()); txtrutinaVZ2.setText(getZona2()); txtrutinaVZ3.setText(getZona3());
        }else if(getZ1() == false && getZ2() == true && getZ3() == true){
            setZona1("0");
            txtrutinaVZ1.setText(getZona1()); txtrutinaVZ2.setText(getZona2()); txtrutinaVZ3.setText(getZona3());
        }else if(getZ1() == true && getZ2() == false && getZ3() == false){
            setZona2("0"); setZona3("0");
            txtrutinaVZ1.setText(getZona1()); txtrutinaVZ2.setText(getZona2()); txtrutinaVZ3.setText(getZona3());
        }else if(getZ1() == true && getZ2() == false && getZ3() == true){
            setZona2("0");
            txtrutinaVZ1.setText(getZona1()); txtrutinaVZ2.setText(getZona2()); txtrutinaVZ3.setText(getZona3());
        }else if(getZ1() == true && getZ2() == true && getZ3() == false){
            setZona3("0");
            txtrutinaVZ1.setText(getZona1()); txtrutinaVZ2.setText(getZona2()); txtrutinaVZ3.setText(getZona3());
        }else if(getZ1() == true && getZ2() == true && getZ3() == true){
            txtrutinaVZ1.setText(getZona1()); txtrutinaVZ2.setText(getZona2()); txtrutinaVZ3.setText(getZona3());
        }

        switch (getImg()){
            case "Ninguna":
                imagen.setImageResource(R.drawable.cir);
                break;
            case "3":
                imagen.setImageResource(R.drawable.zona3);
                break;
            case "2":
                imagen.setImageResource(R.drawable.zona2);
                break;
            case "23":
                imagen.setImageResource(R.drawable.zonas23);
                break;
            case "1":
                imagen.setImageResource(R.drawable.zona1);
                break;
            case "13":
                imagen.setImageResource(R.drawable.zonas13);
                break;
            case "12":
                imagen.setImageResource(R.drawable.zonas12);
                break;
            case "Todas":
                imagen.setImageResource(R.drawable.zonas3);
                break;
            default:
                Toast.makeText(getApplicationContext(),"NO hay imagen",Toast.LENGTH_SHORT).show();
        }
    }

    //Verifica si el dispositivo cuenta con adaptador Bluetooth y lo habilita
    private void initBluetooth() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(context, "Bluetooth no encontrado", Toast.LENGTH_SHORT).show();
        }
    }

    //Pregunta al dispositivo por permiso de localización para identificar nuevos dispositivos Bluetooth
    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(rutinaActiva.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST);
        }
    }

    //Relacionar XML con JAVA
    private void buscarIDs(){
        pruebaFun = (CardView) findViewById(R.id.cardFuncionamiento);
        iniciar = (CardView) findViewById(R.id.cardInicio);
        detener = (CardView) findViewById(R.id.cardFin);
        fabluetooth = (FloatingActionButton) findViewById(R.id.fabtBlue);
        estado = (TextView) findViewById(R.id.txtestado);
        txt=(TextView) findViewById(R.id.txttrama);
        regresar = (Button) findViewById(R.id.btRegresar);
        txtrutinaVZ1  = (TextView) findViewById(R.id.txtrutinaVZ1);
        txtrutinaVZ2  = (TextView) findViewById(R.id.txtrutinaVZ2);
        txtrutinaVZ3  = (TextView) findViewById(R.id.txtrutinaVZ3);
        txtTiempo  = (TextView) findViewById(R.id.txtTiempoVal);
        txtVelocidad = (TextView) findViewById(R.id.txtVelocidadVal);
        txtZona1 = (TextView) findViewById(R.id.txtZona1EnviVal);
        txtZona2 = (TextView) findViewById(R.id.txtZona2EnviVal);
        txtZona3  = (TextView) findViewById(R.id.txtZona3EnviVal);
        imagen = (ImageView) findViewById(R.id.imgRA);
    }

    //Continuación después de cerrar la vista en la conexión de un dispositivo Bluetooth
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Toast.makeText(getApplicationContext(),"Entrando a activity result",Toast.LENGTH_SHORT).show();
        if (requestCode == SELECT_DEVICE && resultCode == RESULT_OK) {
            handler.obtainMessage(STATE_CONNECTING).sendToTarget();
            String address = data.getStringExtra("deviceAddress");
            myThreadConnectBTdevice = new ThreadConnectBTdevice(bluetoothAdapter.getRemoteDevice(address));
            myThreadConnectBTdevice.start();
            setEstatusBluetooth(1);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //Habilita el Bluetooth y revisa el adaptador
    private void enableBluetooth() {
        if (!bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.enable();
        }

        if (bluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoveryIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoveryIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoveryIntent);
        }
    }

    /*Los siguientes métodos colocan o retornan el valor de los atributos */

    private void setEstadoConexion(String stEstado) {
        estado.setText(stEstado);
    }

    public CharSequence getTramaRecibida() {
        return tramaRecibida;
    }

    public void setTramaRecibida(CharSequence tramaRecibida) {
        this.tramaRecibida = tramaRecibida;
    }

    public int getLapso() {
        return lapso;
    }

    public void setLapso(int lapso) {
        this.lapso = lapso;
    }

    public long getTiempoPrimerClick() {
        return tiempoPrimerClick;
    }

    public void setTiempoPrimerClick(long tiempoPrimerClick) {
        this.tiempoPrimerClick = tiempoPrimerClick;
    }

    public String getTramaEnviar() {
        return tramaEnviar;
    }

    public void setTramaEnviar(String tramaEnviar) {
        this.tramaEnviar = tramaEnviar;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getNumeroRutina() {
        return numeroRutina;
    }

    public void setNumeroRutina(String numeroRutina) {
        this.numeroRutina = numeroRutina;
    }

    public String getTiempo() {
        return tiempo;
    }

    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }

    public String getZona1() {
        return zona1;
    }

    public void setZona1(String zona1) {
        this.zona1 = zona1;
    }

    public String getZona2() {
        return zona2;
    }

    public void setZona2(String zona2) {
        this.zona2 = zona2;
    }

    public String getZona3() {
        return zona3;
    }

    public void setZona3(String zona3) {
        this.zona3 = zona3;
    }

    public Boolean getZ1() {
        return Z1;
    }

    public void setZ1(Boolean z1) {
        Z1 = z1;
    }

    public Boolean getZ2() {
        return Z2;
    }

    public void setZ2(Boolean z2) {
        Z2 = z2;
    }

    public Boolean getZ3() {
        return Z3;
    }

    public void setZ3(Boolean z3) {
        Z3 = z3;
    }

    public String getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(String velocidad) {
        this.velocidad = velocidad;
    }

    public String getInicio() {
        return inicio;
    }

    public void setInicio(String inicio) {
        this.inicio = inicio;
    }

    public String getIntermedio() {
        return intermedio;
    }

    public void setIntermedio(String intermedio) {
        this.intermedio = intermedio;
    }

    public String getFin() {
        return fin;
    }

    public void setFin(String fin) {
        this.fin = fin;
    }

    public int getEstatusBluetooth() {
        return estatusBluetooth;
    }

    public void setEstatusBluetooth(int estatusBluetooth) {
        this.estatusBluetooth = estatusBluetooth;
    }

    public int getEstatusTramaEnviada() {
        return estatusTramaEnviada;
    }

    public void setEstatusTramaEnviada(int estatusTramaEnviada) {
        this.estatusTramaEnviada = estatusTramaEnviada;
    }

    //Llamado en ThreadConnectBTdevice una vez que la conexión fue exitosa
    //Para después relaizar el intercambio de mensajes
    private void startThreadConnected(BluetoothSocket socket){
        myThreadConnected = new ThreadConnected(socket);
        myThreadConnected.start();
    }

    //Clase que permite realizar la conexión entre dispositivos
    private class ThreadConnectBTdevice extends Thread {

        private BluetoothSocket bluetoothSocket = null;

        //Constructor
        private ThreadConnectBTdevice(BluetoothDevice device) {

            try {
                bluetoothSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
                estado.setText("bluetoothSocket: \n" + bluetoothSocket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //El hilo realiza la conexión por sockets Bluetooth, apoyandose del Handler y
        // empieza el emparejamiento de dispositivos solamente
        @Override
        public void run() {
            boolean success = false;
            try {
                bluetoothSocket.connect();
                success = true;
            } catch (IOException e) {
                e.printStackTrace();

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Message message=Message.obtain();
                        message.what=STATE_CONNECTION_FAILED;
                        handler.sendMessage(message);

                    }
                });

                try {
                    bluetoothSocket.close();
                } catch (IOException e1) {
                    //e1.printStackTrace();
                    bluetoothSocket = null;
                }
            }

            if(success){
                //connect successful
                final String msgconnected = "Conexión exitosa";

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), msgconnected, Toast.LENGTH_LONG).show();
                        Message message=Message.obtain();
                        message.what=STATE_CONNECTED;
                        handler.sendMessage(message);
                    }
                });

                startThreadConnected(bluetoothSocket);

            }
        }

        //Cancelar la conexión
        public void cancel() {
            try {
                bluetoothSocket.close();
            } catch (IOException e) {
                Log.e("Accept->CloseServer", e.toString());
            }
        }

    }

    //Clase que permite realizar el envío y recepción de mensajes entre dos dispositivos
    //Que ha sido previamente emparejados
    private class ThreadConnected extends Thread {
         private final BluetoothSocket socket;
         public InputStream connectedInputStream;
         public OutputStream connectedOutputStream;

         //Constructor requiere del socket con el que se realizó la conexión
        public ThreadConnected(BluetoothSocket socket) {
            this.socket = socket;
            InputStream in = null;
            OutputStream out = null;

            try {
                in = socket.getInputStream();
                out = socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            connectedInputStream = in;
            connectedOutputStream = out;
        }

        //El hilos estará en la espera de mensajes manteniendo la conexión, al interceptar algún mensaje
        //transforma el buffer de datos en una cadena a un txt y procede a hacerse un análisis para
        //revisar la información que se ha mandado, se apoya del Handler.
        @Override
        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;

            while (!interrupted()) {
                try {
                    bytes = connectedInputStream.read(buffer);
                    final String strReceived = new String(buffer, 0, bytes);
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                                txt.append(strReceived);
                                setTramaRecibida(txt.getText());
                        }
                    });
                    Message message=Message.obtain();
                    message.what=STATE_MESSAGE_RECEIVED;
                    handler.sendMessage(message);
                } catch (IOException e) {
                    // TODO Auto-generated catch block

                    final String msgConnectionLost = "Connection lost:  "
                            + e.getMessage();
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Message message=Message.obtain();
                            message.what=STATE_CONNECTION_FAILED;
                            handler.sendMessage(message);
                        }
                    });
                }
            }
        }

        //Permite enviar información desde el Smartphone al módulo de control
        public void write(byte[] bytes)
        {
            try {
                connectedOutputStream.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    }

