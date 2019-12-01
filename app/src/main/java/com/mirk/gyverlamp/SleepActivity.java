package com.mirk.gyverlamp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SleepActivity extends AppCompatActivity implements View.OnClickListener {


    Button sleepBtn;
    TextView sleepMin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep);

        sleepMin = (TextView) findViewById(R.id.editTextSleep);
        sleepBtn = (Button) findViewById(R.id.buttonSleepStart);
        sleepBtn.setOnClickListener(this);

        Log.d("UDP client", "s=");


    }

    @Override
    public void onClick(View v) {


        if(!sleepMin.getText().toString().matches("\\d{1,3}")){
            Toast toast = Toast.makeText(getApplication(),"Введите значение от 1 до 180", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return;
        }

        int s = Integer.parseInt(sleepMin.getText().toString());

        if(sleepMin.getText().toString().matches("") || s < 1 || s > 180){
            Toast toast = Toast.makeText(getApplication(),"Введите значение от 1 до 180", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return;
        }

        s *= 60;

        Intent intent = new Intent();
        intent.putExtra("timer", s + "000" );
        setResult(RESULT_OK, intent);
        finish();
    }


}
