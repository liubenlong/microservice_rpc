package com.freedom.rpc.thrift.common.extended.server;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.TProcessor;

import com.freedom.rpc.thrift.common.annotation.Model;
import com.freedom.rpc.thrift.common.utils.Logger;

public class ServerSideExtendedTMultiplexedProcessor extends TMultiplexedProcessor {
	private static final Logger logger = Logger.getLogger(ServerSideExtendedTMultiplexedProcessor.class);
	public static final Map<Model, TProcessor> SERVICE_PROCESSOR_MAP = new HashMap<Model, TProcessor>();

	@Override
	public void registerProcessor(String value, TProcessor processor) {
		// 本地保留一份,再抛给真正的注册器
		logger.info("register :" + value + " " + processor);
		String[] array = value.split(":");
		String group = array[0];
		String service = array[1];
		String edition = array[2];
		Model model = new Model(group, service, edition);
		super.registerProcessor(service, processor);
		synchronized (ServerSideExtendedTMultiplexedProcessor.class) {
			SERVICE_PROCESSOR_MAP.put(model, processor);
		}
		logger.debug("用户注册了一个服务:" + service + " ---> " + processor);
	}

	public static synchronized Set<Model> getService() {
		// 跟上面的锁呼应,线程同步，不会有性能问题
		return SERVICE_PROCESSOR_MAP.keySet();
	}
}
