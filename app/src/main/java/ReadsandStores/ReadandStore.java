package ReadsandStores;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class ReadandStore {


    ArrayList<Double> Data = new ArrayList<>();


    public ArrayList<Double> ReadandStoring(InputStream is) {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));


        try {

            String line;

            while ((line = br.readLine()) != null) {
                Data.add(Double.parseDouble(line));

            }

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Data;

    }

}
