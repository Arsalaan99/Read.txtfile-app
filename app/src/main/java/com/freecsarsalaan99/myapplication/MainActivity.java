package com.freecsarsalaan99.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {


    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.text1);

        textView.setMovementMethod(new ScrollingMovementMethod());
        InputStream is = this.getResources().openRawResource(R.raw.output);
        // String data = "";

        //StringBuffer sbuffer = new StringBuffer();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder text = new StringBuilder();
        try {


            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
                Log.i("Test", "text : " + text + " :end");
                text.append('\n');

            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        textView.setText(text.toString());

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





