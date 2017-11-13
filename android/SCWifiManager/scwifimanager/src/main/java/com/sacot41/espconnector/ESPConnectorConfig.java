package com.sacot41.espconnector;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Samuel on 2017-09-23.
 */

public class ESPConnectorConfig implements Parcelable {

    private String espSSID;
    private String espPassPhrase;

    private String ipAddress;
    private int port;

    private int connectionTimeout = 10000;

    private Context context;

    public ESPConnectorConfig() {
        espSSID = "esp8226_wificonnector_network";
        espPassPhrase = "wificonnector_1234";
        ipAddress = "192.168.1.1";
        port = 4210;
    }

    public String getEspSSID() {
        return espSSID;
    }

    public String getEspPassPhrase() {
        return  espPassPhrase;
    }

    String getEspSSIDForLibrary() {
        return "\"" + espSSID + "\"";
    }

    String getEspPassPhraseForLibrary() {
        return "\""+ espPassPhrase +"\"";
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public int getPort() {
        return port;
    }

    Context getContext() {
        return context;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setEspSSID(String espSSID) {
        this.espSSID = espSSID;
    }

    public void setEspPassPhrase(String espPassPhrase) {
        this.espPassPhrase = espPassPhrase;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setPort(int port) {
        this.port = port;
    }

    void setContext(Context context) {
        this.context = context.getApplicationContext();
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(espSSID);
        parcel.writeString(espPassPhrase);
        parcel.writeString(ipAddress);
        parcel.writeInt(port);
    }

    public ESPConnectorConfig(Parcel in){
        this.espSSID = in.readString();
        this.espPassPhrase = in.readString();
        this.ipAddress = in.readString();
        this.port = in.readInt();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public ESPConnectorConfig createFromParcel(Parcel in) {
            return new ESPConnectorConfig(in);
        }

        public ESPConnectorConfig[] newArray(int size) {
            return new ESPConnectorConfig[size];
        }
    };

}
