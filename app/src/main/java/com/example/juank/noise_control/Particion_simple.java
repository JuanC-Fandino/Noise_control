package com.example.juank.noise_control;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.lang.Math;
import java.util.ArrayList;
import java.util.List;

public class Particion_simple extends AppCompatActivity implements AdapterView.OnItemSelectedListener,View.OnClickListener {

    public double E, pw,poisson,h,a,b,n;
    public double [] frecoct= {63,100,125,160,200,250,315,400,500,630,800,1000,1250,1600,2000,2500,3150,4000,8000};
    public double [] frecresultados=new double[19] ;
    public double [] frecuencias=new double[2] ;
    public EditText Width,Height, Thickness;
    public LinearLayout LY;
    public LineChart chart;
    static Spinner sItems = null;
    SQLiteDBHelper dbHelper = null;
    private static int selectedMovieId = 0;
    public TextView f11,fc,tl63,tl125,tl160,tl200,tl250,tl315,tl400,tl500,tl630,tl800,tl1000,tl1250,tl1600,tl2000,tl2500,tl3150,tl4000,tl8000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_particion_simple);
        dbHelper = new SQLiteDBHelper(this);
        populateSpinner();
        Width =(EditText) findViewById(R.id.editText);
        Height =(EditText) findViewById(R.id.editText4);
        Thickness =(EditText) findViewById(R.id.editText2);

        LY = (LinearLayout) findViewById(R.id.LinearLayout);
        chart = (LineChart) findViewById(R.id.chart);

        f11 = (TextView) findViewById(R.id.f11);
        fc = findViewById(R.id.fc);
        tl63 = findViewById(R.id.tl63);
        tl125 = findViewById(R.id.tl125);
        tl250 = findViewById(R.id.tl250);
        tl500 = findViewById(R.id.tl500);
        tl1000 = findViewById(R.id.tl1000);
        tl2000 = findViewById(R.id.tl2000);
        tl4000 = findViewById(R.id.tl4000);

    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        MovieSpinnerVO movieSpinnerVO = (MovieSpinnerVO) sItems.getSelectedItem();
        int movieId = movieSpinnerVO.getMovieId();
        selectedMovieId = movieId;

        MaterialesCaracteristicas matCatVO = dbHelper.getMaterial(movieId);

    }

    public void onNothingSelected(AdapterView<?> parent) {
    }

    private int getSpinnerPosition(Spinner spinner){
        Adapter adapter = spinner.getAdapter();
        int i=0;
        for (; i < adapter.getCount() ; i++){

            MovieSpinnerVO movieSpinnerVO = (MovieSpinnerVO)adapter.getItem(i);
            if(selectedMovieId == movieSpinnerVO.getMovieId())

            {
                return i;
            }

        }
        return 0;
    }

    public void onClick(View v) {

        int ID = v.getId();

        if (ID == R.id.button2) {

            b = Double.parseDouble(Width.getText().toString());
            a = Double.parseDouble(Height.getText().toString());
            h = Double.parseDouble(Thickness.getText().toString());

            sItems = (Spinner) findViewById(R.id.spinner);
            MovieSpinnerVO movieSpinnerVO = (MovieSpinnerVO) sItems.getSelectedItem();
            int materialId = movieSpinnerVO.getMovieId();
            MaterialesCaracteristicas matCatVO = dbHelper.getMaterial(materialId);

            pw=matCatVO.getDENSIDAD();

            n=matCatVO.getCOEFICIENTE();

            E=matCatVO.getYOUNG()*Math.pow(10,9);

            poisson=matCatVO.getPOISSON();

            PSimple pSimple = new PSimple();
            frecresultados = pSimple.PS(h,a,b,pw,n,E,poisson);
            frecuencias = pSimple.frecs(h,a,b,pw,n,E,poisson);

            List<Entry> entries = new ArrayList<Entry>();

            entries.add(new Entry(63, (float) frecresultados[0]));

            int a =125;
            for (int i=1 ; i< frecresultados.length;i++ ){
                // turn your data into Entry objects

                entries.add(new Entry(a, (float) frecresultados[i]));
                a=a*2;
            }

            LineDataSet dataSet = new LineDataSet(entries,matCatVO.getMaterialName()); // add entries to dataset
            dataSet.setColor(Color.BLACK);
            dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);

            String value1 = String.format("%.2f Hz", frecuencias[0]);
            f11.setText("f11: "+"\n"+value1);
            f11.setTypeface(f11.getTypeface(), Typeface.BOLD);
            f11.setVisibility(View.VISIBLE);


            String value2 = String.format("%.2f Hz", frecuencias[1]);
            fc.setText("fc: "+"\n"+value2);
            fc.setTypeface(fc.getTypeface(), Typeface.BOLD);
            fc.setVisibility(View.VISIBLE);


            String value = String.format("%.2f dB", frecresultados[0]);
            tl63.setText("TL @ "+Double.toString(frecoct[0])+"Hz "+"\n"+value);
            tl63.setTypeface(tl63.getTypeface(), Typeface.BOLD);
            tl63.setVisibility(View.VISIBLE);

            String value125 = String.format("%.2f dB", frecresultados[1]);
            tl125.setText("TL @ "+Double.toString(frecoct[1])+"Hz "+"\n"+value125);
            tl125.setTypeface(tl125.getTypeface(), Typeface.BOLD);
            tl125.setVisibility(View.VISIBLE);

            String value250 = String.format("%.2f dB", frecresultados[2]);
            tl250.setText("TL @ "+Double.toString(frecoct[2])+"Hz "+"\n"+value250);
            tl250.setTypeface(tl250.getTypeface(), Typeface.BOLD);
            tl250.setVisibility(View.VISIBLE);

            String value500 = String.format("%.2f dB", frecresultados[3]);
            tl500.setText("TL @ "+Double.toString(frecoct[3])+"Hz "+"\n"+value500);
            tl500.setTypeface(tl500.getTypeface(), Typeface.BOLD);
            tl500.setVisibility(View.VISIBLE);

            String value1000 = String.format("%.2f dB", frecresultados[4]);
            tl1000.setText("TL @ "+Double.toString(frecoct[4])+"Hz "+"\n"+value1000);
            tl1000.setTypeface(tl1000.getTypeface(), Typeface.BOLD);
            tl1000.setVisibility(View.VISIBLE);

            String value2000 = String.format("%.2f dB", frecresultados[5]);
            tl2000.setText("TL @ "+Double.toString(frecoct[5])+"Hz "+"\n"+value2000);
            tl2000.setTypeface(tl2000.getTypeface(), Typeface.BOLD);
            tl2000.setVisibility(View.VISIBLE);

            String value4000 = String.format("%.2f dB", frecresultados[6]);
            tl4000.setText("TL @ "+Double.toString(frecoct[6])+"Hz "+"\n"+value4000);
            tl4000.setTypeface(tl4000.getTypeface(), Typeface.BOLD);
            tl4000.setVisibility(View.VISIBLE);

            LineData lineData = new LineData(dataSet);
            chart.setData(lineData);
            chart.invalidate();
            chart.setVisibility(View.VISIBLE);
            chart.getDescription().setEnabled(false);
            chart.animateXY(3000, 3000);

            Legend legend = chart.getLegend();
            legend.setForm(Legend.LegendForm.LINE);
            legend.setFormSize(12);
            legend.setTextSize(12);
            legend.setXEntrySpace(10);


            XAxis xAxis = chart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

            //ACA SE DEBE CERRAR EL IF
        }

    }

    private void populateSpinner (){
        List<MaterialesCaracteristicas> matCatList = dbHelper.getAllMaterials();
        List<MovieSpinnerVO> matSpinnerList = new ArrayList<>();
        for (int i = 0; i < matCatList.size(); i++) {

            MaterialesCaracteristicas matCatVO = matCatList.get(i);

            MovieSpinnerVO matSpinnerVO = new MovieSpinnerVO(matCatVO.getMaterialId(), matCatVO.getMaterialName());

            matSpinnerList.add(matSpinnerVO);

        }

        ArrayAdapter<String> adapter = new ArrayAdapter(
                this, android.R.layout.simple_spinner_item, matSpinnerList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sItems = findViewById(R.id.spinner);
        sItems.setAdapter(adapter);
        sItems.setOnItemSelectedListener(this);
    }

}
