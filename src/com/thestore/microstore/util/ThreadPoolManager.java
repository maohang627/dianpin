package com.thestore.microstore.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 *	线程池管理 
 *
 * 2014-9-5 下午2:13:07
 */
public class ThreadPoolManager {
	
	private ExecutorService service;

	private ThreadPoolManager() {
		int num = Runtime.getRuntime().availableProcessors();
		service = Executors.newFixedThreadPool(num * 4);
	}

	private static final ThreadPoolManager manager = new ThreadPoolManager();

	public static ThreadPoolManager getInstance() {
		return manager;
	}

	public void addTask(Runnable runnable) {
		service.execute(runnable);
	}
}
