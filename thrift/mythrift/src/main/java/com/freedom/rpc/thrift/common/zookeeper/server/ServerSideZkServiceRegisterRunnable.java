package com.freedom.rpc.thrift.common.zookeeper.server;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.utils.EnsurePath;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException.NodeExistsException;

import com.freedom.rpc.thrift.common.annotation.Model;
import com.freedom.rpc.thrift.common.property.ServerProperties;
import com.freedom.rpc.thrift.common.utils.Logger;
import com.freedom.rpc.thrift.common.utils.NetAddressUtils;
import com.freedom.rpc.thrift.common.utils.StringUtils;
import com.freedom.rpc.thrift.common.utils.TimeUtils;

public class ServerSideZkServiceRegisterRunnable implements Runnable {

	private static final Logger logger = Logger.getLogger(ServerSideZkServiceRegisterRunnable.class);
	private String service = "";
	CuratorFramework client;

	public ServerSideZkServiceRegisterRunnable(CuratorFramework c, Model model) {
		this.client = c;
		// 分组
		String group=model.getGroup();
		if (false == group.startsWith("/")) {
			this.service += "/";
		}
		this.service += group;
		// 加上版本&版本信息
		this.service += "/" + StringUtils.serviceUnionVersion(model.getService(), model.getEdition());
		// 加上提供者标志
		this.service += "/PROVIDER";
	}

	@Override
	public void run() {
		boolean registerSucceed = false;
		int count = 0;
		ServerProperties properties = ServerProperties.getInstance();
		while (false == registerSucceed) {
			logger.debug("try to register service: " + service);
			try {
				// 先保证此永久节点存在
				EnsurePath ensurePath = new EnsurePath(service);
				ensurePath.ensure(client.getZookeeperClient());
				// 然后正式注册---临时节点
				String ip = NetAddressUtils.getRealIp();
				int port = properties.getPort();
				String hostname = NetAddressUtils.getLocalHostName();
				//
				String key = hostname + "-[" + ip + ":" + port + "]";
				String value = ip + ":" + port;
				logger.debug("key:" + key);
				logger.debug("value: " + value);
				// 构造临时节点
				client.create().withMode(CreateMode.EPHEMERAL).forPath(service + "/" + key, value.getBytes());
				// 注册成功，就跳出循环，表示此runnable结束
				registerSucceed = true;
				// 注册成功
				logger.debug("succeed to register service: " + service);
				return;
			} catch (NodeExistsException e) {
				// 假设存在就退出的话,会可能导致永远注册不上
				// 但是，存在也不退出的话，又会导致一直在重复注册
				// 所以，尝试6个会话失效周期后，如果节点还存在，那就是真的存在了，就不需要再继续注册了
				count++;
				if (count >= 7) {
					registerSucceed = true;
					break;
				}
				registerSucceed = false;
				TimeUtils.sleep(properties.getSessionTimeoutMs());
			} catch (Exception e1) {
				count = 0;// 从头再来
				registerSucceed = false;// 注册失败
				logger.info(e1.toString());
			}
		}
		// 运行结束
	}

}
