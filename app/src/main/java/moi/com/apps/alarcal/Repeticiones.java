package moi.com.apps.alarcal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by Moi on 06/03/2018.
 */

public class Repeticiones extends AppCompatActivity {

    //Nombre de la clase desde la cual se llama a esta actividad
    String clase, fecha_elegida;



    /************
     *  METODOS  *
     *************/
    public void onCreate (Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.repeticiones);


        Intent intent = getIntent();
        clase = intent.getStringExtra("Clase");
        fecha_elegida = intent.getStringExtra("Fecha_elegida");

        //Calendario para obtener fecha & hora
        SimpleDateFormat sdf        = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
        SimpleDateFormat day_date   = new SimpleDateFormat("EEEE");
        final Calendar c = Calendar.getInstance();
        int dia = 0;
        String mes_name = null, dia_name = null;

        try {
            Date fecha = sdf.parse(fecha_elegida);
            c.setTime(fecha);

            dia        = Integer.parseInt(sdf.format(fecha).split("/")[0]);
            mes_name   = month_date.format(fecha).substring(0, 1).toUpperCase() + month_date.format(fecha).substring(1);
            dia_name   = day_date.format(fecha).substring(0, 1).toUpperCase() + day_date.format(fecha).substring(1);

            Log.d("TAG", "Dia: "+ dia + " dia_selec: "+ dia_name + " mes_selec: "+ mes_name);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        //Establecemos los textos respecto al dia de hoy en los textview de las opciones
        TextView tv_mensual = (TextView) findViewById(R.id.textview_radio_button5);
        tv_mensual.setText("Mensualmente (cada " + dia + " de mes)");

        TextView tv_semanal = (TextView) findViewById(R.id.textview_radio_button4);
        tv_semanal.setText("Semanalmente (cada " + dia_name + ")");

        TextView tv_2semanal = (TextView) findViewById(R.id.textview_radio_button7);
        tv_2semanal.setText("Cada dos semanas (cada " + dia_name + ")");

        TextView tv_anual = (TextView) findViewById(R.id.textview_radio_button6);
        tv_anual.setText("Anualmente (cada " + dia + " de " + mes_name + ")");


        //Hacer que los botones funcionen como un grupo de botones
        setListenerOnRadioButtons();
    }


    public void setListenerOnRadioButtons(){

        final RadioButton radio_button1   = (RadioButton) findViewById(R.id.radio_button1);
        final RadioButton radio_button2   = (RadioButton) findViewById(R.id.radio_button2);
        final RadioButton radio_button3   = (RadioButton) findViewById(R.id.radio_button3);
        final RadioButton radio_button4   = (RadioButton) findViewById(R.id.radio_button4);
        final RadioButton radio_button5   = (RadioButton) findViewById(R.id.radio_button5);
        final RadioButton radio_button6   = (RadioButton) findViewById(R.id.radio_button6);
        final RadioButton radio_button7   = (RadioButton) findViewById(R.id.radio_button7);


        radio_button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                radio_button1.setChecked(true);
                radio_button2.setChecked(false);
                radio_button3.setChecked(false);
                radio_button4.setChecked(false);
                radio_button5.setChecked(false);
                radio_button6.setChecked(false);
                radio_button7.setChecked(false);
            }
        });

        radio_button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                radio_button1.setChecked(false);
                radio_button2.setChecked(true);
                radio_button3.setChecked(false);
                radio_button4.setChecked(false);
                radio_button5.setChecked(false);
                radio_button6.setChecked(false);
                radio_button7.setChecked(false);
            }
        });;

        radio_button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                radio_button1.setChecked(false);
                radio_button2.setChecked(false);
                radio_button3.setChecked(true);
                radio_button4.setChecked(false);
                radio_button5.setChecked(false);
                radio_button6.setChecked(false);
                radio_button7.setChecked(false);
            }
        });

        radio_button4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                radio_button1.setChecked(false);
                radio_button2.setChecked(false);
                radio_button3.setChecked(false);
                radio_button4.setChecked(true);
                radio_button5.setChecked(false);
                radio_button6.setChecked(false);
                radio_button7.setChecked(false);
            }
        });

        radio_button5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                radio_button1.setChecked(false);
                radio_button2.setChecked(false);
                radio_button3.setChecked(false);
                radio_button4.setChecked(false);
                radio_button5.setChecked(true);
                radio_button6.setChecked(false);
                radio_button7.setChecked(false);
            }
        });

        radio_button6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                radio_button1.setChecked(false);
                radio_button2.setChecked(false);
                radio_button3.setChecked(false);
                radio_button4.setChecked(false);
                radio_button5.setChecked(false);
                radio_button6.setChecked(true);
                radio_button7.setChecked(false);
            }
        });

        radio_button7.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                radio_button1.setChecked(false);
                radio_button2.setChecked(false);
                radio_button3.setChecked(false);
                radio_button4.setChecked(false);
                radio_button5.setChecked(false);
                radio_button6.setChecked(false);
                radio_button7.setChecked(true);
            }
        });

    }



    public void ejecutaActivity(View vista){

        String name_btn = null;
        for (int i=1; i<=7; i++){
            int resID = getResources().getIdentifier("radio_button" + i, "id", getPackageName());
            RadioButton btn = (RadioButton) findViewById(resID);

            if(btn.isChecked()){
                name_btn = "radio_button" + i;
            }
        }


        TextView tv = (TextView) findViewById(getResources().getIdentifier("textview_" + name_btn, "id", getPackageName()));
        if(clase.equals("EditEvento")){
            Intent intent = getIntent();
            intent.putExtra("opcion",tv.getText());
            setResult(RESULT_OK, intent);
            finish();
        }
        else{
            Intent intent = getIntent();
            intent.putExtra("opcion",tv.getText());
            setResult(RESULT_OK, intent);
            finish();
        }
    }


    @Override
    public void onBackPressed() {
        ejecutaActivity(new View(this));
    }
}
