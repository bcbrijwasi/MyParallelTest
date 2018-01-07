package com.parallel.runner;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.Callable;
import com.parallel.test.BasicParallelConfig;
import com.parallel.test.ParallelTesting;

public class WorkerThread implements Runnable{
	public static LinkedHashSet<String> androidDevices = new LinkedHashSet<>();
	public static List<String> globleDeviceList = new ArrayList<>();
	@Override
	public void run(){
		while(!ParallelRunner.tempSuites.isEmpty()){
			try {
			Thread.sleep(100000);
			List<String> devices = getIdealdevices();
		    if(!devices.isEmpty()){
			
				Thread.sleep(30000);
			
			devices = getIdealdevices();
			System.out.println("???????????????"+devices);
			if(!devices.isEmpty()){
				ParallelRunner.flag = true;
			}
		    }
		    } catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}

		  
      public List<String> getIdealdevices(){
	    	List<String> idealDevices=new ArrayList<>();
	    	for(String deviceId : globleDeviceList){
	    		if(isDeviceIdeal(deviceId)){
	    			if(!androidDevices.contains(deviceId)){
	    			androidDevices.add(deviceId);
	    			}
	    			idealDevices.add(deviceId);
	    		}
	    	}
	     return idealDevices; 
	    }
	    	
	    
	    public boolean isDeviceIdeal(String deviceId){
	    	String adbCommand = "adb -s "+deviceId+" shell ps com.appName";
	    	List<String> info = new BasicParallelConfig(). runAdbCommand(adbCommand);
	    	if(info.toString().trim().contains("com.appName")){
	    		return false;
	    	}
	    	return true;
	    }
	  
	    public static void setUDID(){
	    	try {
	    		boolean isWin = BasicParallelConfig.isWindows();
	    		List<String> list= BasicParallelConfig.runProcess(isWin, "adb devices");
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
	    		globleDeviceList.addAll(androidDevices);
	    		System.out.println("devices : "+androidDevices);
	    	}catch(Exception e){
	    		e.printStackTrace();
	    		
	    	}
	    }
		
		
	

}
