package com.demo.helper;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class Configuration {
	   Properties configFile;
	   
	   private String confFileName=System.getProperty("user.dir") + "/src/main/resources/configuration.properties";
	   public Configuration()
	   {
			configFile = new Properties();
			InputStream input = null;
			try {
				input = new FileInputStream(confFileName);
				configFile.load(input);
			}catch(Exception eta){
			    eta.printStackTrace();
			}
	   }
	 
	   public String getProperty(String key)
	   {
		   String value = configFile.getProperty(key);
		   if ( value == null ) {
			   value = "";
		   }
		   return value;
	   }
}
