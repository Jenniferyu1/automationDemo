package com.demo.helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


public class SysHelper {
	String timerStart;
	String timerStop;
	static Logger log = LogManager.getLogger(SysHelper.class);	
	
	public static String getTimestamp(){
		String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss.SS").format(new Date());
		return timestamp;
	}
	public static String getDate(){
		System.setProperty("user.timezone",  "PDT");
		Date d = Calendar.getInstance().getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.CANADA);
		return sdf.format(d);
	}
	public static String getTimestamp(String format){
		System.setProperty("user.timezone",  "PDT");
		Date d = Calendar.getInstance().getTime();
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(d);
	}
	public void startTimer(){
		timerStart = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
	}
	public void stopTimer(){
		timerStop = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
	}

	public static void printtimestamp(){
		String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
		log.info(timestamp);
	}
	
	/**
	 * Kills all running emulators
	 */
	public static void closeEmulator() {
		 
		 log.info("Killing Emulator...");
		 String[] aCommand = new String[] {  System.getProperty("user.home") + getAdbPath(), "emu", "kill" };
		 try {
		  Process process = new ProcessBuilder(aCommand).start();
		  process.waitFor(120, TimeUnit.SECONDS);
		  log.info("Emulator closed successfully!");
		 } catch (Exception e) {
		  e.printStackTrace();
		 }
	}
	/* kill all simulator */
	public static void closeSimulator() {
		log.info("Killing Simulator...");
		 ProcessBuilder builder = new ProcessBuilder("killall", "Simulator");
		 builder.redirectErrorStream(true);
		 try {
			final Process process = builder.start();
			 watch(process);
		} catch (IOException e2) {
			e2.printStackTrace();
		}
	}
	
	private static void watch(final Process process) {
	    new Thread() {
	        @Override
			public void run() {
	            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
	            String line = null; 
	            try {
	                while ((line = input.readLine()) != null) {
	                    System.out.println(line);
	                }
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	    }.start();
	}
	public static void sleep(int second) {
		try {
			TimeUnit.SECONDS.sleep(second);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public static boolean isWindows()
	{
		String OS = "";
		OS = System.getProperty("os.name");
		return OS.startsWith("Windows");
	}
	public static String pathCovertByOS(String pathStr) {
		String path=pathStr;
		if (isWindows()) {
			path=pathStr.replace("/",  "\\");
		}
		else
		{
			path=pathStr.replace("/",  File.separator);
		}
		return path;
	}
	
	private static String getAdbPath() {
		String adbPath  = "/Library/Android/sdk/platform-tools/adb" ; 
		if (isWindows()) {
			adbPath =  "\\AppData\\Local\\Android\\Sdk\\platform-tools\\adb";
		}
	    return adbPath ;
	}
	
    public static String GetIP() {
        InetAddress ip;
        String hostname;
        String ipStr = "";
        try {
            ip = InetAddress.getLocalHost();
            hostname = ip.getHostName();
            System.out.println("Your current IP address : " + ip);
            System.out.println("Your current Hostname : " + hostname);
            ipStr = ip.toString().replaceAll(hostname, "").replaceAll("/", "");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return ipStr;
    }

    public static String randomEmail(String email) {
		String newEmail = email.replace("@", GetIP() + getTimestamp() + "@").toLowerCase();
		return newEmail;
}
    
    public static double roundCryptoCurrency(double amount) {
    		double returnValue = (double)Math.round(amount*100000000)/100000000.00000000;
    		return returnValue;
    }
    public static double roundFiat(double amount) {
		double returnValue =  (double)Math.round(amount*100)/100.00;
		return returnValue;
}
	public static boolean timestampValidate(String first, String second, String format) {
		boolean returnValue = false;
		if ( first == second) {
			returnValue = true;
		}
		else {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			dateFormat.setLenient(true);
			Date firstDate;
			Date secondDate;
			try {
				firstDate = dateFormat.parse(first.replace(" PDT", "").replaceAll(" PST", ""));
				secondDate = dateFormat.parse(second.replace(" PDT", "").replaceAll(" PST", ""));
				if ((firstDate.getTime() - secondDate.getTime())/ 60d<30) {
					returnValue = true;
				}
			
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return returnValue;
	}
}
