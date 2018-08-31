package com.demo.helper;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.remote.DesiredCapabilities;


import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;

public class appiumDriver {

	static Logger log = LogManager.getLogger(appiumDriver.class);	
	static AppiumDriver<?> driver;
	public static Configuration conf= new Configuration();
	public static boolean closeDevice;
	AppiumDriverLocalService appiumDriverLocalService;
	public AppiumDriver<?> getDriver() {
		return driver;
	}

    public void startDriver( String deviceStr)
    {
    		if (driver!=null) {
    			return;
    		}
    		log.info("Starting " + "ios" + " Driver");
    		File appDir = new File(System.getProperty("user.dir"), conf.getProperty("appPath"));
    	    DesiredCapabilities capabilities = new DesiredCapabilities();
        if (conf.getProperty("app_" + deviceStr).equals("") == false)
        {
    			File app = new File(appDir, conf.getProperty("app_" + deviceStr));
    			capabilities.setCapability("app", app.getAbsolutePath());
        }
		capabilities.setCapability("deviceName", conf.getProperty("deviceName_" + deviceStr));
		capabilities.setCapability("udid", conf.getProperty("udid_" + deviceStr));
		capabilities.setCapability("platformVersion", conf.getProperty("platformVersion_" + deviceStr));
		capabilities.setCapability("platformName", "ios");
		capabilities.setCapability("automationName", conf.getProperty("automationName_" + deviceStr));
		capabilities.setCapability("newCommandTimeout", 10000);

		closeDevice=conf.getProperty("closeDevice").equals("true");
		if (closeDevice) {
			SysHelper.closeSimulator();
		}
	
	    try {
    			capabilities.setCapability("clearSystemFiles", true);	
    			capabilities.setCapability("wdaStartupRetryInterval", 20000);
    			capabilities.setCapability("useNewWDA", true);
    			capabilities.setCapability("waitForQuiescence", false);
    			capabilities.setCapability("shouldUseSingletonTestManager", false);
			driver = new IOSDriver<MobileElement>(new URL(conf.getProperty("url_" + deviceStr)), capabilities);
	    } catch (IOException e) {
			e.printStackTrace();
		}
       
	    log.info("Started ios Driver");
    }
	public void stopDriver() {
		
		log.info("Stopping Driver");
		if (driver != null) {
			driver.closeApp();
			driver.quit();
			driver = null;
		}
		log.info("Stopped Driver");
	}
	public String takeScreenshot( String reportPath , String methodName)
	{
		log.debug("takeScreenshot");
		File fileScreenShot = getDriver().getScreenshotAs(OutputType.FILE);
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd-HH_mm_ss a").format(new Date());
		String ssName = reportPath + methodName  + "_" + timeStamp + ".jpg";
		try {
			FileUtils.copyFile(fileScreenShot, new File(ssName));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ssName;
	}
}
