package com.freedom.rpc.thrift.client;

import org.apache.thrift.protocol.TMultiplexedProtocol;

import com.freedom.rpc.thrift.business0.service.HelloWorldService;
import com.freedom.rpc.thrift.common.utils.Logger;
import com.freedom.rpc.thrift.common.utils.SocketUtils;
import com.freedom.rpc.thrift.common.utils.TimeUtils;
import com.freedom.rpc.thrift.common.zookeeper.client.ClientSideZkReadyListener;

public class MyClient {

	private static final Logger logger = Logger.getLogger(MyClient.class);
	static {
		// 必须调用，等待zk准备好!!!
		ClientSideZkReadyListener.ready();
	}

	public static void main(String[] args) {
		// int count = 1;
		// while (count-- >= 0) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						test();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						TimeUtils.sleep(666);
					}
				}
			}

		}).start();
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						test();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						TimeUtils.sleep(666);
					}
				}
			}

		}).start();
		//
		// }

	}

	public static void test() throws Exception {
		// 测试代码
		try {
			HelloWorldService.Client client = (HelloWorldService.Client) SocketUtils
					.getAopObject(HelloWorldService.Client.class);
			String result = client.helloWorldString("https://git.oschina.net/qiangzigege/MyThrift");
			logger.info(result);
		} catch (Exception e) {
			logger.error(e.toString());
			throw e;
		}
	}
}
