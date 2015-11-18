package com.iot.rxtx.radiocloud.serial;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;

import com.iot.rxtx.radiocloud.mqtt.common.Utility;

public class SerialReaderJSSC implements SerialPortEventListener {

	final static Logger logger = LoggerFactory.getLogger(SerialReaderJSSC.class);
	
	public static final String NEW_LINE = System.getProperty("line.separator");
			
	private SerialPort serialPort	=	null;
	
	public SerialPort getSerialPort() {
		return serialPort;
	}

	public void setSerialPort(SerialPort serialPort) {
		this.serialPort = serialPort;
	}

	public SerialReaderJSSC(){

	}
	
	
    final static int SPACE_ASCII = 32;
    final static int DASH_ASCII = 45;
    final static int NEW_LINE_ASCII = 10;

    int bufferSize = 1;
	byte[] buffer = new byte[ bufferSize ];
    int len = -1;
    StringBuffer receivedData	=	new StringBuffer();
    boolean start	=	false;
    
	 public void serialEvent(SerialPortEvent event) {
		 
         if(event.isRXCHAR()){//If data is available
                 try {
                	 
                   buffer = serialPort.readBytes(1);
                     
              	   char recChar	=	(char)buffer[0];
            	   if(recChar=='#'){
            		   
            		   if(start==false){
            			   //Starting to read now.
            			   //if(receivedData==null){
            			   receivedData	= new StringBuffer();   
            			   //}        			   
            			   receivedData.append(recChar);
            			   start	=	true;
            		   } else {
            			   receivedData.append(recChar);
            			   //Start :Processing
            			   //if(logger.isInfoEnabled()){
            				//   logger.info("[OUTPUT]='" + new String( receivedData ) + "'");
            			   //}
            			   System.out.println("[OUTPUT]='" + new String( receivedData ) + "'");
            			   
            			   Utility.processInboundMessage(new String( receivedData ));
            			   
            			   start	=	false;
            			   receivedData	=	null;
            			   //serialPort.purgePort(SerialPort.PURGE_RXCLEAR | SerialPort.PURGE_TXCLEAR);
            		   }
            	   
            	   } else if(recChar!='*'){
            		   if(start==true){
            			   receivedData.append(recChar);   
            		   }   
        		   }
                     
                 } catch (Exception ex) {
                     System.out.println(ex);
                     if(ex!=null){
                    	 logger.error(ex.toString());	 
                     }
                     
                 }
             //}
         }
         else if(event.isCTS()){//If CTS line has changed state
             if(event.getEventValue() == 1){//If line is ON
                 System.out.println("CTS - ON");
             }
             else {
                 System.out.println("CTS - OFF");
             }
         }
         else if(event.isDSR()){///If DSR line has changed state
             if(event.getEventValue() == 1){//If line is ON
                 System.out.println("DSR - ON");
             }
             else {
                 System.out.println("DSR - OFF");
             }
         }
     }
}
