# SCWifiConnector
A library to configure the ESP8266 wifi network using an Android application in the same way Google do with Chromecast.

#### This works with the ESP8266 Arduino platform
https://github.com/esp8266/Arduino

## What this library do ? 
- the library attempt to connect to the given target network.
- if it timeout, the library start a network (called connector network) and wait until it receive new credential via udp packet.
- the library support 3 type of message in upd packet : "ssid:(ssid of target network)", "pass:(passphrase of target network)" and "request:(custom value)".
- this library doesn't save the credential; it's up to you to save it in the connection callback. See exemple.

# Install

### For ESP8266 
You can install through the Arduino Library Manager or copie/paste files under arduino/src/ folders.

### For Android
You can compile and install the demo application or download it on Google play: 

<a href='https://play.google.com/store/apps/details?id=com.sacot41.espconnector_example&hl=fr&pcampaignid=MKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1'><img alt='Get it on Google Play' width="258" height="100" src='https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png'/></a>

# Parameter

### For ESP8266 

#### setConnectorSSID(ssid, passPhrase)

This is the network create by library to wait credential of target network. The default value is : SSID = "esp8226_wificonnector_network"
PassPhrase = "wificonnector_1234"

#### setHidden(isHidden)

Whatever the conenctor network is hidden. Default value True.

#### setConnectorIpAndPort(ipBroadCast, port)

The ip and port the library will waiting for UDP packet. Default is 192.168.1.1 port 4120.

#### setTargetSSID(SSID, passPhrase)

#### setTimeOut(timeout)

#### setConnectionCallback()

#### setConnectionFailureCallback()

#### setDeviceIdenfiticationRequestCallback()
