package moi.com.apps.alarcal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Moi on 03/03/2018.
 */

public class Monitor extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        /*Intent service = new Intent(context.getApplicationContext(), AlarmService.class);
        service.setAction("ServicioAlarcal");
        //service.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startService(service);*/

        Intent intentMemoryService = new Intent(context, AlarmService.class);
        intentMemoryService.setAction("ServicioAlarcal");
        context.startService(intentMemoryService); //Iniciar servicio
    }
}
