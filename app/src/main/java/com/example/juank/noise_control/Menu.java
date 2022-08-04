package com.example.juank.noise_control;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.TextView;

public class Menu extends AppCompatActivity implements View.OnClickListener{

    Button btn1,btn2,btn3, btn4,btn5;
    ImageButton btn7;
    TextView tvtitulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        btn1 = findViewById(R.id.button);
        btn2 = findViewById(R.id.button2);
        btn3 = findViewById(R.id.button3);
        btn4 = findViewById(R.id.button5);
        btn5 = findViewById(R.id.button6);
        tvtitulo = findViewById(R.id.textView9);


        Typeface font = Typeface.createFromAsset(getAssets(), "font.ttf");
        btn1.setTypeface(font);
        btn2.setTypeface(font);
        btn3.setTypeface(font);
        btn4.setTypeface(font);
        btn5.setTypeface(font);
        tvtitulo.setTypeface(font);
    }

    public void onClick(View v) {

        int ID = v.getId();

        if (ID == R.id.button) {

            Intent intent = new Intent(getApplicationContext(), Particion_simple.class);
            startActivity(intent);

        }

        if (ID == R.id.button2) {

            Intent intent = new Intent(getApplicationContext(), Particion_compuesta.class);
            startActivity(intent);

        }

        if (ID == R.id.button5) {

            Intent intent = new Intent(getApplicationContext(), ParticionDoble.class);
            startActivity(intent);

        }

        if (ID == R.id.button6) {

            Intent intent = new Intent(getApplicationContext(), LaminaDoble.class);
            startActivity(intent);

        }

        if (ID == R.id.button3) {

            Intent intent = new Intent(getApplicationContext(), Tabla_materiales.class);
            startActivity(intent);

        }

        //if (ID == R.id.imageButton) {

        //    Toast.makeText(Menu.this, "¡Take a look of a our new remix -Wolves- !",
        //            Toast.LENGTH_SHORT).show();

        //}


    }
}
