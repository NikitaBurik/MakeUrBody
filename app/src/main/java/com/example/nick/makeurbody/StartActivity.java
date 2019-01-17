package com.example.nick.makeurbody;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class StartActivity extends AppCompatActivity {

    int kcal = 100;
    EditText weight;
    RadioGroup calories;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        calories = (RadioGroup)findViewById(R.id.radioGroup);
        RadioButton rb = (RadioButton)calories.findViewById(calories.getCheckedRadioButtonId());
        calories.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup arg0, int id) {
                switch (id) {
                    case R.id.sto:
                        kcal=100;
                        break;
                    case R.id.dwiescie:
                        kcal=200;
                        break;
                    case R.id.piecset:
                        kcal=500;
                        break;
                    default:
                        kcal=100;
                        break;
                }
            }
        });
        RadioGroup calories2 = (RadioGroup)findViewById(R.id.radioGroup2);
        RadioButton rb2 = (RadioButton)calories2.findViewById(calories2.getCheckedRadioButtonId());
        calories2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.tysiace:
                        kcal=1000;
                        break;
                    case R.id.dwatysiace:
                        kcal=2000;
                        break;
                }
            }
        });

    }
    public void PullUp (View view){
        Intent intent = new Intent(this,PullUps.class);
        intent.putExtra("kcal", kcal);
        startActivity(intent);
    }

    public void Stepper(View view){
        Intent intent = new Intent(this,SimplePedometerActivity.class);
        intent.putExtra("kcal", kcal);
        startActivity(intent);
    }
    public void Goal (View view){
        Intent intent = new Intent(this,howToActivity.class);
        startActivity(intent);
    }


}
