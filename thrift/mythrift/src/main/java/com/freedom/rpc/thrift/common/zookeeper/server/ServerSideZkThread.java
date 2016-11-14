package com.freedom.rpc.thrift.common.zookeeper.server;

import java.util.Set;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

import com.freedom.rpc.thrift.common.annotation.Model;
import com.freedom.rpc.thrift.common.extended.server.ServerSideExtendedTMultiplexedProcessor;
import com.freedom.rpc.thrift.common.property.ServerProperties;
import com.freedom.rpc.thrift.common.utils.Logger;



public class ServerSideZkThread implements Runnable {

	private static final Logger logger = Logger.getLogger(ServerSideZkThread.class);
	private static CuratorFramework client = null;
	private static ServerSideZkConnectionStateListener connectionStateListener = null;

	@SuppressWarnings("static-access")
	@Override
	public void run() {
		// http://macrochen.iteye.com/blog/1366136/
		// http://ifeve.com/zookeeper-curato-framework/
		// http://www.codelast.com/%E5%8E%9F%E5%88%9B-zookeeper%E6%B3%A8%E5%86%8C%E8%8A%82%E7%82%B9%E7%9A%84%E6%8E%89%E7%BA%BF%E8%87%AA%E5%8A%A8%E9%87%8D%E6%96%B0%E6%B3%A8%E5%86%8C%E5%8F%8A%E6%B5%8B%E8%AF%95%E6%96%B9%E6%B3%95/
		try {
			String threadName = Thread.currentThread().getName();
			logger.info("current running thread: {" + threadName + "}");
			Set<Model> serviceSet = ServerSideExtendedTMultiplexedProcessor.getService();
			logger.info("service set to be registered: " + serviceSet);
			ServerProperties properties = ServerProperties.getInstance();
			//
			//
			//
			// 选择ZkClient还是Curator?/这里选择Curator
			{
				RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, Integer.MAX_VALUE);
				client = CuratorFrameworkFactory.builder()//
						.connectString(properties.getZkClusterAddress())//
						.sessionTimeoutMs(properties.getSessionTimeoutMs())//
						.connectionTimeoutMs(properties.getConnectionTimeoutMs())//
						.retryPolicy(retryPolicy)//
						// .namespace(properties.getProjectName())// 增加命名空间
						.build();
				logger.debug("client created :" + client);
				// 设置连接状态监听器
				connectionStateListener = new ServerSideZkConnectionStateListener(serviceSet);
				client.getConnectionStateListenable().addListener(connectionStateListener);
			}
			{
				client.start();
			}
			// logger.info("zk线程启动成功...");
		} catch (Exception e) {
			logger.error(e.toString());
		}
		//
		//
		//
		// 进入睡眠阶段
		while (true) {
			try {
				Thread.currentThread().sleep(6000);
			} catch (Exception e) {
				logger.info(e.toString());
			}
		}

	}

}
