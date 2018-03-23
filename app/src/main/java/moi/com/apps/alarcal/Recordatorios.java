package moi.com.apps.alarcal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;


/**
 * Created by Moi on 15/03/2018.
 */

public class Recordatorios extends AppCompatActivity {

    //Nombre de la clase desde la cual se llama a esta actividad
    String clase;



    /************
     *  METODOS  *
     *************/
    public void onCreate (Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.recordatorios);


        Intent intent = getIntent();
        clase = intent.getStringExtra("Clase");


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
        final RadioButton radio_button8   = (RadioButton) findViewById(R.id.radio_button8);
        final RadioButton radio_button9   = (RadioButton) findViewById(R.id.radio_button9);


        radio_button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                radio_button1.setChecked(true);
                radio_button2.setChecked(false);
                radio_button3.setChecked(false);
                radio_button4.setChecked(false);
                radio_button5.setChecked(false);
                radio_button6.setChecked(false);
                radio_button7.setChecked(false);
                radio_button8.setChecked(false);
                radio_button9.setChecked(false);
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
                radio_button8.setChecked(false);
                radio_button9.setChecked(false);
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
                radio_button8.setChecked(false);
                radio_button9.setChecked(false);
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
                radio_button8.setChecked(false);
                radio_button9.setChecked(false);
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
                radio_button8.setChecked(false);
                radio_button9.setChecked(false);
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
                radio_button8.setChecked(false);
                radio_button9.setChecked(false);
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
                radio_button8.setChecked(false);
                radio_button9.setChecked(false);
            }
        });

        radio_button8.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                radio_button1.setChecked(false);
                radio_button2.setChecked(false);
                radio_button3.setChecked(false);
                radio_button4.setChecked(false);
                radio_button5.setChecked(false);
                radio_button6.setChecked(false);
                radio_button7.setChecked(false);
                radio_button8.setChecked(true);
                radio_button9.setChecked(false);
            }
        });

        radio_button9.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                radio_button1.setChecked(false);
                radio_button2.setChecked(false);
                radio_button3.setChecked(false);
                radio_button4.setChecked(false);
                radio_button5.setChecked(false);
                radio_button6.setChecked(false);
                radio_button7.setChecked(false);
                radio_button8.setChecked(false);
                radio_button9.setChecked(true);
            }
        });

    }



    public void ejecutaActivity(View vista){

        String name_btn = null;
        for (int i=1; i<=9; i++){
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
