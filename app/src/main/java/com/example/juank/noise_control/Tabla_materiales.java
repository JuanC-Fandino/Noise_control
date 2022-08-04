package com.example.juank.noise_control;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;

import java.util.ArrayList;
import java.util.List;

public class Tabla_materiales extends AppCompatActivity implements View.OnClickListener {

    SQLiteDBHelper dbHelper = null;
    public static Activity fa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabla_materiales);

        dbHelper = new SQLiteDBHelper(this);
        fa=this;

        Tabla tabla = new Tabla(this, (TableLayout) findViewById(R.id.tabla));
        tabla.agregarCabecera(R.array.cabecera_tabla);
        List<MaterialesCaracteristicas> matCatList = dbHelper.getAllMaterials();

        for (int i = 0; i < matCatList.size(); i++) {
            ArrayList<String> elementos = new ArrayList<>();
            MaterialesCaracteristicas matCatVO = matCatList.get(i);
            elementos.add(matCatVO.getMaterialName());
            elementos.add(Float.toString(matCatVO.getDENSIDAD()));
            elementos.add(Float.toString(matCatVO.getCOEFICIENTE()));
            elementos.add(Float.toString(matCatVO.getYOUNG()));
            elementos.add(Float.toString(matCatVO.getPOISSON()));
            tabla.agregarFilaTabla(elementos)
            ;
        }

    }

    public void onClick(View v) {

        int ID = v.getId();


        if (ID == R.id.button3) {

            Intent intent = new Intent(getApplicationContext(), Materials_Base.class);
            startActivity(intent);
            onStop();
        }


    }
}
