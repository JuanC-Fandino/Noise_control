package com.example.juank.noise_control;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

public class Particion_compuesta extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    static Spinner sItems = null;

    public double [] frecresultados=new double[19] ;
    public double [] atcompuesta=new double[19] ;
    public double [] TL=new double[19] ;
    public TextView tl63,tl125,tl250,tl500,tl1000,tl2000,tl4000;
    public LineChart chart;


    public int Num,buttonid,j;

    SQLiteDBHelper dbHelper = null;

    Button AceptarBtn,SaveBtn;

    private static int selectedMovieId = 0;

    EditText NumMateriales;

    List<Spinner> SpinnerList = new ArrayList<>();

    List<EditText> EditList = new ArrayList<>();
    List<EditText> EditList2 = new ArrayList<>();
    List<EditText> EditList3 = new ArrayList<>();
    List<EditText> EditList4 = new ArrayList<>();
    List<double[]> myList = new ArrayList<double[]>();

    private static int selectedMaterialId = 0;
    double ancho, superficie, alto, espesor, pw, n,E, poisson, TLR1,TLR2,TLR3, CL, f11, fc,CS, KS, MS, at,st;
    double res=0;
    public LinearLayout LY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_particion_compuesta);
        dbHelper = new SQLiteDBHelper(this);
        tl63 = findViewById(R.id.tl);
        tl125 = findViewById(R.id.tl2);
        tl250 = findViewById(R.id.tl3);
        tl500 = findViewById(R.id.tl4);
        tl1000 = findViewById(R.id.tl5);
        tl2000 = findViewById(R.id.tl6);
        tl4000 = findViewById(R.id.tl7);
        chart = (LineChart) findViewById(R.id.chart);

    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

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

    public void populateSpinnerx(Spinner spn) {

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

        spn.setAdapter(adapter);

        spn.setOnItemSelectedListener(this);

    }

    public void onClick(View v){

        int ID = v.getId();

        if (ID == R.id.button) {

            NumMateriales= (EditText) findViewById(R.id.editText);
            Num= Integer.parseInt(NumMateriales.getText().toString());

            if (Num>1) {


                Log.d("v", "Num es: " + Integer.toString(Num));
                NumMateriales.setEnabled(false);
                AceptarBtn = (Button) findViewById(R.id.button);
                AceptarBtn.setEnabled(false);

                View view = this.getCurrentFocus();

                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                LinearLayout mlscroll = findViewById(R.id.linearscroll);

                for (int i = 0; i < Num; i++) {

                    TextView tv = new TextView(this);
                    tv.setText("Superficie" + Integer.toString(i + 1) + ":");

                    EditText textEdit = new EditText(this);
                    int a = textEdit.generateViewId();
                    textEdit.setHint("Alto");
                    textEdit.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    textEdit.setId(a);

                    EditText textEdit2 = new EditText(this);
                    int a2 = textEdit.generateViewId();
                    textEdit2.setHint("Ancho");
                    textEdit2.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    textEdit2.setId(a2);

                    EditText textEdit3 = new EditText(this);
                    int a3 = textEdit.generateViewId();
                    textEdit3.setHint("Espesor");
                    textEdit3.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    textEdit3.setId(a3);

                    EditText textEdit4 = new EditText(this);
                    int a4 = textEdit.generateViewId();
                    textEdit4.setHint("Area");
                    textEdit4.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    textEdit4.setId(a4);

                    Spinner spinner = new Spinner(this);
                    int b = spinner.generateViewId();
                    spinner.setId(b);

                    EditList.add(textEdit);
                    EditList2.add(textEdit2);
                    EditList3.add(textEdit3);
                    EditList4.add(textEdit4);

                    SpinnerList.add(spinner);

                    mlscroll.addView(tv);
                    mlscroll.addView(textEdit);
                    mlscroll.addView(textEdit2);
                    mlscroll.addView(textEdit3);
                    mlscroll.addView(textEdit4);
                    mlscroll.addView(spinner);

                    populateSpinnerx(spinner);

                }

                Button SaveBtn = new Button(this);
                buttonid = SaveBtn.generateViewId();
                SaveBtn.setId(buttonid);
                SaveBtn.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {

                        int p = 0;

                        double[] sup = new double[Num];

                        while (p < SpinnerList.size()) {

                            Spinner spn = SpinnerList.get(p);

                            EditText et = EditList.get(p);
                            EditText et2 = EditList2.get(p);
                            EditText et3 = EditList3.get(p);
                            EditText et4 = EditList4.get(p);

                            alto = Double.parseDouble(et.getText().toString());
                            ancho = Double.parseDouble(et2.getText().toString());
                            espesor = Double.parseDouble(et3.getText().toString());
                            superficie = Double.parseDouble(et4.getText().toString());

                            Log.d("v", "alto " + et.getText().toString());
                            Log.d("v", "ancho " + et2.getText().toString());
                            Log.d("v", "espesor " + et3.getText().toString());
                            Log.d("v", "area " + et4.getText().toString());

                            MovieSpinnerVO movieSpinnerVO = (MovieSpinnerVO) spn.getSelectedItem();
                            int materialId = movieSpinnerVO.getMovieId();

                            selectedMaterialId = materialId;
                            Log.d("v", "materialiD: " + Float.toString(materialId));

                            MaterialesCaracteristicas matCatVO = dbHelper.getMaterial(materialId);

                            String a = matCatVO.getMaterialName();
                            Log.d("v", a);

                            pw = matCatVO.getDENSIDAD();
                            Log.d("v", Double.toString(pw));

                            n = matCatVO.getCOEFICIENTE();
                            Log.d("v", Double.toString(n));


                            E = Math.pow(10, 9) * matCatVO.getYOUNG();
                            Log.d("v", Double.toString(E));

                            poisson = matCatVO.getPOISSON();
                            Log.d("v", Double.toString(poisson));

                            CL = Math.sqrt(E / pw * (1 - (Math.pow(poisson, 2))));
                            Log.d("v", Double.toString(CL));

                            f11 = (Math.PI / (4 * Math.sqrt(3))) * CL * espesor * (((1 / alto) * (1 / alto)) + ((1 / ancho) * (1 / ancho)));

                            Log.d("v", Double.toString(f11));

                            fc = (Math.sqrt(3) * (343.2 * 343.2)) / (Math.PI * CL * espesor);

                            Log.d("v", Double.toString(fc));

                            CS = (768 * (1 - (Math.pow(poisson, 2)))) / (Math.pow(Math.PI, 8) * E * (Math.pow(espesor, 3)) * (Math.pow(Math.pow(1 / ancho, 2) + Math.pow(1 / alto, 2), 2)));

                            Log.d("v", Double.toString(CS));

                            MS = pw * espesor;

                            st = superficie + st;

                            sup[p] = superficie;

                            for (int i = 0; i < frecoct.length; i++) {

                                Log.d("v", Double.toString(frecoct[i]));

                                if (frecoct[i] < f11) {

                                    KS = 4 * Math.PI * poc * CS * frecoct[i];
                                    TLR1 = 10 * Math.log10(1 + (1 / (Math.pow(KS, 2)))) - 10 * Math.log10(Math.log(1 + (1 / (Math.pow(KS, 2)))));
                                    Log.d("v", "entre1");
                                    frecresultados[i] = TLR1;
                                    Log.d("v", "TLR1: " + Double.toString(TLR1));

                                } else if (f11 < frecoct[i] && frecoct[i] < fc) {


                                    TLR2 = 10 * Math.log10(1 + Math.pow(((Math.PI * frecoct[i] * MS) / (poc)), 2)) - 5;
                                    frecresultados[i] = TLR2;
                                    Log.d("v", "entre2");
                                    Log.d("v", "TLR2: " + Double.toString(TLR2));

                                } else if (frecoct[i] > fc) {

                                    TLR3 = 10 * Math.log10(1 + Math.pow(((Math.PI * fc * MS) / (poc)), 2)) + 10 * Math.log10(n) + 33.22 * Math.log10(frecoct[i] / fc) - 5.7;
                                    frecresultados[i] = TLR3;
                                    Log.d("v", "entre3");
                                    Log.d("v", "TLR3: " + Double.toString(TLR3));
                                }

                            }

                            double[] atresultados = new double[7];

                            for (int i = 0; i < frecoct.length; i++) {
                                atresultados[i] = 1 / Math.pow(10, frecresultados[i] / 10);
                            }

                            myList.add(atresultados);

                            p++;
                        }


                        for (int z = 0; z < myList.size(); z++) {
                            double[] test = myList.get(z);
                            for (int g = 0; g < test.length; g++) {
                                Log.d("v", "TLR1: " + Double.toString(test[g]));
                            }
                        }

                        Log.d("v", "ST: " + Double.toString(st));
                        Log.d("v", "S1: " + Double.toString(sup[0]));
                        Log.d("v", "S2: " + Double.toString(sup[1]));
                        Log.d("v", "MyList size: " + Double.toString(myList.size()));

                        for (j = 0; j < frecoct.length; j++) {

                            res = 0;

                            for (int l = 0; l < myList.size(); l++) {

                                double[] darray = myList.get(l);
                                res = (darray[j] * sup[l]) + res;

                            }

                            atcompuesta[j] = res / st;
                        }

                        for (int k = 0; k < frecoct.length; k++) {
                            Log.d("v", Double.toString(atcompuesta[k]));
                            TL[k] = 10 * Math.log10(1 / atcompuesta[k]);
                            Log.d("v", Double.toString(TL[k]));
                        }

                        generar();

                    }

                });
                SaveBtn.setText("CALCULAR");
                mlscroll.addView(SaveBtn);

                // ACA
            }
            else if(Num==1){

                Toast.makeText(Particion_compuesta.this, "El número debe ser superior a 1!",
                        Toast.LENGTH_SHORT).show();
            }
    }

    }

    public void generar(){

        LinearLayout mlscroll = findViewById(R.id.linearscroll);
        LinearLayout mly = findViewById(R.id.linear);

        ScrollView scroller = findViewById(R.id.sv);
        ViewGroup topLayout =  (ViewGroup)scroller.getParent();
        ViewGroup.LayoutParams params = scroller.getLayoutParams();

        int index = topLayout.indexOfChild(scroller);

        scroller.removeAllViews();
        topLayout.removeView(scroller);

        String value = String.format("%.2f dB", TL[0]);
        tl63.setText("TL @ "+Double.toString(frecoct[0])+" Hz "+"\n"+value);
        tl63.setTypeface(tl63.getTypeface(), Typeface.BOLD);
        tl63.setVisibility(View.VISIBLE);

        String value125 = String.format("%.2f dB", TL[1]);
        tl125.setText("TL @ "+Double.toString(frecoct[1])+" Hz "+"\n"+value125);
        tl125.setTypeface(tl125.getTypeface(), Typeface.BOLD);
        tl125.setVisibility(View.VISIBLE);

        String value250 = String.format("%.2f dB", TL[2]);
        tl250.setText("TL @ "+Double.toString(frecoct[2])+" Hz "+"\n"+value250);
        tl250.setTypeface(tl250.getTypeface(), Typeface.BOLD);
        tl250.setVisibility(View.VISIBLE);

        String value500 = String.format("%.2f dB", TL[3]);
        tl500.setText("TL @ "+Double.toString(frecoct[3])+" Hz "+"\n"+value500);
        tl500.setTypeface(tl500.getTypeface(), Typeface.BOLD);
        tl500.setVisibility(View.VISIBLE);

        String value1000 = String.format("%.2f dB", TL[4]);
        tl1000.setText("TL @ "+Double.toString(frecoct[4])+" Hz "+"\n"+value1000);
        tl1000.setTypeface(tl1000.getTypeface(), Typeface.BOLD);
        tl1000.setVisibility(View.VISIBLE);

        String value2000 = String.format("%.2f dB", TL[5]);
        tl2000.setText("TL @ "+Double.toString(frecoct[5])+" Hz "+"\n"+value2000);
        tl2000.setTypeface(tl2000.getTypeface(), Typeface.BOLD);
        tl2000.setVisibility(View.VISIBLE);

        String value4000 = String.format("%.2f dB", TL[6]);
        tl4000.setText("TL @ "+Double.toString(frecoct[6])+" Hz "+"\n"+value4000);
        tl4000.setTypeface(tl4000.getTypeface(), Typeface.BOLD);
        tl4000.setVisibility(View.VISIBLE);



        List<Entry> entries = new ArrayList<Entry>();

        entries.add(new Entry(63, (float) TL[0]));

        for (int i=1 ; i< frecresultados.length;i++ ){
            // turn your data into Entry objects

            entries.add(new Entry( (float) frecoct[i], (float) TL[i]));
        }

        LineDataSet dataSet = new LineDataSet(entries,"Partición Compuesta"); // add entries to dataset
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

