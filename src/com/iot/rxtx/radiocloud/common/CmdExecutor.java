package com.iot.rxtx.radiocloud.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
public class CmdExecutor {

    static class StreamReader extends Thread {
    	InputStream is;

    	String type;

    	StreamReader(InputStream is, String type) {
    		this.is = is;
    		this.type = type;
    	}

    	public void run() {
    		try {
    			InputStreamReader isr = new InputStreamReader(is);
    			BufferedReader br = new BufferedReader(isr);
    			String line = null;
    			while ((line = br.readLine()) != null)
    				System.out.println(type + ">" + line);
    		} catch (IOException ioe) {
    			ioe.printStackTrace();
    		}
    	}
    }

    public boolean executeCmd(String[] cmdArgs){
    	
    	return true;
    }
    
    public static void main(String[] args) {
    	if (args.length < 1) {
    		System.out.println("USAGE: java ShellScriptExecutor script");
    		System.exit(1);
    	}

    	try {
    		String osName = System.getProperty("os.name");
    		String[] cmd = new String[2];
    		cmd[0] = "/bin/sh"; // should exist on all POSIX systems
    		cmd[1] = args[0];

    		Runtime rt = Runtime.getRuntime();
    		System.out.println("Execing " + cmd[0] + " " + cmd[1] );
    		Process proc = rt.exec(cmd);
    		// any error message?
    		StreamReader errorGobbler = new StreamReader(proc
    				.getErrorStream(), "ERROR");

    		// any output?
    		StreamReader outputGobbler = new StreamReader(proc
    				.getInputStream(), "OUTPUT");

    		// kick them off
    		errorGobbler.start();
    		outputGobbler.start();

    		// any error???
    		int exitVal = proc.waitFor();
    		System.out.println("ExitValue: " + exitVal);
    	} catch (Throwable t) {
    		t.printStackTrace();
    	}
    }
}