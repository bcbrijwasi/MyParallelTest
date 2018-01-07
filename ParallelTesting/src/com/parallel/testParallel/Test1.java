package com.parallel.testParallel;

import java.util.logging.Logger;

import org.testng.annotations.Test;

import com.parallel.test.ParallelTesting;

public class Test1 extends ParallelTesting {
	

	
	@Test
	public void f() throws InterruptedException {
		logger.info("method f is under execution");
		logger.info("Waiting....");
		Thread.sleep(30000);
		logger.info("Method f");
	}

	@Test
	public void g() {
		logger.info("Method g");
	}

}
