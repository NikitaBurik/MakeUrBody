package com.example.nick.makeurbody;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.EdgeDetail;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.charts.SeriesLabel;

import static java.lang.String.valueOf;

public class PullUps extends AppCompatActivity {
    SensorManager mySensorManager;
    Sensor myProximitySensor;
  //  TextView dist;
    TextView counter;
    int count;
    Integer calory = 0;
    TextView timer;

    private long startTime = 0L;

    private Handler customHandler = new Handler();

    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pullups);
        // dist = findViewById(R.id.proxx);
         counter = findViewById(R.id.coount);
        Intent intent = getIntent();

        calory = intent.getIntExtra("kcal",calory);


        timer = (TextView) findViewById(R.id.timer);
        startTime = SystemClock.uptimeMillis();
        customHandler.postDelayed(updateTimerThread, 0);
        mySensorManager = (SensorManager) getSystemService(
                Context.SENSOR_SERVICE);
        myProximitySensor = mySensorManager.getDefaultSensor(
                Sensor.TYPE_PROXIMITY);

        if (myProximitySensor == null) {
            //dist.setText("No Proximity Sensor!");
        } else {
            mySensorManager.registerListener(proximitySensorEventListener,
                    myProximitySensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
    }
    SensorEventListener proximitySensorEventListener = new SensorEventListener() {
        int count=0;
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // TODO Auto-generated method stub
        }
        @Override
        public void onSensorChanged(SensorEvent event) {
            // TODO Auto-generated method stub
            if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
                if (event.values[0] == 0) {
                    //dist.setText("Near");
                    count++;
                } else {
                   // dist.setText("Away");
                }
            }
            counter.setText(valueOf(count) + " / " + valueOf(calory) + " kcal");

            DecoView arcView = (DecoView)findViewById(R.id.dynamicArcView);

// Create background track
            arcView.addSeries(new SeriesItem.Builder(Color.argb(255, 218, 218, 218))
                    .setRange(0, 100, 100)
                    .setInitialVisibility(true)
                    .setLineWidth(22f)
                    .build());

//Create data series track
            SeriesItem seriesItem1 = new SeriesItem.Builder(Color.argb(255, 64, 196, 0))
                    .setRange(0, calory, count)
                    .setLineWidth(52f)
                    .addEdgeDetail(new EdgeDetail(EdgeDetail.EdgeType.EDGE_OUTER, Color.parseColor("#22000000"), 0.4f))
                    .setChartStyle(SeriesItem.ChartStyle.STYLE_DONUT)

                    .build();

            int series1Index = arcView.addSeries(seriesItem1);

        }
    };

    private Runnable updateTimerThread = new Runnable() {

        public void run() {

            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;

            updatedTime = timeSwapBuff + timeInMilliseconds;

            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            int milliseconds = (int) (updatedTime % 1000);
            timer.setText("" + mins + ":"
                    + String.format("%02d", secs) + ":"
                    + String.format("%03d", milliseconds));
            customHandler.postDelayed(this, 0);
        }

    };


    public void HowTo(View view){
        Intent intent = new Intent(this,howToActivity.class);
        startActivity(intent);
    }
    public void Finish(View view){
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("result", counter.getText().toString());
        intent.putExtra("time",timer.getText().toString());
        startActivity(intent);
    }
    }

