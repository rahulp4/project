#!/bin/sh


# get path to equinox jar inside $eclipsehome folder
-cp /pi/home_auto/java_rasp/lib/mqtt-client-0.4.1-20140524.040116-91.jar:/home/pi/home_auto/java_rasp/lib/jssc.jar:/usr/share/java/RXTXcomm.jar:/home/pi/home_auto/openHAB/server/plugins/ch.qos.logback.classic_1.0.7.v20121108-1250.jar:/home/pi/home_auto/openHAB/server/plugins/ch.qos.logback.core_1.0.7.v20121108-1250.jar:/home/pi/home_auto/openHAB/server/plugins/ch.qos.logback.slf4j_1.0.7.v20121108-1250.jar:/home/pi/home_auto/openHAB/server/plugins/org.slf4j.api_1.7.2.v20121108-1250.jar:/home/pi/home_auto/openHAB/server/plugins/org.slf4j.log4j_1.7.2.v20130115-1340.jar:/home/pi/home_auto/java_rasp/config:.

echo Launching the openHAB ...

java -Djava.library.path=/usr/lib/jni -Dlogback.configurationFile=/home/pi/home_auto/java_rasp/config/logback.xml -cp /pi/home_auto/java_rasp/lib/mqtt-client-0.4.1-20140524.040116-91.jar:/home/pi/home_auto/java_rasp/lib/jssc.jar:/usr/share/java/RXTXcomm.jar:/home/pi/home_auto/openHAB/server/plugins/ch.qos.logback.classic_1.0.7.v20121108-1250.jar:/home/pi/home_auto/openHAB/server/plugins/ch.qos.logback.core_1.0.7.v20121108-1250.jar:/home/pi/home_auto/openHAB/server/plugins/ch.qos.logback.slf4j_1.0.7.v20121108-1250.jar:/home/pi/home_auto/openHAB/server/plugins/org.slf4j.api_1.7.2.v20121108-1250.jar:/home/pi/home_auto/openHAB/server/plugins/org.slf4j.log4j_1.7.2.v20130115-1340.jar:/home/pi/home_auto/java_rasp/config:. com.homeauto.mqtt.MessageBrokerService  