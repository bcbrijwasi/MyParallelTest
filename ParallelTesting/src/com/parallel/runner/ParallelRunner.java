/**
 * 
 */
package com.parallel.runner;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import org.testng.TestNG;
import org.testng.xml.XmlSuite;

import com.google.common.base.Joiner;
import com.google.common.base.Throwables;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.parallel.test.BasicParallelConfig;
import com.parallel.test.ParallelTesting;


/**
 * @author Bhuwan
 *
 */
public class ParallelRunner {


	/**
	 * @param args
	 */
	public static List<String>tempSuites = new ArrayList<>();
    public static boolean flag = false;
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws FileNotFoundException, IOException, InterruptedException {
		        TestRunnerThread th;
				WorkerThread obj = new WorkerThread();
				WorkerThread.setUDID();
		        TestListenerAdapter adapter = new TestListenerAdapter();
		        List<String> suites = new ArrayList<String>(); 
		        try (BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Admin\\workspace\\ParallelTesting\\suiteFile"))) {
		            String line;
		            while ((line = br.readLine()) != null) {
		            	suites.add(line);
		            }
		        }
		        System.out.println(suites);
		        tempSuites.addAll(suites);
		        System.out.println("??????????????????"+tempSuites);
		        System.out.println(tempSuites.size());
		        List<String> devices =obj.getIdealdevices();
		        System.out.println("number of ideal devices >>>>>>>>>>>>>>" + devices.size());
		        List<String> runningSuite = new ArrayList<>();
		        if(!devices.isEmpty()){
		        	for(int i = 0 ; i<devices.size();i++){
		        		runningSuite.add(tempSuites.get(i));
		        	}
		        	for(int i = 0 ; i<devices.size();i++){
		        		tempSuites.remove(0);
		        	}
		        	
		        	System.out.println("<<<<<<<<<<<<<<<<<"+runningSuite);
		        }
		        th = new TestRunnerThread(runningSuite);
		        th.start();
		        Thread worker = new Thread(obj);
		        worker.start();
		        while(true){
		        	Thread.sleep(30000);
		        	System.out.println("Checking....");
		        	if(flag){
		        		runningSuite.clear();
		        		devices =obj.getIdealdevices();
		        		if(!devices.isEmpty()){
				        	for(int i = 0 ; i<devices.size();i++){
				        		runningSuite.add(tempSuites.get(i));
				        	}
				        	for(int i = 0 ; i<devices.size();i++){
				        		tempSuites.remove(0);
				        	}
				        }
		        		System.out.println("<<<<<<<<<<<<<<<<<"+runningSuite);
		        		th = new TestRunnerThread(runningSuite);
				        th.start();
		        	}	
		        	if(tempSuites.isEmpty()){
		        		System.out.println("Execution for all the suites is completed");
		        		break;
		        	}
		        	
		        }
		       
		        List<ITestResult> failedTests = Lists.newArrayList();
		        failedTests.addAll(adapter.getFailedTests());
		        failedTests.addAll(adapter.getConfigurationFailures());
		        if (!failedTests.isEmpty()) {
		            String header = String.format("Combined Messages (Total:%d)", failedTests.size());

		            List<String> errorMessages = Lists.newArrayList();
		            errorMessages.add(header);
		            errorMessages.addAll(Lists.transform(failedTests, new Function<ITestResult, String>() {
		                int i = 1;
		                @Override
		                public String apply(ITestResult testResult) {
		                    String stackTraceString = Throwables.getStackTraceAsString(testResult.getThrowable());
		                    String template = "Message-%d: %n %s";
		                    return String.format(template, i++, stackTraceString);
		                }
		            }));
		            // transform messages to a single combined string
		            String message = Joiner.on(LINE_SEPARATOR).join(errorMessages);
		            throw new AssertionError(message);
		        }
		    }


	}


