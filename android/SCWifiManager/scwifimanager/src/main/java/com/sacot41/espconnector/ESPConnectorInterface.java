package com.sacot41.espconnector;

/**
 * Created by Samuel on 2017-09-23.
 */

public interface ESPConnectorInterface {
    void onESPConnect();
    void onESPConnectFailure();
    void onReceiveDataFromESP(String data);
}
