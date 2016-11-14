package com.freedom.rpc.thrift.common.zookeeper.client;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ClientSideZkEventQueue {
	// http://blog.csdn.net/z69183787/article/details/46986823
	private static BlockingQueue<ClientSideZkEvent> queue = null;
	static {
		queue = new LinkedBlockingQueue<ClientSideZkEvent>(Integer.MAX_VALUE);// 最大
	}

	public static void put(ClientSideZkEvent event) {
		// 即使阻塞也要放进去
		while (true) {
			try {
				queue.put(event);
				return;
			} catch (InterruptedException e) {
			}
		}
	}

	public static ClientSideZkEvent take() {
		ClientSideZkEvent event = null;
		// 不拿到event不罢休
		while (null == event) {
			try {
				event = queue.take();
			} catch (InterruptedException e) {

			}
		}
		return event;
	}
}
