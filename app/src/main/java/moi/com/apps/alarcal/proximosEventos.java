package moi.com.apps.alarcal;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static moi.com.apps.alarcal.R.color.greenCalendar;

/**
 * Created by Moi on 01/03/2018.
 */

public class proximosEventos extends AppCompatActivity {


    /*
    * Variables
    */
    private LinearLayout LayoutNextEvents;
    private LinearLayout LayoutPrevEvents;
    private ScrollView ScrollViewNextEvents;
    private ScrollView ScrollViewPrevEvents;
    private CheckBox checbok_mostrar_repeticiones;
    private int cont_next, cont_prev;

    final BBDD helper = new BBDD(this);


    /*
    * METODOS
    */
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.muestra_eventos);


        // Asignacion de valores a variables
        LayoutNextEvents             = (LinearLayout) findViewById(R.id.scrollView_layout_prox_eventos);
        LayoutPrevEvents             = (LinearLayout) findViewById(R.id.scrollView_layout_anteriores_eventos);
        ScrollViewNextEvents         = (ScrollView) findViewById(R.id.scrollView_prox_eventos);
        ScrollViewPrevEvents         = (ScrollView) findViewById(R.id.scrollView_anteriores_eventos);
        checbok_mostrar_repeticiones = (CheckBox) findViewById(R.id.checbok_mostrar_repeticiones);

        muestraEventos(true);
    }



    public void muestraEventosRepetidos(View vista){

        if(checbok_mostrar_repeticiones.isChecked()){
            Log.d("TAG", "SI ESTA PULSADO");
            muestraEventos(true);
        }else{
            Log.d("TAG", "NO ESTA PULSADO");
            muestraEventos(false);
        }
    }


    public void muestraEventos(boolean repeticiones){
        LayoutPrevEvents.removeAllViews();
        LayoutNextEvents.removeAllViews();

        //inicializo contadores
        cont_next = 0;
        cont_prev = 0;


        //Textviews para el caso de que no hayan eventos proximos o antiguos
        final TextView ev  = new TextView(getApplicationContext());
        ev.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
        ev.setTextColor(Color.DKGRAY);
        ev.setTextSize(18);
        ev.setText("No hay eventos");

        final TextView ev1  = new TextView(getApplicationContext());
        ev1.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
        ev1.setTextColor(Color.DKGRAY);
        ev1.setTextSize(18);
        ev1.setText("No hay eventos");


        //Para calcular el tamaño del scrollview de next events (el de prev events no hace falta pq miden lo mismo ambos)
        ScrollViewNextEvents.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                ScrollViewNextEvents.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                int tamscrollview = ScrollViewNextEvents.getHeight();
                ev.setPadding(0,tamscrollview/2, 0, 0);
                ev1.setPadding(0,tamscrollview/2, 0, 0);
            }
        });


        //Llamada a los metodos que consultan la base de datos y rellena los layouts con la info de los eventos
        Cursor consulta = null;
        SQLiteDatabase bd = helper.getWritableDatabase();
        if(!repeticiones)
            consulta = bd.rawQuery("SELECT * FROM eventos WHERE repeticiones=0", null);
        else
            consulta = helper.consultaEventos(null,null,null,null,false,false);


        if (consulta.moveToFirst()) {
            do {
                String nombre       = consulta.getString(0);
                String fecha_ev     = consulta.getString(1);
                String hora_ev      = consulta.getString(2);
                String fecha_av     = consulta.getString(3);
                String hora_av      = consulta.getString(4);
                String desc         = consulta.getString(5);
                String fecha        = consulta.getString(6);


                //Calcular fechas
                SimpleDateFormat sdf  = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
                Date eventoDate = null, avisoDate = null;
                try {
                    eventoDate  = sdf.parse(fecha_ev);
                    avisoDate   = sdf2.parse(fecha);
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                if( eventoDate.before(new Date()) ){

                    if( avisoDate.getTime() < new Date().getTime() ){
                        crearTextViewEventos(nombre,fecha_ev,hora_ev,fecha_av,hora_av,desc,LayoutPrevEvents);
                        cont_prev++;
                    }
                    else if( avisoDate.getTime() > new Date().getTime() ){
                        crearTextViewEventos(nombre,fecha_ev,hora_ev,fecha_av,hora_av,desc,LayoutNextEvents);
                        cont_next++;
                    }

                }
                else if(eventoDate.after(new Date())){
                    crearTextViewEventos(nombre,fecha_ev,hora_ev,fecha_av,hora_av,desc,LayoutNextEvents);
                    cont_next++;
                }

            } while(consulta.moveToNext());
        }

        if(cont_prev == 0){ LayoutPrevEvents.addView(ev1); }
        if(cont_next == 0){ LayoutNextEvents.addView(ev);  }
    }


    public TextView crearTextViewEventos (String n,String fev, String hev, String fav, String hav, String d, LinearLayout layout){

        final TextView ev  = new TextView(getApplicationContext());
        ev.setGravity(Gravity.CENTER_VERTICAL);
        ev.setTextColor(Color.DKGRAY);
        ev.setTextSize(18);
        ev.setClickable(true);


        //Establecer largo e imagen en el textview
        ev.setPadding(0,0,32,0);
        ev.setWidth(layout.getWidth());
        ev.setBackgroundColor(Color.WHITE);
        ev.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_vertical_bar2, 0, R.drawable.ic_delete_black_24dp,0);


        //Establecer margenes en el textview
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        llp.setMargins(0, 0, 0, 8); // llp.setMargins(left, top, right, bottom);
        ev.setLayoutParams(llp);


        //Establecer texto con estilo a mostrar y evento
        SpannableString ss=  new SpannableString(n +"\n" + fev + " - " + hev);
        ss.setSpan(new ForegroundColorSpan(Color.BLACK), 0, n.length(), 0);
        ss.setSpan(new StyleSpan(Typeface.BOLD), 0, n.length(), 0);
        int t = fev.length() + hev.length() + n.length() + 3;
        ss.setSpan(new RelativeSizeSpan(0.8f), n.length(), t, 0);
        ev.setText(ss);

        //Añadir evento al textview
        setOnclickEventOnDrawableTextview(ev, n, fev);

        layout.addView(ev);
        return ev;
    }


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
                        Intent i = new Intent(getApplicationContext(), EditEvento.class);
                        Log.d("TAG", "NOMBRE DEL EVENTO DESDE PROXEVENTS: "+ n);
                        i.putExtra("nombre", n);
                        i.putExtra("fecha_evento", fev);
                        startActivity(i);

                        return true;
                    }

                    //Evento en la imagen de la derecha
                    if( event.getRawX() >= (tev.getWidth() - tev.getCompoundDrawables()[2].getBounds().width()-32) ) {
                        tev.getCompoundDrawables()[2].setColorFilter(new PorterDuffColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN));
                        Toast.makeText(getApplicationContext(), "Evento "+n+" eliminado", Toast.LENGTH_SHORT).show();

                        SQLiteDatabase bd = helper.getWritableDatabase();
                        Cursor fila = bd.rawQuery("SELECT * FROM eventos WHERE nombre='"+n+"' AND fecha_evento='"+fev+"' ", null); /*  */
                        if(fila.moveToFirst()){
                            int entero = fila.getInt(7);
                            Log.d("TAG", "Eliminando evento repeticion: " + entero);
                            helper.eliminaEventos(n, entero);
                        }
                        muestraEventosRepetidos(tev);
                        return true;
                    }
                }
                return false;
            }
        });
    }


    public void ejecutaMain(View vista) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}
