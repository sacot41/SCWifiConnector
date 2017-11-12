#ifndef SCWifiConnectorConfig_H
#define SCWifiConnectorConfig_H

#include "Arduino.h"

#include <ESP8266WiFi.h>
#include <WiFiUdp.h>

class SCWifiConnectorConfig {
  public:
      
    const char* connectorSSID = "esp8226_wificonnector_network";
    const char* connectorPass = "wificonnector_1234";

    // 1 = hidden, 0 = display
    int isHidden = 1;

    char* targetSSID;
    char* targetPass;
    
    IPAddress ip = IPAddress(192, 168, 1, 1);
    IPAddress mask = IPAddress(255, 255, 255, 0);
    unsigned int port = 4210;

    unsigned long connectionTimeout = 10000;

    void (*connectionCallback)(const char* ssid, const char* pass) = NULL;
    void (*connectionFailureCallback)() = NULL;
    void (*deviceIdentificationRequestCallback)() = NULL;
    
};

class ConfigBuilder {
    public:
      ConfigBuilder() {};

      ConfigBuilder& setConnectorSSID(const char* ssid, const char* pass); //Network created by ESP8266 when library is in connection mode
      ConfigBuilder& setHidden(boolean isHidden);
      ConfigBuilder& setConnectorIPAndPort(IPAddress ipBroadCast, unsigned int port);//IP and Port for UDP communication in connection mode
      ConfigBuilder& setTargetSSID(char* ssid, char* pass); //If null, library go in connection mode
      ConfigBuilder& setTimeOut(unsigned long timeout);
      ConfigBuilder& setConnectionCallback(void (*func)(const char* ssid, const char* pass));
      ConfigBuilder& setConnnectionFailureCallback(void (*func)());
      ConfigBuilder& setDeviceIdentificationRequestCallBack(void (*func)());
    
      SCWifiConnectorConfig& build();
      
    private:
      SCWifiConnectorConfig configuration;
      
};

#endif
