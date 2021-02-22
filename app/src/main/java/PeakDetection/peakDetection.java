package PeakDetection;

import com.jjoe64.graphview.series.DataPoint;

import java.util.ArrayList;

public class peakDetection {
    double xVal =0.0;
    StringBuilder Text;
    ArrayList<DataPoint> arrDataPoint;
    ArrayList<DataPoint> arrDataPoint1;
    public peakDetection(){
        Text = new StringBuilder();
        arrDataPoint = new ArrayList<>();
        arrDataPoint1 = new ArrayList<>();
    }


    public ArrayList<DataPoint> ArrayofPeak(double[] result, int SIZE, double fs){
        Text.append("R peak = [");
        for(int i=0; i<SIZE; i++){
            if(result[i] > 0.5 ){
                if((result[i]>result[i-1]) && (result[i]>result[i+1])) {
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

    public StringBuilder IndexofString(){
        return Text;
    }


}
