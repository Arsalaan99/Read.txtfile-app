package com.freecsarsalaan99.myapplication;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/*import com.github.psambit9791.jdsp.filter.Bessel;
import com.github.psambit9791.jdsp.filter.Butterworth;*/
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.io.InputStream;
import java.util.ArrayList;

import PeakDetection.peakDetection;
import PeakDetection.robustPeakDetection;
import PlottingOfData.plottingofdata;
import ReadsandStores.ReadandStore;
import Ecgfilter.ecgfilter;

/*import android.util.Log;*/
/*import com.github.psambit9791.jdsp.filter.Butterworth;
import com.github.psambit9791.jdsp.filter.Chebyshev;*/
/*import java.io.BufferedReader;
import java.io.IOException;*/
//import java.io.InputStreamReader;
//import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    //int filterType = 2; //Can be 1 (for type 1) or 2 (for type 2)
    //int rippleFactor = 1; //maximum ripple allowed below unity gain
    double fs = 500;
    int order = 1; //order of the filter
    double lowCutOff = 0.67; //Lower Cut-off Frequency
    double highCutOff = 42; //Higher Cut-off Frequency

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.text);
        textView.setMovementMethod(new ScrollingMovementMethod());
        GraphView graph = (GraphView) findViewById(R.id.graph);

        ArrayList<DataPoint> arrDataPoint;
        ArrayList<DataPoint> arrDataPoint1;


        InputStream is = this.getResources().openRawResource(R.raw.ecg_data_1);

        ReadandStore RS = new ReadandStore();  //RS -> object created of class ReadandStore
        ArrayList<Double> AL = RS.ReadandStoring(is); // method accessed of class ReadandStore

        double[] ecgdata = new double[AL.size()];   // double array of data to pass it to filter
        for (int i = 0; i < AL.size(); i++) {
            ecgdata[i] = AL.get(i);
        }


        // Passed raw ecgdata through filter1 and filter2
        ecgfilter ef = new ecgfilter();   //created ef object of ecgfilter class
        double[] result1 = ef.filter1(ecgdata, fs, lowCutOff, highCutOff);
        double[] result2 = ef.filter2(result1, fs, 8, highCutOff);


        //slicing of both filtered signal
        double[] slice_ecgdata_f1 = new double[result1.length - 1000];
        if (slice_ecgdata_f1.length >= 0)
            System.arraycopy(result1, 1000, slice_ecgdata_f1, 0, slice_ecgdata_f1.length);

        double[] slice_ecgdata_f2 = new double[result2.length - 1000];
        if (slice_ecgdata_f2.length >= 0)
            System.arraycopy(result2, 1000, slice_ecgdata_f2, 0, slice_ecgdata_f2.length);


        //double[] result2 = flt.bandPassFilter(1,0.67, 40);


        //Peak Detection of filtered Data

        //double total_win_size = result2.length/fs;

        peakDetection pd = new peakDetection();
        arrDataPoint1 = pd.ArrayofPeak(slice_ecgdata_f2, fs);
        //arrDataPoint = pd.ArrayofFilteredSignal();
        StringBuilder Text = pd.IndexofString();
        arrDataPoint = pd.ArrayofFilteredSignal(slice_ecgdata_f1, fs);


        //Robust Peak Detection.
        int sampling_rate = (int) fs;
        robustPeakDetection rpd = new robustPeakDetection();
        //int heart_rate = rpd.heart_rate(slice_ecgdata_f2, fs);
        ArrayList<Integer> R_pk = rpd.R_Peaks(slice_ecgdata_f2, sampling_rate);

        /*double[] R_pk_value = new double[R_pk.size()];
        ArrayList<DataPoint> arrDataPoint2 = new ArrayList<>();
        double xVal = 0.0;
        double step = 20.0/(R_pk_value.length);
        for(int i=0; i<R_pk_value.length; i++){
            R_pk_value[i] = slice_ecgdata_f2[R_pk.get(i)];
            DataPoint dp = new DataPoint(xVal, R_pk_value[i]);
            xVal += step;
            arrDataPoint2.add(dp);
        }*/


        StringBuilder R = new StringBuilder();
        R.append("new R peak = [ ");
        for(int i=0; i<R_pk.size(); i++){
            R.append(R_pk.get(i) + "  ");
        }
        R.append(" ]");

        ArrayList<Boolean> missing_R = rpd.getMissing_R();
        StringBuilder missing_r = new StringBuilder();
        missing_r.append("missing_R = [");
        for(int i=0; i<missing_R.size(); i++){
            missing_r.append(missing_R.get(i) + "  ");
        }
        missing_r.append(" ]");

        StringBuilder RR_Interval = rpd.Heart_rate_Text();



        //converting to array of Datapoint.
        DataPoint[] listDp = new DataPoint[arrDataPoint.size()];
        DataPoint[] listDp1 = new DataPoint[arrDataPoint1.size()];


        for (int i = 0; i < arrDataPoint.size(); i++) {

            listDp[i] = arrDataPoint.get(i);
        }

        for (int i = 0; i < arrDataPoint1.size(); i++) {

            listDp1[i] = arrDataPoint1.get(i);
        }

        //Plotting of Data.
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(listDp);
        PointsGraphSeries<DataPoint> series2 = new PointsGraphSeries<>(listDp1);


        plottingofdata plot = new plottingofdata();
        plot.plotECG(series, graph);
        plot.plotPeak(series2, graph);
        plot.SetXYaxis(graph);

        textView.setText(Text.toString() + "\n\n" +"RR Interval Array = "+ RR_Interval+ "\n\n" +
                R.toString() + "\n\n" + missing_r.toString());   // index of R peaks

    }
}