package com.parallel.testParallel1;

import java.util.logging.Logger;

import org.testng.annotations.Test;

import com.parallel.test.ParallelTesting;

public class Test2 extends ParallelTesting {
	


	
	@Test
	public void l(){
	
		logger.info("Method l");
	}
	
	@Test
	public void m() throws InterruptedException  {
		logger.info("method m is under execution");
		Thread.sleep(20000);
		logger.info("Method m");
	}


}
