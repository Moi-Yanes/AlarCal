package moi.com.apps.alarcal;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static moi.com.apps.alarcal.R.color.greenCalendar;


public class MainActivity extends AppCompatActivity {

    /*
    *   Variables
    */
    private CalendarView cal;
    private TextView t_dia;
    private TextView t_ano;
    private TextView t_fecha_selec;
    private ScrollView scrollview;
    private LinearLayout grideventos;
    private Button prox_eventos;
    private Button add_eventos;
    private String fecha_seleccionada;

    final BBDD helper = new BBDD(this);
    int height;

    /*
    *   Metodos
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        height = Resources.getSystem().getDisplayMetrics().heightPixels;

        //Asociando variables con sus views
        cal                 = (CalendarView) findViewById(R.id.calendario);
        t_fecha_selec       = (TextView) findViewById(R.id.textofecha);
        t_dia               = (TextView) findViewById(R.id.textview_dia);
        t_ano               = (TextView) findViewById(R.id.textview_año);
        scrollview          = (ScrollView) findViewById(R.id.scrollView1);
        grideventos         = (LinearLayout) findViewById(R.id.grideventos);
        prox_eventos        = (Button) findViewById(R.id.prox_events);
        add_eventos         = (Button) findViewById(R.id.add_events);


        //Obtener fecha actual
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String todaydate = sdf.format(new Date(cal.getDate()));
        String dia = todaydate.substring(0,2);
        String mes = todaydate.substring(3,5);
        String ano = todaydate.substring(6,10);


        // Dando formato al texto para mostrar dia en imagen de calendario
        t_dia.setBackgroundColor(getResources().getColor(greenCalendar));
        t_dia.setGravity(Gravity.CENTER);
        t_dia.setTextColor(Color.BLACK);
        t_dia.setText(dia);


        // Dando formato al texto para mostrar año en la barra superior
        t_ano.setGravity(Gravity.CENTER_VERTICAL);
        t_ano.setTextColor(Color.BLACK);
        t_ano.setTextSize(25);
        t_ano.setText(ano);


        //Dando formato al texto que muestra la fecha seleccionada por el usuario
        t_fecha_selec.setGravity(Gravity.CENTER);
        t_fecha_selec.setTextColor(Color.BLACK);
        t_fecha_selec.setTextSize(18);
        t_fecha_selec.setPadding(30,5,0,0);
        t_fecha_selec.setBackgroundColor(Color.parseColor("#e0e0e0"));


        //Dando formato a ScrollView
        scrollview.setBackgroundColor(Color.parseColor("#e0e0e0"));


        //Establecer informacion por defecto al iniciar la app, una imagen y un texto informativo
        estableceImagenScrollview(R.drawable.ic_touch_app_black_36dp, 0);
        TextView ev  = new TextView(getApplicationContext());
        ev.setGravity(Gravity.CENTER);
        ev.setTextColor(Color.DKGRAY);
        ev.setTextSize(18);
        ev.setText("Selecciona una fecha");
        grideventos.addView(ev,1);


        // Si pulsamos en un dia del calendario
        cal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
                muestraInfoDiaSeleccionado(year,month,day);
                fecha_seleccionada = ""+year+"/"+month+"/"+day;
            }
        });


        //Inicia el servicio si no esta iniciado. Por ejemplo en el que caso de que hayamos instalado la app recientemente
        if(!isMyServiceRunning()) {
            Log.d("ServiceAlarcalRunning", "Iniciando el servicio");

            Intent intentMemoryService = new Intent(getApplicationContext(), AlarmService.class);
            intentMemoryService.setAction("ServicioAlarcal");
            startService(intentMemoryService);
        }else{
            Log.d("ServiceAlarcalRunning", "El servicio ya esta en ejecucion");
        }
    }



    /*
    * MUESTRA LOS EVENTOS PARA UNA FECHA SELECCIONADA O PARA EL DIA ACTUAL(AL INICIAR LA APP)
    * */
    public void muestraInfoDiaSeleccionado(int year, int month, int day){

        grideventos.removeAllViews();

        //Establecemos dia y año en la parte superior de la app
        t_dia.setText(""+day);
        t_ano.setText(""+year);

        // Obtenemos la fecha actual pero con nombres largos no solo en numeros
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE/dd/MMMM/yyyy");
        String[] fecha_format = sdf.format(calendar.getTime()).split("/");
        String dia_selec = fecha_format[0].substring(0, 1).toUpperCase() + fecha_format[0].substring(1);
        String mes_selec = fecha_format[2].substring(0, 1).toUpperCase() + fecha_format[2].substring(1);

        // Añadimos la fecha al textview que muestra la fecha seleccionada
        t_fecha_selec.setText(dia_selec +" "+ day +" de "+ mes_selec);


        //Formateo el día y el mes obtenido: antepone el 0 si son menores de 10
        int tam_day             = Integer.toString(day).length();
        int tam_month           = Integer.toString(month).length();
        String diaFormateado    = (day < 10 && tam_day<2)? "0" + String.valueOf(day):String.valueOf(day);
        String mesFormateado    = (month+1 < 10 && tam_month<2)? "0" + String.valueOf(month+1):String.valueOf(month+1);
        String fechaFormateada  = diaFormateado+"/"+mesFormateado+"/"+year;


        //Consultamos si ese dia hay eventos
        Cursor consulta = helper.consultaEventos(Estructura_BBDD.COLUMNA2,fechaFormateada, "=",Estructura_BBDD.COLUMNA3,true,true);
        if (consulta.moveToFirst()) {
            do {
                String nombre       = consulta.getString(0);
                String fecha_ev     = consulta.getString(1);
                String hora_ev      = consulta.getString(2);
                String fecha_fin    = consulta.getString(3);
                String hora_fin     = consulta.getString(4);
                String desc         = consulta.getString(5);

                crearTextViewEventos(nombre,fecha_ev,hora_ev,fecha_fin,hora_fin,desc);
            } while(consulta.moveToNext());
        }else {
            crearTextViewEventos(null,null,null,null,null,null);
        }

        //Hacemos visibles las opciones para añadir eventos o consultar proximos eventos
        prox_eventos.setVisibility(View.VISIBLE);
        add_eventos.setVisibility(View.VISIBLE);
    }



    /*
    * CREACION DE TEXTVIEW DE EVENTOS
    * */
    public TextView crearTextViewEventos (String n,String fev, String hev, String ffin, String hfin, String d){

        final TextView ev  = new TextView(getApplicationContext());
        ev.setGravity(Gravity.CENTER_VERTICAL);
        ev.setTextColor(Color.DKGRAY);
        ev.setTextSize(18);
        ev.setClickable(true);



        if(n == null ) {
            estableceImagenScrollview(R.drawable.ic_face_black_24dp,0);

            ev.setGravity(Gravity.CENTER);
            ev.setText("No hay eventos");
        }
        else {

            //Establecer largo e imagen en el textview
            ev.setPadding(0,0,32,0);
            ev.setWidth(grideventos.getWidth());
            ev.setBackgroundColor(Color.WHITE);
            ev.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_vertical_bar2, 0, R.drawable.ic_delete_black_24dp,0);


            //Establecer margenes en el textview
            LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            llp.setMargins(45, 0, 45, 16); // llp.setMargins(left, top, right, bottom);
            ev.setLayoutParams(llp);


            //Establecer texto con estilo a mostrar y evento
            SpannableString ss=  new SpannableString(n +"\n" + hev);
            ss.setSpan(new ForegroundColorSpan(Color.BLACK), 0, n.length(), 0);
            ss.setSpan(new StyleSpan(Typeface.BOLD), 0, n.length(), 0);
            int t = hev.length() + n.length();
            ss.setSpan(new RelativeSizeSpan(0.8f), n.length(), t, 0);
            ev.setText(ss);

            //Añadir evento al textview
            setOnclickEventOnDrawableTextview(ev,n, fev);
        }

        grideventos.addView(ev);
        return ev;
    }



    /*
    *
    * */
    public void setOnclickEventOnDrawableTextview(final TextView tev, final String n, final String fev){

        tev.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    //Evento en la imagen de la derecha
                    if( event.getRawX() >= (tev.getWidth() - tev.getCompoundDrawables()[2].getBounds().width()-32) ) {
                        tev.getCompoundDrawables()[2].setColorFilter(new PorterDuffColorFilter(getResources().getColor(greenCalendar), PorterDuff.Mode.SRC_ATOP)); //src_atop o dst_atop
                        return true;
                    }
                }
                if(event.getAction() == MotionEvent.ACTION_UP) {

                    //Evento en el lado izquierdo del textview hasta la imagen delete
                    if( event.getRawX() < (tev.getWidth() - tev.getCompoundDrawables()[2].getBounds().width()-32) ) {
                        //redirecciona a activity editar evento
                        Intent i = new Intent(MainActivity.this, EditEvento.class);
                        i.putExtra("nombre", n);
                        i.putExtra("fecha_evento", fev);
                        startActivity(i);

                        return true;
                    }

                    //Evento en la imagen de la derecha
                    if( event.getRawX() >= (tev.getWidth() - tev.getCompoundDrawables()[2].getBounds().width()-32) ) {
                        tev.getCompoundDrawables()[2].setColorFilter(new PorterDuffColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN));
                        Toast.makeText(getApplicationContext(), "Evento "+n+" eliminado", Toast.LENGTH_SHORT).show();
                        eliminaEvento(tev, n, fev);
                        return true;
                    }
                }
                return false;
            }
        });
    }



    /*
    * ESTABLECE UNA IMAGEN DETERMINADA EN EL SCROLLVIEW DEL MAINACTIVITY
    * */
    public void estableceImagenScrollview(int imagen, int marginTop){

        ImageView img = new ImageView(getApplicationContext());
        img.setImageResource(imagen);

        int tam_bar             = dpToPx(49);
        int tam_calendar        = dpToPx(340);
        int tam_layout_text     = dpToPx(50);
        int tam_pxl_scrollview  = height - tam_bar - tam_calendar - tam_layout_text;
        int dp_scrollview       = pxToDp(tam_pxl_scrollview);
        int tamttextofecha      = pxToDp(t_fecha_selec.getHeight());
        int tam_img             = pxToDp(img.getHeight());

        if(add_eventos.getVisibility() == View.VISIBLE){
            img.setPadding(0, ((dp_scrollview/2)-tamttextofecha-tam_img)+marginTop, 0, 0);
        }
        else{
            img.setPadding(0, 0, 0, 0);
        }

        grideventos.addView(img,0);
    }



    private int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }


    private int pxToDp(float px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }



    /*
    * ELIMINA UN EVENTO GUARDADO
    * */
    public void eliminaEvento(View vista, String nombre, String fecha_evento){

        SQLiteDatabase bd = helper.getWritableDatabase();
        Cursor fila = bd.rawQuery("SELECT * FROM eventos WHERE nombre='"+nombre+"' AND fecha_evento='"+fecha_evento+"' ", null); /*  */
        if(fila.moveToFirst()){
            int n = fila.getInt(7);
            Log.d("TAG", "Eliminando evento repeticion: " + n);
            helper.eliminaEventos(nombre, n);
        }


        //Vuelve a consultar ese dia para mostrar la info actualizada
        int day = Integer.parseInt(fecha_seleccionada.split("/")[2]);
        int month = Integer.parseInt(fecha_seleccionada.split("/")[1]);
        int year = Integer.parseInt(fecha_seleccionada.split("/")[0]);
        muestraInfoDiaSeleccionado(year,month,day);
    }



    /*
    * EJECUTA EL ACTIVITY CREAEVENTO AL HACER CLIC EN AÑADIR UN EVENTO NUEVO
    * */
    public void ejecutaActivityCreaEvento(View vistaEvento){
        Intent i = new Intent(MainActivity.this, CreaEvento.class);
        startActivity(i);
    }



    /*
    * EJECUTA EL ACTIVITY PROXIMOSEVENTOS AL HACER CLIC EN EL BOTON PROXIMOS EVENTOS
    * */
    public void ejecutaActivityProximosEventos(View vistaEvento){
        Intent i = new Intent(MainActivity.this, proximosEventos.class);
        startActivity(i);
    }



    /*
    * COMPRUEBA SI EL SERVICIO YA ESTA EN EJECUCION O NO
    * */
    private boolean isMyServiceRunning() {
        ActivityManager manager = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (AlarmService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    /*
    * SALIR DE LA APP AL DARLE AL BOTON BACK DESDE EL MAINACTIVITY
    * */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
