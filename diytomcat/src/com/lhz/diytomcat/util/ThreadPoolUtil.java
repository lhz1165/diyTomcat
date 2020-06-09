package com.lhz.diytomcat.util;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author lhzlhz
 * @create 2020/6/7
 */
public class ThreadPoolUtil {
	private static final ThreadPoolExecutor threadPool = new ThreadPoolExecutor(20, 100, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>(10));
	public static void run(Runnable runnable) {
		threadPool.execute(runnable);
	}
}
