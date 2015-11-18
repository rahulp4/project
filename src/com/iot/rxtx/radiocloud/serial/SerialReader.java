package com.iot.rxtx.radiocloud.serial;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iot.rxtx.radiocloud.mqtt.MessagePublisher;
import com.iot.rxtx.radiocloud.mqtt.MessageSubscriber;
import com.iot.rxtx.radiocloud.mqtt.common.Utility;

public class SerialReader implements Runnable {
	 
	final static Logger logger = LoggerFactory.getLogger(SerialReader.class);
	
	private InputStream in;
    private OutputStream out;
    private boolean isRunning	=	true;
    private boolean isWindows	=	true;
    
    public boolean stop(){
    	isRunning	=	false;
    	
    	return true;
    }
    
    public SerialReader( InputStream in ,OutputStream out) {
      this.in = in;
	  this.out	=	out;
    }
 
    public void run() {
    	if(isWindows){
			  String m	=	slurp(in,10);
			  //String m	=	"RESPONSE";
			  System.out.println("\n Systme Message is : "+m);

    		//runWindows();
    	} else {
    		//runTwoWay();
    		runUnix();
    	}
    }
    
    public void runTwoWay() {
    	int bufferSize = 1;
    	 byte[] buffer = new byte[ bufferSize ];
         int len = -1;
         StringBuffer receivedData	=	new StringBuffer();
         boolean start	=	false;
			if(logger.isDebugEnabled()){
         logger.debug("\n runTwoWay");
			}
         try {
           while( ( len = this.in.read( buffer ) ) > -1 ) {
             // TODO: Implement
             /* TODO: Outputs everything that is printed to terminal
                      i.e if you write "HEJ" and press ENTER output will be:
                       [OUTPUT]='H'
                       [OUTPUT]='EJ
                       '
             */
        	   char recChar	=	(char)buffer[0];
        	   if(recChar=='#'){
        		   
        		   if(start==false){
        			   //Starting to read now.
        			   if(receivedData==null){
        				   receivedData	= new StringBuffer();   
        			   }        			   
        			   receivedData.append(recChar);
        			   start	=	true;
        		   } else {
        			   receivedData.append(recChar);
        			   //Start :Processing
        				if(logger.isDebugEnabled()){
        			   logger.debug("[OUTPUT]='" + new String( receivedData ) + "'");
        				}
        			   Utility.processInboundMessage(new String( receivedData ));
        			   start	=	false;
        			   receivedData	=	null;
        		   }
        	   
        	   } else {
        		   if(start==true){
        			   receivedData.append(recChar);   
        		   }
    			   
    		   }

        		   
             //System.out.println("[OUTPUT]='" + new String( buffer, 0, len ) + "'");
           }
         } catch( IOException e ) {
           e.printStackTrace();
         }
       }      
    
    public void runUnix() {
      byte[] buffer = new byte[ 1024 ];
      int len = -1;
      try {
    	  	String dataReceived	=	null;//slurp(in,15);
    	  	BufferedReader inStream = new BufferedReader(new InputStreamReader(in), 15);
    	  	while((dataReceived= inStream.readLine()) != null){
    			if(logger.isDebugEnabled()){
    	  		logger.debug("\n---New Data---");
	    	  	logger.debug(dataReceived);
    			}
	    	  	//processMessage(dataReceived);
	    	  	Utility.processInboundMessage(dataReceived);
				if(logger.isDebugEnabled()){
	    	  	logger.debug("\n---END Data---");
				}
    	  	}    	  
    	  
//        while( ( len = this.in.read( buffer ) ) > -1 ) {
//        	String dataReceived	=	new String( buffer, 0, len ) ;
 //       	processMessage(dataReceived);
        	
//        	if(logger.isDebugEnabled()){
//        		logger.debug("Data received on the Serial port is "+dataReceived);
//        	}
//			MessagePublisher	pub	=	MessagePublisher.getMessagePublisher();
//			pub.publishMessaeg(Utility.TestFomatMQTTMessage(true),"/RAHUL");
        	
//		}
      
      } catch( IOException e ) {
        e.printStackTrace();
      }
    }

    public void runWindows() {
      try {
    	  while(true && isRunning){
    		  while(in.available()	>	0){
    			  String m	=	slurp(in,10);
    			  //String m	=	"RESPONSE";
    			  System.out.println("\n Systme Message is : "+m);
    			  if(m!=null && !m.equals("")){
    		        if(logger.isDebugEnabled()){
    		        		logger.debug("Data received on the Serial port is "+m);
    		        }
        			MessagePublisher	pub	=	MessagePublisher.getMessagePublisher();
        			pub.publishMessaeg(Utility.TestFomatMQTTMessage(true),"/001");
    			  }
    		  }
    	  }
    	  
    	  if(!isRunning){
    		  in.close();
    		  out.close();
		      if(logger.isInfoEnabled()){
	        		logger.info("Serial Port Reader Stopped and stream closed");
		      }    		  
    	  }
      } catch( Throwable e ) {
        e.printStackTrace();
      }
    }
  
    public static String slurp(final InputStream is, final int bufferSize)
    {
      final char[] buffer = new char[bufferSize];
      final StringBuilder out = new StringBuilder();
      try {
        final Reader in = new InputStreamReader(is, "UTF-8");
        try {
          for (;;) {
            int rsz = in.read(buffer, 0, buffer.length);
            if (rsz < 0)
              break;
            out.append(buffer, 0, rsz);
          }
        }
        finally {
          in.close();
        }
      }
      catch (UnsupportedEncodingException ex) {
    	  logger.error("WARNING : UnsupportedEncodingException");
    	  //ex.printStackTrace();
    	  /* ... */
      }
      catch (IOException ex) 
      {
    	  ex.printStackTrace();
    	  logger.error("WARNING : IOException");
    	  //ex.printStackTrace();
    	  /* ... */
      }
      return out.toString();
    }


public String read(InputStream in){
	StringBuilder sb=new StringBuilder();
	try{
	//InputStream in = /* your InputStream */;
	InputStreamReader is = new InputStreamReader(in);
	
	BufferedReader br = new BufferedReader(is);
	String read = br.readLine();

	while(read != null) {
	    //System.out.println(read);
	    sb.append(read);
	    read =br.readLine();

	}
	} catch (Exception e){
		e.printStackTrace();
	}
	return sb.toString();	
}

private void processMessage(String receivedFromNode){
	
	String nodeId	=	null;//receivedFromNode.substring(1, 4);
	//#0000LG1001000#
	if(receivedFromNode!=null && (receivedFromNode.startsWith("#") && receivedFromNode.endsWith("#")) ){
		if(receivedFromNode.length()==15){
		//if(receivedFromNode.length()==13){
			nodeId	=	receivedFromNode.substring(2, 5);
			//nodeId	=	receivedFromNode.substring(1, 4);
        	if(logger.isDebugEnabled()){
        		logger.debug("Data received on the Serial port is "+receivedFromNode+" for Node Id : "+nodeId);
        	}        	
			MessagePublisher	pub	=	MessagePublisher.getMessagePublisher();
			if(nodeId.equals("000")){
				nodeId	=	"001";
			}
			pub.publishMessaeg(Utility.fomatToMQTT(receivedFromNode),"/"+nodeId);			
		}		
	}		
}

}

