package com.freecsarsalaan99.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Double> ecgdata;

    GraphView graph;

    Double xVal = 0.0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        GraphView graph = (GraphView) findViewById(R.id.graph);

        InputStream is = this.getResources().openRawResource(R.raw.output);
        // String data = "";

        ecgdata = new ArrayList<>();

        //StringBuffer sbuffer = new StringBuffer();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder text = new StringBuilder();
        try {

            String line;
            ArrayList<DataPoint> arrDataPoint=new ArrayList<>();

            while ((line = br.readLine()) != null) {
                text.append(line);
                Log.i("Test", "text : " + text + " :end");
                text.append('\n');
                ecgdata.add(Double.parseDouble(line));
                xVal = xVal + 1;

                DataPoint dp = new DataPoint(xVal, Double.parseDouble(line));
                arrDataPoint.add(dp);
            }
            br.close();
            DataPoint[] listDp = new DataPoint[arrDataPoint.size()];
            for(int i=0;i<arrDataPoint.size();i++){
                listDp[i]=arrDataPoint.get(i);
            }
            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(listDp);
            graph.addSeries(series);
            Log.i("Float","ecg data in floating point numbers" + ecgdata);
            Log.i("Float", "the size of array "+ecgdata.size());
            Log.i("Float", "the size"+xVal);
            Log.i("Float", "the points"+arrDataPoint);
        } catch (IOException e) {
            e.printStackTrace();
        }

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(100);

        graph.getViewport().setScrollable(true); // enables horizontal scrolling
        graph.getViewport().setScrollableY(true); // enables vertical scrolling


    }
}
       /* if (is != null){
            try {
                while ((data = reader.readLine()) != null) {
                    sbuffer.append(data + "n");
                }

                textView.setText(sbuffer);
                is.close();
            } catch (Exception e){
                e.printStackTrace();

            }
        } */





