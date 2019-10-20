package com.mirk.gyverlamp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.CollationElementIterator;
import java.util.Calendar;

public class AlarmActivity extends AppCompatActivity implements View.OnClickListener{

    TimePickerDialog mTimePicker;

    TextView t1;
    TextView t2;
    TextView t3;
    TextView t4;
    TextView t5;
    TextView t6;
    TextView t7;
    int currentT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);


        t1 = (TextView) findViewById(R.id.textViewT1);
        t2 = (TextView) findViewById(R.id.textViewT2);
        t3 = (TextView) findViewById(R.id.textViewT3);
        t4 = (TextView) findViewById(R.id.textViewT4);
        t5 = (TextView) findViewById(R.id.textViewT5);
        t6 = (TextView) findViewById(R.id.textViewT6);
        t7 = (TextView) findViewById(R.id.textViewT7);

        t1.setOnClickListener(this);
        t2.setOnClickListener(this);
        t3.setOnClickListener(this);
        t4.setOnClickListener(this);
        t5.setOnClickListener(this);
        t6.setOnClickListener(this);
        t7.setOnClickListener(this);


        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);

        mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int h, int m) {

                String hh = h + "";
                String mm = m + "";

                if(h < 10){ hh = "0" + hh;}
                if(m < 10){ mm = "0" + mm;}
                if(h < 1 ){ hh = "00";}
                if(m < 1 ){ mm = "00";}

                String time = hh + ":" + mm;

                ((TextView) findViewById(currentT)).setText(time);
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");

    }



    @Override
    public void onClick(View view){
        currentT = view.getId();
        mTimePicker.show();

    }
}
