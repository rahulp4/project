package com.iot.rxtx.radiocloud.serial;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.InputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SerialUSBPortUtility {
	
	final static Logger logger = LoggerFactory.getLogger(SerialUSBPortUtility.class);
	
	private SerialReader	serialReader	=	null;
	private Thread	serialReaderThread	=	null;
	
	private SerialPort serialPort = null;
	private OutputStream out = null;
	private InputStream in = null;
	
	
	private static final Object lock = new Object(); // Lock Object
	
	private static SerialUSBPortUtility	instance	=	null;	
	public static SerialUSBPortUtility	getInstance() throws Exception {
		
		if(instance==null){
			synchronized (lock) {
				instance	=	new SerialUSBPortUtility();
				//instance.connect("/dev/ttyACM0" ,true);
				instance.connect("COM15" ,true);
				//instance.connect("/dev/ttyUSB0" ,true);
				if(logger.isInfoEnabled()){
					logger.info("Serial port connected to port ");
				}
			}
		}
		
		return instance;
	}
		
	public boolean stop(){
		try{
			if(serialReader!=null){
				serialReader.stop();
			} else {
				serialPort.removeEventListener();
				serialPort.close();
			}
			//serialReaderThread.stop();
			//serialReaderThread.interrupt();
			
		} catch (Exception e){
			e.printStackTrace();
			return false;
		}
		
		if(logger.isInfoEnabled()){
			logger.info("Stop successfull for Serial port");
		}
		return true;
	}

	
	private void connect( String portName ,boolean isWriter) throws Exception {
		CommPortIdentifier portIdentifier = CommPortIdentifier
		        .getPortIdentifier( portName );
		if( portIdentifier.isCurrentlyOwned() ) {
			logger.error("Port "+portName + " is currently in use.");
		} else {
			int timeout = 2000;
		    CommPort commPort = portIdentifier.open( this.getClass().getName(), timeout );
		 
		    if( commPort instanceof SerialPort ) {
		        serialPort = ( SerialPort )commPort;
		        serialPort.setSerialPortParams( 9600,
		                                        SerialPort.DATABITS_8,
		                                        SerialPort.STOPBITS_1,
		                                        SerialPort.PARITY_NONE );
		        
				serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN
	                    | SerialPort.FLOWCONTROL_RTSCTS_OUT);
		        
		        out = serialPort.getOutputStream();
			    in = serialPort.getInputStream();
		        SerialTest serialTest	=	new SerialTest();
		        
		        serialTest.setInputStream(in);
		        
			    serialPort.addEventListener(serialTest);
				serialPort.notifyOnDataAvailable(true);	
				System.out.print("\n STARTED TO READ");
		    } else {
		        logger.error( "Error: Only serial ports are in application." );
		    }
		}
	}
		 
	public static void main( String[] args ) {
		try {
			//http://mfizz.com/oss/rxtx-for-java
			//( new TwoWaySerialComm() ).connect( "/dev/ttyUSB0" );
		    System.out.println("\n WRITING 1");
		    boolean isWriter	=	true;
		    SerialUSBPortUtility	instance	=	SerialUSBPortUtility.getInstance();
		    //instance.readFromUSB("");
		    Thread.sleep(2000);
		    if(logger.isDebugEnabled()){
		    	logger.debug("connecting");
		    }

		    while(true){
		    	
		    }
/*
		    //instance.connect("COM14",isWriter);//;/dev/ttyACM0"
		    //( new SerialUSBPortUtility() ).connect( "/dev/ttyACM0" ,isWriter);
		    instance.sendToSerialPort("1");
		    for(int i=0;i<=5;i++){
		    	instance.sendToSerialPort("1");
		    }
		    	
		    instance.readFromUSB("");
		    */
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
			//( new Thread( new SerialWriter( out ) ) ).start();				  
			new SerialWriter( out,message ).run();
			
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
			serialReader	=	new SerialReader(in, out ) ;
			serialReaderThread	=	new Thread( serialReader );
			serialReaderThread.start();
		} catch (Throwable th){
			th.printStackTrace();
			return false;  
		}
		if(logger.isInfoEnabled()){
			logger.info("Started Reading from Serial reader");
		}
		return true;
	}
}
