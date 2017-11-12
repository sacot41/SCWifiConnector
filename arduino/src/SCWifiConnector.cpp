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
}

void SCWifiConnector::enableWifi() {
  if (_configuration == NULL) return;
  
  _connectionStart = 0;
  updateMode();

  if (_connectorMode == CONNECTION_MODE) startConnectionMode();
  else startTargetMode();
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

  WiFi.disconnect(true);
  WiFi.mode(WIFI_AP);
  WiFi.softAPConfig(_configuration->ip, _configuration->ip, _configuration->mask);
  WiFi.softAP(_configuration->connectorSSID, _configuration->connectorPass, 1, _configuration->isHidden);

  _udp.begin(_configuration->port);

  while (_connectorMode == CONNECTION_MODE) {
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
          _configuration->deviceIdentificationRequestCallback();
        }
      }
      
      updateMode();
    }
  }

  startTargetMode();
}

void SCWifiConnector::startTargetMode() {
  WiFi.disconnect(true);
  WiFi.mode(WIFI_STA);
  WiFi.begin(_configuration->targetSSID, _configuration->targetPass);
  
  while (WiFi.status() != WL_CONNECTED) {
    delay(250);
    if (hasTimeout()) break;
  }

  if (WiFi.status() != WL_CONNECTED) {
    resetWifi();
    if (_configuration->connectionFailureCallback != NULL) {
      _configuration->connectionFailureCallback();
    }
  } else {
    if (_configuration->connectionCallback != NULL) {
      _configuration->connectionCallback(_configuration->targetSSID, _configuration->targetPass);
    }
  }
  
}

void SCWifiConnector::updateMode() {
  if (_configuration->targetSSID != NULL && _configuration->targetPass != NULL) {
    _connectorMode = TARGET_MODE;
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
