

SET PROJET_HOME=D:\project
set CLASSPATH=%CLASSPATH%;.;%PROJET_HOME%\lib\ch.qos.logback.classic_1.0.7.v20121108-1250.jar;%PROJET_HOME%\lib\ch.qos.logback.core_1.0.7.v20121108-1250.jar;%PROJET_HOME%\lib\ch.qos.logback.slf4j_1.0.7.v20121108-1250.jar;%PROJET_HOME%\lib\commons-io-1.4.jar;%PROJET_HOME%\lib\jssc.jar;%PROJET_HOME%\lib\mqtt-client-0.4.1-20140524.040116-91.jar;%PROJET_HOME%\lib\org.slf4j.api_1.7.2.v20121108-1250.jar;%PROJET_HOME%\lib\RXTXcomm.jar;%PROJET_HOME%\lib\org.slf4j.api_1.7.2.v20121108-1250.jar

run %PROJET_HOME%\src\clearnclass
cd src
javac %PROJET_HOME%\src\com\iot\rxtx\radiocloud\mqtt\MessageBrokerService.java
java -Djava.library.path=D:\project\lib com.iot.rxtx.radiocloud.mqtt.MessageBrokerService

rem javac %PROJET_HOME%\src\com\homeauto\mqtt\MessageBrokerService.java

