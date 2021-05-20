package com.freecsarsalaan99.myapplication;


import android.os.Bundle;

import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;



import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import PeakDetection.peakDetection;
import PeakDetection.robustPeakDetection;
import PlottingOfData.plottingofdata;
import ReadsandStores.ReadandStore;
import Ecgfilter.ecgfilter;



public class MainActivity extends AppCompatActivity {

    double fs = 500;
    int order = 2; //order of the filter
    double lowCutOff = 0.67; //Lower Cut-off Frequency
    double highCutOff = 42; //Higher Cut-off Frequency

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.text);
        textView.setMovementMethod(new ScrollingMovementMethod());
        //GraphView graph = (GraphView) findViewById(R.id.graph);
        GraphView graph1 = findViewById(R.id.graph1);


        ArrayList<DataPoint> arrDataPoint;
        ArrayList<DataPoint> arrDataPoint1;
        ArrayList<DataPoint> arrDataPointd;

        InputStream is = this.getResources().openRawResource(R.raw.ecg_data_1);

        ReadandStore RS = new ReadandStore();  //RS -> object created of class ReadandStore
        ArrayList<Double> AL = RS.ReadandStoring(is); // method accessed of class ReadandStore

        double[] ecgdata = new double[AL.size()];   // double array of data to pass it to filter
        //double xVal =0.0;

        for (int i = 0; i < AL.size(); i++) {

            Double truncatedDouble = BigDecimal.valueOf(AL.get(i)).setScale(4, RoundingMode.HALF_UP).doubleValue();
            ecgdata[i] = truncatedDouble;

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

        double[] slice_ecgdata_f3 = new double[ecgdata.length - 1000];
        if (slice_ecgdata_f3.length >= 0)
            System.arraycopy(ecgdata, 1000, slice_ecgdata_f3, 0, slice_ecgdata_f3.length);


        //Peak Detection of filtered Data

        peakDetection pd = new peakDetection();
        //arrDataPoint1 = pd.ArrayofDP_Peak(slice_ecgdata_f2, fs);
        //arrDataPoint = pd.ArrayofDP_FilteredSignal();
        //StringBuilder Text = pd.IndexOf_RPeak();
        arrDataPoint = pd.ArrayofDP_Signal(slice_ecgdata_f3, fs);   //arrData for Raw Data for graph 2.
        //arrDataPoint = pd.ArrayofDP_Signal(slice_ecgdata_f2, fs);     //arrData for Filtered data for graph 2.
        //arrDataPointd = pd.ArrayofDP_Signal(ecgdata, fs);       // arrData for raw data for graph 1.

        //StringBuilder Text = pd.Mag_of_sig();


        //Robust Peak Detection.
        double sampling_rate =  fs;
        robustPeakDetection rpd = new robustPeakDetection();
        //int heart_rate = rpd.heart_rate(slice_ecgdata_f2, fs);
        ArrayList<Integer> R_pk = rpd.R_Peaks(slice_ecgdata_f2, sampling_rate);
        //ArrayList<Integer> Q_pk = rpd.q_peak_find(ecgdata, R_pk, 8,20, fs);
        //ArrayList<Integer> new_Rpk = rpd.Correct_rpeaks(slice_ecgdata_f2, R_pk, sampling_rate);
        ArrayList<DataPoint> arrDataPointp = new ArrayList<>();
        //ArrayList<DataPoint> arrDataPointq = new ArrayList<>();

        double step = 20.0/(slice_ecgdata_f2.length);
        for(int i=0; i<R_pk.size(); i++){
                DataPoint dp = new DataPoint(R_pk.get(i)*step, ecgdata[999+R_pk.get(i)]);
                arrDataPointp.add(dp);
                //Log.e("new R pk for loop", toString(i));
        }

       /* for(int i=0; i<Q_pk.size(); i++){
            DataPoint dp = new DataPoint(Q_pk.get(i)*step, ecgdata[999+Q_pk.get(i)]);
            arrDataPointq.add(dp);
        }*/


        StringBuilder R = new StringBuilder();
        R.append("R peak = [ ");
        for(int i=0; i<R_pk.size(); i++){
            R.append(R_pk.get(i) + "  ");
        }
        R.append(" ]");
        R.append("\n\nQ peak = [ ");
        for(int i=0; i<R_pk.size(); i++){
            R.append(R_pk.get(i)-13 + "  ");
        }
        R.append(" ]");
        R.append("\n\nP peak = [ ");
        for(int i=0; i<R_pk.size(); i++){
            R.append(R_pk.get(i)-33 + "  ");
        }
        R.append(" ]");
        R.append("\n\nS peak = [ ");
        for(int i=0; i<R_pk.size(); i++){
            R.append(R_pk.get(i)+23 + "  ");
        }
        R.append(" ]");
        R.append("\n\nT peak = [ ");
        for(int i=0; i<R_pk.size(); i++){
            R.append(R_pk.get(i)+48 + "  ");
        }
        R.append(" ]");
        /*R.append("\n[");
        for(int i=0; i<Q_pk.size(); i++){
            R.append(Q_pk.get(i) + "  ");
        }
        R.append("]");*/

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
        DataPoint[] listDpp = new DataPoint[arrDataPointp.size()];
        //DataPoint[] listDpq = new DataPoint[arrDataPointq.size()];

        //DataPoint[] listDpd = new DataPoint[arrDataPointd.size()];


        for (int i = 0; i < arrDataPoint.size(); i++) {

            listDp[i] = arrDataPoint.get(i);
        }

        for (int i = 0; i < arrDataPointp.size(); i++) {

            listDpp[i] = arrDataPointp.get(i);
        }

        /*for (int i = 0; i < arrDataPointq.size(); i++) {

            listDpq[i] = arrDataPointq.get(i);
        }*/

        /*for (int i = 0; i < arrDataPointd.size(); i++) {

            listDpd[i] = arrDataPointd.get(i);
        }*/

        //Plotting of Data.
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(listDp);
        //LineGraphSeries<DataPoint> series3 = new LineGraphSeries<>(listDpd);
        PointsGraphSeries<DataPoint> series2 = new PointsGraphSeries<>(listDpp);
        //PointsGraphSeries<DataPoint> series3 = new PointsGraphSeries<>(listDpq);

        plottingofdata plot = new plottingofdata();
        plot.plot_FILTEREDECG(series, graph1);
        //plot.plot_RAWECG(series, graph);
        plot.plotPeak(series2, graph1);
        //plot.plotPeak(series3, graph1);
        //plot.SetXYaxis(graph);
        plot.SetXYaxis(graph1);

        textView.setText(R.toString());
        //textView.setText(Text.toString()+ '\n' + RR_Interval);
        /*textView.setText(Text.toString() + "\n\n" +"RR Interval Array = "+ RR_Interval+ "\n\n" +
                R.toString() + "\n\n" + missing_r.toString());*/   // index of R peaks

    }
}


