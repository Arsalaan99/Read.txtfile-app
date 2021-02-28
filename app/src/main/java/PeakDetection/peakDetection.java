package PeakDetection;

import com.jjoe64.graphview.series.DataPoint;

import java.util.ArrayList;

public class peakDetection {
    double xVal =0.0;
    StringBuilder Text;
    ArrayList<DataPoint> arrDataPoint;
    ArrayList<DataPoint> arrDataPoint1;
    double threshold;
    public peakDetection(){
        Text = new StringBuilder();
        arrDataPoint = new ArrayList<>();
        arrDataPoint1 = new ArrayList<>();
    }


    double max(double [] result)
    {
        double max = -999;
        for (int i = 0; i < result.length; i++)
            if (result[i] > max)
                max = result[i];
        return max;
    }

    double avg(double result[]){
        double sum = 0.0;

        for (double num: result) {
            sum += num;
        }

        double average = sum / result.length;

        return average;
    }

    public ArrayList<Integer> Arrayofindex_Peak(double[] result){
        ArrayList<Integer> res = new ArrayList<>();
        double max = max(result);
        threshold =  max - 0.5*max;

        for(int i=0; i<result.length; i++){
            if(result[i] > threshold ){
                if((result[i]>result[i-1]) && (result[i]>result[i+1]) && i>=1) {
                    res.add(i);


                }
            }
        }

        return  res;

    }




    public ArrayList<DataPoint> ArrayofPeak(double[] result, double fs){
        double step = 20.0/(result.length);   //total_time/no of samples.
        double max = max(result);
        threshold =  max - 0.5*max;
        //threshold = max/avg(result);
        Text.append("R peak = [");

        for(int i=0; i<result.length; i++){
            if(result[i] > threshold ){
                if((result[i]>result[i-1]) && (result[i]>result[i+1]) && i>=1) {
                    Text.append(String.valueOf(i));
                    Text.append("  ");
                    DataPoint dp1 = new DataPoint(xVal, result[i]);
                    arrDataPoint1.add(dp1);
                }
            }
            DataPoint dp = new DataPoint(xVal, result[i]);
            xVal += (step); // time(in secs)/(no of samples)
            arrDataPoint.add(dp);
        }

        Text.append("]");
        return arrDataPoint1;
    }

    public ArrayList<DataPoint> ArrayofFilteredSignal(){
        return arrDataPoint;
    }

    public ArrayList<DataPoint> ArrayofFilteredSignal(double[] result1, double fs){
        double val =0.0;
        double step = 20.0/(result1.length);
        ArrayList<DataPoint> arrDP = new ArrayList<>();
        for(int i=0; i<result1.length; i++){
            DataPoint dp = new DataPoint(val, result1[i]);
            val += (step); // fs/(no of samples-1)
            arrDP.add(dp);
        }
        return arrDP;
    }

    public StringBuilder IndexofString(){
        return Text;
    }

}
