package com.parallel.runner;

import java.util.List;
import java.util.logging.Logger;

import org.testng.TestListenerAdapter;
import org.testng.TestNG;

import com.parallel.listner.ExtentManager;
import com.parallel.listner.ParallelReporter;
import com.relevantcodes.extentreports.ExtentReports;

public class TestRunnerThread extends Thread {
	public TestNG testng = new TestNG();
	public TestListenerAdapter adapter = new TestListenerAdapter();
	public List<String> runningSuites ;
	public Logger logger;
	TestRunnerThread(List<String> runningSuites){
		this.runningSuites = runningSuites;	
	}
	@Override
	public void run(){
//        ExtentReports reporter = ExtentManager.getInstance();
//	    testng.addListener(reporter);
		testng.addListener(adapter);
	    testng.setTestSuites(runningSuites);
	    testng.setOutputDirectory("C:\\Users\\Admin\\workspace\\ParallelTesting\\test-output");
	    testng.run();
	}
	    
}
