package com.mirk.gyverlamp;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;


public class MainActivity extends AppCompatActivity  implements AppCallback{


    public boolean connectTitle = false;
    public boolean powerOn = false;

    private int SoTimeout = 10000;
    private int responseSize = 80000;


    ImageButton imageButton;

    UDP_Client udpClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        udpClient = new UDP_Client(this);
        udpClient.setIpPort("192.168.0.104", 8888);

        // Получим текущее состояние
        udpClient.sendCommand("GET", true);
        new Thread(udpClient).start();



        // Включение лампы
        imageButton = findViewById(R.id.buttonPower);

        imageButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {

                Log.i("lamp", "Click ");

                // меняем изображение на кнопке 
                if (powerOn){
                    imageButton.setColorFilter( getResources().getColor(R.color.colorNoConnect));
                    udpClient.sendCommand("P_OFF");
                    new Thread(udpClient).start();
                } else {
                    imageButton.setColorFilter(getResources().getColor(R.color.colorPowerOn));
                    udpClient.sendCommand("P_ON");
                    new Thread(udpClient).start();
                }
                powerOn = !powerOn;
            }
        });


    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        udpClient.sendCommand("GET", true);
        new Thread(udpClient).start();


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sleep) {
            showSleepActivity();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

  
    
    // Проверим зарегистрировано ли приложение
    // Показать активити регистрации
    public void showSleepActivity(){
        Intent intent = new Intent(this, SleepActivity.class);
        startActivity(intent);
    }


    @Override
    public void dataHandler(String str) {
        Log.i("dataHandler", str);

        // Есть подключение
        if(str != null){

            if(!connectTitle){
                connectTitle = true;
                setTitle(getString(R.string.app_name) + " - " + getString(R.string.connect_ok) );
                getSupportActionBar().setBackgroundDrawable(
                        new ColorDrawable( getResources().getColor(R.color.colorPowerOn) )
                );
            }


        // Нет подключения
        }else{

            if(connectTitle){
                connectTitle = false;
                setTitle(getString(R.string.app_name) + " - " + getString(R.string.connect_no) );
                getSupportActionBar().setBackgroundDrawable(
                        new ColorDrawable( getResources().getColor(R.color.colorNoConnect) )
                );
            }

        }


    }





}
