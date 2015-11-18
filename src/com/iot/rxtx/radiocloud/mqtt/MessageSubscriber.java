package com.iot.rxtx.radiocloud.mqtt;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iot.rxtx.radiocloud.mqtt.common.AppPropertiesMap;
import com.iot.rxtx.radiocloud.mqtt.common.Utility;
import com.iot.rxtx.radiocloud.serial.SerialUSBPortUtility;
import com.iot.rxtx.radiocloud.serial.SerialUSBPortUtilityJSSC;

public class MessageSubscriber implements MqttCallback {

	final static Logger logger = LoggerFactory.getLogger(MessageSubscriber.class);

	private static MessageSubscriber	instance	=	null;
	
	private static Object lock	=	new Object();

	private MessageSubscriber(){
		
	}
	
	public static MessageSubscriber	getInstance(){
		if(instance==null){
			synchronized(lock){
				instance	=	new MessageSubscriber();
				
				if(logger.isInfoEnabled()){
					logger.info("Successfully initialized MQTT Subscriber");
				}			
				
			}
		}
		
		return instance;
	}
	
	MqttClient myClient;
	MqttConnectOptions connOpt;

	//static final String BROKER_URL =	"tcp://"+AppPropertiesMap.MQTT_SERVER_HOST_IP+":"+AppPropertiesMap.MQTT_SERVER_PORT; 
	//static final String BROKER_URL =	"tcp://localhost:1883";
	static final String BROKER_URL =	"tcp://test.mosquitto.org:1883";
	
	// the following two flags control whether this example is a publisher, a subscriber or both
	static final Boolean subscriber = true;
	static final Boolean publisher = false;
	boolean isRunning	=	false;
	
	public boolean stop(){
		try{
			isRunning	=	false;
			
			myClient.disconnect();
			ServiceStatusHolder.getInstance().setMqttSubRunning(false);
			
			
		} catch (Exception ex){
			ex.printStackTrace();
			return false;
		}
		
		if(logger.isInfoEnabled()){
			logger.info("Successfully Stopped MQTT Subscriber");
		}			
		
		return true;
	}
	
	/**
	 * 
	 * connectionLost
	 * This callback is invoked upon losing the MQTT connection.
	 * 
	 */
	@Override
	public void connectionLost(Throwable t) {
		
		if(logger.isErrorEnabled()){
			logger.error("MQTT Subscriber connection lost");
		}			
		
		// code to reconnect to the broker would go here if desired
	}



	/**
	 * 
	 * MAIN
	 * 
	 */
	public static void main(String[] args) {
		MessageSubscriber smc = new MessageSubscriber();
		smc.runClient();
		
	}
	
	/**
	 * 
	 * runClient
	 * The main functionality of this simple example.
	 * Create a MQTT client, connect to broker, pub/sub, disconnect.
	 * 
	 */
	public void runClient() {
		// setup MQTT Client
		String clientID = AppPropertiesMap.MQTT_CLIENT_ID;
		connOpt = new MqttConnectOptions();
		
		connOpt.setCleanSession(true);
		connOpt.setKeepAliveInterval(30);
		
		// Connect to Broker
		try {
			isRunning	=	true;
			MemoryPersistence persistence = new MemoryPersistence();
			
			myClient = new MqttClient(BROKER_URL, clientID,persistence);
			myClient.setCallback(this);
			myClient.connect(connOpt);
			
		} catch (MqttException e) {
			e.printStackTrace();
			System.exit(-1);
			isRunning	=	false;
		}
		ServiceStatusHolder.getInstance().setMqttSubRunning(true);

		if(logger.isInfoEnabled()){
			logger.info("Successfully initialized MQTT Subscriber and connected to broker "+BROKER_URL);
		}			


		String myTopic = "/mytopic";

		// subscribe to topic if subscriber
		while (subscriber && isRunning) {
			try {
				int subQoS = 0;
				myClient.subscribe(myTopic, subQoS);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void messageArrived(String arg0, MqttMessage arg1) throws Exception {
		// TODO Auto-generated method stub
		try{
			String receivedPayload	=	new String(arg1.getPayload());
			
			if(logger.isDebugEnabled()){
				logger.debug("Message arrived as payload "+receivedPayload);
			}			
			
			if(receivedPayload!=null && receivedPayload.equalsIgnoreCase(Utility.STOP)){
				System.out.println("\n**** Stopping Command Received");
				if(MessageBrokerService.getInstance().stopUSBService()){
					if(MessageBrokerService.getInstance().stopMQTTService()){
						SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");

				        Date resultdate = new Date(System.currentTimeMillis());
						System.out.println("\n System shutdown was successfull at "+Utility.getDateTime());
					}
					
				}
				
				System.exit(0);
				
			} else {
				//Call USB WRITER HERE
	
				if(Utility.isRXTXMode){
			    	SerialUSBPortUtility	instance	=	SerialUSBPortUtility.getInstance();
			    	//receivedPayload	=	Utility.fomatToUSB(receivedPayload);
			    	receivedPayload	=	Utility.toOutboundFormat(receivedPayload);
			    	if(logger.isDebugEnabled()){
			    		logger.debug("No formatting of message");
			    		logger.debug("Sending to SerialUSBUtility message as "+receivedPayload);
			    	}
			    	
			    	
			    	instance.sendToSerialPort(receivedPayload);
				} else {
					
			    	SerialUSBPortUtilityJSSC	instance	=	SerialUSBPortUtilityJSSC.getInstance();
			    	receivedPayload	=	Utility.toOutboundFormat(receivedPayload);
			    	instance.sendToSerialPort(receivedPayload);
				}
		    	if(logger.isInfoEnabled()){
					logger.info("Message formatted to "+receivedPayload+" and sent to USB port");
				}			
				
				//String strMQTTFormat	=	"ON";//Utility.fomatToMQTT(receivedPayload);
				//MessagePublisher.getMessagePublisher().publishMessaeg(strMQTTFormat);
			}
			} catch (Throwable th){
			if(th!=null){
				logger.error(th.toString());	
				th.printStackTrace();
			}			
			
		}
	}
}