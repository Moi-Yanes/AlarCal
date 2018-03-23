package moi.com.apps.alarcal;

import android.provider.BaseColumns;

/**
 * Created by Moi on 27/02/2018.
 */

public class Estructura_BBDD {

    private Estructura_BBDD() {}


    /* Definimos el nombre y los campos de la tabla */
    public static final String TABLE_NAME = "eventos";

    public static final String COLUMNA1 = "nombre";
    public static final String COLUMNA2 = "fecha_evento";           // fecha inicio
    public static final String COLUMNA3 = "hora_evento";            // hora inicio
    public static final String COLUMNA4 = "fecha_fin";              // fecha fin
    public static final String COLUMNA5 = "hora_fin";               // hora fin
    public static final String COLUMNA6 = "descripcion";
    public static final String COLUMNA7 = "fecha_aviso";            // fecha de aviso
    public static final String COLUMNA8 = "repeticiones";           // repeticiones = 0  indica que es el evento original. // repeticiones >= 1   indica que es un evento copia.
    public static final String COLUMNA9 = "repeticion_string";      // por ejemplo: "Diariamente"
    public static final String COLUMNA10 = "recordatorio_string";   // por ejemplo: "1 semana antes"




    /* Definimos variables para:
    * Crear la tabla
    * Borrar la tabla
    * */
    private static final String TIPO_TEXTO = " TEXT";
    private static final String TIPO_INT = " INTEGER";
    private static final String COMMA_SEP = ",";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + Estructura_BBDD.TABLE_NAME + " (" +
                    Estructura_BBDD.COLUMNA1 + TIPO_TEXTO  + COMMA_SEP +
                    Estructura_BBDD.COLUMNA2 + TIPO_TEXTO  + COMMA_SEP +
                    Estructura_BBDD.COLUMNA3 + TIPO_TEXTO  + COMMA_SEP +
                    Estructura_BBDD.COLUMNA4 + TIPO_TEXTO  + COMMA_SEP +
                    Estructura_BBDD.COLUMNA5 + TIPO_TEXTO  + COMMA_SEP +
                    Estructura_BBDD.COLUMNA6 + TIPO_TEXTO  + COMMA_SEP +
                    Estructura_BBDD.COLUMNA7 + TIPO_TEXTO  + COMMA_SEP +
                    Estructura_BBDD.COLUMNA8 + TIPO_INT    + COMMA_SEP +
                    Estructura_BBDD.COLUMNA9 + TIPO_TEXTO  + COMMA_SEP +
                    Estructura_BBDD.COLUMNA10 + TIPO_TEXTO +
                    " )";

    public static final String DELETE_TABLE =
            "DROP TABLE IF EXISTS " + Estructura_BBDD.TABLE_NAME;
}
