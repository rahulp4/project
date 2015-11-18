package com.iot.rxtx.radiocloud.serial;

import java.io.IOException;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iot.rxtx.radiocloud.mqtt.common.Utility;

public class SerialWriter implements Runnable {

	final static Logger logger = LoggerFactory.getLogger(SerialWriter.class);
	
    private OutputStream out;
    private byte[] messageBytes	=	null;
    
    public SerialWriter( OutputStream out ,String message) {
      this.out = out;
      if(Utility.is_RF_Serial){
    	  message	=	Utility.SERIAL_MSG_PREFIX+message+Utility.SERIAL_MSG_SUFFIX;
      }
      messageBytes	=	message.getBytes();
      
    }
 
    public void run() {
      try {

        if(logger.isDebugEnabled()){
        	logger.debug("Sending message over serial port as "+new String(messageBytes));
        }

        this.out.write( messageBytes );          
        if(logger.isDebugEnabled()){
        	logger.debug("Message written successfully to Serial port");
        }

      } catch( IOException e ) {
        e.printStackTrace();
      }
    }
  }
