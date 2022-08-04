package com.example.juank.noise_control;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import java.lang.Math;
import java.util.ArrayList;
import java.util.List;

import static com.example.juank.noise_control.PSimple.frecoct;
import static com.example.juank.noise_control.PSimple.poc;

public class LaminaDoble extends AppCompatActivity implements AdapterView.OnItemSelectedListener,View.OnClickListener {

    double X,E1,h1,E2,h2,Ms,pw1,pw2,B,poisson2,n1,n2,fc,TLR2,TLR3;

    public double E, pw,poisson,h,a,b,n;
    public double [] TLLD = new double[19];

    public EditText Width,Height, Thickness, Thickness2;
    public LinearLayout LY;
    SQLiteDBHelper dbHelper = null;
    static Spinner sItems = null;
    static Spinner sItems2 = null;
    private static int selectedMovieId = 0;
    public TextView fcTV,tl63,tl125,tl250,tl500,tl1000,tl2000,tl4000;
    public LineChart chart;
    double c = 343.2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lamina_doble);

        dbHelper = new SQLiteDBHelper(this);
        populateSpinner();

        Height =(EditText) findViewById(R.id.editText4);
        Width =(EditText) findViewById(R.id.editText);
        Thickness =(EditText) findViewById(R.id.editText2);
        Thickness2 =(EditText) findViewById(R.id.editText14);

        LY = (LinearLayout) findViewById(R.id.LinearLayout);

        fcTV = findViewById(R.id.fc3);
        tl63 = findViewById(R.id.tl19);
        tl125 = findViewById(R.id.tl20);
        tl250 = findViewById(R.id.tl21);
        tl500 = findViewById(R.id.tl22);
        tl1000 = findViewById(R.id.tl23);
        tl2000 = findViewById(R.id.tl24);
        tl4000 = findViewById(R.id.tl25);

        chart = (LineChart) findViewById(R.id.chart);

    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        MovieSpinnerVO movieSpinnerVO = (MovieSpinnerVO) sItems.getSelectedItem();
        int movieId = movieSpinnerVO.getMovieId();
        selectedMovieId = movieId;

        MaterialesCaracteristicas matCatVO = dbHelper.getMaterial(movieId);

    }

    public void onNothingSelected(AdapterView<?> parent) {
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
        sItems2 = (Spinner) findViewById(R.id.spinner5);
        sItems.setAdapter(adapter);
        sItems2.setAdapter(adapter);
        sItems.setOnItemSelectedListener(this);
        sItems2.setOnItemSelectedListener(this);
    }

    public void onClick(View v) {

        int ID = v.getId();

        if (ID == R.id.button2) {

            a = Double.parseDouble(Height.getText().toString());
            b = Double.parseDouble(Width.getText().toString());
            h1 = Double.parseDouble(Thickness.getText().toString());
            h2=Double.parseDouble(Thickness2.getText().toString());


            sItems = (Spinner) findViewById(R.id.spinner);
            sItems2 = (Spinner) findViewById(R.id.spinner5);

            MovieSpinnerVO movieSpinnerVO = (MovieSpinnerVO) sItems.getSelectedItem();
            MovieSpinnerVO movieSpinnerVO2 = (MovieSpinnerVO) sItems2.getSelectedItem();

            int materialId = movieSpinnerVO.getMovieId();
            int materialId2 = movieSpinnerVO2.getMovieId();

            MaterialesCaracteristicas matCatVO = dbHelper.getMaterial(materialId);

            pw1=matCatVO.getDENSIDAD();

            n=matCatVO.getCOEFICIENTE();

            E=matCatVO.getYOUNG()*Math.pow(10,9);

            poisson=matCatVO.getPOISSON();

            matCatVO = dbHelper.getMaterial(materialId2);

            pw2=matCatVO.getDENSIDAD();

            n2=matCatVO.getCOEFICIENTE();

            E2=matCatVO.getYOUNG()*Math.pow(10,9);

            poisson2=matCatVO.getPOISSON();

            Ms = (pw1*h1)+(pw2*h2);
            Log.d("v","Ms = "+Double.toString(Ms));

            if (E1*h1 < E2*h2){
                E1=E2;
                h1=h2;
                matCatVO = dbHelper.getMaterial(materialId);
                E2=matCatVO.getYOUNG()*Math.pow(10,9);
                h2 = Double.parseDouble(Thickness.getText().toString());
            }


            X = ((E1*Math.pow(h1,2))-(E2*Math.pow(h2,2)))/(2*(E1*h1+E2*h2));

            Log.d("v","x = "+Double.toString(X));

            B = ((E1*Math.pow(h1,3))/(12*(1-Math.pow(poisson,2))))*(1+3*(1-Math.pow((2*X/h1),2))) + ((E2*Math.pow(h2,3))/(12*(1-Math.pow(poisson2,2))))*(1+3*(1-Math.pow((2*X/h2),2)));
            Log.d("v","B = "+Double.toString(B));

            n = ((n1*E1*h1+n2*E2*h2)*Math.pow(h1+h2,2))/(E1*Math.pow(h1,3)*(1+3*(1-Math.pow((2*X/h1),2))) + E2*Math.pow(h2,3)*(1+3*(1-Math.pow((2*X/h2),2))));
            fc = (Math.pow(c,2)/(2*Math.PI))*Math.sqrt(Ms/B);

            for (int i=0; i<frecoct.length;i++) {

                Log.d("v",Double.toString(frecoct[i]));
            if (frecoct[i] < fc) {

                Log.d("v","Entre a la región 2");
                TLR2 = 10 * Math.log10(1+ Math.pow((Math.PI*frecoct[i]*Ms)/poc,2))  - 5;
                TLLD[i]=TLR2;

            } else if (fc < frecoct[i]) {

                Log.d("v","Entre a la región 3");
                TLR3 = 10 * Math.log10(1 + Math.pow(((Math.PI * fc * Ms) / (poc)), 2)) + 10 * Math.log10(n) + 33.22 * Math.log10(frecoct[i] / fc) - 5.7;

                TLLD[i]=TLR3;

            }

            }

            String value2 = String.format("%.2f Hz", fc);
            fcTV.setText("fc: "+"\n"+value2);
            fcTV.setTypeface(fcTV.getTypeface(), Typeface.BOLD);
            fcTV.setVisibility(View.VISIBLE);


            String value = String.format("%.2f dB", TLLD[0]);
            tl63.setText("TL @ "+Double.toString(frecoct[0])+"Hz "+"\n"+value);
            tl63.setTypeface(tl63.getTypeface(), Typeface.BOLD);
            tl63.setVisibility(View.VISIBLE);

            String value125 = String.format("%.2f dB", TLLD[1]);
            tl125.setText("TL @ "+Double.toString(frecoct[1])+"Hz "+"\n"+value125);
            tl125.setTypeface(tl125.getTypeface(), Typeface.BOLD);
            tl125.setVisibility(View.VISIBLE);

            String value250 = String.format("%.2f dB", TLLD[2]);
            tl250.setText("TL @ "+Double.toString(frecoct[2])+"Hz "+"\n"+value250);
            tl250.setTypeface(tl250.getTypeface(), Typeface.BOLD);
            tl250.setVisibility(View.VISIBLE);

            String value500 = String.format("%.2f dB", TLLD[3]);
            tl500.setText("TL @ "+Double.toString(frecoct[3])+"Hz "+"\n"+value500);
            tl500.setTypeface(tl500.getTypeface(), Typeface.BOLD);
            tl500.setVisibility(View.VISIBLE);

            String value1000 = String.format("%.2f dB", TLLD[4]);
            tl1000.setText("TL @ "+Double.toString(frecoct[4])+"Hz "+"\n"+value1000);
            tl1000.setTypeface(tl1000.getTypeface(), Typeface.BOLD);
            tl1000.setVisibility(View.VISIBLE);

            String value2000 = String.format("%.2f dB", TLLD[5]);
            tl2000.setText("TL @ "+Double.toString(frecoct[5])+"Hz "+"\n"+value2000);
            tl2000.setTypeface(tl2000.getTypeface(), Typeface.BOLD);
            tl2000.setVisibility(View.VISIBLE);

            String value4000 = String.format("%.2f dB", TLLD[6]);
            tl4000.setText("TL @ "+Double.toString(frecoct[6])+"Hz "+"\n"+value4000);
            tl4000.setTypeface(tl2000.getTypeface(), Typeface.BOLD);
            tl4000.setVisibility(View.VISIBLE);

            List<Entry> entries = new ArrayList<Entry>();

            entries.add(new Entry(63, (float) TLLD[0]));

            //int a =125;
            for (int i=1 ; i< frecoct.length;i++ ){
                // turn your data into Entry objects

                entries.add(new Entry( (float) frecoct[i], (float) TLLD[i]));
                //a=a*2;
            }

            LineDataSet dataSet = new LineDataSet(entries,"Lamina Doble"); // add entries to dataset
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

            //for( int i = 0; i < frecoct.length; i++ )
            //{
            //    String value = String.format("%.2f dB", TLLD[i]);
            //    TextView textView = new TextView(this);
            //    textView.setText("TL @"+Double.toString(frecoct[i])+"Hz = "+value);
            //    textView.setTextSize(24);
            //    textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
            //    LY.addView(textView);
            //}

            //ACA SE DEBE CERRAR EL IF
        }

    }



}
