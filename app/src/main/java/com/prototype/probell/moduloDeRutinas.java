package com.prototype.probell;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import androidx.annotation.Nullable;

public class moduloDeRutinas extends SQLiteOpenHelper {

    //Strings que nos permiten escribir comodamente las sentencias SQL
    public static final String COMA = ",";
    public static final String TEXT_INT = " INTEGER";
    public static final String TEXT_VARCHAR = " VARCHAR";
    public static final String P_ABRIR = " (";
    public static final String P_CERRAR = ")";
    public static final String PRIMARY_KEY = " PRIMARY KEY";

    //Versión y nombre de la base de datos
    public static final int DATA_VERSION = 1;
    public static String BASE_NAME = "mR.db";

    //Constructor
    public moduloDeRutinas(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, BASE_NAME, null, DATA_VERSION);
    }

    //Creamos las tablas
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(hemisferio.CREAR_TABLA_hemisferio);
        db.execSQL(zona.CREAR_TABLA_zona);
        db.execSQL(velocidad.CREAR_TABLA_velocidad);
        db.execSQL(tiempo.CREAR_TABLA_tiempo);
        db.execSQL(zonaHemisferio.CREAR_TABLA_zonaHemisferio);
        db.execSQL(zonaTiemVel.CREAR_TABLA_zonaTiemVel);
    }

    //Con esto podemos borrar las tablas
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(hemisferio.DROP_TABLA_hemisferio);
        db.execSQL(zona.DROP_TABLA_zona);
        db.execSQL(velocidad.DROP_TABLA_velocidad);
        db.execSQL(tiempo.DROP_TABLA_tiempo);
        db.execSQL(zonaHemisferio.DROP_TABLA_zonaHemisferio);
        db.execSQL(zonaTiemVel.DROP_TABLA_zonaTiemVel);
        onCreate(db);
    }

    //----------------------------------------------------------------------HEMISFERIO
    public static final class hemisferio implements BaseColumns {
        //Columnas de la tabla
        public static final String TABLA_hemisferio = "hemisferio";
        public static final String COL_ID_HEMISFERIO = "ID_hemisferio";
        public static final String COL_NOM_HEMISFERIO = "nombreHemisferio";

        //Crear tabla
        public static final String CREAR_TABLA_hemisferio =
                "CREATE TABLE " + TABLA_hemisferio + P_ABRIR
                        + hemisferio.COL_ID_HEMISFERIO + TEXT_INT + PRIMARY_KEY + COMA
                        + hemisferio.COL_NOM_HEMISFERIO + TEXT_VARCHAR +
                        P_CERRAR;
        //Borrar tabla
        public static final String DROP_TABLA_hemisferio = "DROP TABLE IF EXISTS " + hemisferio.TABLA_hemisferio;
    }

    //----------------------------------------------------------------------ZONA
    public static final class zona implements BaseColumns {
        public static final String TABLA_zona = "zona";
        public static final String COL_ID_ZONA = "ID_zona";
        public static final String COL_NOM_ZONA = "nombreZona";
        public static final String COL_DET_ZONA = "detalleZona";

        public static final String CREAR_TABLA_zona =
                "CREATE TABLE " + TABLA_zona + P_ABRIR
                        + zona.COL_ID_ZONA + TEXT_INT + PRIMARY_KEY + COMA
                        + zona.COL_NOM_ZONA + TEXT_VARCHAR + COMA
                        + zona.COL_DET_ZONA + TEXT_VARCHAR +
                        P_CERRAR;
        public static final String DROP_TABLA_zona = "DROP TABLE IF EXISTS " + zona.TABLA_zona;
    }

    //----------------------------------------------------------------------VELOCIDAD
    public static final class velocidad implements BaseColumns {
        public static final String TABLA_velocidad = "velocidad";
        public static final String COL_ID_VELOCIDAD = "ID_velocidad";
        public static final String COL_VALOR = "valorV";
        public static final String COL_VALOR_STRING = "valorVString";

        public static final String CREAR_TABLA_velocidad =
                "CREATE TABLE " + TABLA_velocidad + P_ABRIR
                        + velocidad.COL_ID_VELOCIDAD + TEXT_INT + PRIMARY_KEY + COMA
                        + velocidad.COL_VALOR + TEXT_INT + COMA
                        + velocidad.COL_VALOR_STRING + TEXT_VARCHAR +
                        P_CERRAR;
        public static final String DROP_TABLA_velocidad = "DROP TABLE IF EXISTS " + velocidad.TABLA_velocidad;
    }

    //----------------------------------------------------------------------TIEMPO
    public static final class tiempo implements BaseColumns {
        public static final String TABLA_tiempo = "tiempo";
        public static final String COL_ID_TIEMPO = "ID_tiempo";
        public static final String COL_VALOR = "valorSegundos";
        public static final String COL_VALORSTRING = "valorMinutos";

        public static final String CREAR_TABLA_tiempo =
                "CREATE TABLE " + TABLA_tiempo + P_ABRIR
                        + tiempo.COL_ID_TIEMPO + TEXT_INT + PRIMARY_KEY + COMA
                        + tiempo.COL_VALOR + TEXT_INT + COMA
                        + tiempo.COL_VALORSTRING + TEXT_VARCHAR +
                        P_CERRAR;
        public static final String DROP_TABLA_tiempo = "DROP TABLE IF EXISTS " + tiempo.TABLA_tiempo;
    }

    //----------------------------------------------------------------------ZONA-HEMISFERIO
    public static final class zonaHemisferio implements BaseColumns{
        public static final String TABLA_zonaHemisferio = "zonaHemisferio";
        public static final String COL_ID_ZONA_HEMISFERIO = "ID_zonaHemisferio";
        public static final String COL_IDZH_ZONA = "ID_ZHzona";
        public static final String COL_IDZH_HEMISFERIO = "ID_ZHhemisferio";

        public static final String CREAR_TABLA_zonaHemisferio = "CREATE TABLE " + TABLA_zonaHemisferio + P_ABRIR
                + zonaHemisferio.COL_ID_ZONA_HEMISFERIO + TEXT_INT + PRIMARY_KEY + COMA
                + zonaHemisferio.COL_IDZH_ZONA + TEXT_INT + COMA
                + zonaHemisferio.COL_IDZH_HEMISFERIO + TEXT_INT + COMA
                + " FOREIGN KEY(ID_ZHzona) REFERENCES zona(ID_zona), "
                + " FOREIGN KEY(ID_ZHhemisferio) REFERENCES hemisferio(ID_hemisferio)" + P_CERRAR;

        public static final String DROP_TABLA_zonaHemisferio = "DROP TABLE IF EXISTS " + zonaHemisferio.TABLA_zonaHemisferio;

    }

    //----------------------------------------------------------------------ZONA-TIEMPO-HEMISFERIO
    public static final class zonaTiemVel implements BaseColumns{
        public static final String TABLA_zonaTiemVel = "zonaTiempoVelocidad";
        public static final String COL_ID_ZONA_TIEMPO_VELOCIDAD = "ID_zonaTiempoVelocidad";
        public static final String COL_IDZTV_ZONA = "ID_ZTVzona";
        public static final String COL_IDZTV_TIEMPO = "ID_ZTVtiempo";
        public static final String COL_IDZTV_VELOCIDAD = "ID_ZTVvelocidad";

        public static final String CREAR_TABLA_zonaTiemVel = "CREATE TABLE " + TABLA_zonaTiemVel + P_ABRIR
                + zonaTiemVel.COL_ID_ZONA_TIEMPO_VELOCIDAD + TEXT_INT + PRIMARY_KEY + COMA
                + zonaTiemVel.COL_IDZTV_ZONA + TEXT_INT + COMA
                + zonaTiemVel.COL_IDZTV_TIEMPO + TEXT_INT + COMA
                + zonaTiemVel.COL_IDZTV_VELOCIDAD + TEXT_INT + COMA
                + " FOREIGN KEY(ID_ZTVzona) REFERENCES zona(ID_zona), "
                + " FOREIGN KEY (ID_ZTVtiempo) REFERENCES tiempo(ID_tiempo), "
                + " FOREIGN KEY (ID_ZTVvelocidad) REFERENCES velocidad(ID_velocidad) "
                + P_CERRAR;

        public static final String DROP_TABLA_zonaTiemVel = "DROP TABLE IF EXISTS " + zonaTiemVel.TABLA_zonaTiemVel;

    }

    //----------------------------------------------------------------------INSERTAR DATOS
    public int insertarDatos(){
        SQLiteDatabase mr = this.getWritableDatabase();

        //-------------Hemisferio
        String insertH1 = "INSERT INTO hemisferio(ID_hemisferio,nombreHemisferio) VALUES(1,'Derecho')"; mr.execSQL(insertH1);
        String insertH2 = "INSERT INTO hemisferio(ID_hemisferio,nombreHemisferio) VALUES(2,'Izquierdo')"; mr.execSQL(insertH2);
        //-------------Tiempo
        String insertT1 = "INSERT INTO tiempo(ID_tiempo,valorSegundos,valorMinutos) VALUES(1,900,'15 minutos')"; mr.execSQL(insertT1);
        String insertT2 = "INSERT INTO tiempo(ID_tiempo,valorSegundos,valorMinutos) VALUES(2,1200,'20 minutos')"; mr.execSQL(insertT2);
        String insertT3 = "INSERT INTO tiempo(ID_tiempo,valorSegundos,valorMinutos) VALUES(3,1500,'25 minutos')"; mr.execSQL(insertT3);
        String insertT4 = "INSERT INTO tiempo(ID_tiempo,valorSegundos,valorMinutos) VALUES(4,1800,'30 minutos')"; mr.execSQL(insertT4);
        String insertT5 = "INSERT INTO tiempo(ID_tiempo,valorSegundos,valorMinutos) VALUES(5,2100,'35 minutos')"; mr.execSQL(insertT5);
        String insertT6 = "INSERT INTO tiempo(ID_tiempo,valorSegundos,valorMinutos) VALUES(6,2400,'40 minutos')"; mr.execSQL(insertT6);
        String insertT7 = "INSERT INTO tiempo(ID_tiempo,valorSegundos,valorMinutos) VALUES(7,2700,'45 minutos')"; mr.execSQL(insertT7);

        //-------------Zona
        String insertZ1 = "INSERT INTO zona(ID_zona,nombreZona,detalleZona) VALUES(1,'Zona 1','Frente y cejas')"; mr.execSQL(insertZ1);
        String insertZ2 = "INSERT INTO zona(ID_zona,nombreZona,detalleZona) VALUES(2,'Zona 2','Pómulos y mejillas')"; mr.execSQL(insertZ2);
        String insertZ3 = "INSERT INTO zona(ID_zona,nombreZona,detalleZona) VALUES(3,'Zona 3','Boca')"; mr.execSQL(insertZ3);

        //-------------Velocidad
        String insertV1 = "INSERT INTO velocidad(ID_velocidad,valorV,valorVString) VALUES(1,1,'1')"; mr.execSQL(insertV1);
        String insertV2 = "INSERT INTO velocidad(ID_velocidad,valorV,valorVString) VALUES(2,2,'2')"; mr.execSQL(insertV2);
        String insertV3 = "INSERT INTO velocidad(ID_velocidad,valorV,valorVString) VALUES(3,3,'3')"; mr.execSQL(insertV3);
        String insertV4 = "INSERT INTO velocidad(ID_velocidad,valorV,valorVString) VALUES(4,4,'4')"; mr.execSQL(insertV4);
        String insertV5 = "INSERT INTO velocidad(ID_velocidad,valorV,valorVString) VALUES(5,5,'5')"; mr.execSQL(insertV5);
        String insertV6 = "INSERT INTO velocidad(ID_velocidad,valorV,valorVString) VALUES(6,6,'6')"; mr.execSQL(insertV6);
        String insertV7 = "INSERT INTO velocidad(ID_velocidad,valorV,valorVString) VALUES(7,7,'7')"; mr.execSQL(insertV7);
        //String insertV8 = "INSERT INTO velocidad(ID_velocidad,valorV,valorVString) VALUES(8,8,'8')"; mr.execSQL(insertV8);


        //-------------Zona-Velocidad
        String insertZH1 = "INSERT INTO zonaHemisferio(ID_zonaHemisferio,ID_ZHzona,ID_ZHhemisferio) VALUES(1,1,1)"; mr.execSQL(insertZH1);
        String insertZH2 = "INSERT INTO zonaHemisferio(ID_zonaHemisferio,ID_ZHzona,ID_ZHhemisferio) VALUES(2,1,2)"; mr.execSQL(insertZH2);
        String insertZH3 = "INSERT INTO zonaHemisferio(ID_zonaHemisferio,ID_ZHzona,ID_ZHhemisferio) VALUES(3,2,1)"; mr.execSQL(insertZH3);
        String insertZH4 = "INSERT INTO zonaHemisferio(ID_zonaHemisferio,ID_ZHzona,ID_ZHhemisferio) VALUES(4,2,2)"; mr.execSQL(insertZH4);
        String insertZH5 = "INSERT INTO zonaHemisferio(ID_zonaHemisferio,ID_ZHzona,ID_ZHhemisferio) VALUES(5,3,1)"; mr.execSQL(insertZH5);
        String insertZH6 = "INSERT INTO zonaHemisferio(ID_zonaHemisferio,ID_ZHzona,ID_ZHhemisferio) VALUES(6,3,2)"; mr.execSQL(insertZH6);

        //-------------Zona-Tiempo-Velocidad
        int IDZona, IDtiempo, IDVelocidad,a;
        int [] IDS_zona = {1,2,3};
        int [] IDS_tiempo = {1,2,3,4,5,6,7};
        int [] IDS_velocidad = {1,2,3,4,5,6,7,8};
        int [] IDS_ZTV = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,
                67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122,123,124,
                125,126,127,128,129,130,131,132,133,134,135,136,137,138,139,140,141,142,143,144,145,146,147,148,149,150,151,152,153,154,155,156,157,158,159,160,161,162,163,164,165,166,167,168};

        a=0;
        for(int i=0; i<IDS_zona.length;i++){
            IDZona = IDS_zona[i];
            for(int j=0; j<IDS_tiempo.length;j++){
                IDtiempo = IDS_tiempo[j];
                for(int k=0;k<IDS_velocidad.length;k++){
                    IDVelocidad = IDS_velocidad[k];
                    String insertZHV = "INSERT INTO zonaTiempoVelocidad(ID_zonaTiempoVelocidad,ID_ZTVzona,ID_ZTVtiempo,ID_ZTVvelocidad)" +
                            " VALUES("+IDS_ZTV[a]+","+IDZona+","+IDtiempo+","+IDVelocidad+")";
                    mr.execSQL(insertZHV);
                    a++;
                }
            }
        }

        mr.close();
        return 1;
    }

    //----------------------------------------------------------------------CONSULTAS
    public Cursor obtenerTablaCompleta(String tabla){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + tabla;
        Cursor cursor = db.rawQuery(query,null);
        return cursor;
    }

    public Cursor obtenerHemisferios(String tabla,String idTabla,String idBuscado){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + tabla + " WHERE "+idTabla+" ='"+idBuscado+"'";
        Cursor cursor = db.rawQuery(query,null);
        return cursor;
    }

    public Cursor obtenerZonas(String tabla,String idTabla, String idBuscado){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT nombreZona FROM " + tabla + " WHERE "+idTabla+" ='"+idBuscado+"'";
        Cursor cursor = db.rawQuery(query,null);
        return cursor;
    }

    public Cursor obtenerDetallesZona(String tabla,String idTabla,String idBuscado){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT detalleZona FROM " + tabla + " WHERE "+idTabla+" ='"+idBuscado+"'";
        Cursor cursor = db.rawQuery(query,null);
        return cursor;
    }

    //Con este método recuperamos el ID de cada zona
    public Cursor obtenerNumeroRutina(String iDZonaBuscado, String iDTiemBuscado, String iDVelBuscado){
        Log.i("Base de datos","entrando");
        Log.i("Base de datos","iDZona "+iDZonaBuscado);
        Log.i("Base de datos","iDTiem "+iDTiemBuscado);
        Log.i("Base de datos","iDVel "+iDVelBuscado);
        SQLiteDatabase db = this.getReadableDatabase();
        String query = ("select ID_zonaTiempoVelocidad from zonaTiempoVelocidad where ID_ZTVzona = '"+iDZonaBuscado+"' and ID_ZTVtiempo = '"+iDTiemBuscado+"' and ID_ZTVvelocidad = '"+iDVelBuscado+"'");
        Cursor cursor = db.rawQuery(query,null);
        Log.i("Base de datos","saliendo");
        return cursor;
    }
}
