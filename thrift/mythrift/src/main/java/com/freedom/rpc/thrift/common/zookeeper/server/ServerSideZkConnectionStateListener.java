package com.freedom.rpc.thrift.common.zookeeper.server;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;

import com.freedom.rpc.thrift.common.annotation.Model;
import com.freedom.rpc.thrift.common.utils.Logger;

public class ServerSideZkConnectionStateListener implements ConnectionStateListener {

	private static final Logger logger = Logger.getLogger(ServerSideZkConnectionStateListener.class);
	// http://cuisuqiang.iteye.com/blog/2019372
	private static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);
	private Set<Model> serviceSet = new HashSet<Model>();

	public ServerSideZkConnectionStateListener(Set<Model> s) {
		this.serviceSet = s;
	}

	private void registerSingleService(CuratorFramework client, Model model) {
		// 注册单个服务
		fixedThreadPool.execute(new ServerSideZkServiceRegisterRunnable(client, model));
	}

	private void registerAllService(CuratorFramework client) {
		// 注册所有服务
		for (Model model : serviceSet) {
			registerSingleService(client, model);
		}
	}

	@Override
	public void stateChanged(CuratorFramework client, ConnectionState state) {
		logger.info("(((state changed :" + state + " client: " + client);
		// state的这几种状态，如何区分?
		if (ConnectionState.CONNECTED == state) {
			// Sent for the first successful connection to the server.
			// NOTE: You will only get one of these messages for any
			// CuratorFramework instance.
			// 注册所有服务
			registerAllService(client);
		} else if (ConnectionState.SUSPENDED == state) {
			// There has been a loss of connection. Leaders, locks, etc.
			// should suspend until the connection is re-established.
			// If the connection times-out you will receive a LOST notice
		} else if (ConnectionState.RECONNECTED == state) {
			// A suspended or read-only connection has been re-established
			// 重新注册所有服务
			registerAllService(client);
		} else if (ConnectionState.LOST == state) {
			// The connection is confirmed to be lost.
			// Close any locks, leaders, etc. and attempt to re-create them.
			// NOTE: it is possible to get a RECONNECTED state after this but
			// you should still consider any locks, etc. as dirty/unstable
		} else if (ConnectionState.READ_ONLY == state) {
			// The connection has gone into read-only mode.
			// This can only happen if you pass true for
			// CuratorFrameworkFactory.Builder.canBeReadOnly().
			// See the ZooKeeper doc regarding read only connections:
			// http://wiki.apache.org/hadoop/ZooKeeper/GSoCReadOnlyMode.
			// The connection will remain in read only mode until another state
			// change is sent.
		} else {
			logger.error("unknown state: " + state);
		}
	}

}
