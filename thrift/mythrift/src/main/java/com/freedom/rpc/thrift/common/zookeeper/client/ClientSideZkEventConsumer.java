package com.freedom.rpc.thrift.common.zookeeper.client;


import com.freedom.rpc.thrift.common.machine.MachineResource;
import com.freedom.rpc.thrift.common.utils.Logger;
import com.freedom.rpc.thrift.common.utils.TimeUtils;

public class ClientSideZkEventConsumer implements Runnable {

	private static final Logger logger = Logger.getLogger(ClientSideZkEventConsumer.class);

	@Override
	public void run() {
		while (true) {
			try {
				// 循环消费
				ClientSideZkEvent event = ClientSideZkEventQueue.take();
				if (null == event) {// 实际上不会发生，因为take函数做了判断
					TimeUtils.sleep(5);
					continue;
				}
				// 拿到了event，有效，怎么处理?
				ClientSideZkEventType type = event.getType();
				String serviceWithEdition = event.getService();
				String data = event.getData();
				logger.info("------------------------------------------");
				logger.info("type: " + type);
				logger.info("service:" + serviceWithEdition);
				logger.info("data:" + data);
				logger.info("------------------------------------------");
				String[] array = data.split(":");
				if (null == array || array.length <= 1) {
					throw new Exception("wrong message: " + data);
				}
				String ip = array[0];
				int port = Integer.parseInt(array[1]);
				// 交给machineResource处理
				if (ClientSideZkEventType.ADDED == type) {
					MachineResource.getInstance().addMachine(serviceWithEdition, ip, port);
				} else if (ClientSideZkEventType.REMOVED == type) {
					MachineResource.getInstance().delMachine(serviceWithEdition, ip, port);
				}
			} catch (Exception e) {
				logger.error(e.toString());
			}
		} // while结束

	}

}
