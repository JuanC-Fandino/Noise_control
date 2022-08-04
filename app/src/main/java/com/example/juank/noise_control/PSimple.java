package com.example.juank.noise_control;

import android.util.Log;
import java.lang.Math;


/**
 * Created by Juank on 17/03/18.
 */

public class PSimple {

    public static double TLR1,TLR2, TLR3, CL, f11, fc,CS, KS, MS;
    public static double poc=(1.204*343.2);
    public static double c = 343.2;
    public static double [] frecoct= {63,100,125,160,200,250,315,400,500,630,800,1000,1250,1600,2000,2500,3150,4000,8000};
    public double [] frecresultados=new double[19] ;
    public double [] frec = new double[2];

    public double[] PS(double h, double a, double b, double pw, double n, double E, double poisson)
    {

        Log.d("v","ENTRE A LA FUNCIÓN - Partición Simple  ");

        Log.d("v",Double.toString(a));
        Log.d("v",Double.toString(b));
        Log.d("v",Double.toString(h));

        CL=Math.sqrt(E/(pw*(1-Math.pow(poisson,2))));
        Log.d("v",Double.toString(CL));

        f11=(Math.PI/(4*Math.sqrt(3)))*CL*h*(((1/a)*(1/a))+((1/b)*(1/b)));

        Log.d("v",Double.toString(f11));

        fc=(Math.sqrt(3)*Math.pow(c,2))/(Math.PI*CL*h);

        Log.d("v",Double.toString(fc));

        CS=(768*(1-(Math.pow(poisson,2))))/(Math.pow(Math.PI,8)*E*(Math.pow(h,3))*(Math.pow(Math.pow(1/a,2)+Math.pow(1/b,2),2)  ));

        Log.d("v",Double.toString(CS));

        MS=pw*h;

        for (int i=0; i<frecoct.length;i++) {

            Log.d("v",Double.toString(frecoct[i]));

            if (frecoct[i] < f11) {

                KS=4*Math.PI*poc*CS*frecoct[i];
                TLR1 = 10 * Math.log10(1 + (1 / (Math.pow(KS,2) ) )  ) - 10 * Math.log10(    Math.log(1 + (1 / (Math.pow(KS,2))    )    )     );
                Log.d("v","entre1");
                frecresultados[i]=TLR1;

            } else if (f11 < frecoct[i] && frecoct[i] < fc) {


                TLR2 = 10 * Math.log10(1 + Math.pow(((Math.PI * frecoct[i] * MS) / (poc)), 2)) - 5;
                frecresultados[i]=TLR2;
                Log.d("v","entre2");


            } else if (frecoct[i] > fc) {

                TLR3 = 10 * Math.log10(1 + Math.pow(((Math.PI * fc * MS) / (poc)), 2)) + 10 * Math.log10(n) + 33.22 * Math.log10(frecoct[i] / fc) - 5.7;
                frecresultados[i]=TLR3;
                Log.d("v","entre3");

            }

        }

        return frecresultados;

    }

    public double [] frecs (double h, double a, double b, double pw, double n, double E, double poisson){


        Log.d("v","ENTRE A LA FUNCIÓN - Frecuencias  ");

        Log.d("v",Double.toString(a));
        Log.d("v",Double.toString(b));
        Log.d("v",Double.toString(h));

        CL=Math.sqrt(E/(pw*(1-Math.pow(poisson,2))));
        Log.d("v",Double.toString(CL));

        f11=(Math.PI/(4*Math.sqrt(3)))*CL*h*(((1/a)*(1/a))+((1/b)*(1/b)));

        Log.d("v",Double.toString(f11));

        fc=(Math.sqrt(3)*Math.pow(c,2))/(Math.PI*CL*h);

        Log.d("v",Double.toString(fc));

        frec[0]=f11;
        frec[1]=fc;

        return frec;

    }

}
