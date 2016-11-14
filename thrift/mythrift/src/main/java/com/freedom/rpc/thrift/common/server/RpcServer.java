package com.freedom.rpc.thrift.common.server;

import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadedSelectorServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TNonblockingServerTransport;

import com.freedom.rpc.thrift.common.extended.server.ServerSideProcessorRegisterHelper;
import com.freedom.rpc.thrift.common.extended.server.ServerSideThreadedSelectorServer;
import com.freedom.rpc.thrift.common.property.ServerProperties;

import com.freedom.rpc.thrift.common.utils.Logger;

//相关Thrift文档可参考：http://my.oschina.net/qiangzigege/blog/495920
public class RpcServer {
	private static final Logger logger = Logger.getLogger(RpcServer.class);

	// public static void main(String[] args) {//屏蔽，被真正的业务线程调用
	public static synchronized void start(String[] args) {
		//
		logger.info("---------------------------------------------------------------");
		ServerProperties property = ServerProperties.getInstance();
		logger.info(property.toString());
		try {
			TNonblockingServerTransport serverTransport = new TNonblockingServerSocket(property.getPort());
			TFramedTransport.Factory transportFactory = new TFramedTransport.Factory();
			TBinaryProtocol.Factory protocolFactory = new TBinaryProtocol.Factory();
			// 定义了一个继承类，用来截取用户启动了哪些服务
			TMultiplexedProcessor processor = new com.freedom.rpc.thrift.common.extended.server.ServerSideExtendedTMultiplexedProcessor();
			ServerSideProcessorRegisterHelper.registerProcessor(processor);
			TThreadedSelectorServer.Args tArgs = new TThreadedSelectorServer.Args(serverTransport);
			tArgs.transportFactory(transportFactory);
			tArgs.protocolFactory(protocolFactory);
			tArgs.processor(processor);
			// 以下属于服务器端的参数优化,accept线程默认就是1，所以不设置了
			{
				tArgs.selectorThreads(property.getSelectorThreads());
				tArgs.acceptQueueSizePerThread(property.getQueueSize());
				tArgs.workerThreads(property.getWorkerThreads());
				logger.info("***************");
				logger.info("setting of thrift server is as follows:");
				logger.info("selector threads: " + tArgs.getSelectorThreads());
				logger.info("queue size: " + tArgs.getAcceptQueueSizePerThread());
				logger.info("worker threads: " + tArgs.getWorkerThreads());
				logger.info("***************");
			}
			// 定义了一个继承类，用来截取系统监听成功行为
			TServer server = new ServerSideThreadedSelectorServer(tArgs);
			logger.info("Start thrift server on port " + property.getPort() + "...");
			server.serve();
		} catch (Exception e) {
			logger.error("exception as :" + e.toString());
		} finally {
			logger.info("thrift server exit... @" + System.currentTimeMillis());
			System.exit(0);
		}
	}

}
