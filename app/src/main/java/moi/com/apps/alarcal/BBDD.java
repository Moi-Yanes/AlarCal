package moi.com.apps.alarcal;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Moi on 27/02/2018.
 */

public class BBDD extends SQLiteOpenHelper{

    /*
    * Variables
    */
    public static final int DATABASE_VERSION = 9;
    public static final String DATABASE_NAME = "AlarCal_BBDD.db";


    /*
    * Metodos
    */
    public BBDD(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Estructura_BBDD.CREATE_TABLE);
    }


    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(Estructura_BBDD.DELETE_TABLE);
        onCreate(db);
    }


    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }



    /*
    * CONSULTA LOS EVENTOS
    * */
    public Cursor consultaEventos(String campo_bbdd, String dato, String restriccion, String campo_ordenar, boolean where, boolean order){

        final SQLiteDatabase db = this.getWritableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                Estructura_BBDD.COLUMNA1,
                Estructura_BBDD.COLUMNA2,
                Estructura_BBDD.COLUMNA3,
                Estructura_BBDD.COLUMNA4,
                Estructura_BBDD.COLUMNA5,
                Estructura_BBDD.COLUMNA6,
                Estructura_BBDD.COLUMNA7,
                Estructura_BBDD.COLUMNA8
        };

        // Filter results WHERE "campo_bbdd" = 'dato'
        String selection;
        String[] selectionArgs = {dato};
        if(where) {
            selection = campo_bbdd + " " + restriccion + " ?";
        }else{
            selection = null;
            selectionArgs = null;
        }

        // How you want the results sorted in the resulting Cursor
        String sortOrder;
        if(order) {
            sortOrder = campo_ordenar + " ASC";
        }else{
            sortOrder = null;
        }

        Cursor c = db.query(
                Estructura_BBDD.TABLE_NAME,               // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                            // don't group the rows
                null,                             // don't filter by row groups
                sortOrder                                 // The sort order
        );

        return c;
    }


    /*
    * ELIMINA LOS EVENTOS
    * */
    public void eliminaEventos(String nombre, int n){
        final SQLiteDatabase db = this.getWritableDatabase();

        String consulta;
        if(n == 0) {
            consulta = "DELETE FROM eventos WHERE nombre='"+nombre+"'";
        }
        else{
            consulta = "DELETE FROM eventos WHERE nombre='"+nombre+"' AND repeticiones='"+n+"' ";
        }

        // Borramos de la tabla los valores que coinciden con la seleccion
        db.execSQL(consulta);
        //db.delete(Estructura_BBDD.TABLE_NAME, selection, selectionArgs);
    }

}
