package com.iot.rxtx.radiocloud.mqtt;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.iot.rxtx.radiocloud.mqtt.common.Utility;
import com.iot.rxtx.radiocloud.serial.SerialUSBPortUtility;
import com.iot.rxtx.radiocloud.serial.SerialUSBPortUtilityJSSC;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageBrokerService {

	final static Logger logger = LoggerFactory.getLogger(MessageBrokerService.class);
	
	private static MessageBrokerService	instance	=	null;
	private static Object lock	=	new Object();
	private Thread	messageSubscriberThread	=	null;
	
	public static MessageBrokerService getInstance(){
		synchronized (lock) {
			if(instance==null){
				instance	=	new MessageBrokerService();
			}
		}
		return instance;
	}

	
	public boolean stopMQTTService(){
		try{
		/** Start MQTT Subscriber	******************************************/
			if(logger.isInfoEnabled()){
				logger.info("Stopping MQTT Service...");	
			}
			
			MessageSubscriber	subscriber	=	MessageSubscriber.getInstance();
			if(subscriber.stop()){
				if(logger.isInfoEnabled()){
					logger.info("Stopped MQTT Service Successfully...");	
				}				
			} else {
				if(logger.isInfoEnabled()){
					logger.info("Stop Unsuccessful for MQTT Service...");	
				}				

			}
			messageSubscriberThread.interrupt();			
			MessagePublisher	pub	=	MessagePublisher.getMessagePublisher();
			pub.spot();
			
		} catch (Throwable th){
			th.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean startMQTTService(){
		try{
		/** Start MQTT Subscriber	******************************************/
			
			if(logger.isDebugEnabled()){
				logger.debug("Starting MQTT Listener Service...");				
			}
			

			MessageSubscriber	subscriber	=	MessageSubscriber.getInstance();
			MessageSubscriberRunnable run	=	new MessageSubscriberRunnable();
			run.init(subscriber);

			messageSubscriberThread	=	new Thread(run);
			messageSubscriberThread.start();
		} catch (Throwable th){
			th.printStackTrace();
			return false;
		}
		if(logger.isInfoEnabled()){
			logger.info("Started MQTT Listener Service...");				
		}

		return true;
		
	}
	
	public boolean stopUSBService(){
		try{
			if(Utility.isRXTXMode){
				if(SerialUSBPortUtility.getInstance().stop()){
					return true;
				} else {
					return false;
				}
			} else {
				if(SerialUSBPortUtilityJSSC.getInstance().stop()){
					return true;
				} else {
					return false;
				}
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		
		if(logger.isDebugEnabled()){
			logger.debug("Stop USB Reader Service Unsuccessfull...");				
		}

		return false;
	}
	
	public boolean startUSBService(){
		try{
    		boolean isWriter	=	true;
    		SerialUSBPortUtility	instance	=	SerialUSBPortUtility.getInstance();
    		
    		//instance.readFromUSB("11100000.0011");		
    		
		} catch (Throwable th){
			th.printStackTrace();
			return false;
		}
		
		if(logger.isInfoEnabled()){
			logger.info("Start USB Listener Service Successful...");				
		}
		
		return true;
	}

	public boolean startUSBServiceJSSC(){
		try{
    		boolean isWriter	=	true;
    		SerialUSBPortUtilityJSSC	instance	=	SerialUSBPortUtilityJSSC.getInstance();
    		
    		//instance.readFromUSB("11100000.0011");		
    		
		} catch (Throwable th){
			th.printStackTrace();
			return false;
		}
		
		if(logger.isInfoEnabled()){
			logger.info("Start USB Listener Service Successful...");				
		}
		
		return true;
	}

	public void testSrailWriter(String m){
		try{
    	SerialUSBPortUtility	instance	=	SerialUSBPortUtility.getInstance();
    	//receivedPayload	=	Utility.fomatToUSB(receivedPayload);
    	instance.sendToSerialPort(m);
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if(logger.isDebugEnabled()){
			logger.debug("Starting Message Service Broker");	
		}
		
		
		MessageBrokerService	ins	=	MessageBrokerService.getInstance();
		ins.startMQTTService();
		if(Utility.isRXTXMode){
			ins.startUSBService();
		} else {
			ins.startUSBServiceJSSC();
		}
		
		boolean	isRunning	=	false;
		
		
		
		while (isRunning){
			try{
		    BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
		    String s = bufferRead.readLine();
		    
		    if(s!=null && s.equals("s")){
		    	ins.stopMQTTService();
		    	ins.stopUSBService();
		    	isRunning	=	false;		    	
		    	//System.exit(0);
		    } else if(s!=null && s.equals("u")){
		    	ins.testSrailWriter(args[0]);		    	
		    }
		    
		    System.out.println(s);
			} catch (Exception e){
				e.printStackTrace();
			}
		}
		
		System.out.println("\n Exiting....");
		//-Djava.library.path=D:\Latest\java_rasp\lib
		
	}

}
//O11991100000000
//O1121110000000000
//O1121110000000000
//O1121110000000000