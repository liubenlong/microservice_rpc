package com.freedom.rpc.thrift.common.utils;

public class TimeUtils {
	@SuppressWarnings("static-access")
	public static void sleep(int ms) {
		try {
			Thread.currentThread().sleep(ms);
		} catch (Exception e) {

		}
	}
}
