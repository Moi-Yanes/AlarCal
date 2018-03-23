package moi.com.apps.alarcal;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by Moi on 02/03/2018.
 */

public class AlarmService extends Service {

    /*
    * Variables
    */

    //Alarm static variables
    private static final String TAG = AlarmService.class.getSimpleName();
    TimerTask timerTask;
    Timer timer;

    //Notificacion
    private NotificationHelper mNotificationHelper;

    //Base de datos
    private BBDD helper;
    private Cursor fila;
    private SQLiteDatabase bd;

    //Resultados consulta
    private String descripcion,nombre, fecha_ev, hora_ev;

    //Obtener fechas
    Calendar calendario;
    int hora,min,dia,mes,ano;
    String fecha_sistema, hora_sistema, fecha_sistema_final;

    //Contexto
    Context contexto;


    /*
    *  METODOS
    */
    public AlarmService() {}


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        Log.d(TAG, "Servicio creado...");

        contexto = this;
        mNotificationHelper = new NotificationHelper(this);
        helper = new BBDD(this);

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(timer != null){
            timer.cancel();
            timer = null;
            timerTask.cancel();
            Log.i(TAG, "Cancelando viejo timer");
        }

        Log.d(TAG, "Servicio iniciado...");

        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {

                //consulta bbdd y comprueba si hay alguna fecha de aviso q coincida con la actual
                calendario = Calendar.getInstance();

                dia = calendario.get(Calendar.DAY_OF_MONTH);
                String dia_f = (dia < 10 && String.valueOf(dia).length()<2)? "0" + String.valueOf(dia):String.valueOf(dia);

                mes = calendario.get(Calendar.MONTH)+1;
                String mes_f = (mes < 10 && String.valueOf(mes).length()<2)? "0" + String.valueOf(mes):String.valueOf(mes);

                ano = calendario.get(Calendar.YEAR);
                hora = calendario.get(Calendar.HOUR_OF_DAY);
                String hora_f = (hora < 10 && String.valueOf(hora).length()<2)? "0" + String.valueOf(hora):String.valueOf(hora);

                min = calendario.get(Calendar.MINUTE);
                String min_f = (min < 10 && String.valueOf(min).length()<2)? "0" + String.valueOf(min):String.valueOf(min);

                fecha_sistema=ano+"-"+mes_f+"-"+dia_f;
                hora_sistema=hora_f+":"+min_f+":00";
                fecha_sistema_final = fecha_sistema+" "+ hora_sistema;

                Log.d("TAG", fecha_sistema_final);
                helper = new BBDD(contexto);
                bd = helper.getWritableDatabase();
                if(bd!=null) {
                    fila = bd.rawQuery("SELECT * FROM eventos WHERE fecha_aviso=Datetime('"+fecha_sistema_final+"')", null); /*  */
                    if(fila.moveToFirst()){
                        do {
                            nombre      = fila.getString(0);
                            descripcion = fila.getString(5);
                            fecha_ev    = fila.getString(1);
                            hora_ev    = fila.getString(2);


                            Log.d(TAG, "Service execute. Datos: " + nombre + " -> " + fecha_ev +" "+ hora_ev);
                            createTriggerNotification(nombre, hora_ev);
                        } while (fila.moveToNext());
                    }else{
                        Log.d(TAG, "Service execute. No hay coincidencias");
                    }
                }
                bd.close();
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 60000); //cada 1 segundo


        return START_STICKY;
    }



    @Override
    public void onDestroy() {
        if(timer != null){
            timer.cancel();
            timer = null;
            timerTask.cancel();
            Log.i(TAG, "Cancelando viejo timer");
        }

        mNotificationHelper.getManager().cancel(NotificationHelper.NOTIFICATION_ID);
        super.onDestroy();

        Log.d(TAG, "Servicio destruido...");
    }



    public void createTriggerNotification(String title, String mensaje){
        mNotificationHelper.getChannel1Notification(title, mensaje);
    }


}