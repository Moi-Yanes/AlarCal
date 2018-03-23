package moi.com.apps.alarcal;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

/**
 * Created by Moi on 02/03/2018.
 */

public class NotificationHelper extends ContextWrapper {

    /*
    * Variables
    */
    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), android.R.drawable.ic_dialog_alert);
    NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
    public static final String channelID = "chanel1ID";
    public static final String chanel1Name = "Channel 1";
    private NotificationManager mManager;
    Context contexto;

    public static int value = 0;
    public static final int NOTIFICATION_ID = 237;


    /*
    * Metodos
    */
    public NotificationHelper(Context base) {
        super(base);
        contexto = base;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ){
            createChannels();
        }
        else if(Build.VERSION.SDK_INT >= 21 ){
            getManager();
        }
        else{
            Toast.makeText(this,"No es posible generar la notificacion",Toast.LENGTH_SHORT).show();
        }
    }


    @TargetApi(Build.VERSION_CODES.O)
    public void createChannels(){
        NotificationChannel channel1 = new NotificationChannel(channelID, chanel1Name, NotificationManager.IMPORTANCE_HIGH);
        channel1.enableLights(true);
        channel1.enableVibration(true);
        channel1.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        channel1.setLightColor(R.color.colorPrimary);
        channel1.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        getManager().createNotificationChannel(channel1);

    }


    public NotificationManager getManager(){
        if(mManager == null){
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return  mManager;
    }



    public NotificationCompat.Builder getChannel1Notification(String title, String mesagge){

        value = value + 1;
        inboxStyle.setBigContentTitle("AlarCal");
        inboxStyle.addLine(title + " - " + mesagge);

        Intent notifyIntent;
        if(value == 1) {
            notifyIntent = new Intent(this, EditEvento.class);
            notifyIntent.putExtra("nombre", title);
        }
        else
            notifyIntent = new Intent(this, MainActivity.class);

        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent notifyPendingIntent = PendingIntent.getActivity(this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelID)
                .setContentTitle("Tienes " +value+ " eventos")
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentText("Tienes " +value+ " eventos")
                .setSmallIcon(R.mipmap.ic_launcher)     // required
                .setContentIntent(notifyPendingIntent)
                .setAutoCancel(true)
                .setStyle(inboxStyle)
                .setLights(getResources().getColor(R.color.greenCalendar),5000, 5000)
                .setDefaults(Notification.FLAG_SHOW_LIGHTS);

        builder.getNotification().flags |= Notification.FLAG_AUTO_CANCEL;
        mManager.notify("App Name",NOTIFICATION_ID,builder.build());

        return null;
    }
}
