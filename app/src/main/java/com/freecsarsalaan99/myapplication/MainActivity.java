package com.freecsarsalaan99.myapplication;

import PlottingOfData.plottingofdata;
import PeakDetection.peakDetection;
import ReadsandStores.ReadandStore;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
/*import android.util.Log;*/
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.psambit9791.jdsp.filter.Chebyshev;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

/*import java.io.BufferedReader;
import java.io.IOException;*/
import java.io.InputStream;
//import java.io.InputStreamReader;
import java.util.ArrayList;
//import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    int filterType = 2; //Can be 1 (for type 1) or 2 (for type 2)
    int rippleFactor = 1; //maximum ripple allowed below unity gain
    double fs = 200;
    int order = 2; //order of the filter
    double lowCutOff = 0.67; //Lower Cut-off Frequency
    double highCutOff = 40; //Higher Cut-off Frequency
    double [] ecgdata = new double[10050];
    int counter = 0;
    GraphView graph;

    Double xVal = 0.0;

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.text);
        textView.setMovementMethod(new ScrollingMovementMethod());
        GraphView graph = (GraphView) findViewById(R.id.graph);

        ArrayList<DataPoint> arrDataPoint=new ArrayList<>();
        ArrayList<DataPoint> arrDataPoint1=new ArrayList<>();


        InputStream is = this.getResources().openRawResource(R.raw.output);

        ReadandStore RS = new ReadandStore();  //RS -> object created of class ReadandStore
        ArrayList<Double> AL= RS.ReadandStoring(is); // method accessed of class ReadandStore
                                                     //which return ArrayList
        double[] ecgdata = new double[AL.size()];   // double array of data to pass it to filter
        for(int i=0; i<AL.size(); i++){
            ecgdata[i] = AL.get(i);
        }


        // Passed through bandpass and highpass filter of Chebyshev type 2
            Chebyshev flt = new Chebyshev(ecgdata, fs, filterType);
            double[] result = flt.bandPassFilter(order, lowCutOff, highCutOff, rippleFactor); //get the result after filtering
            Chebyshev flt1 = new Chebyshev(result, fs, filterType);
            result = flt1.highPassFilter(order, highCutOff, rippleFactor);

            //Peak Detection of filtered Data
            peakDetection pd = new peakDetection();
            arrDataPoint1 = pd.ArrayofPeak(result, AL.size(), fs);
            arrDataPoint = pd.ArrayofFilteredSignal();
            StringBuilder Text = pd.IndexofString();

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