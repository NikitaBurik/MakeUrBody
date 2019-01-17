/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.example.nick.makeurbody;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.TextView;

import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.EdgeDetail;
import com.hookedonplay.decoviewlib.charts.SeriesItem;

import static java.lang.String.valueOf;

public class SimplePedometerActivity extends Activity implements SensorEventListener, StepListener {
  private TextView textView;
  private SimpleStepDetector simpleStepDetector;
  private SensorManager sensorManager;
  private Sensor accel;
  private static final String TEXT_NUM_STEPS = "Number of Steps: ";
  private int numSteps;
  Integer calory = 0;
  TextView timerValue;

  private long startTime = 0L;

  private Handler customHandler = new Handler();

  long timeInMilliseconds = 0L;
  long timeSwapBuff = 0L;
  long updatedTime = 0L;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.stepcouter);
    textView = findViewById(R.id.text);


    Intent intent = getIntent();

    calory = intent.getIntExtra("kcal",calory);

    timerValue = (TextView) findViewById(R.id.timer);
    startTime = SystemClock.uptimeMillis();
    customHandler.postDelayed(updateTimerThread, 0);

    // Get an instance of the SensorManager
    sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    simpleStepDetector = new SimpleStepDetector();
    simpleStepDetector.registerListener(this);
  }

  @Override
  public void onResume() {
    super.onResume();
    numSteps = 0;
    textView.setText(valueOf(numSteps) + " / " + valueOf(calory));
    sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_FASTEST);
  }

  @Override
  public void onPause() {
    super.onPause();
    sensorManager.unregisterListener(this);
  }

  @Override
  public void onAccuracyChanged(Sensor sensor, int accuracy) {
  }

  @Override
  public void onSensorChanged(SensorEvent event) {
    if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
      simpleStepDetector.updateAccel(
              event.timestamp, event.values[0], event.values[1], event.values[2]);
    }
  }

  @Override
  public void step(long timeNs) {
    numSteps++;
    textView.setText(valueOf(numSteps) + " / " + valueOf(calory) + "kcal");
    DecoView arcView = (DecoView)findViewById(R.id.dynamicArcView);

// Create background track
    arcView.addSeries(new SeriesItem.Builder(Color.argb(255, 218, 218, 218))
            .setRange(0, 100, 100)
            .setInitialVisibility(true)
            .setLineWidth(22f)
            .build());

//Create data series track
    SeriesItem seriesItem1 = new SeriesItem.Builder(Color.argb(255, 64, 196, 0))
            .setRange(0, calory, numSteps)
            .setLineWidth(52f)
            .addEdgeDetail(new EdgeDetail(EdgeDetail.EdgeType.EDGE_OUTER, Color.parseColor("#22000000"), 0.4f))
            .setChartStyle(SeriesItem.ChartStyle.STYLE_DONUT)

            .build();

    int series1Index = arcView.addSeries(seriesItem1);


  }

    private Runnable updateTimerThread = new Runnable() {

        public void run() {

            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;

            updatedTime = timeSwapBuff + timeInMilliseconds;

            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            int milliseconds = (int) (updatedTime % 1000);
            timerValue.setText("" + mins + ":"
                    + String.format("%02d", secs) + ":"
                    + String.format("%03d", milliseconds));
            customHandler.postDelayed(this, 0);
        }

    };




  public void Finish(View view){
    Intent intent = new Intent(this, ResultActivity.class);
    intent.putExtra("result", textView.getText().toString());
    intent.putExtra("time",timerValue.getText().toString());
    startActivity(intent);
  }

}