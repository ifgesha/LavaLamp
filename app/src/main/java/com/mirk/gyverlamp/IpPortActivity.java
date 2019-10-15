package com.mirk.gyverlamp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static java.lang.Integer.parseInt;

public class IpPortActivity extends AppCompatActivity {


    private SharedPreferences lampSettings;
    private InetAddress lampIp = null;
    private int lampPort = 0;


    TextView ipView;
    TextView portView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ip_port);


        ipView = findViewById(R.id.editTextIp);
        portView = findViewById(R.id.editTextIp);

        // Получить ipView адрес и порт
        lampSettings = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        ipView.setText(lampSettings.getString("lampIp", ""));
        portView.setText(lampSettings.getString("lampPort", ""));

        final Button button = (Button) findViewById(R.id.buttonSaveIpPort);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(validIP(ipView.getText().toString())){
                    lampSettings.edit().putString("ip", ipView.getText().toString());
                }else{
                    Toast.makeText(getApplication(),R.string.not_valid_ip, Toast.LENGTH_LONG).show();
                }



                int port = 0;
                try {
                    port = parseInt(portView.getText().toString());
                } finally {
                    if(port > 1 && port < 65535){
                        lampSettings.edit().putInt("port", port);
                    }else{
                        Toast.makeText(getApplication(),R.string.not_valid_port, Toast.LENGTH_LONG).show();
                    }
                }



            }
        });



    }



    public static boolean validIP (String ip) {
        try {
            if ( ip == null || ip.isEmpty() ) {
                return false;
            }

            String[] parts = ip.split( "\\." );
            if ( parts.length != 4 ) {
                return false;
            }

            for ( String s : parts ) {
                int i = parseInt( s );
                if ( (i < 0) || (i > 255) ) {
                    return false;
                }
            }
            if ( ip.endsWith(".") ) {
                return false;
            }

            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }


}
