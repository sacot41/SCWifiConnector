#include "SCWifiConnectorConfig.h"

ConfigBuilder& ConfigBuilder::setConnectorSSID(const char* ssid, const char* pass) {
  configuration.connectorSSID = ssid;
  configuration.connectorPass = pass;
  return *this; 
}

ConfigBuilder& ConfigBuilder::setHidden(boolean isHidden) {
  if (isHidden) configuration.isHidden = 1;
  else configuration.isHidden = 0;
  return *this;
}

ConfigBuilder& ConfigBuilder::setConnectorIPAndPort(IPAddress ipBroadCast, unsigned int port) {
  configuration.ip = ipBroadCast;
  configuration.port = port;
  return *this; 
}

ConfigBuilder& ConfigBuilder::setTargetSSID(char* ssid, char* pass) {
  configuration.targetSSID = ssid;
  configuration.targetPass = pass;
  return *this;
}

ConfigBuilder& ConfigBuilder::setTimeOut(unsigned long timeout) {
  configuration.connectionTimeout = timeout;
  return *this;
}

ConfigBuilder& ConfigBuilder::setConnectionCallback(void (*func)(const char* ssid, const char* pass)) {
  configuration.connectionCallback = func;
  return *this;
}

ConfigBuilder& ConfigBuilder::setConnnectionFailureCallback(void (*func)()) {
  configuration.connectionFailureCallback = func;
  return *this;
}

ConfigBuilder& ConfigBuilder::setDeviceIdentificationRequestCallBack(void (*func)()) {
  configuration.deviceIdentificationRequestCallback = func;
  return *this;
}

SCWifiConnectorConfig& ConfigBuilder::build() {
  return configuration;
}
