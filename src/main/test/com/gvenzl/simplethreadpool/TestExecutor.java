package com.gvenzl.simplethreadpool;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestExecutor
{
	private Executor exec;

	/**
	 * Dummy class to run JUnit tests on shutdown/run
	 * @author gvenzl
	 *
	 */
	static public class TestClass implements Runnable {
		@Override
		public void run() {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) { }
		}
		
	}
	
	@Before
	public void setup() {
		exec = new Executor(Thread.class);
	}
	
	@Test
	public void test_instantiation() {
		new Executor(Thread.class);
		new Executor(1, Thread.class);
		new Executor(1, 1, Thread.class);
	}
	
	@Test
	public void test_setMaxPoolSize() {
		exec.setMaxPoolSize(10);
	}
	
	@Test
	public void test_getMaxPoolSize () {
		int maxSize = 10;
		exec.setMaxPoolSize(maxSize);
		Assert.assertTrue(maxSize == exec.getMaxPoolSize());
	}
	
	@Test
	public void test_getPoolSize() {
		int poolSize = 10;
		exec.setMaxPoolSize(poolSize);
		exec.setPoolSize(poolSize);
		Assert.assertTrue(poolSize == exec.getPoolSize());
	}
	
	@Test
	public void test_setPoolSize() {
		exec.setMaxPoolSize(1);
		exec.setPoolSize(1);
	}

	@Test
	public void test_start() throws Exception {
		exec.run();
	}


	@Test public void test_isRunningTrue() throws Exception {
		Executor execTemp = new Executor(TestClass.class);
		execTemp.run();
		Assert.assertTrue(execTemp.isRunning());
	}
	@Test
	public void test_isRunningFalse() {
		Assert.assertFalse(exec.isRunning());
	}
	
	@Test
	public void test_isTerminatedFalse() {
		Assert.assertFalse(exec.isTerminated());
	}
	
	@Test
	public void test_isTerminatedTrue() throws Exception {
		exec.submit(TestClass.class);
		exec.run();
		exec.shutdown();
		
		Assert.assertTrue(exec.isTerminated());
	}

	@Test
	public void test_shutdownNotRunning() {
		exec.shutdown();
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void negativeTest_setPoolSizeTooHigh() {
		exec.setMaxPoolSize(1);
		exec.setPoolSize(2);
	}
	
	@Test(expected=IllegalStateException.class)
	public void negativeTest_runNoSubmission() throws Exception {
		exec.submit(null);
		exec.run();
	}
	
	@Test(expected=InstantiationException.class)
	public void negativeTest_runNoDefaultConstructor() throws Exception {
		class Test implements Runnable {
			public void run() {
			}
		}
		
		exec.submit(Test.class);
		exec.run();
	}
	
	@Test
	public void test_runIncreasePoolSize() throws Exception {
		int initPoolSize = 1;
		int maxPoolSize = 10;
		
		exec.setMaxPoolSize(maxPoolSize);
		exec.setPoolSize(initPoolSize);
		exec.submit(TestClass.class);
		exec.run();
		Assert.assertTrue(exec.getPoolSize() == initPoolSize);
		exec.setPoolSize(maxPoolSize);
		Assert.assertTrue(exec.getPoolSize() == maxPoolSize);
	}

	@Test
	public void test_runDecreasePoolSize() throws Exception {
		int lowLevelPoolSize = 1;
		int maxPoolSize = 10;
		
		exec.setMaxPoolSize(maxPoolSize);
		exec.setPoolSize(maxPoolSize);
		exec.submit(TestClass.class);
		exec.run();
		Assert.assertTrue(exec.getPoolSize() == maxPoolSize);
		exec.setPoolSize(lowLevelPoolSize);
		Assert.assertTrue(exec.getPoolSize() == lowLevelPoolSize);
	}
}
