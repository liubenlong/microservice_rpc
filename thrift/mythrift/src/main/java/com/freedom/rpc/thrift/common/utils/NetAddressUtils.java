package com.freedom.rpc.thrift.common.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import com.freedom.rpc.thrift.common.utils.Logger;

public class NetAddressUtils {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(NetAddressUtils.class);

	//
	// public static InetAddress getLocalAddress() throws SocketException {
	// Enumeration<NetworkInterface> ifaces =
	// NetworkInterface.getNetworkInterfaces();
	// while (ifaces.hasMoreElements()) {
	// NetworkInterface iface = ifaces.nextElement();
	// Enumeration<InetAddress> addresses = iface.getInetAddresses();
	// //
	// while (addresses.hasMoreElements()) {
	// InetAddress addr = addresses.nextElement();
	// if (addr instanceof Inet4Address && !addr.isLoopbackAddress()) {
	// return addr;
	// }
	// }
	// }
	// return null;
	// }

	public static String getRealIp() throws Exception {
		String localip = null;// 本地IP，如果没有配置外网IP则返回它
		String netip = null;// 外网IP

		Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
		InetAddress ip = null;
		boolean finded = false;// 是否找到外网IP
		while (netInterfaces.hasMoreElements() && !finded) {
			NetworkInterface ni = netInterfaces.nextElement();
			Enumeration<InetAddress> address = ni.getInetAddresses();
			while (address.hasMoreElements()) {
				ip = address.nextElement();
				if (!ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1) {// 外网IP
					netip = ip.getHostAddress();
					finded = true;
					break;
				} else if (ip.isSiteLocalAddress() && !ip.isLoopbackAddress()
						&& ip.getHostAddress().indexOf(":") == -1) {// 内网IP
					localip = ip.getHostAddress();
				}
			}
		}

		if (netip != null && !"".equals(netip)) {
			return netip;
		} else {
			return localip;
		}
	}

	public static String getLocalHostName() {
		try {
			InetAddress addr = InetAddress.getLocalHost();
			return addr.getHostName();
		} catch (Exception e) {
			return "unknown";
		}
	}

	public static void main(String[] args) throws Exception {
		System.out.println("NetAddressUtils --- " + NetAddressUtils.getRealIp());
	}
}
