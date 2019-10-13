package com.mirk.gyverlamp;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UDP_Client implements Runnable {
    private String cmd;
    private String ip;
    private int port;
    private int SoTimeout = 10000;
    private int responseSize = 80000;
    private boolean needResp = false;

    AppCallback caller;


    public UDP_Client(AppCallback caller) {
        super();
        this.caller = caller;
    }


    public void setIpPort (String _ip, int _port){
       ip = _ip;
       port = _port;
    }

    public void sendCommand(String command){
        cmd = command;
        needResp = false;
    }

    public void sendCommand(String command, boolean needResponse){
        cmd = command;
        needResp = needResponse;
    }


    @Override
    public void run() {

        try {

            // Отправка UDP запроса
            DatagramSocket udpSocket = new DatagramSocket(port);
            InetAddress serverAddr = InetAddress.getByName(ip);
            byte[] buf = cmd.getBytes();
            DatagramPacket sp = new DatagramPacket(buf, buf.length,serverAddr, port);
            udpSocket.send(sp);

            // Получение UDP ответа от лампы
            if(needResp){
                try {
                    String responce = null;
                    byte[] message = new byte[responseSize];
                    DatagramPacket rp = new DatagramPacket(message,message.length);

                    Log.d("UDP client: ", "about to wait to receive");
                    udpSocket.setSoTimeout(SoTimeout);

                    udpSocket.receive(rp);
                    responce = new String(message, 0, rp.getLength());
                    Log.d("udp", "Received text: " +  responce);

                    // Вызываем обработчик для приеховших данных
                    if(responce!= null)
                        caller.dataHandler(responce);

                } catch (IOException e) {
                    caller.dataHandler(null);
                    Log.e("udp", " UDP client has IOException", e);
                }

            }

            udpSocket.close();

        } catch (SocketException e) {
            Log.e("Socket Open:", "Error:", e);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}
