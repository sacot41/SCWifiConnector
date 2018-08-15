#include "SCWifiConnector.h"

/**
 * 
 * Public Function
 * 
 */

SCWifiConnector::SCWifiConnector() {
 
}

void SCWifiConnector::init(ConfigBuilder configuration) {
  _configuration = &configuration.build();

  if (_configuration == NULL) return;
  
  _connectionStart = 0;
  updateMode();

  if (_connectorMode == CONNECTION_MODE) startConnectionMode();
  else startTargetMode();

  _timer = new ETSTimer;
  os_timer_setfn(_timer, reinterpret_cast<ETSTimerFunc*>(&SCWifiConnector::runLoop), reinterpret_cast<void*>(this));
  os_timer_arm(_timer, 1000, 0);
}

void SCWifiConnector::resetWifi() {
  if (_configuration == NULL) return;
  _configuration->targetSSID = NULL;
  _configuration->targetPass = NULL;
  _connectionStart = 0;
  _connectorMode = CONNECTION_MODE;
  startConnectionMode();
}

void SCWifiConnector::disableWifi() {
   WiFi.disconnect(true);
   _connectorMode = OFF_MODE;
}

SCWifiConnectorMode SCWifiConnector::getMode() {
  return _connectorMode;
}

/**
 * 
 * Private Function
 * 
 */

void SCWifiConnector::startConnectionMode() {
  _connectionStart = 0;
  WiFi.disconnect(true);
  WiFi.mode(WIFI_AP);
  WiFi.softAPConfig(_configuration->ip, _configuration->ip, _configuration->mask);
  WiFi.softAP(_configuration->connectorSSID, _configuration->connectorPass, 1, _configuration->isHidden);

  _udp.begin(_configuration->port);
}

void SCWifiConnector::startTargetMode() {
  WiFi.disconnect(true);
  WiFi.persistent(false);
  WiFi.mode(WIFI_OFF);
  WiFi.mode(WIFI_STA);
  WiFi.begin(_configuration->targetSSID, _configuration->targetPass);
}

void SCWifiConnector::updateMode() {
  if (_configuration->targetSSID != NULL && _configuration->targetPass != NULL) {
    _connectorMode = CONNECTION_TO_TARGET;
    return;
  }
  _connectorMode = CONNECTION_MODE;
}

boolean SCWifiConnector::hasTimeout(){
    if(_connectionStart <= 0){
      _connectionStart = millis();
      return false;
    }
    return (millis() > (_connectionStart + _configuration->connectionTimeout));
}

void SCWifiConnector::actualise() {
  if (_connectorMode == CONNECTION_MODE) {
    int packetSize = _udp.parsePacket();
    
    if (packetSize) {
      
      int len = _udp.read(_incomingPacket, 255);
      if (len > 0) _incomingPacket[len] = 0;
      
      String stringPacket(_incomingPacket);
      int delimitorIndex = stringPacket.indexOf(':');
      
      String fieldName = stringPacket.substring(0, delimitorIndex);
      String fieldValue = stringPacket.substring(delimitorIndex + 1);

      char *cstr = new char[fieldValue.length() + 1];
      strcpy(cstr, fieldValue.c_str());
 
      if (fieldName.equals("ssid") > 0){
        _configuration->targetSSID = cstr;
      } else if (fieldName.equals("pass") > 0){
        _configuration->targetPass = cstr;
      } else if (fieldName.equals("request") > 0) {
        if (_configuration->deviceIdentificationRequestCallback != NULL) {
          schedule_function(_configuration->deviceIdentificationRequestCallback);
        }
      }
   
      updateMode();
      if (_connectorMode == CONNECTION_TO_TARGET){
        startTargetMode();
      }
      
    }
  } else if (_connectorMode == CONNECTION_TO_TARGET) {
    
    if (WiFi.status() == WL_CONNECTED || hasTimeout()) {
      
      if (WiFi.status() != WL_CONNECTED) {
          if (_configuration->connectionFailureCallback != NULL) {
            schedule_function(_configuration->connectionFailureCallback);
          }
          resetWifi();
      } else {
          _connectorMode = TARGET_MODE;
          if (_configuration->connectionCallback != NULL) {

            char* ssid = new char[33];
            char* passphrase = new char[33];
            
            WiFi.SSID().toCharArray(ssid, 33);
            WiFi.psk().toCharArray(passphrase, 33);
            
            schedule_function(std::bind(_configuration->connectionCallback, ssid, passphrase));
          }
      }
    }
  }

  os_timer_arm(_timer, 1000, 0);
}
