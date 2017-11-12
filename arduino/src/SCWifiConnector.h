/**
 * 
 * ConnectorSSID : 
 * It's the network created by ESP8266 when no target network is specified. It's use when the system is in connection mode.
 * 
 * TargetSSID:
 * It's the network you target to connect; credential is provided by a external device like Android phone when the system is in connection mode
 * 
 * 
 * This library doesn't save target network credential (SSID and password). It's to the user to save it (with EEPROM) and give it to
 * the library in Builder. If Builder have empty or null target network, library go in connection mode.
 * 
 */

#ifndef SCWifiConnector_H
#define SCWifiConnector_H

#include "Arduino.h"

#include <ESP8266WiFi.h>
#include <WiFiUdp.h>

#include "SCWifiConnectorConfig.h"

enum SCWifiConnectorMode {
  CONNECTION_MODE,
  TARGET_MODE,
  OFF_MODE,
};

class SCWifiConnector {
  public:
    SCWifiConnector();

    void init(ConfigBuilder configuration);
    void enableWifi();
    void resetWifi();
    void disableWifi();
    
    SCWifiConnectorMode getMode();

  private:
  
    void startConnectionMode();
    void startTargetMode();
    void updateMode();
    boolean hasTimeout();

    SCWifiConnectorConfig* _configuration = NULL;
    WiFiUDP _udp;
    char _incomingPacket[255];
    SCWifiConnectorMode _connectorMode;

    unsigned long _connectionStart = 0;
    
};

#endif
