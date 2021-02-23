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

    double max(double [] result, int SIZE)
    {
        double max = -999;
        for (int i = 0; i < SIZE; i++)
            if (result[i] > max)
                max = result[i];
        return max;
    }

    public ArrayList<DataPoint> ArrayofPeak(double[] result, int SIZE, double fs){
        double max = max(result, SIZE);
        threshold =  max - 0.4*max;
        Text.append("R peak = [");
        for(int i=0; i<SIZE; i++){
            if(result[i] > threshold ){
                if((result[i]>result[i-1]) && (result[i]>result[i+1]) && i>=1) {
                    Text.append(String.valueOf(i));
                    Text.append("  ");
                    DataPoint dp1 = new DataPoint(xVal, result[i]);
                    arrDataPoint1.add(dp1);
                }
            }
            DataPoint dp = new DataPoint(xVal, result[i]);
            xVal += (fs / (SIZE-1)); // fs/(no of samples-1)
            arrDataPoint.add(dp);
        }

        Text.append("]");
        return arrDataPoint1;
    }

    public ArrayList<DataPoint> ArrayofFilteredSignal(){
        return arrDataPoint;
    }

    public ArrayList<DataPoint> ArrayofFilteredSignal(double[] result, int SIZE, double fs){
        for(int i=0; i<SIZE; i++){
            DataPoint dp = new DataPoint(xVal, result[i]);
            xVal += (fs / (SIZE-1)); // fs/(no of samples-1)
            arrDataPoint.add(dp);
        }
        return arrDataPoint;
    }

    public StringBuilder IndexofString(){
        return Text;
    }


}
