package com.freecsarsalaan99.myapplication;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.psambit9791.jdsp.filter.Bessel;
import com.github.psambit9791.jdsp.filter.Butterworth;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.io.InputStream;
import java.util.ArrayList;

import PeakDetection.peakDetection;
import PlottingOfData.plottingofdata;
import ReadsandStores.ReadandStore;

/*import android.util.Log;*/
/*import com.github.psambit9791.jdsp.filter.Butterworth;
import com.github.psambit9791.jdsp.filter.Chebyshev;*/
/*import java.io.BufferedReader;
import java.io.IOException;*/
//import java.io.InputStreamReader;
//import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    int filterType = 2; //Can be 1 (for type 1) or 2 (for type 2)
    int rippleFactor = 1; //maximum ripple allowed below unity gain
    double fs = 125;
    int order = 1; //order of the filter
    double lowCutOff = 0.67; //Lower Cut-off Frequency
    double highCutOff = 40; //Higher Cut-off Frequency

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.text);
        textView.setMovementMethod(new ScrollingMovementMethod());
        GraphView graph = (GraphView) findViewById(R.id.graph);

        ArrayList<DataPoint> arrDataPoint = new ArrayList<>();
        ArrayList<DataPoint> arrDataPoint1 = new ArrayList<>();


        InputStream is = this.getResources().openRawResource(R.raw.ecg_data_6);

        ReadandStore RS = new ReadandStore();  //RS -> object created of class ReadandStore
        ArrayList<Double> AL= RS.ReadandStoring(is); // method accessed of class ReadandStore
                                                     //which return ArrayList
        double[] ecgdata = new double[AL.size()];   // double array of data to pass it to filter
        for(int i=0; i<AL.size(); i++){
            ecgdata[i] = AL.get(i);
        }


           // Passed through bandpass and highpass filter of Chebyshev type 2
           /* Bessel flt = new Bessel(ecgdata, fs);
            double[] result1 = flt.lowPassFilter(10, 40);*/ //get the result after filtering

            /*Bessel flt1 = new Bessel(ecgdata, fs);
            double []result1 = flt1.lowPassFilter(2, 42);
            Bessel flt = new Bessel(result1, fs);
            result1 = flt.highPassFilter(10, 0.67);

            Bessel flt2 = new Bessel(result1, fs);
            double []result2 = flt2.highPassFilter(2, 0.67);*/


            Bessel flt2 = new Bessel(ecgdata, fs);
            double []result1 = flt2.lowPassFilter(4, 42);
            Bessel flt3 = new Bessel(result1, fs);
            result1 = flt3.highPassFilter(10, 0.67);

            Bessel flt4 = new Bessel(result1, fs);
            double []result2 = flt4.highPassFilter(4, 8);

            /*Bessel flt = new Bessel(result1, fs);
            double []result2 = flt.highPassFilter(10, 0.67);*/


        double[] slice_ecgdata_f1 = new double[result1.length-2000];
        for(int i=0; i<slice_ecgdata_f1.length; i++){
            slice_ecgdata_f1[i] = result1[i+2000];
        }

        double[] slice_ecgdata_f2 = new double[result2.length-2000];
        for(int i=0; i<slice_ecgdata_f2.length; i++){
            slice_ecgdata_f2[i] = result2[i+2000];
        }



            //double[] result2 = flt.bandPassFilter(1,0.67, 40);



            //Peak Detection of filtered Data
            peakDetection pd = new peakDetection();
            arrDataPoint1 = pd.ArrayofPeak(slice_ecgdata_f2, fs);
            //arrDataPoint = pd.ArrayofFilteredSignal();
            StringBuilder Text = pd.IndexofString();
            arrDataPoint = pd.ArrayofFilteredSignal(slice_ecgdata_f1, fs);
            //converting to array of Datapoint.
            DataPoint[] listDp = new DataPoint[arrDataPoint.size()];
            DataPoint[] listDp1 = new DataPoint[arrDataPoint1.size()];


            for(int i=0;i<arrDataPoint.size();i++){

                listDp[i]=arrDataPoint.get(i);
            }

            for(int i=0;i<arrDataPoint1.size();i++){

                listDp1[i]=arrDataPoint1.get(i);
            }

        //Plotting of Data.
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(listDp);
        PointsGraphSeries<DataPoint> series2 = new PointsGraphSeries<>(listDp1);


        plottingofdata plot = new plottingofdata();
        plot.plotECG(series, graph);
        plot.plotPeak(series2, graph);
        plot.SetXYaxis(graph);

        textView.setText(Text);   // index of R peaks
    }
}