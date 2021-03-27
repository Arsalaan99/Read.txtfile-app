package com.freecsarsalaan99.myapplication;

import uk.me.berndporr.iirj.*;
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

import org.w3c.dom.Text;

import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
    int order = 2; //order of the filter
    double lowCutOff = 0.67; //Lower Cut-off Frequency
    double highCutOff = 42; //Higher Cut-off Frequency

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //textView = findViewById(R.id.text);
        //textView.setMovementMethod(new ScrollingMovementMethod());
        GraphView graph = (GraphView) findViewById(R.id.graph);
        GraphView graph1 = findViewById(R.id.graph1);

        ArrayList<DataPoint> arrDataPoint;
        ArrayList<DataPoint> arrDataPoint1;
        ArrayList<DataPoint> arrDataPointd;

        InputStream is = this.getResources().openRawResource(R.raw.ecg_data_1);

        ReadandStore RS = new ReadandStore();  //RS -> object created of class ReadandStore
        ArrayList<Double> AL = RS.ReadandStoring(is); // method accessed of class ReadandStore

        double[] ecgdata = new double[AL.size()];   // double array of data to pass it to filter
        double xVal =0.0;

        for (int i = 0; i < AL.size(); i++) {

            Double truncatedDouble = BigDecimal.valueOf(AL.get(i)).setScale(4, RoundingMode.HALF_UP).doubleValue();
            ecgdata[i] = truncatedDouble;
            /*DataPoint dp = new DataPoint(xVal, ecgdata[i]);
            xVal += (60.0/AL.size());
            arrDataPointd.add(dp);*/
        }


        // Passed raw ecgdata through filter1 and filter2
        ecgfilter ef = new ecgfilter();   //created ef object of ecgfilter class
        double[] result1 = ef.filter1(ecgdata, fs, lowCutOff, highCutOff);
        double[] result2 = ef.filter2(result1, fs, 8, highCutOff);
        /*Butterworth butterworth = new Butterworth();
        butterworth.bandPass(2,fs,(lowCutOff+highCutOff)/2.0,highCutOff - lowCutOff);
        double[] result1 = new double[ecgdata.length];

        for (int i=0; i<ecgdata.length; i++){
            result1[i] = butterworth.filter(ecgdata[i]);
        }

        double[] result2 = new double[result1.length];

        for (int i=0; i<ecgdata.length; i++){
            result2[i] = butterworth.filter(result1[i]);
        }*/









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
        arrDataPoint = pd.ArrayofFilteredSignal(slice_ecgdata_f2, fs);
        arrDataPointd = pd.ArrayofFilteredSignal(ecgdata, fs);
        //StringBuilder Text = pd.Mag_of_filt_sig();


        //Robust Peak Detection.
        double sampling_rate =  fs;
        robustPeakDetection rpd = new robustPeakDetection();
        //int heart_rate = rpd.heart_rate(slice_ecgdata_f2, fs);
        ArrayList<Integer> R_pk = rpd.R_Peaks(slice_ecgdata_f2, sampling_rate);

        ArrayList<DataPoint> arrDataPoint2 = new ArrayList<>();

        double step = 20.0/(slice_ecgdata_f2.length);
        for(int i=0; i<R_pk.size(); i++){
                DataPoint dp = new DataPoint(R_pk.get(i)*step, slice_ecgdata_f2[R_pk.get(i)]);
                arrDataPoint2.add(dp);
        }


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
        DataPoint[] listDp1 = new DataPoint[arrDataPoint2.size()];
        DataPoint[] listDpd = new DataPoint[arrDataPointd.size()];


        for (int i = 0; i < arrDataPoint.size(); i++) {

            listDp[i] = arrDataPoint.get(i);
        }

        for (int i = 0; i < arrDataPoint2.size(); i++) {

            listDp1[i] = arrDataPoint2.get(i);
        }

        for (int i = 0; i < arrDataPointd.size(); i++) {

            listDpd[i] = arrDataPointd.get(i);
        }

        //Plotting of Data.
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(listDp);
        LineGraphSeries<DataPoint> series3 = new LineGraphSeries<>(listDpd);
        PointsGraphSeries<DataPoint> series2 = new PointsGraphSeries<>(listDp1);


        plottingofdata plot = new plottingofdata();
        plot.plot_FILTEREDECG(series, graph1);
        plot.plot_RAWECG(series3, graph);
        plot.plotPeak(series2, graph1);
        plot.SetXYaxis(graph);
        plot.SetXYaxis(graph1);

        //textView.setText(Text.toString()+ '\n' + RR_Interval);
        /*textView.setText(Text.toString() + "\n\n" +"RR Interval Array = "+ RR_Interval+ "\n\n" +
                R.toString() + "\n\n" + missing_r.toString());*/   // index of R peaks

    }
}


