package com.example.juank.noise_control;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

import static com.example.juank.noise_control.PSimple.frecoct;
import static com.example.juank.noise_control.PSimple.poc;

public class ParticionDoble extends AppCompatActivity implements AdapterView.OnItemSelectedListener,View.OnClickListener {

    static Spinner sItems = null;
    static Spinner sItems2 = null;
    public LineChart chart;

    private static int selectedMovieId = 0;

    public LinearLayout LY;
    public EditText Width,Height, Thickness,Width2,Height2, Thickness2, Distance, Alfa;
    SQLiteDBHelper dbHelper = null;
    public double c= 343.2;
    public double E, pw,poisson,h,a,b,n, alfa, a2,b2,h2,E2,pw2,poisson2,n2,f1,f0,f3,Ms1,Ms2,d,TLR1,TLR2,TLR3;

    private double [] frec = new double[19] ;
    private double [] resultados = new double[19] ;
    public double [] TLPD =new double[19] ;

    public TextView f11,f00,f33,tl63,tl125,tl250,tl500,tl1000,tl2000,tl4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_particion_doble);
        dbHelper = new SQLiteDBHelper(this);
        chart = (LineChart) findViewById(R.id.chart);

        populateSpinner();

        Width =(EditText) findViewById(R.id.editText);
        Height =(EditText) findViewById(R.id.editText4);
        Thickness =(EditText) findViewById(R.id.editText2);

        Height2 =(EditText) findViewById(R.id.editText7);
        Width2 =(EditText) findViewById(R.id.editText8);
        Thickness2 =(EditText) findViewById(R.id.editText9);

        Distance = findViewById(R.id.editText10);
        Alfa =  findViewById(R.id.et11);

        LY = (LinearLayout) findViewById(R.id.LinearLayout);

        f11 = findViewById(R.id.f1);
        f00 = findViewById(R.id.f0);
        f33= findViewById(R.id.f3);
        tl63 = findViewById(R.id.tl7);
        tl125 = findViewById(R.id.tl8);
        tl250 = findViewById(R.id.tl9);
        tl500 = findViewById(R.id.tl10);
        tl1000 = findViewById(R.id.tl11);
        tl2000 = findViewById(R.id.tl12);
        tl4000 = findViewById(R.id.tl13);

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


            a = Double.parseDouble(Height.getText().toString());
            b = Double.parseDouble(Width.getText().toString());
            h = Double.parseDouble(Thickness.getText().toString());

            a2=Double.parseDouble(Height2.getText().toString());
            b2=Double.parseDouble(Width2.getText().toString());
            h2=Double.parseDouble(Thickness2.getText().toString());

            d=Double.parseDouble(Distance.getText().toString());
            alfa=Double.parseDouble(Alfa.getText().toString());

            sItems = (Spinner) findViewById(R.id.spinner);
            sItems2 = (Spinner) findViewById(R.id.spinner2);

            MovieSpinnerVO movieSpinnerVO = (MovieSpinnerVO) sItems.getSelectedItem();
            MovieSpinnerVO movieSpinnerVO2 = (MovieSpinnerVO) sItems2.getSelectedItem();

            int materialId = movieSpinnerVO.getMovieId();
            int materialId2 = movieSpinnerVO2.getMovieId();

            MaterialesCaracteristicas matCatVO = dbHelper.getMaterial(materialId);

            pw=matCatVO.getDENSIDAD();

            n=matCatVO.getCOEFICIENTE();

            E=matCatVO.getYOUNG()*Math.pow(10,9);

            poisson=matCatVO.getPOISSON();

            matCatVO = dbHelper.getMaterial(materialId2);

            pw2=matCatVO.getDENSIDAD();

            n2=matCatVO.getCOEFICIENTE();

            E2=matCatVO.getYOUNG()*Math.pow(10,9);

            poisson2=matCatVO.getPOISSON();

            Ms1 = pw*h;
            Ms2 = pw2*h2;

            PSimple pSimple = new PSimple();
            frec = pSimple.PS(h,a,b,pw,n,E,poisson);

            PSimple pSimple2 = new PSimple();
            resultados = pSimple2.PS(h2,a2, b2,pw2,n2,E2,poisson2);

            f1 = poc/(Math.PI*(Ms1+Ms2));
            Log.d("v",Double.toString(f1));

            String valuef1 = String.format("%.2f Hz", f1);
            f11.setText("f1: "+valuef1);
            f11.setTextSize(24);
            f11.setTypeface(f11.getTypeface(), Typeface.BOLD);
            f11.setVisibility(View.VISIBLE);


            f0 = (c/(2*Math.PI))*(Math.sqrt((1.204/d)*((1/Ms1)+(1/Ms2))));
            Log.d("v",Double.toString(f0));

            String valuef0 = String.format("%.2f Hz", f0);
            f00.setText("f0: "+valuef0);
            f00.setTextSize(24);
            f00.setTypeface(f00.getTypeface(), Typeface.BOLD);
            f00.setVisibility(View.VISIBLE);

            f3=(c/(2*Math.PI*d));
            Log.d("v",Double.toString(f3));

            String valuef3 = String.format("%.2f Hz", f3);
            f33.setText("f3: "+valuef3);
            f33.setTextSize(24);
            f33.setTypeface(f33.getTypeface(), Typeface.BOLD);
            f33.setVisibility(View.VISIBLE);

            for (int i=0; i<frecoct.length;i++) {

                Log.d("v",Double.toString(frecoct[i]));

                if (f1 < frecoct[i] && frecoct[i] < f0) {

                    Log.d("v","Entre a la región 1");
                    TLR1 = 20 * Math.log10(Ms1+Ms2) + 20 * Math.log10(frecoct[i])-47.3;
                    TLPD[i]=TLR1;

                } else if (f0 < frecoct[i] && frecoct[i] < f3) {

                    Log.d("v","Entre a la región 2");
                    TLR2 = frec[i] + resultados[i]+20 * Math.log10((4*Math.PI*frecoct[i]*d)/c);
                    Log.d("v","TL1: "+Double.toString(frec[i])+" TL2: "+Double.toString(resultados[i]));
                    Log.d("v",Double.toString(TLR2));
                    TLPD[i]=TLR2;

                } else if (frecoct[i] > f3) {

                    Log.d("v","Entre a la región 3");
                    TLR3 = frec[i]+resultados[i]+10 * Math.log10(4/(1+(2/alfa))) ;
                    Log.d("v","TL1: "+Double.toString(frec[i])+" TL2: "+Double.toString(resultados[i]));
                    Log.d("v",Double.toString(TLR3));
                    TLPD[i]=TLR3;

                }

            }

            String value = String.format("%.2f dB", TLPD[0]);
            tl63.setText("TL @ "+Double.toString(frecoct[0])+"Hz "+"\n"+value);
            tl63.setTypeface(tl63.getTypeface(), Typeface.BOLD);
            tl63.setVisibility(View.VISIBLE);

            String value125 = String.format("%.2f dB", TLPD[1]);
            tl125.setText("TL @ "+Double.toString(frecoct[1])+"Hz "+"\n"+value125);
            tl125.setTypeface(tl125.getTypeface(), Typeface.BOLD);
            tl125.setVisibility(View.VISIBLE);

            String value250 = String.format("%.2f dB", TLPD[2]);
            tl250.setText("TL @ "+Double.toString(frecoct[2])+"Hz "+"\n"+value250);
            tl250.setTypeface(tl250.getTypeface(), Typeface.BOLD);
            tl250.setVisibility(View.VISIBLE);

            String value500 = String.format("%.2f dB", TLPD[3]);
            tl500.setText("TL @ "+Double.toString(frecoct[3])+"Hz "+"\n"+value500);
            tl500.setTypeface(tl500.getTypeface(), Typeface.BOLD);
            tl500.setVisibility(View.VISIBLE);

            String value1000 = String.format("%.2f dB", TLPD[4]);
            tl1000.setText("TL @ "+Double.toString(frecoct[4])+"Hz "+"\n"+value1000);
            tl1000.setTypeface(tl1000.getTypeface(), Typeface.BOLD);
            tl1000.setVisibility(View.VISIBLE);

            String value2000 = String.format("%.2f dB", TLPD[5]);
            tl2000.setText("TL @ "+Double.toString(frecoct[5])+"Hz "+"\n"+value2000);
            tl2000.setTypeface(tl2000.getTypeface(), Typeface.BOLD);
            tl2000.setVisibility(View.VISIBLE);

            String value4000 = String.format("%.2f dB", TLPD[6]);
            tl4000.setText("TL @ "+Double.toString(frecoct[6])+"Hz "+"\n"+value4000);
            tl4000.setTypeface(tl4000.getTypeface(), Typeface.BOLD);
            tl4000.setVisibility(View.VISIBLE);

            List<Entry> entries = new ArrayList<Entry>();

            entries.add(new Entry(63, (float) TLPD[0]));

            for (int i=1 ; i< frecoct.length;i++ ){
                // turn your data into Entry objects

                entries.add(new Entry((float) frecoct[i], (float) TLPD[i]));
            }

            LineDataSet dataSet = new LineDataSet(entries,"Partición Doble"); // add entries to dataset
            dataSet.setColor(Color.BLACK);
            dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);

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

        sItems = (Spinner) findViewById(R.id.spinner);
        sItems2 = (Spinner) findViewById(R.id.spinner2);
        sItems.setAdapter(adapter);
        sItems2.setAdapter(adapter);
        sItems.setOnItemSelectedListener(this);
        sItems2.setOnItemSelectedListener(this);
    }


}
