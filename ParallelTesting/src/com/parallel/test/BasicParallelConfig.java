package com.parallel.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.testng.ITestContext;
import org.testng.TestNG;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;

import com.parallel.runner.ParallelRunner;
import com.parallel.runner.WorkerThread;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;



public class BasicParallelConfig {
	//constants
	protected static Process appium;
	private static final String[] WIN_RUNTIME = { "cmd.exe", "/C" };
	private static final String[] OS_LINUX_RUNTIME = { "/bin/bash", "-l", "-c" };
	private static final String constantURL = "127.0.0.1:%s/wd/hub";
	private static final String appiumFolderPath = "C://Program Files (x86)//Appium//node_modules//appium//bin";
	private static final String appiumCommand = "cmd.exe /c cd \""+appiumFolderPath+"\" & start cmd.exe /k \"node appium.js -a 127.0.0.1 -p %s -cp %s -bp 2345\"";
	//public static LinkedHashSet<String> androidDevices;
	//public static List<String> globleDeviceList;
	
    
    public BasicParallelConfig(){
    	
    	//logger = Logger.getLogger("MyLog");  
    	//logger = Logger.getAnonymousLogger();
        
    }
	
	private static Integer getRandomOpenPortOnAllLocalInterfaces() throws IOException {
	    try (ServerSocket socket = new ServerSocket(0);) {
	      return socket.getLocalPort();
	    }
	}
	
	private static <T> T[] concat(T[] first, T[] second) {
		T[] result = Arrays.copyOf(first, first.length + second.length);
		System.arraycopy(second, 0, result, first.length, second.length);
		return result;
	}

	public static List<String> runProcess(boolean isWin, String... command) {
		System.out.print("command to run: ");
		for (String s : command) {
			System.out.print(s);
		}
		System.out.print("\n");
		String[] allCommand = null;
		try {
			if (isWin) {
				allCommand = concat(WIN_RUNTIME, command);
			} else {
				allCommand = concat(OS_LINUX_RUNTIME, command);
			}
			ProcessBuilder pb = new ProcessBuilder(allCommand);
			pb.redirectErrorStream(true);
			Process p = pb.start();
			p.waitFor();
			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String _temp = null;
			List<String> line = new ArrayList<String>();
			while ((_temp = in.readLine()) != null) {
				System.out.println("temp line: " + _temp);
				line.add(_temp);
			}
			System.out.println("result after command: " + line);
			return line;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<String> runAdbCommand(String adbCommand) {
		List<String> info;
		try {
			boolean isWin = isWindows();
			info = runProcess(isWin, adbCommand);
		} catch (Exception e) {
			throw e;
		}
		return info;
	}
	
	public static boolean isWindows() {
		boolean flag = false;
		RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();
		Map<String, String> systemProperties = runtimeBean.getSystemProperties();
		Set<String> keys = systemProperties.keySet();
		for (String key : keys) {
			systemProperties.get(key);
			// System.out.printf("[%s] = %s.\n", key, value);
		}
		if (systemProperties.containsValue("windows")) {
			flag = true;
		} else if (systemProperties.containsValue("Mac OS X")) {
			flag = false;
		}
		return flag;
	}
	
	public synchronized String openNewInstanceOfAppium() throws IOException, InterruptedException{
		String url="";
		int port = getRandomOpenPortOnAllLocalInterfaces();
		try{
			AppiumServiceBuilder builder = new AppiumServiceBuilder();
			builder.withIPAddress("127.0.0.1");
			builder.withArgument(GeneralServerFlag.LOG_LEVEL, "error");
			//builder.withArgument(GeneralServerFlag.SESSION_OVERRIDE);
			builder.usingPort(port);
			AppiumDriverLocalService service = AppiumDriverLocalService.buildService(builder);
			service.start();
			
			//appium = Runtime.getRuntime().exec(getAppiumStartCommand(port));
		    Thread.sleep(10000);
		}catch(RuntimeException e){
			
		}
		url = String.format(constantURL, port);
		System.out.println(url);
	    return url;
	}
	
	public String getAppiumStartCommand(int port){
		return String.format(appiumCommand, port, port);
	}
		
    public String getUDID(){
			String udid = "";
			synchronized (WorkerThread.androidDevices) {
			try{
		    List<String> lst = new ArrayList<>(WorkerThread.androidDevices);
		    udid = lst.get(0);
		    System.out.println("using device with device id: "+udid);
		    WorkerThread.androidDevices.remove(udid);
		    System.out.println("Remainig devices in list: "+WorkerThread.androidDevices);
			}catch(Exception e){	
			}
			}
			return udid;
			
		
	 }
    
    public List<String> getIdealdevices(){
    	List<String> idealDevices=new ArrayList<>();
    	for(String deviceId :WorkerThread. globleDeviceList){
    		if(isDeviceIdeal(deviceId)){
    			WorkerThread.androidDevices.add(deviceId);
    			idealDevices.add(deviceId);
    		}
    	}
     return idealDevices; 
    }
    	
    
    public boolean isDeviceIdeal(String deviceId){
    	String adbCommand = "adb -s "+deviceId+" shell ps com.appName";
    	List<String> info = new BasicParallelConfig(). runAdbCommand(adbCommand);
    	if(info.toString().trim().contains("com.appName")){
    		return true;
    	}
    	return false;
    }
  
	

}
