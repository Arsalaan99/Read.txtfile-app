package PeakDetection;

import java.util.ArrayList;

public class robustPeakDetection {
    //int total_win_size;
    double sampling_rate;
    int heart_rate;
    StringBuilder Text = new StringBuilder();
    ArrayList<Boolean> missing_R;


   /* public ArrayList<Double> window(double[] slice_ecgdata_f2, int win_size, int start){
        double[] res = new double[win_size*sampling_rate];
        for(int i=start, j=0; i<win_size*sampling_rate; i++,j++){
            res[j] = slice_ecgdata_f2[i];
        }

        peakDetection pd = new peakDetection();
        ArrayList<Double> detected_pk = pd.Arrayofindex_Peak(res);
        return  detected_pk;
    }*/



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


    public int heart_rate(double []slice_ecgdata_f2, double sampling_rate){

        this.sampling_rate = sampling_rate;
        peakDetection pd = new peakDetection();
        ArrayList<Integer> pk = pd.Arrayofindex_Peak(slice_ecgdata_f2);
        Integer sum = 0;
        ArrayList<Integer> RR_Intervel = new ArrayList<>();
        Text.append("\n");
        Text.append("RR Interval = ");
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

        heart_rate = (int) (Math.round(sampling_rate*60)/(sum.doubleValue()/RR_Intervel.size()));

        Text.append("\n\n"+ "Heart Rate = "+heart_rate);


        return heart_rate;
    }

    public ArrayList<Integer> R_Peaks(double[] slice_ecgdata_f2, double sampling_rate){
        ArrayList<Integer> R = new ArrayList<>();
        ArrayList<Boolean> missing_R = new ArrayList<>();
        this.heart_rate = heart_rate(slice_ecgdata_f2, sampling_rate);
        this.sampling_rate = sampling_rate;
        peakDetection pd = new peakDetection();

        double abs_thr = 0.8;
        int count=3;
        int a = 70;
        boolean found_first_pk = false;
        boolean found_second_pk = false;

        int first_R = 0;
        int second_R = 0;

        if(found_first_pk == false) {
            int start = 0;
            int end = heart_rate+10;
            //System.arraycopy(slice_ecgdata_f2, 0, first_array, 0, first_array.length );
            first_R = pd.max_index(slice_ecgdata_f2, start, end);
        }

        if(first_R < (heart_rate*0.7) ){
            int start = first_R + 3;
            int end = ((int) Math.round(a*(sampling_rate/heart_rate)+first_R)) + start;

            first_R = pd.max_index(slice_ecgdata_f2, start, end);
        }

        R.add(first_R);
        missing_R.add(true);
        //Text.append("\n\n" + "First peak = " + String.valueOf(first_R));

        found_first_pk = true;
        found_second_pk = false;

        if(found_first_pk == true && found_second_pk == false){
            int start = first_R+5;
            int end = start + ((int) Math.round(a*(sampling_rate/heart_rate)));
            second_R = pd.max_index(slice_ecgdata_f2, start, end);
        }

        R.add(second_R);
        missing_R.add(true);
        //Text.append("\n\n" + "Second peak = " + String.valueOf(second_R));
        int first_derivative = second_R - first_R;
        double min_ht = abs_thr * (slice_ecgdata_f2[first_R] + slice_ecgdata_f2[second_R])/2;
        found_second_pk = true;

        int prev_pk = second_R;
        int var = 2;

        if(found_first_pk == true && found_second_pk == true){
            while(true){
                int start = prev_pk+5;
                int end = start + ((int)(Math.round(a*(sampling_rate/heart_rate))));

                if(end > slice_ecgdata_f2.length){
                    break;
                }

                int pk = pd.max_index(slice_ecgdata_f2, start, end);

                if(((pk - prev_pk) < var*first_derivative) && min_ht < slice_ecgdata_f2[pk]){
                    R.add(pk);
                    missing_R.add(true);
                    first_derivative = (int) Math.floor(((pk - prev_pk) + first_derivative)/2);
                    min_ht = abs_thr * (((min_ht/abs_thr) + slice_ecgdata_f2[pk])/2);
                    prev_pk = pk;
                    count++;
                }
                else{
                    pk = (prev_pk+first_derivative-10);
                    R.add(pk);
                    missing_R.add(false);
                    prev_pk = pk;
                    count++;
                }
            }
        }
        this.missing_R = missing_R;

        return R;

    }

    public ArrayList<Boolean> getMissing_R(){
        return missing_R;
    }
    public StringBuilder Heart_rate_Text(){
        return Text;
    }


}
