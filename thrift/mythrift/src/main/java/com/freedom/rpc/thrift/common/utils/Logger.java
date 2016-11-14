package com.freedom.rpc.thrift.common.utils;

/**
 * 
 * @author zhiqiang.liu
 * @2016年1月1日
 *
 */
public class Logger {// log4j
	private org.apache.log4j.Logger logger;

	private Logger(@SuppressWarnings("rawtypes") Class clazz) {
		logger = org.apache.log4j.LogManager.getLogger(clazz);
	}

	public void debug(String msg) {
		if (logger.isDebugEnabled()) {
			logger.debug(msg);
		}
	}

	public void info(String msg) {
		if (logger.isInfoEnabled()) {
			logger.info(msg);
		}
	}

	public void error(String msg) {
		// 错误直接打
		logger.error(msg);
	}

	public static Logger getLogger(@SuppressWarnings("rawtypes") Class clazz) {
		return new Logger(clazz);
	}
}
