package com.sacot41.espconnector;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by Samuel on 2017-09-20.
 */

public class ESPConnector {

    public static class Builder {

        private ESPConnectorConfig config;
        private Context context;

        public Builder(Context context) {
            config = new ESPConnectorConfig();
            this.context = context;
        }

        public Builder setESPConfigNetwork(String ssid, String passphrase) {
            config.setEspSSID(ssid);
            config.setEspPassPhrase(passphrase);
            return this;
        }

        public Builder setUDPConfig(String ipAddress, int port) {
            config.setIpAddress(ipAddress);
            config.setPort(port);
            return this;
        }

        public Builder setConfig(ESPConnectorConfig config) {
            this.config = config;
            return this;
        }

        public ESPConnector build() {
            config.setContext(this.context);
            return new ESPConnector(config);
        }

    }

    private ESPConnectorInterface listener;

    private ESPConnectorConfig config;
    private WifiManager wifiManager;

    private int currentNetworkId;
    private int espNetworkId;

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable(){
        public void run() {
            if (listener != null) listener.onESPConnectFailure();
            stop();
        }
    };

    private ESPConnector(ESPConnectorConfig config) {
        this.config = config;
    }

    public void setOnESPConnectorInterface(ESPConnectorInterface onESPConnectorInterface) { this.listener = onESPConnectorInterface; }

    public void start() throws Exception {
        if (ActivityCompat.checkSelfPermission(config.getContext(), Manifest.permission.CHANGE_WIFI_STATE) != PackageManager.PERMISSION_GRANTED)
            throw new Exception("Bad permission");

        wifiManager = (WifiManager) config.getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        currentNetworkId = wifiManager.getConnectionInfo().getNetworkId();
        wifiManager.disconnect();

        config.getContext().registerReceiver(networkReceiver,  new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        handler.postDelayed(runnable, config.getConnectionTimeout());
    }

    public boolean connectionToESP() throws Exception {
        int espNetworkId = addESPNetwork();
        return wifiManager.enableNetwork(espNetworkId, true);
    }

    private Thread thread;

    public boolean sendWifiConfigurationToESP(String ssid, String passphrase) {
        String ssidMsg = "ssid:" + ssid;
        String passPhraseMsg = "pass:" + passphrase;

        boolean success = sendMessage(ssidMsg);
        if (success) success = sendMessage(passPhraseMsg);

        return success;
    }

    public boolean sendDeviceIdentificationRequest(String message) {
        String ssidMsg = "request:" + message;
        return sendMessage(ssidMsg);
    }

    public void stop() {
        if (wifiManager == null || config == null) return;
        connectToNetworkId(currentNetworkId);
        wifiManager.removeNetwork(espNetworkId);
        config.getContext().unregisterReceiver(networkReceiver);
    }

    private BroadcastReceiver networkReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (wifiManager == null || context == null || listener == null) return;

            ConnectivityManager conn =  (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = conn.getActiveNetworkInfo();

            if (networkInfo == null) return;
            if (networkInfo.getType() != ConnectivityManager.TYPE_WIFI) return;
            if (!wifiManager.getConnectionInfo().getSSID().equals(config.getEspSSIDForLibrary())) return;

            handler.removeCallbacks(runnable);
            listener.onESPConnect();
        }
    };

    private int addESPNetwork() {
        WifiConfiguration conf = new WifiConfiguration();
        conf.SSID = config.getEspSSIDForLibrary();
        conf.preSharedKey = config.getEspPassPhraseForLibrary();

        for (WifiConfiguration current : wifiManager.getConfiguredNetworks()) {
            if (current.SSID.equals(conf.SSID)) {
                return current.networkId;
            }
        }

        return wifiManager.addNetwork(conf);
    }

    private boolean connectToNetworkId(int networkId) {
        if (networkId < 0) return false;
        wifiManager.disconnect();
        wifiManager.enableNetwork(networkId, true);
        return wifiManager.reconnect();
    }

    private boolean sendMessage(String msg) {
        try {
            DatagramSocket s = new DatagramSocket();
            InetAddress local  = InetAddress.getByName(config.getIpAddress());

            int msg_lenght = msg.length();
            byte []message = msg.getBytes();
            DatagramPacket p = new DatagramPacket(message, msg_lenght, local, config.getPort());
            s.send(p);
        } catch (SocketException e) {
            e.printStackTrace();
            return false;
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
/*
    private Runnable updListener = new Runnable() {
        @Override
        public void run() {
            try {

                InetAddress broadcastIP = InetAddress.getByName(config.getIpAddress());
                Integer port = config.getPort();
                DatagramSocket socket = null;
                while (true) {
                    byte[] recvBuf = new byte[15000];
                    if (socket == null || socket.isClosed()) {
                        socket = new DatagramSocket(port, broadcastIP);
                        socket.setBroadcast(true);
                    }

                    DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);
                    Log.e("UDP", "Waiting for UDP broadcast");
                    socket.receive(packet);

                    String senderIP = packet.getAddress().getHostAddress();
                    String message = new String(packet.getData()).trim();

                    Log.e("UDP", "Got UDB broadcast from " + senderIP + ", message: " + message);

                    socket.close();
                }

            } catch (Exception e) {
                Log.i("UDP", "no longer listening for UDP broadcasts cause of error " + e.getMessage());
            }
        }
    };
*/
}
