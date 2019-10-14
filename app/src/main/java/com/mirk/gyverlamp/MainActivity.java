package com.mirk.gyverlamp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import static java.lang.Integer.parseInt;


public class MainActivity extends AppCompatActivity {


    public boolean connectTitle = false;
    public int power = 0;
    public int mode = 1;
    public int brightness = 50;
    public int speed = 1;
    public int scale = 1;


    public InetAddress lampIp = null;
    public int lampPort = 0;
    public int soccetTimeout = 3000;
    public int responseSize = 8000;

    ImageButton imageButton;



    private SharedPreferences lampSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // Получить ip адрес и порт
        lampSettings = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        try {
            lampIp = InetAddress.getByName(lampSettings.getString("lampIp", null));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        lampPort = lampSettings.getInt("lampPort", 0);
        Log.i("lampSettings", "ip="+ lampIp.toString() + " port="+lampPort);



         // Проверить сокдинение с лампой при запуске приложения
        new TaskUDP().execute("GET", "need_response");


       // Toast.makeText(this,"Не настроен IP / порт", Toast.LENGTH_LONG).show();

        // Включение/Включение лампы
        imageButton = findViewById(R.id.buttonPower);
        imageButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {

                Log.i("lamp", "Click ");
                // меняем изображение на кнопке 
                if (power == 1){
                    new TaskUDP().execute("P_OFF", "need_response");
                } else {
                    new TaskUDP().execute("P_ON", "need_response");
                }

            }
        });



    }

    // Обновить интерфейс
    public void updateUI(String str){

        // Не настроен коннект
        if("noIpPort".contains(str)){
            Toast.makeText(this,"Не настроен IP / порт", Toast.LENGTH_LONG).show();
            return;
        }


        // Нет подключения
        if(str == null){
            setTitle(getString(R.string.app_name) + " - " + getString(R.string.connect_no));
            getSupportActionBar().setBackgroundDrawable(
                    new ColorDrawable(getResources().getColor(R.color.colorNoConnect))
            );
            imageButton.setColorFilter( getResources().getColor(R.color.colorNoConnect));
            return;
        }

        // Есть подключение
        setTitle(getString(R.string.app_name) + " - " + getString(R.string.connect_ok) );
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable( getResources().getColor(R.color.colorPowerOn) )
        );

        // Парсим ответ
        String[] resp = str.split("\\s+");   //CURR 1 102 29 5 1

        // Текущее состояние
        if("CURR".contains(resp[0])) {
            mode = parseInt(resp[1]);
            brightness = parseInt(resp[2]);
            speed = parseInt(resp[3]);
            scale = parseInt(resp[4]);
            power = parseInt(resp[5]);

            // Обновить UI
            if (power == 1){
                imageButton.setColorFilter(getResources().getColor(R.color.colorPowerOn));
            } else {
                imageButton.setColorFilter( getResources().getColor(R.color.colorNoConnect));
            }

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // Мен. ...
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        Intent intent = new Intent();


        if (id == R.id.action_sleep) {
             intent = new Intent(this, SleepActivity.class);
        }

        if (id == R.id.action_lamp_setup) {
            intent = new Intent(this, IpPortActivity.class);
        }

        if (id == R.id.action_alarm) {
            intent = new Intent(this, AlarmActivity.class);
        }

        startActivity(intent);

        return true;

    }





    class TaskUDP extends AsyncTask<String, String, String> {

        boolean need_response = false;

        @Override
        protected String doInBackground(String... params) {

            if(lampIp.isLoopbackAddress() || lampPort < 1){
                need_response = true;
                return "noIpPort";
            }

            Log.d("UDP client", "doInBackground" );
            String responce = null;
            try {
                // Отправка UDP запроса
                DatagramSocket udpSocket = new DatagramSocket(lampPort);
                byte[] buf = params[0].getBytes();
                DatagramPacket sp = new DatagramPacket(buf, buf.length, lampIp, lampPort);
                udpSocket.send(sp);
                Log.i("UDP client", "send:" +params[0] );

                // Получение UDP ответа от лампы если нужно
                if("need_response".contains(params[1])) {
                    need_response = true;
                    try {

                        byte[] message = new byte[responseSize];
                        DatagramPacket rp = new DatagramPacket(message, message.length);

                        Log.d("UDP client", "about to wait to receive");
                        udpSocket.setSoTimeout(soccetTimeout);

                        udpSocket.receive(rp);
                        responce = new String(message, 0, rp.getLength());
                        Log.i("UDP client", "Received text: " + responce);

                    } catch (IOException e) {
                        Log.e("UDP client", "IOException", e);
                    }
                }
                udpSocket.close();

            } catch (SocketException e) {
                Log.e("SocketOpen", "Error:", e);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return responce;
        }

        @Override
        protected void onPostExecute(String responce) {
            if(need_response)
                updateUI(responce);
        }
    }



}
