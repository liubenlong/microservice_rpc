package com.freedom.rpc.thrift.common.machine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.freedom.rpc.thrift.common.property.ClientProperties;
import com.freedom.rpc.thrift.common.socket.TSocketPool;
import com.freedom.rpc.thrift.common.utils.Logger;
import com.freedom.rpc.thrift.common.utils.StringUtils;

public class MachineResource {
	private static final Logger logger = Logger.getLogger(MachineResource.class);
	private static final Long LongValue = (long) (0 - Integer.MIN_VALUE);

	// 单例模式
	private MachineResource() {

	}

	// 静态区域

	private static MachineResource instance = new MachineResource();

	public static MachineResource getInstance() {
		return instance;
	}

	// ////////内部成员
	// 锁
	private ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
	// 序号
	private Map<String/* service:edition */, List<Machine/* ip,port */>> machineLists = new HashMap<String, List<Machine>>();
	private Map<String/* service:edition */, AtomicInteger/* nextToUseIndex */> machineIndexs = new HashMap<String, AtomicInteger>();
	// ip:port->连接池
	private Map<String/* ip:port */, TSocketPool> machinePools = new HashMap<String, TSocketPool>();

	//
	//
	//
	private void print() {
		logger.info("machineLists:" + machineLists);
		logger.info("machineIndexs:" + machineIndexs);
		logger.info("machinePools:" + machinePools);
	}

	private void incrRefCountPool(String ip, int port) {
		// 看看是否需要新建连接池
		// 因为可能这台机器在别的服务里被使用提供别的服务，这样连接池就已经创建了
		String key = StringUtils.ipUnionPort(ip, port);
		TSocketPool pool = machinePools.get(key);
		if (null == pool) {
			pool = TSocketPool.getInstance(ip, port);
			machinePools.put(key, pool);
			logger.info("create socket pool for " + key);
		}
		int refCount = pool.incrRefCountAndGet();
		logger.info("current pool ref count --- " + refCount);
	}

	private void addMachineWhenServiceEditionFirstFound(String serviceWithEdition, Machine machine) {// 服务第一次进来
		// 加列表
		List<Machine> machineList = new ArrayList<Machine>();
		machineLists.put(serviceWithEdition, machineList);
		// 加机器
		machineList.add(machine);
		// 建立下标
		machineIndexs.put(serviceWithEdition, new AtomicInteger(-1));// 下标是指下一个要拿的机器的序号，以-1开始
		// 增加连接池引用计数
		incrRefCountPool(machine.getIp(), machine.getPort());
	}

	private void addMachineWhenMachineFirstFound(String serviceWithEdition, Machine machine) {// 服务之前已经发现，但是机器第一次发现
		// 列表
		List<Machine> machineList = machineLists.get(serviceWithEdition);
		// 加机器
		machineList.add(machine);
		// 下标不变
		// 增加连接池引用计数
		incrRefCountPool(machine.getIp(), machine.getPort());
	}

	public void addMachine(String serviceWithEdition, String ip, int port) {
		// 先获取锁
		logger.info("add Machine:" + serviceWithEdition + " " + ip + " " + port);
		try {
			rwLock.writeLock().lock();
			//
			List<Machine> machineList = machineLists.get(serviceWithEdition);
			if (null == machineList) {
				// 服务第一次进来
				addMachineWhenServiceEditionFirstFound(serviceWithEdition, new Machine(ip, port));
			} else {
				Machine machine = new Machine(ip, port);
				int index = machineList.indexOf(machine);
				if (-1 == index) {
					// 服务已经有过，机器第一次进来
					addMachineWhenMachineFirstFound(serviceWithEdition, machine);
				} else {
					// 服务也有，机器也有，那就什么都不做
					logger.info("this machine has existed,do nothing,just return back...");
				}
			}
			this.print();
		} catch (Exception e) {

		} finally {// 后释放锁
			rwLock.writeLock().unlock();
		}
	}

	private void decrRefCountPool(String ip, int port) {
		String key = StringUtils.ipUnionPort(ip, port);
		TSocketPool pool = machinePools.get(key);
		int refCount = pool.decrRefCountAndGet();
		// logger.info("current pool ref count --- " + refCount);
		if (0 == refCount) {
			// 清除连接池,注意：之前已经借出去的连接，在返回时，会被自动销毁
			pool = machinePools.remove(key);
			pool.close();
			logger.info("destory socket pool : " + ip + ":" + port);
		}
	}

	private void delMachineWithIndex(String serviceWithEdition, List<Machine> machineList, int index) {
		// 先删除机器
		Machine tobeRemoved = machineList.remove(index);
		// 如果列表为空，也删除列表和下标
		if (0 == machineList.size()) {
			machineLists.remove(serviceWithEdition);
			machineIndexs.remove(serviceWithEdition);
		}
		// 减少连接池的引用计数
		decrRefCountPool(tobeRemoved.getIp(), tobeRemoved.getPort());
		// 结束
	}

	public void delMachine(String serviceWithEdition, String ip, int port) {
		// 先获取锁
		logger.info("del Machine:" + serviceWithEdition + " " + ip + " " + port);
		try {
			rwLock.writeLock().lock();
			// 如果是删除，那必须之前已经存在，否则没有必要继续
			List<Machine> machineList = machineLists.get(serviceWithEdition);
			if (null != machineList) {
				Machine machine = new Machine(ip, port);
				int index = machineList.indexOf(machine);
				if (-1 != index) {
					// 确实找到了
					delMachineWithIndex(serviceWithEdition, machineList, index);
				}
			}
			// this.print();
		} catch (Exception e) {

		} finally {
			// 后释放锁
			rwLock.writeLock().unlock();
		}
	}

	public TSocketPool getSocketPool(String group, String service, String edition) {
		// 通过服务查找版本
		String serviceWithEdition = StringUtils.groupUnionServiceUnionEdition(group, service, edition);
		if (null == serviceWithEdition) {
			return null;
		}
		return getSocketPoolWithEdition(serviceWithEdition);
	}

	private TSocketPool getSocketPoolWithEdition(String serviceWithEdition) {// 只是读操作
		// 获取锁
		try {
			rwLock.readLock().lock();
			// 拿取机器列表
			List<Machine> machineList = machineLists.get(serviceWithEdition);
			if (null == machineList) {
				throw new Exception("no machineList found for :" + serviceWithEdition);
			}
			// 计算机器下标
			AtomicInteger index = machineIndexs.get(serviceWithEdition);
			int newIndex = index.incrementAndGet();// [-2147483648,
			// 修正
			if (newIndex < 0) {// 务必要进行修正// 最大值的话，就要重新开始,否则会成为负数
				newIndex = (int) ((long) newIndex + LongValue);
			}
			// 应用
			newIndex = newIndex % machineList.size();
			// 拿取目标machine
			Machine targetMachine = machineList.get(newIndex);
			// 取出ip,port
			String ip = targetMachine.getIp();
			int port = targetMachine.getPort();
			// 查找全局pool
			TSocketPool pool = machinePools.get(StringUtils.ipUnionPort(ip, port));
			// 返回pool
			return pool;
		} catch (Exception e) {
			logger.error(e.toString());
		} finally {// 释放锁
			rwLock.readLock().unlock();
		}
		return null;
	}

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		//
		// boolean result = index.compareAndSet(2147483647, -1);//
		// 最大值的话，就要重新开始,否则会成为负数

		{
			AtomicInteger ai = new AtomicInteger();
			ai.set(2147483646);
			int count = 5;
			while (count-- > 0) {
				int newIndex = ai.incrementAndGet();
				if (newIndex < 0) {
					newIndex = (int) ((long) newIndex + LongValue);
				}
				System.out.println(newIndex);
			}
			if (1 == 1)
				return;
		}
		MachineResource r = MachineResource.getInstance();
		logger.info("---------------------------------------");

		r.addMachine("hello:0.1", "127.0.0.1", 10001);
		logger.info("---------------------------------------");
		r.addMachine("hello:0.1", "127.0.0.1", 10002);
		logger.info("---------------------------------------");
		r.addMachine("hello:0.2", "127.0.0.1", 10001);
		logger.info("---------------------------------------");
		r.addMachine("hello:0.2", "127.0.0.1", 10002);
		logger.info("---------------------------------------");
		r.addMachine("hello:0.3", "127.0.0.1", 10001);
		logger.info("---------------------------------------");
		r.addMachine("hello:0.3", "127.0.0.1", 10002);
		logger.info("---------------------------------------");
		r.addMachine("hello:0.4", "127.0.0.1", 10001);
		logger.info("---------------------------------------");
		r.addMachine("hello:0.4", "127.0.0.1", 10002);
		logger.info("---------------------------------------");
		r.delMachine("hello:0.1", "127.0.0.1", 10001);
		logger.info("---------------------------------------");
		r.delMachine("hello:0.1", "127.0.0.1", 10002);
		logger.info("---------------------------------------");
		r.delMachine("hello:0.2", "127.0.0.1", 10001);
		logger.info("---------------------------------------");
		r.delMachine("hello:0.2", "127.0.0.1", 10002);
		logger.info("---------------------------------------");
		r.delMachine("hello:0.5", "127.0.0.1", 10001);
		logger.info("---------------------------------------");
		r.delMachine("hello:0.5", "127.0.0.1", 10002);
		logger.info("---------------------------------------");
		r.delMachine("hello:0.3", "127.0.0.1", 10001);
		logger.info("---------------------------------------");
		r.delMachine("hello:0.3", "127.0.0.1", 10002);
		logger.info("---------------------------------------");
		r.delMachine("hello:0.4", "127.0.0.1", 10001);
		logger.info("---------------------------------------");
		r.delMachine("hello:0.4", "127.0.0.1", 10002);
		logger.info("---------------------------------------");
		r.delMachine("hello:0.5", "127.0.0.1", 10001);
		logger.info("---------------------------------------");
		r.delMachine("hello:0.5", "127.0.0.1", 10002);
		logger.info("---------------------------------------");
	}

}
