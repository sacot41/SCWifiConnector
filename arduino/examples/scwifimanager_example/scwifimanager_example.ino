#include <ESP8266WiFi.h>
#include <WiFiUdp.h>
#include <EEPROM.h>
#include "SCWifiConnector.h"

SCWifiConnector wifiConnector;

int PIN_LED = 2;
int FLASH_INTERVAL = 500;

struct NetworkConfig {
  boolean isEmpty;
  char ssid[33];
  char passphrase[33];
};

void setup() {
  Serial.begin(115200);
  pinMode(PIN_LED, OUTPUT); 
  digitalWrite(PIN_LED, HIGH);

  setNetworkConfig("", "");
  NetworkConfig networkConfig = getNetworkConfig();
  wifiConnector.init(ConfigBuilder().setHidden(false)
                                    .setTargetSSID(networkConfig.ssid, networkConfig.passphrase)
                                    .setConnectionCallback(onWifiNetworkConnection)
                                    .setConnnectionFailureCallback(onWifiNetworkConnectionFailure)
                                    .setDeviceIdentificationRequestCallBack(onDeviceIdentificationRequest));

  wifiConnector.enableWifi();
}

void loop() {

}

/**
 * 
 * WifiConnector Callback
 * 
 */

void onWifiNetworkConnection(const char* ssid, const char* pass) {
  setNetworkConfig(ssid, pass);
}

void onWifiNetworkConnectionFailure() {
  Serial.print("on failure");
}

void onDeviceIdentificationRequest() {                  
  digitalWrite(PIN_LED, LOW);
  delay(FLASH_INTERVAL);
  digitalWrite(PIN_LED, HIGH);
  delay(FLASH_INTERVAL);                      
  digitalWrite(PIN_LED, LOW);
  delay(FLASH_INTERVAL);
  digitalWrite(PIN_LED, HIGH);
  delay(FLASH_INTERVAL);                      
  digitalWrite(PIN_LED, LOW);
  delay(FLASH_INTERVAL); 
  digitalWrite(PIN_LED, HIGH);
  delay(FLASH_INTERVAL);                      
  digitalWrite(PIN_LED, LOW);
  delay(FLASH_INTERVAL); 
  digitalWrite(PIN_LED, HIGH);
}

/**
 * 
 * EEPROM function
 * 
 */
 
NetworkConfig getNetworkConfig() {
  NetworkConfig networkConfig;
  EEPROM.begin(512);
  EEPROM.get<NetworkConfig>(0*sizeof(NetworkConfig), networkConfig);
  EEPROM.end();
  return networkConfig;
}

void setNetworkConfig(const char* ssid, const char* passphrase) {
  NetworkConfig networkConfig;

  strcpy(networkConfig.ssid, ssid);
  strcpy(networkConfig.passphrase, passphrase);
  
  EEPROM.begin(512);
  EEPROM.put<NetworkConfig>(0*sizeof(NetworkConfig), networkConfig);
  EEPROM.end();
}

