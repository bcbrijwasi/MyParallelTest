package com.parallel.listner;


import com.relevantcodes.extentreports.ExtentReports;

public class ExtentManager {
	 
	public ExtentManager(){
	}
	
	private static ExtentReports extent;
	public static synchronized ExtentReports getInstance() {
        if (extent == null) {
            extent = new ExtentReports("C:/Users/Admin/workspace/ParallelTesting/test-output/Parallel_AutomationReport.html",true);
        }
        return extent;
    }

}
