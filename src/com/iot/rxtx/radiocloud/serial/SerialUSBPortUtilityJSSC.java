package com.iot.rxtx.radiocloud.serial;

import jssc.SerialPort;
import jssc.SerialPortException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iot.rxtx.radiocloud.mqtt.common.Utility;

public class SerialUSBPortUtilityJSSC {
	
	final static Logger logger = LoggerFactory.getLogger(SerialUSBPortUtilityJSSC.class);
	private Thread	serialReaderThread	=	null;	
	private SerialPort serialPort =	null;
	private static boolean isWindows	=	false;
	private static final Object lock = new Object(); // Lock Object	
	private static SerialUSBPortUtilityJSSC	instance	=	null;	
	
	
	public static SerialUSBPortUtilityJSSC	getInstance() throws Exception {		
		if(instance==null){
			synchronized (lock) {
				instance	=	new SerialUSBPortUtilityJSSC();
				//instance.connect("/dev/ttyACM0" ,true);
				if(isWindows){
					instance.connect("COM15" ,true);
				} else {
					instance.connect("/dev/ttyACM0" ,true);

//instance.connect("/dev/ttyAMA0" ,true);
				}
					
				if(logger.isInfoEnabled()){
					logger.info("Serial port connected to port ");
				}
			}
		}
		
		return instance;
	}
		
	public boolean stop(){
		try{
			serialPort.closePort();
			System.out.println("\n USB port closed at "+Utility.getDateTime());
			return true;
		} catch (Exception e){
			logger.equals("Error Closing USB port");
			return false;
		}
	}
	
	private void connect( String portName ,boolean isWriter) throws Exception {
		
		serialPort = new SerialPort(portName);
        try {
        	SerialReaderJSSC	serialReaderJSSC	=	new SerialReaderJSSC();
        	serialReaderJSSC.setSerialPort(serialPort);
        	
        	serialPort.openPort();//Open port
            serialPort.setParams(9600, 8, 1, 0);//Set params
            int mask = SerialPort.MASK_RXCHAR + SerialPort.MASK_CTS + SerialPort.MASK_DSR;//Prepare mask
            serialPort.setEventsMask(mask);//Set mask
            serialPort.addEventListener(serialReaderJSSC);//Add SerialPortEventListener
        
        } catch (SerialPortException ex) {
            System.out.println(ex);
        }	
	
	}
		 
	public static void main( String[] args ) {
		try {
			//http://mfizz.com/oss/rxtx-for-java
			//( new TwoWaySerialComm() ).connect( "/dev/ttyUSB0" );
		    System.out.println("\n WRITING 1");
		    boolean isWriter	=	true;
		    SerialUSBPortUtilityJSSC	instance	=	SerialUSBPortUtilityJSSC.getInstance();
		    //instance.readFromUSB("");
		    Thread.sleep(2000);
		    while(true){
		    	
		    }

		    //-Djava.library.path=D:\Latest\java_rasp\lib
		} catch( Exception e ) {
		    	e.printStackTrace();
		}
		
		if(logger.isDebugEnabled()){
			logger.debug("\n DONE");			
		}

	}
		  
	public boolean sendToSerialPort(String message){
		try{
			 serialPort.writeBytes(message.getBytes());

		} catch (Throwable th){
			th.printStackTrace();
			return false;
		}
		if(logger.isDebugEnabled()){
			logger.debug("Send data over serial port. Message sent is "+message);
		}
		return true;
	}

		  
	public boolean readFromUSB(String message){
		try{
//			serialReader	=	new SerialReader(in, out ) ;
//			serialReaderThread	=	new Thread( serialReader );
//			serialReaderThread.start();
		} catch (Throwable th){
			th.printStackTrace();
			return false;  
		}
//		if(logger.isInfoEnabled()){
//			logger.info("Started Reading from Serial reader");
//		}
		return true;
	}
}
