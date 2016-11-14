package com.freedom.rpc.thrift.common.utils;

public class StringUtils {
	public static String serviceUnionVersion(String service, String version) {
		return service + ":" + version;
	}

	public static String ipUnionPort(String ip, int port) {
		return ip + ":" + port;
	}

	// public static String groupUnionService(String group, String service) {
	// return "/" + group + "/" + service;
	// }

	public static String groupUnionServiceUnionEdition(String group, String service, String edition) {
		return "/" + group + "/" + service + ":" + edition;
	}

}
