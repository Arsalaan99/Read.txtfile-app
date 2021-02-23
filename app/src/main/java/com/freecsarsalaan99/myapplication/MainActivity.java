package com.freecsarsalaan99.myapplication;


import android.graphics.Color;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.psambit9791.jdsp.filter.Chebyshev;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
//import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    int filterType = 2; //Can be 1 (for type 1) or 2 (for type 2)
    int rippleFactor = 1; //maximum ripple allowed below unity gain
    double fs = 200;
    int order = 2; //order of the filter
    double lowCutOff = 0.67; //Lower Cut-off Frequency
    double highCutOff = 40; //Higher Cut-off Frequency
    double [] ecgdata = new double[7550];
    int counter = 0;
    GraphView graph;

    Double xVal = 0.0;

    //ArrayList<Integer> index_Rpeak = new ArrayList<Integer>();
    TextView textView;
    StringBuilder Text = new StringBuilder();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.text);
        textView.setMovementMethod(new ScrollingMovementMethod());
        GraphView graph = (GraphView) findViewById(R.id.graph);




        InputStream is = this.getResources().openRawResource(R.raw.output);
        // String data = "";

        //StringBuffer sbuffer = new StringBuffer();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder text = new StringBuilder();
        try {

            String line;
            ArrayList<DataPoint> arrDataPoint=new ArrayList<>();
            ArrayList<DataPoint> arrDataPoint1=new ArrayList<>();

            while ((line = br.readLine()) != null) {

                text.append(line);
                Log.i("Test", "text : " + text + " :end");
                text.append('\n');
                ecgdata[counter] = Double.parseDouble(line);
                counter++;
                /*DataPoint dp = new DataPoint(xVal, Double.parseDouble(line));
                xVal += (fs / 7499.0); // fs/(no of samples-1)
                arrDataPoint.add(dp);*/

            }

            br.close();
            Chebyshev flt = new Chebyshev(ecgdata, fs, filterType);
            // Chebyshev flt = new Chebyshev(ecgdata, fs, rippleFactor, filterType); //signal is of type double[]
            double[] result = flt.bandPassFilter(order, lowCutOff, highCutOff, rippleFactor); //get the result after filtering
            Chebyshev flt1 = new Chebyshev(result, fs, filterType);
            result = flt1.highPassFilter(order, highCutOff, rippleFactor);

            Text.append("R peak = [");
            for(int i=0; i<result.length; i++){
                if(result[i] > 0.5 ){
                    if((result[i]>result[i-1]) && (result[i]>result[i+1])) {
                        Text.append(String.valueOf(i));
                        Text.append("  ");
                        DataPoint dp1 = new DataPoint(xVal, result[i]);
                        arrDataPoint1.add(dp1);
                    }
                }
                DataPoint dp = new DataPoint(xVal, result[i]);
                xVal += (fs / (ecgdata.length-1)); // fs/(no of samples-1)
                arrDataPoint.add(dp);
            }

            Text.append("]");
            DataPoint[] listDp = new DataPoint[arrDataPoint.size()];
            DataPoint[] listDp1 = new DataPoint[arrDataPoint1.size()];
            //Log.i("Int","Size of ecgdata : " + ecgdata.size());



            for(int i=0;i<arrDataPoint.size();i++){

                listDp[i]=arrDataPoint.get(i);
            }

            for(int i=0;i<arrDataPoint1.size();i++){

                listDp1[i]=arrDataPoint1.get(i);
            }

            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(listDp);
            PointsGraphSeries<DataPoint> series2 = new PointsGraphSeries<>(listDp1);
            series2.setSize(10);
            /*series2.setDataPointsRadius(10);
            series2.setThickness(8);*/
            series2.setColor(Color.RED);
            graph.addSeries(series);
            graph.addSeries(series2);
            Log.i("Float","ecg data in floating point numbers" + ecgdata);
            // Log.i("Float", "the size of array "+ecgdata.size());
            Log.i("Float", "the size"+xVal);
            Log.i("Float", "the points"+arrDataPoint);
        } catch (IOException e) {
            e.printStackTrace();
        }


        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(10);

        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(-1);
        graph.getViewport().setMaxY(2);

        graph.getViewport().setScrollable(true); // enables horizontal scrolling
        graph.getViewport().setScrollable(true); // enables vertical scrolling

        textView.setText(Text);
    }
}