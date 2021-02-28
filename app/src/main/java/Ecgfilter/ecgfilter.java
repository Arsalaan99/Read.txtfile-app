package Ecgfilter;

import com.github.psambit9791.jdsp.filter.Bessel;

public class ecgfilter {

    public double[] filter1(double[] ecgdata, double fs, double lowCutOff, double highCutOff){
        Bessel flt2 = new Bessel(ecgdata, fs);
        double []result1 = flt2.lowPassFilter(2, highCutOff);
        Bessel flt3 = new Bessel(result1, fs);
        result1 = flt3.highPassFilter(2, lowCutOff);

        return result1;
    }

    public double[] filter2(double[] result1,double fs, double lowCutOff){
        Bessel flt4 = new Bessel(result1, fs);
        double []result2 = flt4.highPassFilter(2, lowCutOff);

        return result2;
    }
}
