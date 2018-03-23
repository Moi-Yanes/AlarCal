package moi.com.apps.alarcal;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Moi on 06/03/2018.
 */

public class EditEvento extends AppCompatActivity {


    /**************
     *  VARIABLES  *
     ***************/
    //Calendario para obtener fecha & hora
    public final Calendar c = Calendar.getInstance();

    //Variables para obtener la fecha y hora
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);
    final int hora = c.get(Calendar.HOUR_OF_DAY);
    final int minuto = c.get(Calendar.MINUTE);

    private BBDD helper;

    private EditText nombre, descripcion, fecha_evento, fecha_fin, hora_evento, hora_fin, n_repeticiones, recordatorios;        //variables del formulario
    private TextInputLayout layout1,layout2,layout3,layout4,layout5;                                                            //variables del formulario
    private Button btnrepe, btnrecord;
    private TextView textview_repe;
    private RelativeLayout layout_repe_edit;
    private String nombre_evento, fecha_ev;                                                                                     //Variables del intent
    private Boolean fechas_iguales;                                                                                             //Variables para validacion de fechas
    private String repeticion, record;                                                                                          //variables para determinar las repeticiones y recordatorios

    String fecha_evento_vieja, repeticiones_string_bbdd, recordatorio_string_bbdd;
    int repeticiones_value_bbdd;



    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.evento_edit);

        nombre              = (EditText) findViewById(R.id.nombre_evento_edit);
        fecha_evento        = (EditText) findViewById(R.id.fecha_evento_edit);
        hora_evento         = (EditText) findViewById(R.id.hora_evento_edit);
        fecha_fin           = (EditText) findViewById(R.id.fecha_fin_edit);
        hora_fin            = (EditText) findViewById(R.id.hora_fin_edit);
        descripcion         = (EditText) findViewById(R.id.descripcion_edit);
        n_repeticiones      = (EditText) findViewById(R.id.editTextEleccion_edit);
        recordatorios       = (EditText) findViewById(R.id.editTextEleccionRecordatorio_edit);
        btnrepe             = (Button) findViewById(R.id.repeticiones_edit);
        btnrecord           = (Button) findViewById(R.id.recordatorio_edit);
        textview_repe       = (TextView) findViewById(R.id.textView_repeticiones);
        layout_repe_edit    = (RelativeLayout) findViewById(R.id.LayoutRelative_repe_edit);


        //onclick events
        fecha_evento.setOnClickListener(new View.OnClickListener()       { public void onClick(View v) { obtenerFecha(findViewById(R.id.image_fecha_evento_edit)); }});
        fecha_fin.setOnClickListener(new View.OnClickListener() { public void onClick(View v) { obtenerFecha(findViewById(R.id.image_fecha_fin_edit)); }});
        hora_evento.setOnClickListener(new View.OnClickListener()        { public void onClick(View v) { obtenerHora(findViewById(R.id.image_hora_evento_edit)); }});
        hora_fin.setOnClickListener(new View.OnClickListener()         { public void onClick(View v) { obtenerHora(findViewById(R.id.image_hora_fin_edit)); }});


        //manejador de la bbdd
        helper = new BBDD(this);


        //obtenemos los datos que se pasan de otras actividades a esta
        Intent i = getIntent();
        nombre_evento   = i.getStringExtra("nombre");
        fecha_ev        = i.getStringExtra("fecha_evento");
        Log.d("TAG", "NOMBRE A EDITAR: " + nombre_evento);


        //rellenamos el formulario con los datos del evento
        rellenaFormulario(nombre_evento, fecha_ev);
    }


    /*
    * Si la activity esta en Pause y se envia otro intent distinto al anterior
    * */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        nombre_evento   = intent.getStringExtra("nombre");
        fecha_ev        = intent.getStringExtra("fecha_evento");
        Log.d("TAG", "NOMBRE A EDITAR onNewIntent: " + nombre_evento);

        rellenaFormulario(nombre_evento, fecha_ev);
    }



    /*
    * Recoge los resultados de las activity llamadas desde esta activity a traves del metodo StartActivityForResult
    * */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_CANCELED) {
            if (requestCode == 200) {
                Bundle datos = data.getExtras();
                repeticion   = datos.getString("opcion");

                if(repeticion != null)
                        n_repeticiones.setText(repeticion);
            }

            if(requestCode == 201){
                Bundle datos = data.getExtras();
                record       = datos.getString("opcion");

                if(record != null)
                    recordatorios.setText(record);
            }
        }
    }



    /*
    * RELLENA EL FORMULARIO DE EDICION DEL EVENTO CON LOS DATOS DE ESE EVENTO
    * */
    public void rellenaFormulario(String n, String fev){

        SQLiteDatabase bd = helper.getWritableDatabase();
        Cursor fila = bd.rawQuery("SELECT * FROM eventos WHERE nombre='"+n+"' AND fecha_evento='"+fev+"' ", null);
        if(fila.moveToFirst()){
            String nombre            = fila.getString(0);
            String fecha_ev          = fila.getString(1);
            String hora_ev           = fila.getString(2);
            String fecha_fin         = fila.getString(3);
            String hora_fin          = fila.getString(4);
            String desc              = fila.getString(5);
            repeticiones_value_bbdd  = fila.getInt(7);
            repeticiones_string_bbdd = fila.getString(8);
            recordatorio_string_bbdd = fila.getString(9);
            fecha_evento_vieja       = fecha_ev;

            //establecer textos en textviews o edittexts
            this.nombre.setText(nombre);
            this.fecha_evento.setText(fecha_ev);
            this.hora_evento.setText(hora_ev);
            this.hora_fin.setText(hora_fin);
            this.fecha_fin.setText(fecha_fin);
            this.descripcion.setText(desc);
            Log.d("TAG", "Repeticiones string: "+repeticiones_string_bbdd);
            Log.d("TAG", "Recordatorios string: "+recordatorio_string_bbdd);
            this.n_repeticiones.setText(repeticiones_string_bbdd);
            this.recordatorios.setText(recordatorio_string_bbdd);


            //deshabilitar la opcion de elegir las repeticiones sino es el evento principal de la serie
            if(repeticiones_value_bbdd != 0){
                LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(0,0);
                params1.setMargins(0, 0, 0, 0);
                layout_repe_edit.setLayoutParams(params1);

                layout_repe_edit.setVisibility(View.INVISIBLE);
            }
            else{
                float d = getApplicationContext().getResources().getDisplayMetrics().density; //para establecer los valores en dpi
                LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,(int)(50 * d));
                params1.setMargins((int)(8 * d), (int)(10 * d), (int)(8 * d), 0);
                layout_repe_edit.setLayoutParams(params1);

                layout_repe_edit.setVisibility(View.VISIBLE);
            }
        }
    }



    /*
    * GUARDA UN EVENTO EN LA BASE DE DATOS
    * */
    public void GuardaEventoEdit(View vista){

        layout1             = (TextInputLayout) findViewById(R.id.textinputlayout1);
        layout2             = (TextInputLayout) findViewById(R.id.textinputlayout2);
        layout3             = (TextInputLayout) findViewById(R.id.textinputlayout3);
        layout4             = (TextInputLayout) findViewById(R.id.textinputlayout4);
        layout5             = (TextInputLayout) findViewById(R.id.textinputlayout5);

        if( !validaFormulario() ){
            return;
        }

        // Obtenemos la base de datos en modo escritura
        SQLiteDatabase db = helper.getWritableDatabase();


        //Obtenemos la fecha de aviso a partir de la opcion de recordatorios seleccionada
        String fecha_aviso = calculaFechaAviso(fecha_evento.getText().toString(), hora_evento.getText().toString() );


        // Crear un contenedor de valores con las parejas clave-valor de datos que queremos guardar en la bbdd
        ContentValues values = new ContentValues();
        values.put(Estructura_BBDD.COLUMNA1, nombre.getText().toString());
        values.put(Estructura_BBDD.COLUMNA2, fecha_evento.getText().toString());
        values.put(Estructura_BBDD.COLUMNA3, hora_evento.getText().toString());
        values.put(Estructura_BBDD.COLUMNA4, fecha_fin.getText().toString());
        values.put(Estructura_BBDD.COLUMNA5, hora_fin.getText().toString());
        values.put(Estructura_BBDD.COLUMNA6, descripcion.getText().toString());
        values.put(Estructura_BBDD.COLUMNA7, fecha_aviso);
        values.put(Estructura_BBDD.COLUMNA9, n_repeticiones.getText().toString());
        values.put(Estructura_BBDD.COLUMNA10, recordatorios.getText().toString());


        // Si es el evento principal
        if(repeticiones_value_bbdd == 0){
            if(repeticiones_string_bbdd.equals(n_repeticiones.getText().toString())){ //Si no se ha modificado las repeticiones del evento

                db.update(Estructura_BBDD.TABLE_NAME, values, "nombre = '"+nombre.getText().toString()+"' AND repeticiones = '"+repeticiones_value_bbdd+"'", null);
                Log.d("TAG", "Actualizando: " + nombre.getText().toString() + " repeticion: "+ repeticiones_value_bbdd);

            }else{
                //borrar todos los eventos repetidos y crear nuevos eventos de repeticion
                helper.eliminaEventos(nombre.getText().toString(), 0);

                //crear nuevos eventos repetidos con los datos proporcionados
                Object[] objeto = calculaRepeticiones();
                int repeticiones_evento  = (int)objeto[0];
                ArrayList<String> fechas = (ArrayList<String>)objeto[1];

                for (int i=0;i<repeticiones_evento; i++){
                    Log.d("TAG", "FechaTAG: "+fechas.get(i));
                    guardaEventoBBDD(i, fechas.get(i));
                }
            }

        }else{
            db.update(Estructura_BBDD.TABLE_NAME, values, "nombre = '"+nombre.getText().toString()+"' AND repeticiones = '"+repeticiones_value_bbdd+"'", null);
            Log.d("TAG", "Actualizando: " + nombre.getText().toString() + " repeticion: "+ repeticiones_value_bbdd);
        }


        // Mensaje para verificar la insercion
        Toast.makeText(getApplicationContext(), "Evento editado correctamente ", Toast.LENGTH_SHORT).show();

        // Vuelve a la ventana principal
        cancelaCreaEventoEdit(vista);
        db.close();
    }



    public void guardaEventoBBDD(int n, String fecha){

        // Obtenemos la base de datos en modo escritura
        SQLiteDatabase db = helper.getWritableDatabase();


        //Obtenemos la fecha de aviso a partir de la opcion de recordatorios seleccionada
        String fecha_aviso = calculaFechaAviso(fecha, hora_evento.getText().toString() );


        // Crear un contenedor de valores con las parejas clave-valor de datos que queremos guardar en la bbdd
        if(fecha.contains("-")) {
            String[] fecha_arr  = fecha.split("-");
            fecha = fecha_arr[2] + "/" + fecha_arr[1] + "/" + fecha_arr[0];
        }

        ContentValues values = new ContentValues();
        values.put(Estructura_BBDD.COLUMNA1, nombre.getText().toString());
        values.put(Estructura_BBDD.COLUMNA2, fecha);
        values.put(Estructura_BBDD.COLUMNA3, hora_evento.getText().toString());
        if(n == 0)
            values.put(Estructura_BBDD.COLUMNA4, fecha_fin.getText().toString());
        else
            values.put(Estructura_BBDD.COLUMNA4, fecha);
        values.put(Estructura_BBDD.COLUMNA5, hora_fin.getText().toString());
        values.put(Estructura_BBDD.COLUMNA6, descripcion.getText().toString());
        values.put(Estructura_BBDD.COLUMNA7, fecha_aviso);
        values.put(Estructura_BBDD.COLUMNA8, n);
        values.put(Estructura_BBDD.COLUMNA9, n_repeticiones.getText().toString());
        values.put(Estructura_BBDD.COLUMNA10, recordatorios.getText().toString());


        // Insert the new row, returning the primary key value of the new row
        db.insert(Estructura_BBDD.TABLE_NAME, null, values);
        db.close();
    }



    /*
 * CALCULA LA FECHA DE AVISO DEL EVENTO A PARTIR DE LA OPCION DE RECORDATORIO ELEGIDA Y LA FECHA DEL EVENTO
 * */
    public String calculaFechaAviso(String fecha_evento, String hora_evento){


        String[] keys = {"minutos", "dias", "dia", "hora", "semana"};
        String match = "default";
        int value    = 0;
        String fecha_aviso = "";

        for (int i = 0; i < keys.length; i++) {
            if(record == null)
                match = "default";
            else if (record.contains(keys[i])) {
                match = keys[i];
                value = Integer.parseInt(record.split(keys[i])[0].trim());
                break;
            }
        }


        final SimpleDateFormat df  = new SimpleDateFormat( "yyyy-MM-dd" );
        final SimpleDateFormat df2 = new SimpleDateFormat( "HH:mm:ss" );
        Date fecha = null, date_hora = null;
        String[] fecha_arr;

        String hora         = hora_evento.substring(0,5);
        if(fecha_evento.contains("/"))
            fecha_arr  = fecha_evento.split("/");
        else
            fecha_arr  = fecha_evento.split("-");


        try {
            fecha       = df.parse( fecha_arr[2] + "-" + fecha_arr[1] + "-" + fecha_arr[0] + " " + hora.substring(0,2) + ":" + hora.substring(3,5) + ":00" );
            date_hora   = df2.parse( hora.substring(0,2) + ":" + hora.substring(3,5) + ":00" );
        } catch (ParseException e) {
            e.printStackTrace();
        }


        final Calendar cal = Calendar.getInstance();            cal.setTime( fecha );
        final Calendar cal2 = Calendar.getInstance();           cal2.setTime( date_hora);
        switch (match) {
            case "minutos":
                cal2.add( Calendar.MINUTE, -value );
                fecha_aviso = fecha_arr[2] + "-" + fecha_arr[1] + "-" + fecha_arr[0] + " " + df2.format(cal2.getTime());
                Log.d("TAG","fecha: "+ fecha_aviso);
                break;

            case "hora":
                cal2.add( Calendar.HOUR_OF_DAY, -value );
                fecha_aviso = fecha_arr[2] + "-" + fecha_arr[1] + "-" + fecha_arr[0] + " " + df2.format(cal2.getTime());
                Log.d("TAG","fecha: "+ fecha_aviso);
                break;

            case "dia":
                cal.add( Calendar.DAY_OF_MONTH, -value );
                fecha_aviso = df.format(cal.getTime()) + " " + hora.substring(0,2) + ":" + hora.substring(3,5) + ":00";
                Log.d("TAG","fecha: " +  fecha_aviso);
                break;

            case "dias":
                cal.add( Calendar.DAY_OF_MONTH, -value );
                fecha_aviso = df.format(cal.getTime()) + " " + hora.substring(0,2) + ":" + hora.substring(3,5) + ":00";
                Log.d("TAG","fecha: " +  fecha_aviso);
                break;

            case "semana":
                cal.add( Calendar.WEEK_OF_MONTH, -value );
                fecha_aviso = df.format(cal.getTime()) + " " + hora.substring(0,2) + ":" + hora.substring(3,5) + ":00";
                Log.d("TAG","fecha: " +  fecha_aviso);
                break;

            default:
                fecha_aviso = fecha_arr[2] + "-" + fecha_arr[1] + "-" + fecha_arr[0] + " " + hora.substring(0,2) + ":" + hora.substring(3,5) + ":00";
                Log.d("TAG","A la hora del evento "+ fecha_aviso);
                break;
        }

        fecha_arr   = fecha_aviso.split(" ");
        fecha_aviso = fecha_arr[0].split("-")[2] + "-" + fecha_arr[0].split("-")[1] + "-" + fecha_arr[0].split("-")[0] + " " + fecha_arr[1];
        return fecha_aviso;
    }



    /*CALCULA CUANTAS REPETICIONES TENDRA EL EVENTO; DEVUELVE EL NUMERO DE REPETICIONES Y UN ARRAYLIST CON LAS FECHAS*/
    public Object[] calculaRepeticiones(){

        String[] keys = {"Diariamente", "Dias de semana", "Semanalmente", "Cada dos semanas", "Mensualmente", "Anualmente"};
        ArrayList<String> fechas = new ArrayList<String>();
        String match = "default";
        int numero_repeticiones = 0;

        for (int i = 0; i < keys.length; i++) {
            if(repeticion == null)
                match = "default";
            else if (repeticion.contains(keys[i])) {
                match = keys[i];
                break;
            }
        }

        //Creamos objetos de tipo Date a partir de las fechas selccionadas por el usuario
        final SimpleDateFormat df  = new SimpleDateFormat( "yyyy-MM-dd" );
        Date fecha_inicial = null, fecha_final = null;
        String[] fecha_ini  = fecha_evento.getText().toString().split("/");
        String[] fecha_f  = fecha_fin.getText().toString().split("/");
        try {
            fecha_inicial   = df.parse( fecha_ini[2] + "-" + fecha_ini[1] + "-" + fecha_ini[0]);
            fecha_final     = df.parse( fecha_f[2] + "-" + fecha_f[1] + "-" + fecha_f[0]);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        final Calendar cal  = Calendar.getInstance();
        final Calendar cal2 = Calendar.getInstance();
        cal.setTime( fecha_inicial );
        cal2.setTime( fecha_final );


        //Determinamos la opcion elegida y comparamos las fechas para saber el numero de repeticiones del evento antes de que finalice
        switch (match) {
            case "Diariamente":
                do{
                    Log.d("TAG", "Fecha nueva: " + df.format(cal.getTime()));
                    fechas.add(df.format(cal.getTime()));
                    cal.add( Calendar.DAY_OF_MONTH, 1);
                    numero_repeticiones++;

                }while (cal.before(cal2) || cal.compareTo(cal2)==0);
                Log.d("TAG", "repeticiones: " + numero_repeticiones );
                break;


            case "Dias de semana":
                do{
                    Log.d("TAG", "Fecha nueva: " + df.format(cal.getTime()));
                    fechas.add(df.format(cal.getTime()));
                    cal.add( Calendar.DAY_OF_MONTH, 1);

                    int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
                    if (dayOfWeek == Calendar.SATURDAY) {               // If it's Saturday skip to Monday
                        cal.add(Calendar.DATE, 2);
                    } else if (dayOfWeek == Calendar.SUNDAY){           // If it's Sunday skip to Monday
                        cal.add(Calendar.DATE, 1);
                    }

                    numero_repeticiones++;

                }while (cal.before(cal2) || cal.compareTo(cal2)==0);
                Log.d("TAG", "repeticiones: " + numero_repeticiones );
                break;


            case "Semanalmente":
                do{
                    Log.d("TAG", "Fecha nueva: " + df.format(cal.getTime()));
                    fechas.add(df.format(cal.getTime()));
                    cal.add( Calendar.DAY_OF_MONTH, 7);
                    numero_repeticiones++;

                }while (cal.before(cal2) || cal.compareTo(cal2)==0);
                Log.d("TAG", "repeticiones: " + numero_repeticiones );
                break;


            case "Cada dos semanas":
                do{
                    Log.d("TAG", "Fecha nueva: " + df.format(cal.getTime()));
                    fechas.add(df.format(cal.getTime()));
                    cal.add( Calendar.DAY_OF_MONTH, 14);
                    numero_repeticiones++;

                }while (cal.before(cal2) || cal.compareTo(cal2)==0);
                Log.d("TAG", "repeticiones: " + numero_repeticiones );
                break;


            case "Mensualmente":
                do{
                    Log.d("TAG", "Fecha nueva: " + df.format(cal.getTime()));
                    fechas.add(df.format(cal.getTime()));
                    cal.add( Calendar.MONTH, 1);
                    numero_repeticiones++;

                }while (cal.before(cal2) || cal.compareTo(cal2)==0);
                Log.d("TAG", "repeticiones: " + numero_repeticiones );
                break;


            case "Anualmente":
                do{
                    Log.d("TAG", "Fecha nueva: " + df.format(cal.getTime()));
                    fechas.add(df.format(cal.getTime()));
                    cal.add( Calendar.YEAR, 1);
                    numero_repeticiones++;

                }while (cal.before(cal2) || cal.compareTo(cal2)==0);
                Log.d("TAG", "repeticiones: " + numero_repeticiones );
                break;


            default:
                numero_repeticiones = 1;
                fechas.add(df.format(cal.getTime()));
                Log.d("TAG", "repeticiones: " + numero_repeticiones );
                break;
        }

        Object[] mixedArray = new Object[2];
        mixedArray[0]=numero_repeticiones;
        mixedArray[1]=fechas;

        return mixedArray;
    }


    /*
    * REGRESA AL USUARIO A LA VENTANA PRINCIPAL DE LA APP
    * */
    public void cancelaCreaEventoEdit(View vista) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }


    /*
    * FUNCION PARA OBTENER LA FECHA SELECCIONADA EN EL DATEPICKER Y ESTABLECER ESA FECHA EN EL TEXTVIEW
    * */
    public void obtenerFecha(final View vista_cal){

        class DatePickerListener implements DatePickerDialog.OnDateSetListener {

            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //Esta variable lo que realiza es aumentar en uno el mes ya que comienza desde 0 = enero
                final int mesActual = month + 1;

                //Formateo el día obtenido: antepone el 0 si son menores de 10
                String diaFormateado = (dayOfMonth < 10)? "0" + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);

                //Formateo el mes obtenido: antepone el 0 si son menores de 10
                String mesFormateado = (mesActual < 10)? "0" + String.valueOf(mesActual):String.valueOf(mesActual);


                String id_vista     = getResources().getResourceEntryName(vista_cal.getId());
                String subcadena    = id_vista.substring(12,18);

                Log.d("TAG", subcadena);
                if(subcadena.equals("evento")){
                    fecha_evento.setText(diaFormateado + "/" + mesFormateado + "/" + year);
                }else{
                    fecha_fin.setText(diaFormateado + "/" + mesFormateado + "/" + year);
                }
            }
        }

        final DatePickerDialog calendario = new DatePickerDialog(this, new DatePickerListener(),anio,mes,dia); //inicializa el datepicker en el dia actual
        calendario.show();
    }


    /*
    * FUNCION PARA OBTENER LA HORA SELECCIONADA EN EL DATEPICKER Y ESTABLECER ESA HORA EN EL TEXTVIEW
    * */
    public void obtenerHora(final View vista_time){

        class TimePickerListener implements TimePickerDialog.OnTimeSetListener {

            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                //Formateo el hora obtenido: antepone el 0 si son menores de 10
                String horaFormateada =  (hourOfDay < 10)? String.valueOf("0" + hourOfDay) : String.valueOf(hourOfDay);

                //Formateo el minuto obtenido: antepone el 0 si son menores de 10
                String minutoFormateado = (minute < 10)? String.valueOf("0" + minute):String.valueOf(minute);

                //Obtengo el valor a.m. o p.m., dependiendo de la selección del usuario
                String AM_PM;
                if(hourOfDay < 12) {
                    AM_PM = "a.m.";
                } else {
                    AM_PM = "p.m.";
                }

                //Muestro la hora con el formato deseado
                String id_vista     = getResources().getResourceEntryName(vista_time.getId());
                String subcadena    = id_vista.substring(11,17);
                Log.d("TAG", subcadena);

                if(subcadena.equals("evento")){
                    hora_evento.setText(horaFormateada + ":" + minutoFormateado + " " + AM_PM);
                }else{
                    hora_fin.setText(horaFormateada + ":" + minutoFormateado + " " + AM_PM);
                }
            }
        }

        final TimePickerDialog timedialog = new TimePickerDialog(this, new TimePickerListener(),hora, minuto, false); //inicializa el datepicker en el dia actual
        timedialog.show();
    }



    public void ejecutaActivityRepeticiones(View vista){
        Intent i = new Intent(this, Repeticiones.class);
        i.putExtra("Clase", "EditEvento");
        startActivityForResult(i,200);
    }


    /*
    *  LLEVA AL USUARIO A LA ACTIVITY DE RECORDATORIOS PARA QUE ELIJA CUANDO QUIERE QUE SE LE RECUERDE EL EVENTO
    * */
    public void ejecutaActivityRecordatorios(View vista){
        Intent i = new Intent(this, Recordatorios.class);
        i.putExtra("Clase", "EditEvento");
        startActivityForResult(i,201);
    }



    /*
    *  ----------- VALIDACIONES -----------
    * */
    public boolean validaFormulario(){

        if(validaNombre() && validaFechaEvento() && validaHoraEvento() && validaFechaAvisoEvento()  && validaHoraAviso() ){
            return true;
        }
        else{
            return false;
        }
    }


    public boolean validaNombre(){

        nombre = (EditText) findViewById(R.id.nombre_evento_edit);
        String nombre_bbdd = null;
        Cursor consulta = helper.consultaEventos(Estructura_BBDD.COLUMNA1, nombre.getText().toString(),"=", null, true, false);
        if (consulta.moveToFirst()) {
            nombre_bbdd = consulta.getString(0);
        }

        if( nombre.getText().toString().trim().isEmpty() || nombre_bbdd != null ){
            layout1.setErrorEnabled(true);
            layout1.setError("Campo vacío o nombre existente");
            nombre.requestFocus();
            return false;
        }
        else{
            layout1.setErrorEnabled(false);
            return true;
        }
    }


    public boolean validaFechaEvento(){

        fecha_evento = (EditText) findViewById(R.id.fecha_evento_edit);
        if( fecha_evento.getText().toString().trim().isEmpty() ){
            layout2.setErrorEnabled(true);
            layout2.setError("Campo vacío");
            fecha_evento.requestFocus();
            return false;
        }
        else{
            layout2.setErrorEnabled(false);
            return true;
        }
    }


    public boolean validaHoraEvento(){

        hora_evento = (EditText) findViewById(R.id.hora_evento_edit);
        if( hora_evento.getText().toString().trim().isEmpty() ){
            layout3.setErrorEnabled(true);
            layout3.setError("Campo vacío");
            hora_evento.requestFocus();
            return false;
        }
        else{
            layout3.setErrorEnabled(false);
            return true;
        }
    }


    public boolean validaFechaAvisoEvento(){

        fecha_evento    = (EditText) findViewById(R.id.fecha_evento_edit);
        fecha_fin       = (EditText) findViewById(R.id.fecha_fin_edit);

        if( fecha_fin.getText().toString().trim().isEmpty() ){
            layout4.setErrorEnabled(true);
            layout4.setError("Campo vacío");
            fecha_fin.requestFocus();
            return false;
        }
        else{
            //Creamos objetos de tipo Date a partir de las fechas selccionadas por el usuario
            final SimpleDateFormat df  = new SimpleDateFormat( "yyyy-MM-dd" );
            Date fecha_inicial = null, fecha_final = null;
            String[] fecha_ini  = fecha_evento.getText().toString().split("/");
            String[] fecha_f  = fecha_fin.getText().toString().split("/");
            try {
                fecha_inicial   = df.parse( fecha_ini[2] + "-" + fecha_ini[1] + "-" + fecha_ini[0]);
                fecha_final     = df.parse( fecha_f[2] + "-" + fecha_f[1] + "-" + fecha_f[0]);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            final Calendar eventoDate  = Calendar.getInstance();
            final Calendar avisoDate = Calendar.getInstance();
            eventoDate.setTime( fecha_inicial );
            avisoDate.setTime( fecha_final );

            if( avisoDate.before(eventoDate) ){
                layout4.setErrorEnabled(true);
                layout4.setError("La fecha final debe ser mayor a la del evento");
                fecha_fin.requestFocus();
                return false;
            }else {
                fechas_iguales = false;
                if(avisoDate.compareTo(eventoDate) == 0)
                    fechas_iguales = true;

                layout4.setErrorEnabled(false);
                return true;
            }
        }
    }


    public boolean validaHoraAviso(){

        hora_evento = (EditText) findViewById(R.id.hora_evento_edit);
        hora_fin    = (EditText) findViewById(R.id.hora_fin_edit);

        if( hora_fin.getText().toString().trim().isEmpty() ){
            layout5.setErrorEnabled(true);
            layout5.setError("Campo vacío");
            hora_fin.requestFocus();
            return false;
        }
        else{
            if(fechas_iguales) {
                SimpleDateFormat inputParser = new SimpleDateFormat("HH:mm");
                Date avisoDate = null, eventoDate = null;
                try {
                    avisoDate  = inputParser.parse(hora_fin.getText().toString());
                    eventoDate = inputParser.parse(hora_evento.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (avisoDate.before(eventoDate)) {
                    layout5.setErrorEnabled(true);
                    layout5.setError("La hora final debe ser mayor a la del evento");
                    hora_fin.requestFocus();
                    return false;
                } else {
                    layout5.setErrorEnabled(false);
                    return true;
                }
            }else {
                layout5.setErrorEnabled(false);
                return true;
            }
        }
    }

}
