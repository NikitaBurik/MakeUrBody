package com.example.nick.makeurbody;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.SeriesItem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class ResultActivity extends Activity {

    EditText textmsg;
    static final int READ_BLOCK_SIZE = 1000;

    TextView result;
    TextView textRes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_activity);
//        Date currentTime = Calendar.getInstance().getTime();
        DateFormat currentTime = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
        String date = currentTime.format(Calendar.getInstance().getTime());

        result = findViewById(R.id.result);
        textRes =findViewById(R.id.textRes);
        textRes.setMovementMethod(new ScrollingMovementMethod());



        Intent intent = getIntent();

        String res = intent.getStringExtra("result");
        String time = intent.getStringExtra("time");

        result.setText("Result is: "+ res +
                        "\n"+ "Time is: " + time +
                        "\n"+ "Date of training: " + date);

    }

    public void SaveRes(View view) {
// add-write text into file
        try {
            FileOutputStream fileout=openFileOutput("mytextfile.txt", MODE_APPEND);
            OutputStreamWriter outputWriter=new OutputStreamWriter(fileout);
            outputWriter.write(result.getText().toString()+ "\n");
            outputWriter.close();

            //display file saved message
            //Toast.makeText(getBaseContext(), "File saved successfully!",
            //        Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void OpenRes(View view) {
        try {
            FileInputStream fileIn=openFileInput("mytextfile.txt");
            InputStreamReader InputRead= new InputStreamReader(fileIn);

            char[] inputBuffer= new char[READ_BLOCK_SIZE];
            String s="";
            int charRead;

            while ((charRead=InputRead.read(inputBuffer))>0) {
                // char to string conversion
                String readstring=String.copyValueOf(inputBuffer,0,charRead);
                s +=readstring;
            }
            InputRead.close();
            //Toast.makeText(getBaseContext(), s,Toast.LENGTH_SHORT).show();
            textRes.setText(s);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    }
