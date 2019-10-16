package com.mirk.gyverlamp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.net.InetAddress;

import static java.lang.Integer.parseInt;

public class IpPortActivity extends AppCompatActivity {

    public static final String PREF = "lampPrefs";
    private SharedPreferences sharedPreferences;
    private InetAddress lampIp = null;
    private int lampPort = 0;


    TextView ipView;
    TextView portView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ip_port);


        ipView = findViewById(R.id.editTextIp);
        portView = findViewById(R.id.editTextPort);

        // Получить ipView адрес и порт
        sharedPreferences = getSharedPreferences(PREF, MODE_PRIVATE);
        ipView.setText(sharedPreferences.getString("lampIp", ""));
        portView.setText(sharedPreferences.getString("lampPort", ""));

        final Button button = (Button) findViewById(R.id.buttonSaveIpPort);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(validIP(ipView.getText().toString())){
                    sharedPreferences.edit().putString("lampIp", ipView.getText().toString()).commit();
                }else{
                    Toast toast = Toast.makeText(getApplication(),R.string.not_valid_ip, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                }

                Integer port = 0;
                try {
                    port = parseInt(portView.getText().toString());
                } catch (Exception e){
                    Log.e("parce", e.getMessage());

                }

                if(port > 1 && port < 65535){
                    sharedPreferences.edit().putString("lampPort", port.toString()).commit();
                }else{
                    Toast toast = Toast.makeText(getApplication(),R.string.not_valid_port, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                }

                finish();

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
