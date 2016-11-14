package com.freedom.rpc.thrift.common.machine;

public class Machine {
	private String ip;
	private int port;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public Machine(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}

	@Override
	public boolean equals(Object obj) {
		if (null == obj) {
			return false;
		}
		Machine objMachine = (Machine) obj;
		return (//
		ip.equals(objMachine.getIp())//
				&& //
				port == objMachine.getPort()//
		);
	}
}
