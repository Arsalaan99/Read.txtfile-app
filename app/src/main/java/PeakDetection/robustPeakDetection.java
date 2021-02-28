package PeakDetection;

import java.util.ArrayList;

public class robustPeakDetection {
    int total_win_size;
    int sampling_rate;
    double heart_rate;

   /* public ArrayList<Double> window(double[]slice_ecgdata_f2, int win_size, int start){
        double[] res = new double[win_size*sampling_rate];
        for(int i=start, j=0; i<win_size*sampling_rate; i++,j++){
            res[j] = slice_ecgdata_f2[i];
        }

        peakDetection pd = new peakDetection();
        ArrayList<Double> detected_pk = pd.Arrayofindex_Peak(res);
        return  detected_pk;
    }*/

    public StringBuilder heart_rate(double []slice_ecgdata_f2, double sampling_rate){
        /*this.total_win_size = total_win_size;
        this.sampling_rate = sampling_rate;
        int win_size = 6;
        int start = 0;
        int tot_pk = 0;
        for(int i=1; i<=(total_win_size/win_size); i++){
            ArrayList<Double> detected_pk = window(slice_ecgdata_f2, win_size, start);
            tot_pk +=(detected_pk.size());
            start += (win_size*sampling_rate);
        }

        return ((double)tot_pk/(total_win_size/win_size));*/
        peakDetection pd = new peakDetection();
        ArrayList<Integer> pk = pd.Arrayofindex_Peak(slice_ecgdata_f2);
        Integer sum = 0;
        ArrayList<Integer> RR_Intervel = new ArrayList<>();
        StringBuilder Text = new StringBuilder();
        Text.append("[ ");
        for (int i = 1; i <= pk.size() - 1; ++i) {
            //sum += (pk.get(i) - pk.get(i-1));
            RR_Intervel.add(pk.get(i) - pk.get(i-1));
            Text.append(pk.get(i) - pk.get(i-1));
            Text.append("  ");
        }
        Text.append(" ]");

        for(int i=0; i<RR_Intervel.size(); i++){
            sum += RR_Intervel.get(i);
        }
        Text.append('\n'+ "Heart Rate = "+(sampling_rate*60)/(sum.doubleValue()/RR_Intervel.size()));

        return Text;
    }


}
