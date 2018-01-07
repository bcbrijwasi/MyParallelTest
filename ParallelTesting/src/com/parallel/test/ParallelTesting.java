package com.parallel.test;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Configuration;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
public class ParallelTesting extends BasicParallelConfig{
	
private static final String deviceName = "ANDROID";
private static final String platformVersion = "5.1.1";
private static AndroidDriver driver;
private static DesiredCapabilities capabilities = new DesiredCapabilities();
private static String testName;
public Logger logger;    

//@BeforeSuite
/*public static void setUDID(){
	try {
		androidDevices = new LinkedHashSet<>();
		boolean isWin = isWindows();
		List<String> list= runProcess(isWin, "adb devices");
		list.remove(0);
		if(list.isEmpty()){
			System.out.println("No adb device found");
		}
		for(int i=0; i<list.size();i++){
			String[] deviceDetails = list.get(i).trim().split("\\s+");
			if(deviceDetails.length == 2){
				String deviceId = new String(deviceDetails[0]);
				androidDevices.add(deviceId);
			}
			
		}
		System.out.println("devices : "+androidDevices);
	}catch(Exception e){
		e.printStackTrace();
		
	}
}*/
//@Parameters({"testName"})
@BeforeTest
public void setLogFile(ITestContext context){
	try {  
        // This block configure the logger with handler and formatter 
		testName = context.getCurrentXmlTest().getParameter("testName");
		logger = Logger.getLogger(testName);
		testName +=".log" ;
		File fd=new File("E:/temp/test/"+testName);
		fd.createNewFile();
		logger = Logger.getLogger(testName);
		synchronized (logger) {
		FileHandler fh = new FileHandler("E:/temp/test/"+testName);  
        logger.addHandler(fh);
        SimpleFormatter formatter = new SimpleFormatter();  
        fh.setFormatter(formatter);  

        // the following statement is used to log any messages  
        logger.info("My first log");  
        logger.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<"+fd.exists());
		}

    } catch (SecurityException e) {  
        e.printStackTrace();  
    } catch (IOException e) {  
        e.printStackTrace();  
    }  

    logger.info("Hi How r u?");  
}


//@Parameters({ "deviceName_","UDID_","platformVersion_", "URL_"})
@BeforeTest
public synchronized void beforeMethod() throws InterruptedException, IOException {

	String URL = openNewInstanceOfAppium();
	Thread.sleep(20000);
	String UDID = getUDID();
	File classpathRoot = new File(System.getProperty("user.dir"));
	File appDir = new File(classpathRoot, "//app//");
	File app = new File(appDir, "app.apk");
	System.out.println(app);
	synchronized (capabilities) {
	//capabilities.setCapability(CapabilityType.BROWSER_NAME, "");
	capabilities.setCapability("deviceName", deviceName);
	capabilities.setCapability("udid", UDID);
	capabilities.setCapability("platformVersion", platformVersion);
	capabilities.setCapability("platformName", "Android");
	capabilities.setCapability("--session-override","true");
	capabilities.setCapability("app", app.getAbsolutePath());
	capabilities.setCapability("appPackage", "");
    capabilities.setCapability("appActivity", "");
	//capabilities.setCapability("hubPort", "4444");
	driver = new AndroidDriver(new URL("http://"+URL), capabilities);
	}
	driver.manage().timeouts().implicitlyWait(80, TimeUnit.SECONDS);
	Thread.sleep(10000);
}

@AfterMethod
public void afterMethod() {
	driver.quit();
}

@AfterTest
public void closeConsole(){
	
}

//@AfterSuite
//public void closeAppiumInstances(){
//    if (appium != null) {
//        appium.destroy();
//    }
//    System.out.println("Appium server stop");
//}
public void stopServer(){

CommandLine command = new CommandLine("cmd");
command.addArgument("/c");
command.addArgument("taskkill");
command.addArgument("/F");
command.addArgument("/IM");
command.addArgument("node.exe");

DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
DefaultExecutor executor = new DefaultExecutor();
executor.setExitValue(1);

try {
executor.execute(command, resultHandler);
} catch (IOException e) {
e.printStackTrace();
}


}

}